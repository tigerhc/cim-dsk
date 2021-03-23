package com.lmrj.oven.batchlot.handler;

import com.lmrj.oven.batchlot.entity.ParticleDataBean;
import com.lmrj.oven.batchlot.service.IOvnParticleService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OvnParticleHandler {
    @Autowired
    private IOvnParticleService ovnParticleService;

    @RabbitHandler
    @RabbitListener(queues= {"C2S.Q.PARTICLE.DATA"})
    public void handleParticleData(String dataJson){
        try{
            ParticleDataBean data = JsonUtil.from(dataJson, ParticleDataBean.class);
            ovnParticleService.insert(data);
        }catch (Exception e){
            log.error("尘埃计数器接收队列数据出错:"+dataJson,e);
        }
    }
}
