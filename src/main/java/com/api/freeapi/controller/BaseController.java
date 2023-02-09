package com.api.freeapi.controller;


import com.api.freeapi.entity.Api;
import com.api.freeapi.entity.UserInfo;
import com.api.freeapi.service.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitBootstrapConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Resource
    protected RedisTemplate redisTemplate;

    @Resource
    protected MainService mainService;
    @Resource
    protected ApiService apiService;
    @Resource
    protected UserPrivilegeService userPrivilegeService;
    @Resource
    protected UserService userService;
    @Resource
    protected AuthenticationService authenticationService;
    @Resource
    protected UserInfoService userInfoService;
    @Resource
    protected AmqpTemplate amqpTemplate;



    //@ModelAttribute 会在此controller的每个方法执行前被执行 ，如果有返回值，则自动将该返回值加入到ModelMap中。
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
    }

}
