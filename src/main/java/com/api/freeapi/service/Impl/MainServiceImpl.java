package com.api.freeapi.service.Impl;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.common.UserException;
import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.User;
import com.api.freeapi.entity.dto.UserDto;
import com.api.freeapi.mapper.MainMapper;
import com.api.freeapi.mapper.UserMapper;
import com.api.freeapi.service.MainService;
import com.api.freeapi.utils.IPUtil;
import com.api.freeapi.utils.IpAddressUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.api.freeapi.common.ErrorCode.KEY_ERROR;
import static com.api.freeapi.common.ErrorCode.PARAMS_ERROR;

@Slf4j
@Service
public class MainServiceImpl  extends ServiceImpl<MainMapper, Context> implements MainService {

    @Resource
    private MainMapper mainMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private HttpServletRequest request;

    private HashMap<Object, Object> map = new HashMap<>();

    @Override
    public ResponseResult insert(UserDto userDto) {
        //判空
        if (StringUtils.isBlank(userDto.getName()) | StringUtils.isBlank(userDto.getEmail()) | StringUtils.isBlank(userDto.getContext())){
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }
        Integer uuid = userDto.getUuid();
        Context context = new Context();
        context.setName(userDto.getName());
        context.setEmail(userDto.getEmail());
        context.setContext(userDto.getContext());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid",uuid);
        List<User> user = userMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(user)){
            throw  new UserException(KEY_ERROR.getErrMsg());
        }
        Integer uid = 0;
        //判断是否有uuid
        for (User user1:user) {
             uid = user1.getId();
        }
        context.setUid(uid);
        String ipAddr = IPUtil.getIpAddr(request);
        String address = IpAddressUtils.getIpSource(ipAddr);
        context.setIp(ipAddr);
        context.setAddress(address);
        mainMapper.insert(context);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult search(String contexts,String key) {
        LambdaQueryWrapper<User> UserQueryWrapper = new LambdaQueryWrapper<>();
        UserQueryWrapper.eq(User::getUuid,key);
        UserQueryWrapper.select(User::getId);
        log.info("key是：{}",key);
        List<User> usersList = userMapper.selectList(UserQueryWrapper);
        //判断是否有key
        if (CollectionUtils.isEmpty(usersList)){
            throw  new UserException(KEY_ERROR.getErrMsg());
        }
        User user = new User();
        usersList.stream().map((item) ->{
           user.setId(item.getId());
           return user;
        }).collect(Collectors.toList());
        log.info("id {}",user.getId());
        LambdaQueryWrapper<Context> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Context::getUid,user.getId());
        queryWrapper.like(Context::getContext,contexts);
        //按照时间倒叙
        queryWrapper.orderByDesc(Context::getCreateTime);
        queryWrapper.select(Context::getEmail,Context::getContext,Context::getCreateTime,Context::getName,Context::getAddress);
        Page<Context> page = new Page<>(1, 4);

        List<Context> contextList = mainMapper.selectList(queryWrapper);
        map.put("list",contextList);
        log.info("模糊查询 {}",contextList);
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult searchAll(String key,int page,int pageSize) {
        Page pageInfo = new Page(page,pageSize);
        log.info("key是：{}",key);
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUuid,key);
        userQueryWrapper.select(User::getId);
        List<User>  usersList = userMapper.selectList(userQueryWrapper);
        //判断是否有key
        if (CollectionUtils.isEmpty(usersList)){
            throw  new UserException(KEY_ERROR.getErrMsg());
        }
        User user = new User();
        usersList.stream().map((item) ->{
            user.setId(item.getId());
            return user;
        }).collect(Collectors.toList());
        log.info("id {}",user.getId());
        LambdaQueryWrapper<Context> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Context::getUid,user.getId());
        //按照时间倒叙
        queryWrapper.orderByDesc(Context::getCreateTime);
        queryWrapper.select(Context::getName,Context::getEmail,Context::getContext,Context::getCreateTime,Context::getAddress);
        //查询
        Page page1 = mainMapper.selectPage(pageInfo, queryWrapper);

        map.put("list",page1);
        return ResponseResult.success(map);
    }
}
