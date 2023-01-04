package com.api.freeapi.controller;

import com.api.freeapi.common.ResponseResult;
import lombok.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.api.freeapi.common.RedisKey.Search_Key;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @GetMapping("/signIn/{key}")
    public ResponseResult signInDay(@PathVariable String key) {
        return userService.signInDay(key);
    }

    @GetMapping("/setAccessCount/{key}")
    public ResponseResult setAccessCount(@PathVariable String key) {
        return userService.setAccessCount(key);
    }
}
