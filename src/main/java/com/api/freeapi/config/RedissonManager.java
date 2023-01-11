package com.api.freeapi.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "redisson")
public class RedissonManager {
    private int timeout = 3000;
    private String schema;

    private String address;
    private String password;
    private int connectionPoolSize = 64;
    private int connectionMinimumIdleSize = 10;
    private int slaveConnectionPoolSize = 250;
    private int masterConnectionPoolSize = 250;
    private String[] sentinelAddresses;
    private String masterName;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public int getConnectionMinimumIdleSize() {
        return connectionMinimumIdleSize;
    }

    public void setConnectionMinimumIdleSize(int connectionMinimumIdleSize) {
        this.connectionMinimumIdleSize = connectionMinimumIdleSize;
    }

    public int getSlaveConnectionPoolSize() {
        return slaveConnectionPoolSize;
    }

    public void setSlaveConnectionPoolSize(int slaveConnectionPoolSize) {
        this.slaveConnectionPoolSize = slaveConnectionPoolSize;
    }

    public int getMasterConnectionPoolSize() {
        return masterConnectionPoolSize;
    }

    public void setMasterConnectionPoolSize(int masterConnectionPoolSize) {
        this.masterConnectionPoolSize = masterConnectionPoolSize;
    }

    public String[] getSentinelAddresses() {
        return sentinelAddresses;
    }

    public void setSentinelAddresses(String[] sentinelAddresses) {
        this.sentinelAddresses = sentinelAddresses;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }



    /*测试时mock使用*/
    /*@Bean
    RedissonClient redissonSingle() {
        RedissonClient redissonClient = Mockito.mock(RedissonClient.class);
        return redissonClient;
    }*/

    /**
     * 单机模式
     */
    /*@Bean
                .setConnectionMinimumIdleSize(getConnectionMinimumIdleSize());
    RedissonClient redissonSingle() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(address)
                .setTimeout(timeout)
                .setConnectionPoolSize(connectionPoolSize)
        if (StringUtils.isNotBlank(password)) {
            serverConfig.setPassword(password);
        }
        return Redisson.create(config);
    }*/

    /**
     * 哨兵模式
     * @return
     */
    @Bean(name = "myRedisson", destroyMethod = "shutdown")
    RedissonClient redissonSentinel() throws Exception{
        Config config = new Config();
        log.info("schema:{}",schema);
        log.info("masterName:{}",masterName);
        log.info("sentinelAddresses:{}",sentinelAddresses);
        log.info("password:{}",password);

        // 拼接协议
        String[] sentinelAddressesWithSchema = new String[sentinelAddresses.length];
        for (int i = 0; i < sentinelAddresses.length; i++) {
            sentinelAddressesWithSchema[i] = schema + sentinelAddresses[i];
        }
        SentinelServersConfig serversConfig = config.useSentinelServers()
                .setCheckSentinelsList(false)
                .setMasterName(masterName)
                .addSentinelAddress(sentinelAddressesWithSchema)
                .setMasterConnectionPoolSize(masterConnectionPoolSize)
                .setSlaveConnectionPoolSize(slaveConnectionPoolSize)
                .setMasterConnectionMinimumIdleSize(getConnectionMinimumIdleSize())
                .setSlaveConnectionMinimumIdleSize(getConnectionMinimumIdleSize());


        if (StringUtils.isNotBlank(password)) {
            serversConfig.setPassword(password);
        }
        return Redisson.create(config);
    }
}