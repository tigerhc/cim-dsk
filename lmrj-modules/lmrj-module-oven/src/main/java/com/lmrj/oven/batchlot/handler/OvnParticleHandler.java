package com.lmrj.oven.batchlot.handler;

import com.alibaba.fastjson.JSONObject;
import com.lmrj.oven.batchlot.entity.ParticleDataBean;
import com.lmrj.oven.batchlot.service.IOvnParticleService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Service
public class OvnParticleHandler {
    @Autowired
    private IOvnParticleService ovnParticleService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    String queueName = "C2S.Q.MSG.MAIL";

    @RabbitHandler
    @RabbitListener(queues= {"C2S.Q.PARTICLE.DATA"})
    public void handleParticleData(String dataJson){
        try{
            ParticleDataBean data = JsonUtil.from(dataJson, ParticleDataBean.class);
            ovnParticleService.insert(data);
            StringBuilder msg =new StringBuilder();
            msg.append("尘埃粒子计数器数值异常:");
            if(data.getParticle03Alarm()==1){
                msg.append("0.3μm,");
            } if (data.getParticle05Alarm()==1){
                msg.append("0.5μm,");
            } if (data.getParticle1Alarm()==1){
                msg.append("1μm,");
            } if (data.getParticle3Alarm()==1){
                msg.append("3μm,");
            } if (data.getParticle5Alarm()==1){
                msg.append("5μm,");
            } if (data.getParticle10Alarm()==1){
                msg.append("10μm,");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("EQP_ID",msg.toString());
            jsonObject.put("ALARM_CODE", "E-0009");
            String jsonString = jsonObject.toJSONString();
            rabbitTemplate.convertAndSend(queueName, jsonString);
        }catch (Exception e){
            log.error("尘埃计数器接收队列数据出错:"+dataJson,e);
        }
    }
}
