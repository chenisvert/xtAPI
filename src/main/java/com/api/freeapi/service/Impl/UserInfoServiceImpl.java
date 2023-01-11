package com.api.freeapi.service.Impl;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.common.UserException;
import com.api.freeapi.entity.Permissions;
import com.api.freeapi.entity.UserInfo;
import com.api.freeapi.mapper.PermissionsMapper;
import com.api.freeapi.mapper.UserInfoMapper;
import com.api.freeapi.service.PermissionsService;
import com.api.freeapi.service.UserInfoService;
import com.api.freeapi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.api.freeapi.common.ErrorCode.USERNAME_ERROR;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public void addThumbsCountByUsername(String username) {
        Integer count = userInfoMapper.addThumbsCountByUsername(username);
        if (count >= 0){
            throw new UserException(USERNAME_ERROR.getErrMsg());
        }
    }
}


