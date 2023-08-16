-- CREATE DATABASE Chat_database CHARACTER SET utf8mb4;
use Chat_database;
DROP TABLE IF EXISTS user;
-- 用户表
create table if not exists user(
                                   userID BIGINT primary key auto_increment ,
                                   userName varchar(12) unique not null ,
    conversationID BIGINT default null,
    userPassword varchar(64) not null ,
    isDeleted tinyint default 0 ,
    index idx_userID (userID),
    index ids_userName(userName),
    index idx_conversationID(conversationID)
    ) comment '用户表' collate = utf8mb4_unicode_ci;

create table if not exists chat(
                                   conversationID BIGINT primary key auto_increment,
                                   userID BIGINT not null ,
                                   conversationName varchar(64) default 'Na',
    nextIndex BIGINT default 0,
    conversationHistory Json default null,
    isDeleted tinyint default 0,
    index conversation_ID (conversationID),
    index user_id (userID),
    index conversation_name (conversationName)
    ) comment '聊天表' collate = utf8mb4_unicode_ci