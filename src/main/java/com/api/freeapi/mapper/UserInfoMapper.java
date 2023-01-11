package com.api.freeapi.mapper;

import com.api.freeapi.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    //根据用户名加用户总点赞量
    Integer addThumbsCountByUsername(@Param("username") String username);
    //根据用户名查总点赞量
    List<UserInfo> selectThumbsCountByUsername(@Param("username") String username);
}
