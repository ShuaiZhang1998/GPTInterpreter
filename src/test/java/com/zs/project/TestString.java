package com.zs.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestString {
    @Test
    void testStringNull(){

        String s1 = new String("");
        String s2 = new String("");
        if(s1==s2)
            System.out.println("true");
        else
            System.out.println("false");



    }
}
