package com.zs.project.model.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName(value = "user")

/**
 * @author ShuaiZhang
 * 用户实体类
 */

public class User {
    @TableId(value = "userID",type = IdType.ASSIGN_ID)
    private Long userID;
    /**
     * 用户名
     */
    @TableField("userName")
    private String userName;
    /**
     * 对话ID
     */
    @TableField("conversationID")
    private Long conversationID;
    /**
     * 用户密码
     */

    @TableField("userPassword")
    private String userPassword;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
