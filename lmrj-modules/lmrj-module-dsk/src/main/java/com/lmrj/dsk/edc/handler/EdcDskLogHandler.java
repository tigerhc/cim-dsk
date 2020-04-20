package com.lmrj.dsk.edc.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
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
    @Autowired
    IEdcEvtRecordService edcEvtRecordService;
    @Autowired
    IEdcAmsRecordService edcAmsRecordService;
    @Autowired
    IEdcDskLogRecipeService edcDskLogRecipeService;


    //{"eqpId":"OVEN-F-01","eventId":"ON","eventParams":null,"startDate":"2019-11-12 19:31:33 416"}
    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.PRODUCTIONLOG.DATA"})
    public void parseProductionlog(String msg) {
        //String msg = new String(message, "UTF-8");
        System.out.println("接收到的消息" + msg);
        List<EdcDskLogProduction> edcDskLogProductionList = JsonUtil.from(msg, new TypeReference<List<EdcDskLogProduction>>() {});

        if(edcDskLogProductionList != null && edcDskLogProductionList.size()>0){
            EdcDskLogProduction edcDskLogProduction0 = edcDskLogProductionList.get(0);
            String eqpId = edcDskLogProduction0.getEqpId();
            if(StringUtil.isNotBlank(eqpId)){
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
                edcDskLogProductionList.forEach(edcDskLogProduction -> {
                    edcDskLogProduction.setEqpNo(fabEquipment.getEqpNo());
                    edcDskLogProduction.setEqpModelId(fabEquipment.getModelId());
                    edcDskLogProduction.setEqpModelName(fabEquipment.getModelName());
                });
            }

            edcDskLogProductionService.insertBatch(edcDskLogProductionList);
        }

    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.OPERATIONLOG.DATA"})
    public void parseOperationlog(String msg) {
        log.info("recieved message:" + msg);
        //public void cureAlarm(byte[] message) throws UnsupportedEncodingException {
        //    String msg = new String(message, "UTF-8");
        //    System.out.println("接收到的消息"+msg);
        List<EdcDskLogOperation> edcDskLogOperationlist = JsonUtil.from(msg, new TypeReference<List<EdcDskLogOperation>>() {});

        if(edcDskLogOperationlist == null ||  edcDskLogOperationlist.size() == 0){
            return;
        }

        EdcDskLogOperation edcDskLogOperation0 = edcDskLogOperationlist.get(0);
        String eqpId = edcDskLogOperation0.getEqpId();
        if(StringUtil.isNotBlank(eqpId)){
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            edcDskLogOperationlist.forEach(edcDskLogOperation -> {
                edcDskLogOperation.setEqpNo(fabEquipment.getEqpNo());
                edcDskLogOperation.setEqpModelId(fabEquipment.getModelId());
                edcDskLogOperation.setEqpModelName(fabEquipment.getModelName());
            });
        }

        edcDskLogOperationService.insertBatch(edcDskLogOperationlist);

        //插入event或者alarm中
        //(エラーや装置の稼働変化)
        //0:停止中
        //1:自動運転開始(再開含む)　
        //2:ALM発生
        //3:製品待ち/材料待ち
        //4:電源ON時
        //5:電源OFF時
        //6:マニュアル1サイクル運転開始
        //7:マニュアル1サイクル運転停止
        //※項目4～7は装置によるため別途協議
        List<EdcEvtRecord> edcEvtRecordList = Lists.newArrayList();
        List<EdcAmsRecord> edcAmsRecordList = Lists.newArrayList();
        String status = "";
        for(EdcDskLogOperation edcDskLogOperation: edcDskLogOperationlist){
            String eventId = edcDskLogOperation.getEventId();
            if("2".equals(eventId)){
                EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
                edcAmsRecord.setEqpId(edcDskLogOperation.getEqpId());
                edcAmsRecord.setAlarmCode(edcDskLogOperation.getAlarmCode());
                edcAmsRecord.setAlarmName(edcDskLogOperation.getEventDetail());
                edcAmsRecord.setAlarmSwitch("1");
                edcAmsRecord.setStartDate(edcDskLogOperation.getCreateDate());
                edcAmsRecordList.add(edcAmsRecord);
                status = "ALARM";
            }else{
                EdcEvtRecord edcEvtRecord = new EdcEvtRecord();
                edcEvtRecord.setEqpId(edcDskLogOperation.getEqpId());
                edcEvtRecord.setEventId(eventId);
                edcEvtRecord.setEventDesc(edcDskLogOperation.getEventName());
                edcEvtRecord.setEventParams(edcDskLogOperation.getEventDetail());
                edcEvtRecord.setStartDate(edcDskLogOperation.getCreateDate());
                edcEvtRecordList.add(edcEvtRecord);
                if("0".equals(eventId)||"7".equals(eventId)){
                    status = "STOP";
                }else if("1".equals(eventId)||"6".equals(eventId)){
                    status = "RUN";
                }else if("3".equals(eventId)){
                    status = "IDLE";
                }
            }
        }
        if(edcEvtRecordList.size() != 0){
            edcEvtRecordService.insertBatch(edcEvtRecordList);
        }
        if(edcAmsRecordList.size() != 0){
            edcAmsRecordService.insertBatch(edcAmsRecordList);
        }
        if(StringUtil.isNotBlank(status)){
            fabEquipmentStatusService.updateStatus(edcDskLogOperationlist.get(0).getEqpId(),status);
        }
        //edcDskLogOperation.setCreateDate(new Date());
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.RECIPELOG.DATA"})
    public void parseRecipelog(String msg) {
        log.info("recieved message 开始解析{}recipe文件 : {} " + msg);
        List<EdcDskLogRecipe> edcDskLogRecipeList = JsonUtil.from(msg, new TypeReference<List<EdcDskLogRecipe>>() {});
        if(edcDskLogRecipeList == null ||  edcDskLogRecipeList.size() == 0){
            return;
        }
        edcDskLogRecipeService.insertBatch(edcDskLogRecipeList);
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.TEMPLOG.DATA"})
    public void parseTempHlog(String msg) {
        log.info("recieved message 开始解析{}温度曲线文件 : {} " + msg);
        OvnBatchLot ovnBatchLot = JsonUtil.from(msg, OvnBatchLot.class);
        if(ovnBatchLot == null){
            return;
        }
        String eqpId = ovnBatchLot.getEqpId();
        if(StringUtil.isNotBlank(eqpId)){
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            ovnBatchLot.setOfficeId(fabEquipment.getOfficeId());
            ovnBatchLotService.insert(ovnBatchLot);
        }
    }


}
