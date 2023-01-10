package com.api.freeapi.mapper;

import com.api.freeapi.entity.Authentication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthenticationMapper extends BaseMapper<Authentication> {
}
