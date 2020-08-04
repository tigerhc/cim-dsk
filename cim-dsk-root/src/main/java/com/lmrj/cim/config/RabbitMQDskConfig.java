package com.lmrj.cim.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nangua
 */
@Configuration
public class RabbitMQDskConfig {

    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Bean
    public String[] operationLogQueue() {
        String[] queueNames = {"C2S.Q.FAB_LOG_D", "C2S.Q.EVENT.DATA","C2S.Q.ALARM.DATA","C2S.Q.STATE.DATA",
                "C2S.Q.PRODUCTIONLOG.DATA", "C2S.Q.OPERATIONLOG.DATA","C2S.Q.RECIPELOG.DATA","C2S.Q.TEMPLOG.DATA",
                "C2S.Q.MEASURE.DATA",
                "C2S.Q.ALARMRPT.DATA"};
        for(String queueName :queueNames){
            Queue queue=new Queue(queueName,true,false,false);
            rabbitAdmin.declareQueue(queue);
        }
        return queueNames;
    }

}

