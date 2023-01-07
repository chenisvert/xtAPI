package com.api.freeapi;


import org.springframework.boot.SpringApplication;
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
