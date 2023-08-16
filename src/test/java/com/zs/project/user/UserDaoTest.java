package com.zs.project.user;

import com.zs.project.mapper.UserDao;
import com.zs.project.model.entry.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void testAddOneUser(){
        User user = new User();
        user.setUserName("zdsggs");
        user.setUserPassword("adasdasda");
        long i = userDao.addUser(user);
        System.out.println(i);
    }


    @Test
    void testAddUserEfficiency() {
        int counter = 300;
        long startTime = System.currentTimeMillis();

        User user = new User();
        for (int i = 200; i < counter; i++) {

            user.setUserName("u" + i); // Trimming timestamp to ensure username length
            user.setUserPassword("123456");
            userDao.addUser(user);
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Execution time for addUser for " + counter + " times: " + executionTime + " milliseconds");
    }

    @Test
    void testUserFindByUsername(){
        User xxx = userDao.getUserByName("xxx");
        if(xxx==null)
            System.out.println("yes");
    }


}
