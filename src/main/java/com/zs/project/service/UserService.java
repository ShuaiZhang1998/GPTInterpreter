package com.zs.project.service;

import com.zs.project.model.entry.User;
import com.zs.project.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;


public interface UserService {
    /**
     * 用户注册
     * @param userName 用户名
     * @param userPassword 第一次输入的密码
     * @param repeatPassword 确认密码
     * @return
     */
    long userRegister(String userName,String userPassword,String repeatPassword);

    /**
     * 用户登陆
     * @param userName
     * @param userPassword
     * @param request
     * @return
     */
    UserVO userLogin(String userName, String userPassword, HttpServletRequest request);

    /**
     * 获取用户脱敏信息
     * @param user
     * @return
     */
    UserVO getUserVO(User user);
}
