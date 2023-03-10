package com.api.freeapi.service.Impl;

import com.api.freeapi.api.Authentication;
import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.common.UserException;
import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.User;
import com.api.freeapi.mapper.MainMapper;
import com.api.freeapi.mapper.UserMapper;
import com.api.freeapi.service.AuthenticationService;
import com.api.freeapi.service.UserService;
import com.api.freeapi.utils.RedissonUtils;
import com.api.freeapi.utils.TokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.api.freeapi.common.ErrorCode.*;
import static com.api.freeapi.common.RedisConstants.*;

@Slf4j
@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MainMapper mainMapper;
    @Resource
    private AuthenticationService authenticationService;

    @Resource(description = "myRedisson")
    private RedissonClient redissonClient;
    @Resource
    private RedissonUtils redissonUtils;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    private HashMap<Object, Object> map = new HashMap<>();




    @SneakyThrows
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
        String lockName = AUTH_USER_LOCK+name;
        redissonUtils.lock(lockName);
        boolean isLock = redissonUtils.tryLock(lockName);
        if (!isLock){
            log.info("??????????????????-????????????????????????");
            return ResponseResult.error(ILLEGAl_ERROR.getErrCode(),ILLEGAl_ERROR.getErrMsg());
        }
        try {
            if (authentication == 1) {
                throw new UserException("????????????????????????");
            }
            Boolean checkAuth = new Authentication().check(idCard, name);
            if (!checkAuth) {
                throw new UserException("??????????????????-????????????:????????????");
            }
            User users = new User();
            users.setId(id);
            users.setAuthentication(1);
            //????????????
            userMapper.updateById(users);
            //??????????????????
            authenticationService.addAuth(usernameToken, name, idCard);
            map.put("username", usernameToken);
            map.put("msg", "??????????????????");
            return ResponseResult.success(map);
        }finally {
            redissonUtils.unlock(lockName);
        }
    }

    @Override
    public String getTokenInfo() {
        String token = ((HttpServletRequest)request).getHeader("token");
        return TokenUtil.getAccount(token);
    }

    @Override
    public Integer getUidByUsername(String username) {
        return userMapper.queryUidByUsername(username);
    }

    @Override
    public Integer getMessageCountById(String id) {
        if (id == null){
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,id);
        return this.count(queryWrapper);
    }

    @Override
    public String selectUserKeyByUserName(String username) {
        return userMapper.selectUuidByUserName(username);
    }



    @Override
    public ResponseResult signInDay(String username) {
        map.clear();
        log.info("signInDay?????? ?????????{}",username);
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
        log.info("???????????????????????? {}",user.getSignIn());
        if (user.getSignIn() == 1){
            throw new UserException("???????????????????????????");
        }

        LocalDateTime now = LocalDateTime.now();
        String keSuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY+username+keSuffix;
        //?????????????????????????????????
        int dayOfMonth = now.getDayOfMonth();
        //??????redis SETBIT key offset 1
        stringRedisTemplate.opsForValue().setBit(key,dayOfMonth -1 ,true);


        //???????????????????????????
        Random ran = new Random();
        int add = ran.nextInt(120);

        if (user.getSize()-add < 0){
            user.setSize(0);
        }else {
            user.setSize(user.getSize() - add);
        }
        //?????????????????????
        user.setSignIn(1);
        userMapper.updateById(user);


        map.put("msg","????????????");
        map.put("addSize", add);
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult signCount(String username) {
        LocalDateTime now = LocalDateTime.now();
        String keSuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY+username+keSuffix;
        int dayOfMonth = now.getDayOfMonth();
        List<Long> result =  stringRedisTemplate.opsForValue().bitField(
                key,
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0)
        );
        if (result == null || result.isEmpty()){
            //????????????????????????
            map.put("sign", 0);
            return ResponseResult.success(map);
        }
        Long num = result.get(0);
        if (num == null || num == 0){
            map.put("sign", 0);
            return ResponseResult.success(map);
        }
        map.put("sign", num);
        return ResponseResult.success(map);
    }

    @Override
    public List<User> verifyKey(String key) {
        map.clear();
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
        Integer id = null;
        for (User user1:userList) {
             id = user1.getId();
        }
        log.info("setAccessCount ????????????id???{}",id);
        //??????????????????
        Boolean status = this.checkUserStatus(id);
        if (!status){
            throw new UserException(USER_ERROR.getErrMsg());
        }
        //????????????
        userMapper.addVisitCountById(id);
        Integer count = userMapper.selectVisitCountById(id);

        map.put("count",count);
        return ResponseResult.success(map);
    }

    @Override
    public Boolean checkUserStatus(Integer id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,id);
        queryWrapper.select(User::getStatus);
        List<User> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        Integer status = null;
        for (User user:list) {
             status = user.getStatus();
        }
        if (status == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public ResponseResult getUserInfo(String username) {
        map.clear();
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
        //??????
        Integer identity = null;
        DateTimeFormatter dfDateTime = DateTimeFormatter.ofPattern("yyyy???MM???dd??? HH???mm???ss???");
        String zcTime = null;
        String loginTime = null;
        for (User user:userList) {
            user1.setPassword(user.getUsername());
            user1.setStatus(user.getStatus());
            user1.setSignIn(user.getSignIn());
            user1.setId(user.getId());
            user1.setAuthentication(user.getAuthentication());
            loginTime = dfDateTime.format(user.getLastLogin());
            identity = user.getIdentity();
             zcTime = dfDateTime.format(user.getCreateTime());
        }

        LambdaQueryWrapper<Context> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Context::getUid,user1.getId());
        Integer count = mainMapper.selectCount(queryWrapper);
        String identitys = "";
        String statusInfo = "";
        String signln = "";
        String authStatus = "";
        //??????????????????
        if (user1.getAuthentication() == 1){
            authStatus = "?????????";
        }else {
            authStatus = "?????????";
        }
        if (user1.getSignIn() == 1){
            signln = "?????????";
        }else {
            signln = "?????????";
        }
        if (user1.getStatus() == 1){
            statusInfo = "??????";
        }else {
            statusInfo = "??????";
        }
        //??????????????????
        if (identity == 0){
            identitys = "?????????";
        }else if (identity == 1){
            identitys = "????????????";
        }else {
            identitys = "VIP??????";
        }
        map.put("username",username);
        map.put("identity",identitys);
        map.put("signin",signln);
        map.put("auth",authStatus);
        map.put("count",count);
        map.put("status",statusInfo);
        map.put("createtime",zcTime);
        map.put("lastlogin",loginTime);
        return ResponseResult.success(map);
    }
}
