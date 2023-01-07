package com.api.freeapi.controller;


import com.api.freeapi.common.ResponseResult;

import com.api.freeapi.entity.User;
import com.api.freeapi.utils.MD5Util;
import com.api.freeapi.utils.RedisUtil;
import com.api.freeapi.utils.TokenUtil;
import com.api.freeapi.utils.UUIDUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.NonNull;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.api.freeapi.common.ErrorCode.*;
import static com.api.freeapi.common.RedisKey.Search_Key;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private HashMap<Object, Object> map = new HashMap<>();

    @GetMapping("/signIn/{key}")
    public ResponseResult signInDay(@PathVariable String key) {
        return userService.signInDay(key);
    }



    @GetMapping("/login/{username}/{password}")
    public ResponseResult login(@PathVariable String username,@PathVariable String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername,username);
        User user1 = userService.getOne(userLambdaQueryWrapper);
        System.out.println(user1);
        if(username.equals(user1.getUsername())) {
            if (!password.equals(user1.getPassword())) {
                return ResponseResult.error(PASSWORD_ERROR.getErrCode(), PASSWORD_ERROR.getErrMsg());
            }
        }else if (username.equals(user1.getUsername())) {
            if (!password.equals(user1.getPassword())) {
                return ResponseResult.error(PASSWORD_ERROR.getErrCode(), PASSWORD_ERROR.getErrMsg());

            }
        } else {
            return ResponseResult.error(USERNAME_ERROR.getErrCode(), USERNAME_ERROR.getErrMsg());

        }


        Long currentTimeMillis = System.currentTimeMillis();
        String token = TokenUtil.sign(username, currentTimeMillis);
        RedisUtil.set(username, currentTimeMillis, TokenUtil.REFRESH_EXPIRE_TIME);
        response.setHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        map.put("token",token);
        return ResponseResult.success(map);
    }

    @GetMapping("/register/{username}/{password}/{email}")
    public ResponseResult Register(@PathVariable String username,@PathVariable String password,@PathVariable String email) {
        User user = new User();
        String md5Password = MD5Util.getMD5(password);
        user.setUsername(username);
        user.setPassword(md5Password);
        user.setEmail(email);
        user.setUuid(UUIDUtils.create());
        userService.save(user);
        map.put("msg","注册成功");
        return ResponseResult.success(map);
    }
}
