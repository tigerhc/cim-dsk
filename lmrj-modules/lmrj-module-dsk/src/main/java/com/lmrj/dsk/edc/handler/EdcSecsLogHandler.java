package com.lmrj.dsk.edc.handler;

import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.evt.entity.EdcEvtDefine;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.service.IEdcEvtDefineService;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.util.lang.ArrayUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    @Autowired
    private IEdcDskLogProductionService edcDskLogProductionService;

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
        // TODO: 2020/7/14 后期针对trm产量事件,可不存储 
        edcEvtRecordService.insert(edcEvtRecord);
        //Mold单独处理
        if("TRM".equals(fabEquipment.getStepCode())){
            handleMoldYield(edcEvtRecord, fabEquipment);
        }

    }
    //处理Mold产量特殊事件
    //通过track in获取设备开始shotcount数
    public void handleMoldYield(EdcEvtRecord evtRecord, FabEquipment fabEquipment){
        String eqpId = evtRecord.getEqpId();
        String[] ceids = {"11201","11202", "11203"};
        String ceid = evtRecord.getEventId();
        if(ArrayUtil.contains(ceids,ceid)){
            fabEquipmentStatusService.increaseYield(eqpId, 12);
            FabEquipmentStatus equipmentStatus = fabEquipmentStatusService.findByEqpId(eqpId);
            // TODO: 2020/7/8 写入 edc_dsk_log_production
            EdcDskLogProduction productionLog = new EdcDskLogProduction();
            productionLog.setEqpId(evtRecord.getEqpId());
            productionLog.setRecipeCode(equipmentStatus.getRecipeCode());
            productionLog.setStartTime(new Date());
            productionLog.setEndTime(new Date());
            productionLog.setDayYield(equipmentStatus.getDayYield());
            productionLog.setLotYield(equipmentStatus.getLotYield());
            productionLog.setDuration(0D);
            //productionLog.setMaterialNo(columns[columnNo++]); //制品的序列号
            //productionLog.setMaterialLotNo(columns[columnNo++]); //制品的批量
            //productionLog.setMaterialModel(columns[columnNo++]);//制品的品番
            //productionLog.setMaterialNo2(columns[columnNo++]); //制品序列
            //productionLog.setOrderNo(columns[columnNo++]);  //作业指示书的订单
            productionLog.setLotNo(equipmentStatus.getLotNo());  //作业指示书的批量
            productionLog.setProductionNo(equipmentStatus.getProductionNo()); //作业指示书的品番
            edcDskLogProductionService.insert(productionLog);

            String eventParams = evtRecord.getEventParams();
            if(eventParams != null){
                String[] params = eventParams.split(",");
                if(params.length == 3){
                    int shotcount = Integer.parseInt(params[0])+Integer.parseInt(params[1])+Integer.parseInt(params[2]);
                    String maxShotCountStr = fabEquipment.getEqpParam().split(",")[0];
                    if( shotcount > Integer.parseInt(maxShotCountStr)){
                        //转发alarm至MQ
                    }
                }
            }
        }


    }

}
