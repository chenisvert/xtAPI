package com.api.freeapi.service;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.dto.UserDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface MainService  extends IService<Context> {
    //插入
    ResponseResult insert(UserDto contextDto);
    //根据标题查询
    ResponseResult search(String contexts,String key);
    //查询所有
    ResponseResult searchAll(String key,int page,int pageSize);
}
