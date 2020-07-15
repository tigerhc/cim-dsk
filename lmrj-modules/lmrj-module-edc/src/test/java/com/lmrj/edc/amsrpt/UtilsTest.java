package com.lmrj.edc.amsrpt;

import com.lmrj.edc.EdcBootApplication;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.amsrpt.utils.RepeatAlarmUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = EdcBootApplication.class)
@RunWith(SpringRunner.class)
public class UtilsTest {

    @Autowired
    RepeatAlarmUtil repeatAlarmUtil;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Test
    public void putMQMsg() throws Exception {
        EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
        edcAmsRecord.setEqpId("SIM-DM3");
        edcAmsRecord.setAlarmCode("War22002002");
        edcAmsRecord.setAlarmName("固晶部 BDH00-2002  吸嘴计数已达到设定。");
        edcAmsRecord.setLotNo("0514E");
        edcAmsRecord.setLotYield(0);
        edcAmsRecord.setAlarmSwitch("1");
        Date date = new Date();
        edcAmsRecord.setStartDate(date);
        edcAmsRecord.setEndDate(date);
        edcAmsRecord.setCreateDate(date);
        Map<String, Object> map = new HashMap<>();
        map.put("alarm", JsonUtil.toJsonString(edcAmsRecord));
        String msg = JsonUtil.toJsonString(map);
        System.out.println(msg);
        Object test = rabbitTemplate.convertSendAndReceive("C2S.Q.ALARMRPT.DATA", msg);
        System.out.println(test);

    }
}
