package com.zs.project.model.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("chat")
public class Chat {
    /**
     * 对话ID
     * 这个🪝8问题可是...
     */
    @TableId(value = "conversationID", type = IdType.ASSIGN_ID)
    private Long conversationID;

    /**
     * 用户ID
     */
    @TableField("userID")
    private Long userID;

    /**
     * 对话简介
     */
    @TableField("conversationName")
    private String conversationName;

    /**
     * 对话编号
     */
    @TableField("nextIndex")
    private Long nextIndex;

    /**
     * 对话
     */
    @TableField("conversationHistory")
    private String conversationHistory;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
