package com.api.freeapi;


import com.api.freeapi.entity.User;
import com.api.freeapi.service.Impl.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@ServletComponentScan
@EnableTransactionManagement
@SpringBootApplication
public class FreeApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreeApiApplication.class, args);
    }
}
