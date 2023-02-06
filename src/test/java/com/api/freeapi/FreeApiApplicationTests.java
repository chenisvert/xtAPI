package com.api.freeapi;

import com.api.freeapi.api.Authentication;
import com.api.freeapi.entity.User;
import com.api.freeapi.mapper.UserInfoMapper;
import com.api.freeapi.service.MainService;
import com.api.freeapi.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class FreeApiApplicationTests {
    @Resource
    private UserService userService;
    @Resource
    private MainService mainService;

    @Test
    void contextLoads() throws IOException {
        Boolean check = new Authentication().check("xxx", "xxx");
        if (check){
            System.out.println("实名认证成功");
        }else {
            System.out.println("实名认证失败");
        }

    }
//    @Test
//    void d(){
//        Integer count = userInfoMapper.addThumbsCountByUsername("chen");
//        System.out.println(count);
//    }

    @Test
    void selectDemo(){
//        List<User> chen = userService.lambdaQuery()
//                .select(User::getUsername, User::getEmail)
//                .eq(User::getUsername, "chen")
//                .list();
    }

}
