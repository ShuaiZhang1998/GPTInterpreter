package com.zs.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.project.constant.UserConstant;
import com.zs.project.exception.ErrorCode;
import com.zs.project.exception.ServiceException;
import com.zs.project.mapper.ChatDao;
import com.zs.project.mapper.ChatMapper;
import com.zs.project.model.entry.Chat;
import com.zs.project.model.entry.User;
import com.zs.project.service.ChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {
    @Resource
    ChatDao chatDao;
    @Resource
    ChatMapper chatMapper;
    @Override
    public List<Chat> getAllChatByUserID(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(userObj==null)
            throw new ServiceException(ErrorCode.NOT_LOGIN_ERROR,"用户未登陆");
        User user = (User) userObj;
        long userLoginID = user.getUserID();
        List<Chat> chats = chatDao.getAllChatByUserID(userLoginID);
        return chats;
    }

    @Override
    public long createNewConversion(String conversationName,HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(userObj==null)
            throw new ServiceException(ErrorCode.NOT_LOGIN_ERROR,"用户未登陆");
        User user = (User) userObj;
        Long userID = user.getUserID();
        Chat chat = new Chat();
        chat.setUserID(userID);
        chat.setConversationName(conversationName);
        //为空时出现了BUG，没办法添加记录，初始化为json格式
        chat.setConversationHistory("{}");
        chatMapper.insert(chat);
        return chat.getConversationID();
    }



    @Override
    public long addConversionByID(Long conversationID, String message,Long index) {
        Chat allChatByID = chatDao.getAllChatByID(conversationID);
        if(allChatByID==null)
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"不存在的记录");
        Map<String, String> map = new HashMap<>();
        map.put(index+"",message);
        // 使用ObjectMapper将map转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json="";
        try{
           json = objectMapper.writeValueAsString(map);
        }catch (Exception e){
            throw new ServiceException(ErrorCode.PARAMS_ERROR);
        }

        Long aLong = chatDao.updateConversationHistory(conversationID, json);
        chatDao.updateConversationIndex(conversationID,allChatByID.getNextIndex()+1);
        return aLong;
    }

    @Override
    public Chat getChatByID(long conversationID,HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(userObj==null)
            throw new ServiceException(ErrorCode.NOT_LOGIN_ERROR,"用户未登陆");
        Chat allChatByID = chatDao.getAllChatByID(conversationID);
        return allChatByID;
    }

}
