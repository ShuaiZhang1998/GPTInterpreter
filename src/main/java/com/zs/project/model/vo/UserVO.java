package com.zs.project.model.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author 鱼皮
 * 脱敏的用户信息
 */
@Data
public class UserVO implements Serializable {


    private Long userID;
    /**
     * 用户名
     */

    private String userName;
    /**
     * 对话ID
     */

    private Long conversationID;
    /**
     * 版本号
     */

    private static final long serialVersionUID = 1L;


}
