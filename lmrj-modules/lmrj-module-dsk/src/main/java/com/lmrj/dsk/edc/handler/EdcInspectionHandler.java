package com.lmrj.dsk.edc.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lmrj.dsk.eqplog.entity.EdcDskLogInspection;
import com.lmrj.dsk.eqplog.service.IEdcDskLogInspectionService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EdcInspectionHandler {

    @Autowired
    IEdcDskLogInspectionService edcDskLogInspectionService;

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.INSPECTIONLOG.DATA"})
    public void handleAlarm(String msg) {
        log.info("C2S.Q.INSPECTIONLOG.DATA接收到的消息" + msg);
        try {
            List<EdcDskLogInspection> inspectionList = JsonUtil.from(msg, new TypeReference<List<EdcDskLogInspection>>() {
            });
            if (inspectionList.size()>0) {
                edcDskLogInspectionService.insertBatch(inspectionList,500);
            }
        } catch (Exception e) {
            log.error("INSPECTIONLOG 数据接收入库出错", e);
            e.printStackTrace();
        }

    }
}
