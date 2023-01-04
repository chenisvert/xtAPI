package com.api.freeapi.service;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    //每日签到
    ResponseResult signInDay(String key);
    //验证key
    List<User> verifyKey(String key);
    //重置签到状态（全部）
    Boolean resetSignIn();
    //增加访问量
    ResponseResult setAccessCount(String key);
}
