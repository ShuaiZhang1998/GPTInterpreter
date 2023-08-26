package com.zs.project.controller;


import cn.hutool.core.util.IdUtil;
import com.zs.project.exception.ErrorCode;
import com.zs.project.exception.ServiceException;
import com.zs.project.model.dto.chat.CreateChatRequest;
import com.zs.project.model.dto.chat.UpdateChatRequest;
import com.zs.project.model.entry.Chat;
import com.zs.project.model.response.BaseResponse;
import com.zs.project.service.ChatService;
import com.zs.project.service.CodeInterpreter;
import com.zs.project.service.ExService;
import com.zs.project.service.GptService;
import com.zs.project.util.PatternUtils;
import com.zs.project.util.ResultUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ShuaiZhang
 * 聊天页面
 */
@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {
    /**
     * 一般地聊天服务
     */
    @Resource
    ChatService chatService;
    /**
     * GPT外部服务
     */
    @Resource
    GptService gptService;
    /**
     * 执行器服务
     */
    @Resource
    ExService exService;

    /**
     * 代码解释器服务
     */
    @Resource
    CodeInterpreter codeInterpreter;

    /**
     * 获取已登陆用户的所有对话记录
     * 这里未来要改成分页查询
     * @param request
     * @return 已登录用户的所有聊天记录
     */
    @GetMapping("/history")
    BaseResponse<List<Chat>> getLoginUserAllConversation(HttpServletRequest request){
        if(request==null)
            return null;
        List<Chat> allChatByUserID = chatService.getAllChatByUserID(request);
        return ResultUtils.success(allChatByUserID);
    }

    /**
     * 在数据库里创建一条新的对话
     * @param createChatRequest
     * @param request
     * @return 新对话地ID
     */

    @PostMapping("/create")
    BaseResponse<Long> createNewConversation(@RequestBody CreateChatRequest createChatRequest,HttpServletRequest request){
        if(createChatRequest==null)
            return null;
        String conversationName = createChatRequest.getConversationName();
        long newConversion = chatService.createNewConversion(conversationName, request);
        return ResultUtils.success(newConversion);
    }

    /**
     * 组合用户的结果并同步到数据库
     * @param updateChatRequest
     * @param request
     * @return 代码解释器地运行结果
     */

    @PostMapping("/update")
    BaseResponse<String> updateConversationHistory(@RequestBody UpdateChatRequest updateChatRequest,HttpServletRequest request){
        if(updateChatRequest==null)
            return null;
        // 获取用户问题，以及对应的对话
        Long conversationID = updateChatRequest.getConversationID();
        String newContent = updateChatRequest.getNewContent();
        // 查看对话是否存在
        Chat chatByID = chatService.getChatByID(conversationID, request);
        if(chatByID==null)
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"不存在的记录");
        // 获取对话插入位置 先把问题插入
        Long index = chatByID.getNextIndex();
        chatService.addConversionByID(conversationID, newContent,index);
        // 将问题发送给gpt服务端
        String code = gptService.GptResponse(newContent);
        String result = exService.resultExPython(PatternUtils.extractPythonCode(code));

        //将结果响应给前段，将gpt整个回答存入数据库
        chatService.addConversionByID(conversationID, code,index+1);
        return ResultUtils.success(result);
    }


    /**
     * 存储信息的ID以及信息本身
     */
    Map<String, String> msgMap = new ConcurrentHashMap<>();


    /**
     * 发送消息
     * @param msg 消息
     * @return 消息ID
     */
    @ResponseBody
    @PostMapping("/sendMsg")
    public String sendMsg(String msg) {
        String msgId = IdUtil.simpleUUID();
        msgMap.put(msgId, msg);
        return msgId;
    }

    /**
     * 对话
     * GET方式存在一些问题，消息限制，长度限制，安全性等
     * @param msgId 消息ID
     * @return SseEmitter
     */
    private Map<String, String> resultsStore = new ConcurrentHashMap<>();

    @GetMapping(value = "/conversation/{msgId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter conversation(@PathVariable("msgId") String msgId) {
        SseEmitter sseEmitter = new SseEmitter();
        String msg = msgMap.remove(msgId);
        msg ="你是一个代码解释器，你的职责就是负责解决用户的编程问题。当你接收到用户的问题，首先你会判断这个问题是否是一个可以用python编程解决的问题，如果不可以的话你会回复用户理由；如果问题可以用python" +
                "编程解决的话，请你直接将代码和最简单的解释回复给用户，代码请用markdown包裹并注明语言是python。"+"\n\n"+"用户问题是:"+msg;
        gptService.streamChatCompletion(msg, sseEmitter, result -> {
            resultsStore.put(msgId, result);
        });
        return sseEmitter;
    }

    @GetMapping("/getResult/{msgId}")
    public ResponseEntity<String> getResult(@PathVariable("msgId") String msgId) {
        String result = resultsStore.get(msgId);
        if (result != null) {
            log.info("代码是"+PatternUtils.extractPythonCode(result));
            String s = exService.resultExPython(PatternUtils.extractPythonCode(result));
            return ResponseEntity.ok(s);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Result not ready yet");
        }
    }


}
