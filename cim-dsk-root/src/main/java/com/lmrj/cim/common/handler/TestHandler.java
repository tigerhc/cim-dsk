package com.lmrj.cim.common.handler;

import com.lmrj.cim.CimBootApplication;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.ms.record.service.IMsMeasureRecordService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class TestHandler {
    @Autowired
    IMsMeasureRecordService msMeasureRecordService;

    @RabbitHandler
    @RabbitListener(queues = "zx_lxy")
    public  void TestCommand(String msg) {
        String str=JsonUtil.from(msg,String.class);
        String a[]=str.split(",");

        MsMeasureRecord msMeasureRecord=new MsMeasureRecord();
        msMeasureRecord.setRecordId(a[1]);
        List<MsMeasureRecord> ms=new ArrayList<>();
        ms.add(msMeasureRecord);
    msMeasureRecordService.insertBatch(ms);
        System.out.println(msMeasureRecord);

    }
}
