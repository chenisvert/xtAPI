package com.api.freeapi.service;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.entity.Api;
import com.api.freeapi.entity.Context;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ApiService extends IService<Api> {
    ResponseResult getUserInfo(Integer id, String key);
}
