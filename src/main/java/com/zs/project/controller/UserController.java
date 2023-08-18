package com.zs.project.controller;

import com.zs.project.constant.UserConstant;
import com.zs.project.model.dto.user.UserLoginRequest;
import com.zs.project.model.dto.user.UserRegisterRequest;
import com.zs.project.model.entry.User;
import com.zs.project.model.response.BaseResponse;
import com.zs.project.model.vo.UserVO;
import com.zs.project.service.UserService;
import com.zs.project.util.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author ShuaiZhang
 * 用户相关的页面
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return 注册好的用户ID
     */

    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest==null)
            return null;
        String userName = userRegisterRequest.getUserName();
        String userPassword = userRegisterRequest.getUserPassword();
        String repeatPassword = userRegisterRequest.getRepeatPassword();
        if(userName.equals("")||userPassword.equals("")||repeatPassword.equals(""))
            return null;
        long l = userService.userRegister(userName, userPassword, repeatPassword);
        return ResultUtils.success(l);
    }

    /**
     * 用户登陆
     * @param userLoginRequest 登陆参数
     * @param request 登陆后将登陆信息保存到Session
     * @return 用户脱敏后的信息
     */

    @PostMapping("/login")
    public BaseResponse<UserVO> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest==null)
            return null;
        String userName = userLoginRequest.getUserName();
        String userPassword = userLoginRequest.getUserPassword();
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(user!=null)
            return ResultUtils.success(null,"用户已登陆");
        UserVO userVOFirstLogin = userService.userLogin(userName, userPassword, request);
        return ResultUtils.success(userVOFirstLogin,"登陆成功");
    }

}
