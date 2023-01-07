package com.api.freeapi.controller;

import com.api.freeapi.common.ResponseResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.api.freeapi.common.RedisKey.Search_Key;

@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {

    @GetMapping("/getUserInfo/{key}")
    public ResponseResult getUserInfo(@PathVariable String key){
        return apiService.getUserInfo(1, key);
    }
    @GetMapping("/setAccessCount/{key}")
    public ResponseResult setAccessCount(@PathVariable String key) {
        return userService.setAccessCount(key);
    }

}
