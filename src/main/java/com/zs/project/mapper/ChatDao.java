package com.zs.project.mapper;

import com.zs.project.model.entry.Chat;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatDao {
    /**
     * 无ID返回插入
     * @param chat
     * @return
     */
    @Insert("INSERT INTO chat (userID, conversationName, nextIndex, conversationHistory, isDeleted) " +
            "VALUES (#{chat.userID}, #{chat.conversationName}, #{chat.nextIndex}, #{chat.conversationHistory}, #{chat.isDeleted})")
    long add(@Param("chat") Chat chat);

    /**
     * 插入新的聊天记录
     * @param conversationID 聊天ID
     * @param newContent 聊天内容
     * @return
     */
    @Update("UPDATE chat " +
                "SET conversationHistory = JSON_MERGE_PATCH(conversationHistory, #{newContent}) " +
                "WHERE conversationID = #{conversationID}")
    Long updateConversationHistory(@Param("conversationID") Long conversationID, @Param("newContent") String newContent);

    /**
     * 获取某个用户的所有聊天记录
     * @param userID
     * @return
     */
    @Select("select * from chat where userID = #{userID}")
    List<Chat> getAllChatByUserID(@Param("userID") long userID);


    /**
     * 根据聊天ID获取某条对话
     * @param conversationID
     * @return
     */
    @Select("select * from chat where conversationID = #{conversationID}")
    Chat getAllChatByID(@Param("conversationID") long conversationID);

    /**
     * 根据聊天ID维护下一个索引
     * @param conversationID
     * @param nextIndex
     * @return
     */
    @Update("UPDATE chat " +
            "SET nextIndex = #{nextIndex} " +
            "WHERE conversationID = #{conversationID}")
    Long updateConversationIndex(@Param("conversationID") Long conversationID, @Param("nextIndex") Long nextIndex);


}
