package com.api.freeapi.service.Impl;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.common.UserException;
import com.api.freeapi.entity.Api;
import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.User;
import com.api.freeapi.entity.UserPrivilege;
import com.api.freeapi.mapper.ApiMapper;
import com.api.freeapi.mapper.MainMapper;
import com.api.freeapi.mapper.UserMapper;
import com.api.freeapi.mapper.UserPrivilegeMapper;
import com.api.freeapi.service.ApiService;
import com.api.freeapi.service.MainService;
import com.api.freeapi.service.UserPrivilegeService;
import com.api.freeapi.service.UserService;
import com.api.freeapi.utils.IPUtil;
import com.api.freeapi.utils.IpAddressUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

import static com.api.freeapi.common.ErrorCode.*;

@Slf4j
@Service
public class ApiServiceImpl  extends ServiceImpl<ApiMapper,Api> implements ApiService {

    @Resource
    private HttpServletRequest request;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ApiMapper apiMapper;
    @Resource
    private UserService userService;
    @Resource
    private UserPrivilegeService userPrivilegeService;

    private HashMap<Object, Object> map = new HashMap<>();

    @Transactional
    @Override
    public ResponseResult getAccessInfo(Integer id, String key) {
        List<User> userList = userService.verifyKey(key);
        if (CollectionUtils.isEmpty(userList)){
            throw new UserException(KEY_ERROR.getErrMsg());
        }
        User user1 = new User();
        Integer userServiceSize = null;
        for (User users:userList) {
            user1.setId(users.getId());
            user1.setIdentity(users.getIdentity());
            user1.setStatus(users.getStatus());
             userServiceSize = users.getSize();
        }
        //判断账户有无被禁用
        if (user1.getStatus() != 1){
            throw new UserException(USER_ERROR.getErrMsg());
        }
        LambdaQueryWrapper<UserPrivilege> userPrivilegeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userPrivilegeLambdaQueryWrapper.eq(UserPrivilege::getUserIdentity,user1.getIdentity());
        List<UserPrivilege> list = userPrivilegeService.list(userPrivilegeLambdaQueryWrapper);
        Integer maxSize = null;
        for (UserPrivilege userPrivilege:list) {
             maxSize = userPrivilege.getMaxSize();
        }
        log.info("当前用户最大调用：{}",maxSize);
        log.info("当前用户实际调用：{}",userServiceSize);
        if (maxSize < userServiceSize){
            throw new UserException(SERVICE_ERROR.getErrMsg());
        }
        log.info("上一次api总调用量 id:{} size:{}",id,userServiceSize);
        userServiceSize = userServiceSize + 1;
        user1.setSize(userServiceSize);
        log.info("User1 ：{}",user1);
        //更新
        userMapper.updateById(user1);
        //统计api总调用次数
        LambdaQueryWrapper<Api> apiQueryWrapper = new LambdaQueryWrapper<>();
        apiQueryWrapper.eq(Api::getId,id);
        List<Api> apiList = apiMapper.selectList(apiQueryWrapper);
        Integer size = null;
        Api api1 = new Api();
        for (Api api:apiList) {
             size = api.getSize();
             api1.setId(api.getId());
        }
        size = size + 1;
        api1.setSize(size);
        log.info("api1 ：{}",api1);
        apiMapper.updateById(api1);
        String ipAddr = IPUtil.getIpAddr(request);
        String address = IpAddressUtils.getIpSource(ipAddr);
        map.put("ip",ipAddr);
        map.put("address",address);
        map.put("number",maxSize-size);
        return ResponseResult.success(map);
    }
}
