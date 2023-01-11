package com.api.freeapi.service;

import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserInfoService extends IService<UserInfo> {

    //根据用户名加总点赞
    void addThumbsCountByUsername(String username);
}
