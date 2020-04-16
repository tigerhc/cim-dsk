package com.lmrj.dsk.edc.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.service.impl
 * @title: ovn_batch_lot服务实现
 * @description: ovn_batch_lot服务实现
 * @author: zhangweijiang
 * @date: 2019-06-09 08:49:15
 * @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
 */
@Service
@Slf4j
public class EdcDskLogHandler {

    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;
    @Autowired
    IEdcDskLogOperationService edcDskLogOperationService;
    @Autowired
    IFabEquipmentStatusService fabEquipmentStatusService;
    @Autowired
    IOvnBatchLotService ovnBatchLotService;
    @Autowired
    private IFabEquipmentService fabEquipmentService;


    //{"eqpId":"OVEN-F-01","eventId":"ON","eventParams":null,"startDate":"2019-11-12 19:31:33 416"}
    //@RabbitHandler
    //@RabbitListener(queues = {"C2S.Q.PRODUCTIONLOG.DATA"})
    public void parseProductionlog(String msg) {
        //String msg = new String(message, "UTF-8");
        System.out.println("接收到的消息" + msg);
        List<EdcDskLogProduction> list = JsonUtil.from(msg, new TypeReference<List<EdcDskLogProduction>>() {
        });
        edcDskLogProductionService.insertBatch(list);
        //if("TEMPERATURE-UPLOAD".equals(edcDskLogProduction.getEventId())){
        //    //"20190623202600.csv"
        //    try {
        //        logger.info("开始解析{}温度曲线文件{}", edcDskLogProduction.getEqpId(), edcDskLogProduction.getEventParams());
        //        ovnBatchLotService.resolveTemperatureFile(edcDskLogProduction.getEqpId(), edcDskLogProduction.getEventParams());
        //    } catch (Exception e) {
        //        e.printStackTrace();
        //    }
        //}
    }

    //@RabbitHandler
    //@RabbitListener(queues = {"C2S.Q.OPERATIONLOG.DATA"})
    public void parseOperationlog(String msg) {
        log.info("recieved message:" + msg);
        //public void cureAlarm(byte[] message) throws UnsupportedEncodingException {
        //    String msg = new String(message, "UTF-8");
        //    System.out.println("接收到的消息"+msg);
        List<EdcDskLogOperation> list = JsonUtil.from(msg, new TypeReference<List<EdcDskLogOperation>>() {
        });
        edcDskLogOperationService.insertBatch(list);
        //edcDskLogOperation.setCreateDate(new Date());
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.TEMPLOG.DATA"})
    public void parseTempHlog(String msg) {
        log.info("recieved message 开始解析{}温度曲线文件 : {} " + msg);
        OvnBatchLot ovnBatchLot = JsonUtil.from(msg, OvnBatchLot.class);
        if(ovnBatchLot != null){
            String eqpId = ovnBatchLot.getEqpId();
            if(StringUtil.isNotBlank(eqpId)){
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
                ovnBatchLot.setOfficeId(fabEquipment.getOfficeId());
                ovnBatchLotService.insert(ovnBatchLot);
            }

        }

    }


}
