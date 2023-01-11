package com.api.freeapi.service.Impl;

import com.api.freeapi.api.Authentication;
import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.common.UserException;
import com.api.freeapi.config.RedissonManager;
import com.api.freeapi.entity.User;
import com.api.freeapi.mapper.UserMapper;
import com.api.freeapi.service.AuthenticationService;
import com.api.freeapi.service.UserService;
import com.api.freeapi.utils.RedisUtil;
import com.api.freeapi.utils.TokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.api.freeapi.common.ErrorCode.*;
import static com.api.freeapi.common.RedisKey.USER_APP;

@Slf4j
@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserMapper userMapper;
    @Resource
    private AuthenticationService authenticationService;

    @Resource(description = "myRedisson")
    private RedissonClient redissonClient;
    @Resource
    private RedisTemplate redisTemplate;


    private HashMap<Object, Object> map = new HashMap<>();



    @PostConstruct
    public void InitUser(){
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("user_login");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getId,User::getUsername,User::getPassword,User::getEmail,User::getStatus);
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        User user = new User();
        for (Map<String, Object> userMap:maps) {
            log.info("maps :{}",maps);
            String  username = (String) userMap.get("username");
            user.setUsername(username);
            user.setPassword((String) userMap.get("password"));
            user.setStatus((Integer) userMap.get("status"));
            user.setId((Integer) userMap.get("id"));
            redisTemplate.opsForValue().set(USER_APP + "_login_" + username,user,20 ,TimeUnit.HOURS);

            bloomFilter.add(USER_APP+"_login_"+username);
        }
        log.info("[初始化数据]-用户数据初始化成功");
    }

    @Override
    public ResponseResult changeUserRealAuthSataus(String name,String idCard) {
        String usernameToken = this.getTokenInfo();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,usernameToken);
        queryWrapper.select(User::getId,User::getAuthentication);
        List<User> list = this.list(queryWrapper);

        Integer authentication = null;
        Integer id = null;
        for (User user:list) {
             authentication = user.getAuthentication();
             id = user.getId();
            log.info("======={}",id);
        }
        if (authentication == 1){
            throw new UserException("您已经实名认证了");
        }
        try {
            Boolean checkAuth = new Authentication().check(idCard, name);
            if (!checkAuth){
                throw new UserException("实名认证失败");
            }
            User users = new User();
            users.setId(id);
            users.setAuthentication(1);
            //更改状态
            userMapper.updateById(users);
            //写实名信息表
            authenticationService.addAuth(usernameToken,name,idCard);
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("username",usernameToken);
        map.put("msg","实名认证成功");
        return ResponseResult.success(map);
    }

    @Override
    public String getTokenInfo() {
        String token = ((HttpServletRequest)request).getHeader("token");
        return TokenUtil.getAccount(token);
    }

    @Override
    public ResponseResult signInDay(String username) {
        log.info("signInDay接口 入参：{}",username);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername,username);
        List<User> userList = userMapper.selectList(userLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(userList)){
            throw new UserException(KEY_ERROR.getErrMsg());
        }

        User user = new User();
        for (User user1:userList) {
            user.setSignIn(user1.getSignIn());
            user.setId(user1.getId());
            user.setSize(user1.getSize());
        }
        log.info("当前用户签到状态 {}",user.getSignIn());
        if (user.getSignIn() == 1){
            throw new UserException("您今天已经签到过了");
        }
        //签到奖励服务调用量
        Random ran = new Random();
        int add = ran.nextInt(500);

        if (user.getSize()-add < 0){
            user.setSize(0);
        }else {
            user.setSize(user.getSize() - add);
        }
        //用户没签到执行
        user.setSignIn(1);
        userMapper.updateById(user);


        map.put("msg","签到成功");
        map.put("addSize", add);
        return ResponseResult.success(map);
    }

    @Override
    public List<User> verifyKey(String key) {
        if (StringUtils.isBlank(key)){
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUuid,key);
//        queryWrapper.select(User::getUuid,User::getId,User::getSize,User::getStatus,User::getIdentity,User::getSignIn);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean resetSignIn() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getSignIn,1);
        User user = new User();
        user.setSignIn(0);
        return update(user, queryWrapper);
    }

    @Override
    public ResponseResult setAccessCount(String key) {
        List<User> userList = this.verifyKey(key);
        if (CollectionUtils.isEmpty(userList)){
            throw new UserException(KEY_ERROR.getErrMsg());
        }
        User user = new User();
        for (User user1:userList) {
            user.setId(user1.getId());
            user.setVisitSize(user1.getVisitSize());
        }
        Integer accessCount = user.getVisitSize();
        log.info("未更新前访问量：{}",accessCount);
        if (accessCount >= 0){
            user.setVisitSize(accessCount+1);
        }
        log.info("更新后访问量参数：{}",user);
        userMapper.updateById(user);
        map.put("count",user.getVisitSize());
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult getUserInfo(String username) {
        if (StringUtils.isBlank(username)){
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername,username);
        List<User> userList = userMapper.selectList(userLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(userList)){
            throw new UserException(USERNAME_ERROR.getErrMsg());
        }
        User user1 = new User();
        //遍历
        Integer identity = null;
        for (User user:userList) {
            user1.setPassword(user.getUsername());
            user1.setStatus(user.getStatus());
            user1.setSignIn(user.getSignIn());
            identity = user.getIdentity();
            user1.setCreateTime(user.getCreateTime());
        }
        String identitys = "";
        String statusInfo = "";
        String signln = "";
        //封装账户状态
        if (user1.getSignIn() == 1){
            signln = "已签到";
        }else {
            signln = "未签到";
        }
        if (user1.getStatus() == 1){
            statusInfo = "启用";
        }else {
            statusInfo = "禁用";
        }
        //封装身份返回
        if (identity == 0){
            identitys = "管理员";
        }else if (identity == 1){
            identitys = "普通用户";
        }else {
            identitys = "VIP用户";
        }
        map.put("username",username);
        map.put("identity",identitys);
        map.put("signin",signln);
        map.put("status",statusInfo);
        map.put("createtime",user1.getCreateTime());
        return ResponseResult.success(map);
    }
}