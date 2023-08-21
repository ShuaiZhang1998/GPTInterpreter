package com.zs.project.service.impl;


import com.zs.project.constant.Salt;
import com.zs.project.constant.UserConstant;
import com.zs.project.exception.ErrorCode;
import com.zs.project.exception.ServiceException;
import com.zs.project.mapper.UserDao;
import com.zs.project.model.entry.User;
import com.zs.project.model.vo.UserVO;
import com.zs.project.service.UserService;
import com.zs.project.util.PatternUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author 用户相关服务
 * 登录注册
 */

@Service
public class userServiceImpl implements UserService {
    /**
     * 用户操作数据库的dao
     */
    @Resource
    private final UserDao userDao;

    @Autowired
    public userServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 用户注册
     * @param userName 用户名
     * @param userPassword 第一次输入的密码
     * @param repeatPassword 确认密码
     * @return
     */
    @Override
    public long userRegister(String userName, String userPassword, String repeatPassword) {
        //万恶的JAVA...
        if(Objects.equals(userName, "")
                || Objects.equals(userPassword, "")
                || Objects.equals(repeatPassword, ""))
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"参数为空");
        if(userName.length()>12)
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"用户名不能超过12位");
        if(userPassword.length()<8)
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"密码不能小于8位");
        if(!userPassword.equals(repeatPassword))
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        User user = userDao.getUserByName(userName);
        if(user!=null)
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"用户名重复");
        //这个锁和yupi佬学的，锁常量池确实想不到啊
        synchronized (userName.intern()){
            String encryptPassword = DigestUtils.md5DigestAsHex((Salt.salt + userPassword).getBytes());
            user = new User();
            user.setUserName(userName);
            user.setUserPassword(encryptPassword);
            int i = userDao.addUser(user);
            if(i!=1)
                throw new ServiceException(ErrorCode.SYSTEM_ERROR,"数据库异常");
        }
        return userDao.getUserByName(userName).getUserID();
    }

    /**
     * 用户登录
     * @param userName
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public UserVO userLogin(String userName, String userPassword, HttpServletRequest request) {
        if(Objects.equals(userName, "") || Objects.equals(userPassword, ""))
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"参数为空");
        if(userName.length()>12)
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"用户名错误");
        if(userPassword.length()<8)
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"密码错误");
        String encryptPassword = DigestUtils.md5DigestAsHex((Salt.salt + userPassword).getBytes());
        User user = userDao.getUserByName(userName);
        if(!user.getUserPassword().equals(encryptPassword))
            throw new ServiceException(ErrorCode.PARAMS_ERROR,"密码错误");
        if(request!=null){
            HttpSession session = request.getSession();
            session.setAttribute(UserConstant.USER_LOGIN_STATE, user);
//            request.setAttribute(UserConstant.USER_LOGIN_STATE,user);
//            HttpSession session = request.getSession();
//            User user = (User) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        }
        else
            throw new ServiceException(ErrorCode.SYSTEM_ERROR);

        return this.getUserVO(user);

    }

    /**
     * 脱敏信息
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if(user==null)
            return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }
}
