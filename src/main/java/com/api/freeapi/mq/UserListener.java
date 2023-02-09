package com.api.freeapi.mq;

import com.api.freeapi.config.RabbitMQConfig;
import com.api.freeapi.entity.User;
import com.api.freeapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
@RabbitListener(queues = RabbitMQConfig.FANOUT_TIME_QUEUE)
public class UserListener {
    @Resource
    private UserService userService;

    @RabbitHandler
    public void updateLoginTimeListener(User updateTimeLogin) {
        log.info(RabbitMQConfig.FANOUT_TIME_QUEUE+"消费者消息msg:{}<<", updateTimeLogin);
        //更新上一次登录时间
        userService.saveOrUpdate(updateTimeLogin);
    }
}
