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

    @AccessLimit(seconds = 1, maxCount = 5)
    @GetMapping("/setAccessCount/{key}")
    public ResponseResult setAccessCount(@PathVariable String key) {
        return userService.setAccessCount(key);
    }
    @GetMapping("/getUserInfo/{username}")
    public ResponseResult getUserInfo(@PathVariable String username) {
        return userService.getUserInfo(username);
    }

    @GetMapping("/getMessageRanking")
    public ResponseResult getMessageRanking() {
        return apiService.getActiveRanking();
    }

    @GetMapping("/getMessageCount/{username}")
    public ResponseResult getVisitCountByUsername(@PathVariable String username) {
        return apiService.getVisitCountByUsername(username);
    }
}
