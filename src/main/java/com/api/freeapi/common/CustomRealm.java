package com.api.freeapi.common;

import com.api.freeapi.entity.Permissions;
import com.api.freeapi.entity.User;
import com.api.freeapi.service.PermissionsService;
import com.api.freeapi.service.UserService;
import com.api.freeapi.utils.TokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @ Program       :  com.ljnt.blog.config.CustomRealm
 * @ Description   :  自定义Realm，实现Shiro认证
 * @ Author        :  lj
 * @ CreateDate    :  2020-2-4 18:15
 */
@Component
public class CustomRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;
    @Resource
    private PermissionsService permissionsService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("用户授权");
        String username= TokenUtil.getAccount(principalCollection.toString());
        SimpleAuthorizationInfo info= new SimpleAuthorizationInfo();

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper();
        userLambdaQueryWrapper.eq(User::getUsername,username);
        User user = userService.getOne(userLambdaQueryWrapper);
        //拿权限
        String permissions = permissionsService.getPermissions(user.getIdentity());

        if (username.equals(user.getUsername())){
            Set<String> role=new HashSet<>();
            role.add(permissions);
            info.setRoles(role);
        }else {
            Set<String> role=new HashSet<>();
            role.add(permissions);
            info.setRoles(role);
        }
        return info;
    }

    /**
     * 用户身份认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("身份认证");
        String token= (String) authenticationToken.getCredentials();
        String username= TokenUtil.getAccount(token);
        System.out.println(username);
        //这里要去数据库查找是否存在该用户，这里直接放行
        if (username==null){
            throw new AuthenticationException("认证失败！");
        }
        return new SimpleAuthenticationInfo(token,token,"MyRealm");
    }
}