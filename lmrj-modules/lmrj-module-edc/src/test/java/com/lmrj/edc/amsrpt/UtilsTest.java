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
    public void putMQMsg(){
        Map<String, String> map = new HashMap<>();
        map.put("alarm", "成功");
        String msg = JsonUtil.toJsonString(map);
        System.out.println(msg);
        Object test = rabbitTemplate.convertSendAndReceive("test_a", msg);
        System.out.println(test.toString());

    }
}
