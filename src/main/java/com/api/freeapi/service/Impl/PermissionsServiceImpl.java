package com.api.freeapi.service.Impl;

import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.Permissions;
import com.api.freeapi.mapper.MainMapper;
import com.api.freeapi.mapper.PermissionsMapper;
import com.api.freeapi.service.MainService;
import com.api.freeapi.service.PermissionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements PermissionsService {

    @Resource
    private PermissionsMapper permissionsMapper;

    @Override
    public String getPermissions(Integer identity) {
        LambdaQueryWrapper<Permissions> permissionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        permissionsLambdaQueryWrapper.eq(Permissions::getIdentity,identity);
        List<Permissions> permissionsList = permissionsMapper.selectList(permissionsLambdaQueryWrapper);
        String permissions = null;
        for (Permissions permissionss:permissionsList) {
            permissions = permissionss.getPerms();
        }
        return permissions;
    }
}
