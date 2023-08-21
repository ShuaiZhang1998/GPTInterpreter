package com.zs.project.model.dto.chat;

import lombok.Data;

/**
 * @Author ShuaiZhang
 * 用于创建对话的request
 */
@Data
public class CreateChatRequest {
    /**
     * 聊天名称
     */
    private String conversationName;
    private static final long serialVersionUID = 1L;
}
