package com.api.freeapi.service;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.entity.Api;
import com.api.freeapi.entity.Context;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ApiService extends IService<Api> {
    //访问量
    ResponseResult getAccessInfo(Integer id, String key);
    //获取活跃榜
    ResponseResult getActiveRanking();
    //根据用户名查网站访问量
    ResponseResult getVisitCountByUsername(String username);
}
