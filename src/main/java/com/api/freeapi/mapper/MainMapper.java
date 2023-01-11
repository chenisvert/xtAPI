package com.api.freeapi.mapper;

import com.api.freeapi.entity.Context;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MainMapper extends BaseMapper<Context> {
    //点赞+1
    Integer addGiveThumbsUpById(@Param("id") Integer id);
    //根据id查留言的点赞
    List<Context> selectThumbsUpById(@Param("id") Integer id);

}
