package com.api.freeapi.service.Impl;

import com.api.freeapi.entity.Authentication;
import com.api.freeapi.mapper.AuthenticationMapper;
import com.api.freeapi.service.AuthenticationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl extends ServiceImpl<AuthenticationMapper, Authentication> implements AuthenticationService {
    @Override
    public void addAuth(String username,String name,String idCard) {
        Authentication authentication = new Authentication();
        authentication.setUsername(username);
        authentication.setName(name);
        authentication.setIdCard(idCard);
        //保存
        this.save(authentication);
    }
}
