package com.lmrj.dsk.edc.handler;

import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.evt.entity.EdcEvtDefine;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.service.IEdcEvtDefineService;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.lang.ArrayUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class EdcSecsLogHandler {
    @Autowired
    private IEdcEvtDefineService iEdcEvtDefineService;
    @Autowired
    private IEdcEvtRecordService edcEvtRecordService;
    @Autowired
    private IEdcAmsRecordService edcAmsRecordService;
    @Autowired
    private IEdcEvtDefineService edcEvtDefineService;
    @Autowired
    private IFabEquipmentService fabEquipmentService;
    @Autowired
    private IMesLotTrackService mesLotTrackService;
    @Autowired
    private IFabEquipmentStatusService fabEquipmentStatusService;
    @Autowired
    private IEdcDskLogProductionService edcDskLogProductionService;
    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    private IEdcDskLogOperationService edcDskLogOperationService;
    @Autowired
    IFabEquipmentService iFabEquipmentService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    IEdcEqpStateService iEdcEqpStateService;
    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.ALARM.DATA"})
    public void handleAlarm(String msg) {
        System.out.println("接收到的消息" + msg);
        EdcAmsRecord edcAmsRecord = JsonUtil.from(msg, EdcAmsRecord.class);
        FabEquipment fabEquipment=iFabEquipmentService.findEqpByCode(edcAmsRecord.getEqpId());
        if(fabEquipment!=null){
            edcAmsRecord.setStationCode(fabEquipment.getStationCode());
            edcAmsRecord.setLineNo(fabEquipment.getLineNo());
        }
        //if(){
        ////
        ////}
        edcAmsRecordService.insert(edcAmsRecord);
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.EVENT.DATA"})
    public void handleEvent(String msg) {
        try {
            System.out.println("接收到的消息" + msg);
            EdcEvtRecord edcEvtRecord = JsonUtil.from(msg, EdcEvtRecord.class);
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(edcEvtRecord.getEqpId());
            String modelId = "";
            if (fabEquipment != null) {
                modelId = fabEquipment.getModelId();

                EdcEvtDefine edcEvtDefine = edcEvtDefineService.selectOne(new EntityWrapper<EdcEvtDefine>().eq("event_id", edcEvtRecord.getEventId()).eq("eqp_model_id", modelId));
                if (edcEvtDefine != null) {
                    edcEvtRecord.setEventDesc(edcEvtDefine.getEventName());
                }
            }
            // TODO: 2020/7/14 后期针对trm产量事件,可不存储
            edcEvtRecordService.insert(edcEvtRecord);
            //Mold单独处理
            if ("TRM".equals(fabEquipment.getStepCode())) {
                handleMoldYield(edcEvtRecord, fabEquipment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String eventId = StringUtil.randomTimeUUID("RPT");
            fabLogService.info("", eventId, "TRM抛错", "TRM数据更新错误" + e, "", "gxj");
        }

    }

    //处理Mold产量特殊事件
    //通过track in获取设备开始shotcount数
    public void handleMoldYield(EdcEvtRecord evtRecord, FabEquipment fabEquipment) {
        String eqpId = evtRecord.getEqpId();
        String[] ceids = {"11201", "11202", "11203"};
        String ceid = evtRecord.getEventId();
        MesLotTrack mesLotTrack = mesLotTrackService.findLotNo1(eqpId, new Date());
        if (ArrayUtil.contains(ceids, ceid)) {
            fabEquipmentStatusService.increaseYield(eqpId, 24);
            FabEquipmentStatus equipmentStatus = fabEquipmentStatusService.findByEqpId(eqpId);
            log.info("TRM设备产量+24  eqpId："+eqpId+"DayYield"+equipmentStatus.getDayYield()+"LotYield"+equipmentStatus.getLotYield()+"LotYieldEqp"+equipmentStatus.getLotYieldEqp());
            // TODO: 2020/7/8 写入 edc_dsk_log_production
            EdcDskLogProduction productionLog = new EdcDskLogProduction();
            productionLog.setEqpId(evtRecord.getEqpId());
            productionLog.setRecipeCode(equipmentStatus.getRecipeCode());
            Date startTime = new Date();
            productionLog.setStartTime(startTime);
            Date endTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND,30);
            endTime=calendar.getTime();
            productionLog.setEndTime(endTime);
            productionLog.setEqpModelId(fabEquipment.getModelId());
            productionLog.setEqpModelName(fabEquipment.getModelName());
            productionLog.setEqpNo(fabEquipment.getEqpNo());
            productionLog.setJudgeResult("y");
            EdcDskLogProduction pro= edcDskLogProductionService.findLastYield(eqpId,new Date());
            if(pro==null){
                productionLog.setDayYield(24);
            }else{
                productionLog.setDayYield(pro.getDayYield()+24);
            }
            productionLog.setLotYield(equipmentStatus.getLotYield());
            productionLog.setDuration(0D);
            //productionLog.setMaterialNo(columns[columnNo++]); //制品的序列号
            //productionLog.setMaterialLotNo(columns[columnNo++]); //制品的批量
            //productionLog.setMaterialModel(columns[columnNo++]);//制品的品番
            //productionLog.setMaterialNo2(columns[columnNo++]); //制品序列
            //productionLog.setOrderNo(columns[columnNo++]);  //作业指示书的订单
            productionLog.setLotNo(equipmentStatus.getLotNo());  //作业指示书的批量
            productionLog.setProductionNo(equipmentStatus.getProductionNo()); //作业指示书的品番
            String eventParams = evtRecord.getEventParams();
            productionLog.setParamValue(eventParams);

            productionLog.setOrderNo(mesLotTrack.getOrderNo());
            Double duration = (double)(endTime.getTime()-startTime.getTime())/100;
            productionLog.setDuration(duration);
            log.info("持续时间"+productionLog.getDuration());
            edcDskLogProductionService.insert(productionLog);
            List<EdcDskLogProduction> proList = edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(), mesLotTrack.getEqpId(), mesLotTrack.getProductionNo());
            if (proList.size() > 0) {
                mesLotTrack.setLotYieldEqp(proList.size() * 24);
            } else {
                mesLotTrack.setLotYieldEqp(24);
            }
            boolean updateFlag = mesLotTrackService.updateById(mesLotTrack);
            if (!updateFlag) {
                mesLotTrack.setStartTime(new Date());
                mesLotTrack.setCreateBy("EQP");
                mesLotTrackService.insert(mesLotTrack);
            }
            if (eventParams != null) {
                String[] params = eventParams.split(",");
                if (params.length == 3) {
                    int shotcount = Integer.parseInt(params[0]) + Integer.parseInt(params[1]) + Integer.parseInt(params[2]);
                    String maxShotCountStr = fabEquipment.getEqpParam().split(",")[0];
                    if (shotcount > Integer.parseInt(maxShotCountStr)) {
                        //转发alarm至MQ
                    }
                }
            }
        }
        EdcDskLogOperation edcDskLogOperation = new EdcDskLogOperation();
        FabEquipmentStatus equipmentStatus = fabEquipmentStatusService.findByEqpId(eqpId);
        if (equipmentStatus != null) {
            edcDskLogOperation.setLotNo(equipmentStatus.getLotNo());
            edcDskLogOperation.setLotYield(equipmentStatus.getLotYield());
            edcDskLogOperation.setDayYield(equipmentStatus.getDayYield());
            edcDskLogOperation.setRecipeCode(equipmentStatus.getRecipeCode());
            edcDskLogOperation.setProductionNo(equipmentStatus.getProductionNo());
        }
        edcDskLogOperation.setOrderNo(mesLotTrack.getOrderNo());
        edcDskLogOperation.setEqpId(eqpId);
        edcDskLogOperation.setEqpModelId(fabEquipment.getModelId());
        edcDskLogOperation.setEqpModelName(fabEquipment.getModelName());
        edcDskLogOperation.setEventId(evtRecord.getEventId());
        edcDskLogOperation.setCreateDate(new Date());
        edcDskLogOperation.setStartTime(evtRecord.getStartDate());
        edcDskLogOperation.setEventParams(evtRecord.getEventParams());
        EdcEvtDefine edcEvtDefine = iEdcEvtDefineService.findDataByEvtId(evtRecord.getEventId());
        if (edcEvtDefine != null) {
            edcDskLogOperation.setEventName(edcEvtDefine.getEventName());
            edcDskLogOperation.setEventDetail(edcEvtDefine.getEventDesc());
        }
        edcDskLogOperationService.insert(edcDskLogOperation);

        //新建TRM状态数据+
        EdcEqpState edcEqpState = new EdcEqpState();
        edcEqpState.setEqpId(evtRecord.getEqpId());
        edcEqpState.setStartTime(evtRecord.getStartDate());
        if(evtRecord.getEventParams()!= null){
            if(evtRecord.getEventId().equals("11201")){
                edcEqpState.setState("RUN");
            }else if(evtRecord.getEventParams().equals("1")){
                edcEqpState.setState("DOWN");
            }else if(evtRecord.getEventId().startsWith("21")){
                edcEqpState.setState("ALARM");
            }else if(evtRecord.getEventId().equals("23")){
                edcEqpState.setState("IDLE");
            }
            if(edcEqpState.getState()!=null){
                EdcEqpState oldEdcEqpState = iEdcEqpStateService.findLastData(evtRecord.getStartDate(),evtRecord.getEqpId());
                oldEdcEqpState.setEndTime(evtRecord.getStartDate());
                Double state = (double) (edcEqpState.getStartTime().getTime() - oldEdcEqpState.getStartTime().getTime());
                oldEdcEqpState.setStateTimes(state);
                iEdcEqpStateService.updateById(oldEdcEqpState);
                iEdcEqpStateService.insert(edcEqpState);
                equipmentStatus.setEqpStatus(edcEqpState.getState());
                fabEquipmentStatusService.updateById(equipmentStatus);
            }
        }
    }
}
