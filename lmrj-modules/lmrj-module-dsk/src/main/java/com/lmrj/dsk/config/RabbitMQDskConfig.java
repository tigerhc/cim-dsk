package com.lmrj.dsk.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nangua
 */
@Configuration
public class RabbitMQDskConfig {

    @Bean
    public Queue operationLogQueue() {
        return QueueBuilder.durable("C2S.Q.OPERATIONLOG.DATA").build();
    }

    @Bean
    public Queue productionLogQueue() {
        return QueueBuilder.durable("C2S.Q.PRODUCTIONLOG.DATA").build();
    }

}

