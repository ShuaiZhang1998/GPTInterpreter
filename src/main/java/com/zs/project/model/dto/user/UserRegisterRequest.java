package com.zs.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ShuaiZhang
 * 用于用户注册的request
 */

@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String userPassword;
    private String repeatPassword;
}
