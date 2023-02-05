package com.api.freeapi.config;

import com.api.freeapi.hander.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/***
 *
 * 将拦截器注册到容器中(访问控制)
 * @Author chen
 * @Date  18:05
 * @Param
 * @Return
 * @Since version-11

 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private SessionInterceptor sessionInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(sessionInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}