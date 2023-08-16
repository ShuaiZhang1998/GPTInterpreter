package com.zs.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.project.model.entry.Chat;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
}