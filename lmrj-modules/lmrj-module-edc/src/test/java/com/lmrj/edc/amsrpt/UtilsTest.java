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
import org.springframework.test.context.junit4.SpringRunner;

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
        List<EdcAmsRecord> edcAmsRecordList = repeatAlarmUtil.getEdcAmsRecordList();
        Map<String, Object> map = new HashMap<>();
        for (EdcAmsRecord edcAmsRecord:edcAmsRecordList) {
            map.put("edcAmsRecord", edcAmsRecord);
            String msg = JsonUtil.toJsonString(map);
            rabbitTemplate.convertAndSend("test_a", msg);
        }

    }
}
