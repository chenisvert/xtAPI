package com.api.freeapi.service;


import com.api.freeapi.entity.Permissions;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PermissionsService extends IService<Permissions> {
    //根据身份查权限
    String getPermissions(Integer identity);
}
