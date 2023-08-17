package com.zs.project.controller;


import cn.hutool.core.util.IdUtil;
import com.zs.project.exception.ErrorCode;
import com.zs.project.exception.ServiceException;
import com.zs.project.model.dto.chat.CreateChatRequest;
import com.zs.project.model.dto.chat.UpdateChatRequest;
import com.zs.project.model.entry.Chat;
import com.zs.project.model.response.BaseResponse;
import com.zs.project.service.ChatService;
import com.zs.project.service.ExService;
import com.zs.project.service.GptService;
import com.zs.project.util.PatternUtils;
import com.zs.project.util.ResultUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Resource
    ChatService chatService;
    @Resource
    GptService gptService;
    @Resource
    ExService exService;

    /**
     * 获取已登陆用户的所有对话记录
     * 这里未来要改成分页查询
     * @param request
     * @return
     */
    @GetMapping("/history")
    BaseResponse<List<Chat>> getLoginUserAllConversation(HttpServletRequest request){
        if(request==null)
            return null;
        List<Chat> allChatByUserID = chatService.getAllChatByUserID(request);
        return ResultUtils.success(allChatByUserID);
    }

    @PostMapping("/create")
    BaseResponse<Long> createNewConversation(@RequestBody CreateChatRequest createChatRequest,HttpServletRequest request){
        if(createChatRequest==null)
            return null;
        String conversationName = createChatRequest.getConversationName();
        long newConversion = chatService.createNewConversion(conversationName, request);
        return ResultUtils.success(newConversion);
    }

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

    Map<String, String> msgMap = new ConcurrentHashMap<>();


    /**
     * 发送消息
     *
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
     *
     * @param msgId 消息ID
     * @return SseEmitter
     */
    @GetMapping(value = "/conversation/{msgId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter conversation(@PathVariable("msgId") String msgId) {
        SseEmitter sseEmitter = new SseEmitter();
        String msg = msgMap.remove(msgId);

        //调用流式会话服务
        gptService.streamChatCompletion(msg, sseEmitter);

        //及时返回SseEmitter对象
        return sseEmitter;
    }


}
