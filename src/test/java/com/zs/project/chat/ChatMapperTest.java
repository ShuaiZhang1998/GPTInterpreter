package com.zs.project.chat;

import com.zs.project.mapper.ChatMapper;
import com.zs.project.model.entry.Chat;
import com.zs.project.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatMapperTest {

    @Autowired
    ChatMapper chatMapper;
    @Autowired
    ChatService chatService;

    /**
     * insert 或者 IService的save都会实现回传
     */
    @Test
    void testChatMapper(){
        Chat chat = new Chat();
//        System.out.println(chat.getConversationID());
        chat.setUserID(25L);
//        chatService.save(chat);
        chatMapper.insert(chat);
        System.out.println(chat.getConversationID());
    }


}
