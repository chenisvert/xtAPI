package com.api.freeapi.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * mq配置类
 * @ClassName RabbitMQConfig
 * @Author chen
 * @Version V1.0
 **/
@Component
public class RabbitMQConfig {
    /**
     * 定义交换机
     */
    public static final String EXCHANGE_SPRINGBOOT_NAME = "/xt_ex";


    /**
     * 短信队列
     */
    public static final String  FANOUT_TIME_QUEUE = "fanout_loginTime_queue";

    /**
     * 日志队列
     */
    public static final String  FANOUT_LOG_QUEUE = "fanout_log_queue";



    /**
     * 配置timeQueue
     *
     * @return
     */
    @Bean
    public Queue timeQueue() {
        return new Queue(FANOUT_TIME_QUEUE);
    }

    /**
     * 配置logQueue
     *
     * @return
     */
    @Bean
    public Queue logQueue() {
        return new Queue(FANOUT_LOG_QUEUE);
    }


    /**
     * 配置fanoutExchange
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_SPRINGBOOT_NAME);
    }


    // 绑定交换机 ex
    @Bean
    public Binding bindingUpdateFanoutExchange(Queue timeQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(timeQueue).to(fanoutExchange);
    }
    @Bean
    public Binding bindingThumbsFanoutExchange(Queue logQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(logQueue).to(fanoutExchange);
    }
}
