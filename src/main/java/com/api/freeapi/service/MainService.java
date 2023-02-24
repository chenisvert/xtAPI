package com.api.freeapi.service;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

public interface MainService  extends IService<Context> {
    //校验key授权
    Boolean checkKeyUrl(String username);
    //插入
    ResponseResult insert(UserVO contextDto);
    //根据标题查询
    ResponseResult searchKeyWord(String contexts,String key,Integer page,Integer pageSize);
    //查询所有
    ResponseResult searchPage(String key,int page,int pageSize);
    //点赞
    ResponseResult giveThumbsUp(Integer id);
    //带token的查询
    ResponseResult selectPage(int page,int pageSize);
    //删除留言
    ResponseResult deleteById(Integer id);

    ResponseResult writeDbtoExcel(Integer uid) throws IOException;
}
