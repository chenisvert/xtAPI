package com.api.freeapi;

import com.api.freeapi.api.Authentication;
import com.api.freeapi.mapper.UserInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class FreeApiApplicationTests {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Test
    void contextLoads() throws IOException {
        Boolean check = new Authentication().check("330109200605195134", "陈佳杰");
        if (check){
            System.out.println("实名认证成功");
        }else {
            System.out.println("实名认证失败");
        }

    }
    @Test
    void d(){
        Integer count = userInfoMapper.addThumbsCountByUsername("chen");
        System.out.println(count);
    }

}
