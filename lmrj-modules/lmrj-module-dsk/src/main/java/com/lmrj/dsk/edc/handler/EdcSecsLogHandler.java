package com.lmrj.dsk.edc.handler;

import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.evt.entity.EdcEvtDefine;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.service.IEdcEvtDefineService;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.util.lang.ArrayUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EdcSecsLogHandler {

    @Autowired
    IEdcEvtRecordService edcEvtRecordService;
    @Autowired
    IEdcAmsRecordService edcAmsRecordService;
    @Autowired
    IEdcEvtDefineService edcEvtDefineService;
    @Autowired
    IFabEquipmentService fabEquipmentService;

    @Autowired
    IFabEquipmentStatusService fabEquipmentStatusService;

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.ALARM.DATA"})
    public void handleAlarm(String msg) {
        System.out.println("接收到的消息" + msg);
        EdcAmsRecord edcAmsRecord = JsonUtil.from(msg, EdcAmsRecord.class);
        //if(){
        //
        //}
        edcAmsRecordService.insert(edcAmsRecord);
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.EVENT.DATA"})
    public void handleEvent(String msg) {
        System.out.println("接收到的消息" + msg);
        EdcEvtRecord edcEvtRecord = JsonUtil.from(msg, EdcEvtRecord.class);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(edcEvtRecord.getEqpId());
        String modelId = "";
        if(fabEquipment != null){
            modelId = fabEquipment.getModelId();

            EdcEvtDefine edcEvtDefine = edcEvtDefineService.selectOne(new EntityWrapper<EdcEvtDefine>().eq("event_id", edcEvtRecord.getEventId()).eq("eqp_model_id", modelId));
            if(edcEvtDefine != null){
                edcEvtRecord.setEventDesc(edcEvtDefine.getEventName());
            }
        }
        edcEvtRecordService.insert(edcEvtRecord);
        //Mold单独处理
        if("TRM".equals(fabEquipment.getStepCode())){
            handleMoldYield(edcEvtRecord);
        }

    }
    //处理Mold产量特殊事件
    //通过track in获取设备开始shotcount数
    public void handleMoldYield(EdcEvtRecord evtRecord){
        String eqpId = evtRecord.getEqpId();
        String[] ceids = {"11201","11202", "11203"};
        String ceid = evtRecord.getEventId();
        if(ArrayUtil.contains(ceids,ceid)){
            fabEquipmentStatusService.increaseYield(eqpId, 12);
            // TODO: 2020/7/8 写入 edc_dsk_log_production
        }

    }

}
