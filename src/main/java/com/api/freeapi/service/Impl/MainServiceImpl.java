package com.api.freeapi.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.common.UserException;
import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.User;
import com.api.freeapi.entity.UserInfo;
import com.api.freeapi.entity.dto.UserDto;
import com.api.freeapi.mapper.MainMapper;
import com.api.freeapi.mapper.UserInfoMapper;
import com.api.freeapi.mapper.UserMapper;
import com.api.freeapi.service.MainService;
import com.api.freeapi.utils.IPUtil;
import com.api.freeapi.utils.IpAddressUtils;
import com.api.freeapi.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.alibaba.druid.sql.ast.expr.SQLSizeExpr.Unit.T;
import static com.api.freeapi.common.ErrorCode.*;
import static com.api.freeapi.common.RedisKey.KEY_SEARCH;

@Slf4j
@Service
public class MainServiceImpl  extends ServiceImpl<MainMapper, Context> implements MainService {

    @Resource
    private MainMapper mainMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisTemplate redisTemplate;

    private HashMap<Object, Object> map = new HashMap<>();

    @Override
    public ResponseResult insert(UserDto userDto) {
        //判空
        if (StringUtils.isBlank(userDto.getName()) | StringUtils.isBlank(userDto.getEmail()) | StringUtils.isBlank(userDto.getContext())){
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }

        Integer uuid = userDto.getUuid();
        //内容缓存
        Set keysPage = redisTemplate.keys(KEY_SEARCH +"_"+"searchPage"+"_" + uuid + "_" + 1 +"_"+"*");
        //关键词查询缓存
        Set keysKeyWord = redisTemplate.keys(KEY_SEARCH +"_"+"KeyWord"+ "_" + uuid + "_" +"_"+1+"_"+"*");
        redisTemplate.delete(keysPage);
        redisTemplate.delete(keysKeyWord);
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
    public ResponseResult searchKeyWord(String contexts,String key,Integer page,Integer pageSize) {

        Object contextPageCache =  RedisUtil.get(KEY_SEARCH +"_"+"KeyWord"+ "_" + key + "_" +"_"+page+"_"+pageSize+"_"+contexts);
        if (contextPageCache != null){
            map.put("list",contextPageCache);
            return ResponseResult.success(map);
        }
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
        queryWrapper.orderByDesc(Context::getId,Context::getThumbsUp);
        queryWrapper.select(Context::getEmail,Context::getContext,Context::getCreateTime,Context::getName,Context::getAddress);
        Page<Context> pageInfo = new Page<>(page, pageSize);

        Page pageContext = mainMapper.selectPage(pageInfo, queryWrapper);
        RedisUtil.set(KEY_SEARCH +"_"+"KeyWord"+ "_" + key + "_" +"_"+page+"_"+pageSize+"_"+contexts,pageContext,60);

        map.put("list",pageContext);
        log.info("模糊查询 {}",pageContext);
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult searchPage(String key,int page,int pageSize) {
        Object pageCache = RedisUtil.get(KEY_SEARCH +"_"+"searchPage"+"_" + key + "_" + page + "_" + pageSize);
        if (!Objects.isNull(pageCache)){
            map.put("list",pageCache);
            return ResponseResult.success(map);
        }
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
        //按照id和点赞量倒叙
        queryWrapper.orderByDesc(Context::getId,Context::getThumbsUp);
        queryWrapper.select(Context::getName,Context::getThumbsUp,Context::getEmail,Context::getContext,Context::getCreateTime,Context::getAddress,Context::getId);
        //查询
        Page page1 = mainMapper.selectPage(pageInfo, queryWrapper);
        List<Context> list = mainMapper.selectMaxThumbsUpById(user.getId());
        Integer maxThumbs = null;
        for (Context context:list) {
            maxThumbs = context.getThumbsUp();
        }
        maxThumbs = maxThumbs - 10;
        RedisUtil.set(KEY_SEARCH +"_"+"searchPage"+"_" + key + "_" + page + "_" + pageSize,page1,300);
        map.put("list",page1);
        map.put("hot",maxThumbs);
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult giveThumbsUp(Integer id) {
        map.clear();
        if (id == null){
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }

        String username = userMapper.selectUserNameByContextId(id);
        if (StringUtils.isBlank(username)){
            throw new UserException(USERNAME_ERROR.getErrMsg());
        }
        mainMapper.addGiveThumbsUpById(id);
        userInfoMapper.addThumbsCountByUsername(username);
        String uuid = userMapper.selectUuidByUserName(username);

        List<Context> contextList = mainMapper.selectThumbsUpById(id);
        Set keysPage = redisTemplate.keys(KEY_SEARCH +"_"+"searchPage"+"_" + uuid +"_*");
        redisTemplate.delete(keysPage);

        Integer thumbs = null;
        for (Context context:contextList) {
             thumbs = context.getThumbsUp();
        }
        map.put("thumbs",thumbs);
        return ResponseResult.success(map);
    }
}
