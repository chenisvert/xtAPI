package com.api.freeapi.service.Impl;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.common.UserException;
import com.api.freeapi.entity.User;
import com.api.freeapi.entity.UserPrivilege;
import com.api.freeapi.mapper.UserMapper;
import com.api.freeapi.mapper.UserPrivilegeMapper;
import com.api.freeapi.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.UserVariable;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import javax.annotation.Resource;
import java.lang.ref.PhantomReference;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.api.freeapi.common.ErrorCode.*;

@Slf4j
@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    private HashMap<Object, Object> map = new HashMap<>();

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
            identity = user.getIdentity();
            user1.setCreateTime(user.getCreateTime());
        }
        String identitys = "";
        String statusInfo = "";
        //封装账户状态
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
        map.put("status",statusInfo);
        map.put("identity",identitys);
        map.put("createtime",user1.getCreateTime());
        return ResponseResult.success(map);
    }
}
