package com.zs.project.model.dto.chat;

import lombok.Data;

@Data
public class UpdateChatRequest {
    private Long conversationID;
    private String newContent;

    private static final long serialVersionUID = 1L;
}
