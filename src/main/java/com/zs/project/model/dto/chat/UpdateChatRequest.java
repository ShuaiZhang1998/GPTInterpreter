package com.zs.project.model.dto.chat;

import lombok.Data;

/**
 * @author ShuaiZhang
 * 用于更新聊天的request
 */
@Data
public class UpdateChatRequest {
    /**
     * 聊天表额主键，聊天的ID
     */
    private Long conversationID;
    /**
     * 直接存Json比较搞心态，用字符串存，数据库是json格式的
     */
    private String newContent;

    private static final long serialVersionUID = 1L;
}
