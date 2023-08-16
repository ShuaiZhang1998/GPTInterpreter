package com.zs.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.project.model.entry.Chat;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface ChatService extends IService<Chat> {
    /**
     * 获取登陆用户的所有聊天记录
     * @param request
     * @return
     */
    List<Chat> getAllChatByUserID(HttpServletRequest request);

    /**
     * 创建一个新的聊天
     * @param conversationName 聊天内容
     * @param request
     * @return
     */
    long createNewConversion(String conversationName,HttpServletRequest request);

    /**
     * 添加聊天记录
     * @param conversationID
     * @param message
     * @param index
     * @return
     */

    long addConversionByID(Long conversationID,String message,Long index);

    Chat getChatByID(long conversationID,HttpServletRequest request);

}
