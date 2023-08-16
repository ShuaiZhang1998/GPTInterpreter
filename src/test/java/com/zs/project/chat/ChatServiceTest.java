package com.zs.project.chat;

import com.zs.project.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatServiceTest {
    @Autowired
    ChatService chatService;
    @Test
    void testChatQueryByUserid(){

    }
}
