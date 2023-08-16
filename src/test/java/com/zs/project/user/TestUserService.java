package com.zs.project.user;

import com.zs.project.model.entry.User;
import com.zs.project.model.vo.UserVO;
import com.zs.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUserService {

    @Autowired
    UserService userService;
    @Test
    void testUserService(){
        userService.userRegister("zhakmhf","12345678","12345678");
    }

    @Test
    void testGetUserVO(){
        User user = new User();
        user.setUserID(99L);
        UserVO userVO = userService.getUserVO(user);
        System.out.println(userVO.getUserID());
    }

    @Test
    void testUserLogin(){
        UserVO userVO = userService.userLogin("zhangshuai", "12345678", null);
        System.out.println(userVO);

    }
}
