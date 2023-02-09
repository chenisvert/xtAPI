package com.api.freeapi.mq;


import com.api.freeapi.config.RabbitMQConfig;
import com.api.freeapi.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = RabbitMQConfig.FANOUT_LOG_QUEUE)
public class LogListener {

}
