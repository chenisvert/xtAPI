package com.api.freeapi.config;
 
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
 
public class RedissonManager {
 
    @Value("${spring.redis.host}")
    private String address;
    @Value("${spring.redis.password}")
    private String password;
 
    @Bean(name = "myRedisson", destroyMethod = "shutdown")
    public RedissonClient getRedisson() throws Exception {
        Config config = new Config();
        config.useSingleServer().setAddress(address).setPassword(password);
 
        return Redisson.create(config);
    }
 
}