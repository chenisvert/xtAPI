package com.api.freeapi.controller;


import com.api.freeapi.common.ResponseResult;

import com.api.freeapi.common.UserException;
import com.api.freeapi.entity.Authentication;
import com.api.freeapi.entity.User;
import com.api.freeapi.entity.UserInfo;
import com.api.freeapi.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

import static com.api.freeapi.common.ErrorCode.*;
import static com.api.freeapi.common.RedisKey.USER_APP;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private HashMap<Object, Object> map = new HashMap<>();
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedissonUtils redissonUtils;


    @GetMapping("/signIn")
    public ResponseResult signInDay() {
        String token = ((HttpServletRequest) request).getHeader("token");
        String username = TokenUtil.getAccount(token);
        log.info("token用户名 ,{}", username);
        return userService.signInDay(username);
    }


    @GetMapping("/signCount")
    public ResponseResult signCount() {
        String token = ((HttpServletRequest) request).getHeader("token");
        String username = TokenUtil.getAccount(token);
        log.info("token用户名 ,{}", username);
        return userService.signCount(username);
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User users) {
        map.clear();
        String username = users.getUsername();
        String password = users.getPassword();
        //校验入参
        if (StringUtils.isBlank(username) | StringUtils.isBlank(password)) {
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }


        //加密
        password = MD5Util.getMD5(password);

        //查数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        User user1 = userService.getOne(userLambdaQueryWrapper);
        if (Objects.isNull(user1)) {
            return ResponseResult.error(USERNAME_ERROR.getErrCode(), USERNAME_ERROR.getErrMsg());
        }
        log.info("数据库登录密码：{}", user1.getPassword());
        log.info("传入后加密的密码：{}", password);
        System.out.println("--------------" + username.equals(user1.getUsername()));
        System.out.println("----------------" + username.equals(user1.getUsername()));
        if (username.equals(user1.getUsername())) {
            if (!password.equals(user1.getPassword())) {
                return ResponseResult.error(PASSWORD_ERROR.getErrCode(), PASSWORD_ERROR.getErrMsg());
            }
        } else if (username.equals(user1.getUsername())) {
            if (!password.equals(user1.getPassword())) {
                return ResponseResult.error(PASSWORD_ERROR.getErrCode(), PASSWORD_ERROR.getErrMsg());
            }
        } else {
            return ResponseResult.error(USERNAME_ERROR.getErrCode(), USERNAME_ERROR.getErrMsg());

        }
        //数据库登录成功

        log.info("登录成功 方式:{数据库}");
        User userUpDataTime = new User();
        userUpDataTime.setId(user1.getId());
        userUpDataTime.setLastLogin(LocalDateTime.now());
        //更新上一次登录时间
        userService.saveOrUpdate(userUpDataTime);

        redisTemplate.opsForValue().set(USER_APP + "_login_" + user1.getUsername(), user1);
        Long currentTimeMillis = System.currentTimeMillis();
        String token = TokenUtil.sign(username, currentTimeMillis);
        RedisUtil.set(username, currentTimeMillis, TokenUtil.REFRESH_EXPIRE_TIME);
        response.setHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        map.put("token", token);
        return ResponseResult.success(map);
    }

    @PostMapping("/register")
    public ResponseResult Register(@RequestBody User users) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("user_login");
        //取出传入的参数
        String username = users.getUsername();
        String password = users.getPassword();
        String email = users.getEmail();
        if (StringUtils.isBlank(username) | StringUtils.isBlank(password) | StringUtils.isBlank(email)) {
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }

        User user = new User();
        UserInfo userSave = new UserInfo();
        String md5Password = MD5Util.getMD5(password);
        user.setUsername(username);
        user.setPassword(md5Password);
        user.setEmail(email);
        user.setUuid(UUIDUtils.create());
        userService.save(user);
        userSave.setUsername(username);
        userInfoService.save(userSave);
        map.put("msg", "注册成功");
        return ResponseResult.success(map);
    }

    @GetMapping("/nameRepeat/{username}")
    public ResponseResult Repeat(@PathVariable String username) {
        //这里暂时先这样写，后面封装到接口里
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        List<User> list = userService.list(userLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            throw new UserException("用户名已存在");
        }
        return ResponseResult.success();
    }

    @PostMapping("/userRealAuth")
    public ResponseResult Repeat(@RequestBody Authentication authentication) {
        String name = authentication.getName();
        String idCard = authentication.getIdCard();
        if (StringUtils.isBlank(name) | StringUtils.isBlank(idCard)) {
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }
        return userService.changeUserRealAuthSataus(name, idCard);
    }
}
