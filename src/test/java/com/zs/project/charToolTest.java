package com.zs.project;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
public class charToolTest {
    @Test
    void testMd5(){
        String base = "bs";
        String pass = "1234567890121212124";
        String encode = DigestUtils.md5DigestAsHex((base + pass).getBytes());
        System.out.println(encode.length());
    }
}
