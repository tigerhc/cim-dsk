package com.lmrj.dsk.edc.handler;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.lmrj.dsk.DskBootApplication;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import com.lmrj.util.mapper.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest(classes = DskBootApplication.class)
@RunWith(SpringRunner.class)
public class EdcSecsLogHandlerTest {
    @Autowired
    IEdcEvtRecordService edcEvtRecordService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 修复event handler处理异常导致数据重复
     */
    @Test
    public void handleEvent() {
        List<EdcEvtRecord> list = edcEvtRecordService.selectList(new EntityWrapper<EdcEvtRecord>().eq("eqp_id", "SIM-TRM1")
                .eq("event_id", "11203").gt("start_date", "2020-08-10 16:28:41"));
        Map<String, String> map = Maps.newHashMap();
        for (EdcEvtRecord edcEvtRecord : list) {
            edcEvtRecordService.deleteById(edcEvtRecord.getId());
            if(map.get(edcEvtRecord.getEqpId()+edcEvtRecord.getStartDate()) == null){
                String evtJson = JsonUtil.toJsonString(edcEvtRecord);
                if (!"[]".equals(evtJson)) {
                    rabbitTemplate.convertAndSend("C2S.Q.EVENT.DATA", evtJson);
                }
            }else{
                map.put(edcEvtRecord.getEqpId()+edcEvtRecord.getStartDate(),"1");
            }

        }

    }
}
