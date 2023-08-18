package com.zs.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.project.model.entry.Chat;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author ShuaiZhang
 * 普通聊天功能接口
 */

public interface ChatService extends IService<Chat> {
    /**
     * 获取登陆用户的所有聊天记录
     * @param request
     * @return 已登录用户的所有聊天记录
     */
    List<Chat> getAllChatByUserID(HttpServletRequest request);

    /**
     * 创建一个新的聊天
     * @param conversationName 聊天内容
     * @param request
     * @return 新聊天的ID
     */
    long createNewConversion(String conversationName,HttpServletRequest request);

    /**
     * 添加聊天记录
     * @param conversationID 对话ID
     * @param message 具体的信息
     * @param index 当前对话的下一条子记录在Json中的索引
     * @return
     */

    long addConversionByID(Long conversationID,String message,Long index);

    /**
     * 获取指定对话的所有信息
     * @param conversationID 对话ID
     * @param request
     * @return 聊天记录
     */

    Chat getChatByID(long conversationID,HttpServletRequest request);

}
