package com.zs.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ShuaiZhang
 * 用于用户登录的request
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String userPassword;
}
