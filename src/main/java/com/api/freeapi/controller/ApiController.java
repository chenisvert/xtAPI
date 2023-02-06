package com.api.freeapi.controller;

import com.api.freeapi.anno.AccessLimit;
import com.api.freeapi.common.ResponseResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {


    @GetMapping("/getAccessInfo/{key}")
    public ResponseResult getAccessInfo(@PathVariable String key){
        return apiService.getAccessInfo(1, key);
    }

    @AccessLimit(sec = 1, limit = 6)
    @GetMapping("/setAccessCount/{key}")
    public ResponseResult setAccessCount(@PathVariable String key) {
        return userService.setAccessCount(key);
    }

    @AccessLimit(sec = 1, limit = 5)
    @GetMapping("/getUserInfo/{username}")
    public ResponseResult getUserInfo(@PathVariable String username) {
        return userService.getUserInfo(username);
    }

    @AccessLimit(sec = 1, limit = 3)
    @GetMapping("/getMessageRanking")
    public ResponseResult getMessageRanking() {
        return apiService.getActiveRanking();
    }

    @AccessLimit(sec = 1, limit = 5)
    @GetMapping("/getMessageCount/{username}")
    public ResponseResult getVisitCountByUsername(@PathVariable String username) {
        return apiService.getVisitCountByUsername(username);
    }
}
