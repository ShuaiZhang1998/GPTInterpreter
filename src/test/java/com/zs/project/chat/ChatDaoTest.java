package com.zs.project.chat;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.project.mapper.ChatDao;
import com.zs.project.model.entry.Chat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ChatDaoTest {
    @Autowired
    ChatDao chatDao;


    @Test
    public void testInsert() {
        Chat chat = new Chat();
        chat.setUserID(1L);
        chat.setConversationID(1689949675463385090L);
        chat.setConversationName("Test");
        chat.setNextIndex(1L);
        chat.setConversationHistory("{\"0\":\"hell!\"}");
        chat.setIsDeleted(0);
        long add = chatDao.add(chat);
        System.out.println(add);
    }

    @Test
    public void testUpdate() throws Exception{
        // 创建一个map来存储你的数据
        Map<String, String> map = new HashMap<>();
        map.put("1", "hello!");
        // 使用ObjectMapper将map转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(map);
        System.out.println(json);
        chatDao.updateConversationHistory(1689949675463385092L,json);
    }


    @Test
    public void testQueryChat(){
        List<Chat> chats = chatDao.getAllChatByUserID(1L);
        Chat chats1 = chatDao.getAllChatByID(100L);
        System.out.println(chats);
        System.out.println(chats1);
        if(chats1==null)
            System.out.println("null");
    }

    @Test
    public void testIndex(){
        chatDao.updateConversationIndex(1689958402992119810L,1L);
    }





}
