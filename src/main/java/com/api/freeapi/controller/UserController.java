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
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Objects;

import static com.api.freeapi.common.ErrorCode.*;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private HashMap<Object, Object> map = new HashMap<>();

    @GetMapping("/signIn")
    public ResponseResult signInDay() {
        String token = ((HttpServletRequest)request).getHeader("token");
        String username = TokenUtil.getAccount(token);
        log.info("token用户名 ,{}",username);
        return userService.signInDay(username);
    }


    @PostMapping("/login")
    public ResponseResult login(@RequestBody User users) {
        String username = users.getUsername();
        String password = users.getPassword();
        log.info("姓名：{}，密码：{}",username,password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername,username);
        User user1 = userService.getOne(userLambdaQueryWrapper);
        if (Objects.isNull(user1)){
            return ResponseResult.error(USERNAME_ERROR.getErrCode(), USERNAME_ERROR.getErrMsg());
        }
        //加密
        password = MD5Util.getMD5(password);

        log.info("数据库登录密码：{}",user1.getPassword());
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

    @GetMapping("/register")
    public ResponseResult Register(@RequestBody User users) {
        //取出传入的参数
        String username = users.getUsername();
        String password = users.getPassword();
        String email = users.getEmail();

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
