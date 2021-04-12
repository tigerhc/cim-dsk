package com.lmrj.oven.batchlot.handler;

import com.alibaba.fastjson.JSONObject;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
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
            boolean flag =false;
            ParticleDataBean data = JsonUtil.from(dataJson, ParticleDataBean.class);
            ovnParticleService.insert(data);
            StringBuilder msg =new StringBuilder();
            msg.append("尘埃粒子计数器数值异常:");
            if(data.getParticle03Alarm()==1){
                msg.append("0.3μm,");
                flag=true;
            } if (data.getParticle05Alarm()==1){
                msg.append("0.5μm,");
                flag=true;
            } if (data.getParticle1Alarm()==1){
                msg.append("1μm,");
                flag=true;
            } if (data.getParticle3Alarm()==1){
                msg.append("3μm,");
                flag=true;
            } if (data.getParticle5Alarm()==1){
                msg.append("5μm,");
                flag=true;
            } if (data.getParticle10Alarm()==1){
                msg.append("10μm,");
                flag=true;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("EQP_ID",msg.toString());
            jsonObject.put("ALARM_CODE", "E-0070");
            String jsonString = jsonObject.toJSONString();
            if (flag==true){
            rabbitTemplate.convertAndSend(queueName, jsonString);
            EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
            edcAmsRecord.setCreateDate(data.getStartTime());
            edcAmsRecord.setAlarmName("尘埃粒子计数器数值异常:"+msg);
            edcAmsRecord.setEqpId("APJ-PARTICLE1");
            edcAmsRecord.setAlarmCode("E-0070");
            edcAmsRecord.setAlarmSwitch("1");
            String json = JsonUtil.toJsonString(edcAmsRecord);
            rabbitTemplate.convertAndSend("C2S.Q.ALARMRPT.DATA", json);
            }
        }catch (Exception e){
            log.error("尘埃计数器接收队列数据出错:"+dataJson,e);
        }
    }
}
