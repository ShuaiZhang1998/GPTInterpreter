package com.zs.project.model.dto.chat;

import com.zs.project.model.dto.page.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author ShuaiZhang
 * 用于聊天的request
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatQueryRequest extends PageRequest implements Serializable {
    /**
     * 聊天ID
     */
    private Long conversationID;
    /**
     * 聊天名称
     */
    private String conversationName;
    /**
     * 用户ID
     */
    private Long userID;
    /**
     * 下一个索引
     */
    private Long nextIndex;
    /**
     * 聊天历史
     */
    private String conversationHistory;
    private static final long serialVersionUID = 1L;
}
