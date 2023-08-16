package com.zs.project.mapper;

import com.zs.project.model.entry.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {
    /**
     * 添加用户
     * @param user
     * @return
     */
    @Insert("INSERT INTO user (userName, conversationID, userPassword, isDeleted) " +
            "VALUES (#{user.userName}, #{user.conversationID}, #{user.userPassword}, #{user.isDeleted})")
    @Options(useGeneratedKeys = true, keyProperty = "userID", keyColumn = "userID")
    int addUser(@Param("user") User user);

    /**
     * 根据用户ID查询用户
     * @param userID
     * @return
     */
    @Select("SELECT * FROM user WHERE userID = #{userID}")
    User getUserById(@Param("userID") Long userID);

    /**
     * 根据用户姓名查看用户
     * @param userName
     * @return
     */
    @Select("SELECT * FROM user WHERE userName = #{userName}")
    User getUserByName(@Param("userName") String userName);
}
