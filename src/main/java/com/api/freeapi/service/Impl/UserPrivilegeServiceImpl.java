package com.api.freeapi.service.Impl;

import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.UserPrivilege;
import com.api.freeapi.mapper.MainMapper;
import com.api.freeapi.mapper.UserPrivilegeMapper;
import com.api.freeapi.service.MainService;
import com.api.freeapi.service.UserPrivilegeService;
import com.api.freeapi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserPrivilegeServiceImpl  extends ServiceImpl<UserPrivilegeMapper, UserPrivilege> implements UserPrivilegeService {
}
