package com.lmrj.cim.common.handler;

import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.ms.record.service.IMsMeasureRecordService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class filereceive {
    @Autowired
    IMsMeasureRecordService msMeasureRecordService;
    MsMeasureRecord msMeasureRecord=new MsMeasureRecord();
    @RabbitHandler
    @RabbitListener(queues = {"gxj_test"})
    public void doCommand(String msg) {
        System.out.println(msg);
        msMeasureRecord = JsonUtil.from(msg, MsMeasureRecord.class);
        List<MsMeasureRecord> Mslist= new ArrayList<>();
        Mslist.add(msMeasureRecord);
        msMeasureRecordService.insertBatch(Mslist);
    }
}
