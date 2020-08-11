package com.lmrj.dsk.edc.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.core.entity.MesResult;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.amsrpt.utils.RepeatAlarmUtil;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private IFabLogService fabLogService;
    @Autowired
    IEdcEqpStateService iEdcEqpStateService;
    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;
    @Autowired
    IEdcDskLogOperationService edcDskLogOperationService;
    @Autowired
    IFabEquipmentStatusService fabEquipmentStatusService;
    @Autowired
    IOvnBatchLotService ovnBatchLotService;
    @Autowired
    IFabEquipmentService fabEquipmentService;
    @Autowired
    IEdcEvtRecordService edcEvtRecordService;
    @Autowired
    IEdcAmsRecordService edcAmsRecordService;
    @Autowired
    IEdcDskLogRecipeService edcDskLogRecipeService;
    @Autowired
    IMesLotTrackLogService mesLotTrackLogService;
    @Autowired
    IMesLotTrackService mesLotTrackService;
    @Autowired
    private IEmailSendService emailSendService;
    @Autowired
    RepeatAlarmUtil repeatAlarmUtil;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    String[] paramEdit = {"Pick up pos  Z", "取晶位置 Z",
            "Pick up press level", "取晶位置下压量",
            "1st bonding pos  Z", "第一固晶位置 Z",
            "Bonding press level", "固晶位置下压量",
            "Pickup search level", "取晶搜索高度",
            "Pickup search speed", "取晶搜索速度",
            "1st plunge up height", "第1段突上量",
            "2nd plunge up height", "第2段突上量"
    };
    // TODO: 2020/7/8 改为可配置
    String[] emails = {"hanzy@ms5.sanken-ele.co.jp", "suchang@ms5.sanken-ele.co.jp",
            "zhangwj@lmrj.com", "403396835@qq.com"};


    //{"eqpId":"OVEN-F-01","eventId":"ON","eventParams":null,"startDate":"2019-11-12 19:31:33 416"}
    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.PRODUCTIONLOG.DATA"})
    public void parseProductionlog(String msg) {
        //String msg = new String(message, "UTF-8");
        System.out.println("接收到的消息" + msg);
        List<EdcDskLogProduction> edcDskLogProductionList = JsonUtil.from(msg, new TypeReference<List<EdcDskLogProduction>>() {
        });
        List<MesLotTrack> mesList=null;
        if (edcDskLogProductionList != null && edcDskLogProductionList.size() > 0) {
            EdcDskLogProduction edcDskLogProduction0 = edcDskLogProductionList.get(0);
            String eqpId = edcDskLogProduction0.getEqpId();
            EdcDskLogProduction edcDskLogProductionLast=edcDskLogProductionList.get(edcDskLogProductionList.size()-1);
            //批量内连番起始值
            int i=1;
            //判断数据是否为同一批次
            mesList = mesLotTrackService.findLotNo(eqpId,edcDskLogProduction0.getStartTime(),edcDskLogProductionLast.getEndTime());
            if(mesList.size()==1){
                //查询批次是否已存在，获取该批次最新数据
                MesLotTrack mesLotTrack=mesList.get(0);
                List<EdcDskLogProduction> proList=edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(),mesLotTrack.getEqpId(),mesLotTrack.getProductionNo());
                if(proList.size()>0){
                    i=proList.size()+1;
                }
                for (EdcDskLogProduction edcDskLogProduction : edcDskLogProductionList) {
                    edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
                    edcDskLogProduction.setLotYield(i);
                    i++;
                }
                //不属于同一个批次
            }else if(mesList.size()>1){
                for (EdcDskLogProduction edcDskLogProduction : edcDskLogProductionList) {
                    //查询当前数据批次信息
                    MesLotTrack mesLotTrack1=mesLotTrackService.findLotNo1(edcDskLogProduction.getEqpId(),edcDskLogProduction.getStartTime(),edcDskLogProduction.getEndTime());
                    if(mesLotTrack1!=null){
                        //查询批次是否已存在，获取该批次最新数据
                        List<EdcDskLogProduction> proList=edcDskLogProductionService.findDataBylotNo(mesLotTrack1.getLotNo(),mesLotTrack1.getEqpId(),mesLotTrack1.getProductionNo());
                        if(proList.size()>0){
                            edcDskLogProduction.setLotNo(mesLotTrack1.getLotNo());
                            edcDskLogProduction.setLotYield(proList.size()+1);
                        }else{
                            edcDskLogProduction.setLotNo(mesLotTrack1.getLotNo());
                            edcDskLogProduction.setLotYield(1);
                        }
                    }else{
                        log.info("没有满足该条数据的批次"+eqpId,edcDskLogProduction.getStartTime(),edcDskLogProduction.getEndTime());
                    }
                }
            }
            if (StringUtil.isNotBlank(eqpId)) {
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
                edcDskLogProductionList.forEach(edcDskLogProduction -> {
                    edcDskLogProduction.setEqpNo(fabEquipment.getEqpNo());
                    edcDskLogProduction.setEqpModelId(fabEquipment.getModelId());
                    edcDskLogProduction.setEqpModelName(fabEquipment.getModelName());
                    edcDskLogProduction.setJudgeResult("Y");
                });
            }
            if (eqpId.contains("SIM-DM")) {
                Iterator it = edcDskLogProductionList.iterator();
                while (it.hasNext()) {
                    EdcDskLogProduction obj = (EdcDskLogProduction) it.next();
                    String[] params = obj.getParamValue().split(",");
                    if (params.length > 2 && !"1".equals(params[1]))
                        it.remove();
                }
            }
            edcDskLogProductionService.insertBatch(edcDskLogProductionList);
            String eventId = StringUtil.randomTimeUUID("RPT");
            fabLogService.info("", eventId, "production更新", "本次数据为不同批次，track更新成功","", "gxj");
        }
        if(mesList.size()==1){
            //该批数据为同一批次
            EdcDskLogProduction lastPro = edcDskLogProductionList.get(edcDskLogProductionList.size() - 1);
            MesLotTrack mesLotTrack1 = new MesLotTrack();
            mesLotTrack1.setEqpId(lastPro.getEqpId());
            mesLotTrack1.setProductionNo(lastPro.getProductionNo());
            mesLotTrack1.setLotNo(lastPro.getLotNo());
            mesLotTrack1.setLotYieldEqp(lastPro.getLotYield());
            boolean updateFlag = mesLotTrackService.updateById(mesLotTrack1);
            if(!updateFlag){
                mesLotTrack1.setStartTime(new Date());
                mesLotTrack1.setOrderNo(lastPro.getOrderNo());
                mesLotTrack1.setCreateBy("EQP");
                mesLotTrackService.insert(mesLotTrack1);
            }
            String eventId = StringUtil.randomTimeUUID("RPT");
            fabLogService.info("", eventId, "mes_lot_track更新", "本次数据为同一批次，track更新结束", lastPro.getLotNo(), "gxj");
        //不同批次数据
        }else if(mesList.size()>1){
            for (MesLotTrack mesLotTrack : mesList) {
                List<EdcDskLogProduction> proList=edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(),mesLotTrack.getEqpId(),mesLotTrack.getProductionNo());
                EdcDskLogProduction lastPro =proList.get(proList.size()-1);
                MesLotTrack mesLotTrack1 = new MesLotTrack();
                mesLotTrack1.setEqpId(lastPro.getEqpId());
                mesLotTrack1.setProductionNo(lastPro.getProductionNo());
                mesLotTrack1.setLotNo(lastPro.getLotNo());
                mesLotTrack1.setLotYieldEqp(lastPro.getLotYield());
                boolean updateFlag = mesLotTrackService.updateById(mesLotTrack1);
                if(!updateFlag){
                    mesLotTrack1.setStartTime(new Date());
                    mesLotTrack1.setOrderNo(lastPro.getOrderNo());
                    mesLotTrack1.setCreateBy("EQP");
                    mesLotTrackService.insert(mesLotTrack1);
                }
            }
            String eventId = StringUtil.randomTimeUUID("RPT");
            fabLogService.info("", eventId, "mes_lot_track更新", "本次数据为不同批次，track更新结束","", "gxj");
        }
        //产量不准,改为自己运算后更新
        //if(StringUtil.isNotBlank(lotNo) || StringUtil.isNotBlank(recipeCode)){
        //    fabEquipmentStatusService.updateYield(eqpId,"", lotNo, recipeCode, lotYield, dayYield);
        //}
    }
    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.OPERATIONLOG.DATA"})
    public void parseOperationlog(String msg) {
        log.info("recieved message:" + msg);
        //public void cureAlarm(byte[] message) throws UnsupportedEncodingException {
        //    String msg = new String(message, "UTF-8");
        //    System.out.println("接收到的消息"+msg);
        List<EdcDskLogOperation> edcDskLogOperationlist = JsonUtil.from(msg, new TypeReference<List<EdcDskLogOperation>>() {
        });

        if (edcDskLogOperationlist == null || edcDskLogOperationlist.size() == 0) {
            return;
        }
        EdcDskLogOperation edcDskLogOperation0 = edcDskLogOperationlist.get(0);
        String eqpId = edcDskLogOperation0.getEqpId();
        if (StringUtil.isNotBlank(eqpId)) {
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
        for (EdcDskLogOperation edcDskLogOperation : edcDskLogOperationlist) {
            String eventId = edcDskLogOperation.getEventId();
            if ("2".equals(eventId)) {
                EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
                edcAmsRecord.setEqpId(edcDskLogOperation.getEqpId());
                String alarmCode = edcDskLogOperation.getAlarmCode();
                edcAmsRecord.setAlarmCode(alarmCode);
                edcAmsRecord.setAlarmName(edcDskLogOperation.getEventDetail());
                edcAmsRecord.setAlarmSwitch("1");
                edcAmsRecord.setLotNo(edcDskLogOperation.getLotNo());
                edcAmsRecord.setLotYield(edcDskLogOperation.getLotYield());
                edcAmsRecord.setStartDate(edcDskLogOperation.getStartTime());
                edcAmsRecordList.add(edcAmsRecord);
                if ("War04002004".equals(alarmCode) || "War01002013".equals(alarmCode) || "War01002012".equals(alarmCode)) {

                } else {
                    status = "ALARM";
                }
            } else {
                EdcEvtRecord edcEvtRecord = new EdcEvtRecord();
                edcEvtRecord.setEqpId(edcDskLogOperation.getEqpId());
                edcEvtRecord.setEventId(eventId);
                String eventDesc = edcDskLogOperation.getEventName();
                String eventParams = edcDskLogOperation.getEventDetail();
                edcEvtRecord.setEventDesc(eventDesc);
                // TODO: 2020/5/24  部分参数不可修改判断
                List<String> paramEditList = Lists.newArrayList(paramEdit);
                if ("PARAM CHG1".equals(eventDesc) || "PRODUCT SET".equals(eventDesc)) {
                    for (String paramName : paramEditList) {
                        if (eventParams.contains(paramName)) {
                            Map<String, Object> datas = Maps.newHashMap();
                            datas.put("EQP_ID", edcEvtRecord.getEqpId());
                            datas.put("PARAM_CODE", eventParams);
                            datas.put("OLD_VAL", "");
                            datas.put("NEW_VAL", "");

                            emailSendService.send(emails, "PARAM_CHANGE", datas);
                            break;
                        }
                    }
                }
                edcEvtRecord.setEventParams(eventParams);
                edcEvtRecord.setStartDate(edcDskLogOperation.getStartTime());
                edcEvtRecordList.add(edcEvtRecord);
                if ("0".equals(eventId) || "7".equals(eventId)) {
                    status = "DOWN";
                } else if ("1".equals(eventId) || "6".equals(eventId)) {
                    status = "RUN";
                } else if ("3".equals(eventId)) {
                    status = "IDLE";
                }
            }
            EdcEqpState edcEqpState = new EdcEqpState();
            edcEqpState.setEqpId(edcDskLogOperation.getEqpId());
            edcEqpState.setStartTime(edcDskLogOperation.getStartTime());
            edcEqpState.setState(status);
            if(StringUtil.isNotBlank(status)){
                String stateJson = JsonUtil.toJsonString(edcEqpState);
                rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateJson);
            }
        }
        if (edcEvtRecordList.size() != 0) {
            edcEvtRecordService.insertBatch(edcEvtRecordList);
        }
        if (edcAmsRecordList.size() != 0) {
            edcAmsRecordService.insertBatch(edcAmsRecordList);
            repeatAlarmUtil.putEdcAmsRecordInMq(edcAmsRecordList);
        }
        // TODO: 2020/8/3 改为发送mq消息处理
        /*if(StringUtil.isNotBlank(status)){
            fabEquipmentStatusService.updateStatus(edcDskLogOperationlist.get(0).getEqpId(),status, "", "");
        }*/
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.ALARMRPT.DATA"})
    public String repeatAlarm(String msg) {
        repeatAlarmUtil.queryAlarmDefine();
        Map<String, String> msgMap = JsonUtil.from(msg, Map.class);
        EdcAmsRecord edcAmsRecord = JsonUtil.from(msgMap.get("alarm"), EdcAmsRecord.class);
        System.out.println(edcAmsRecord);
        repeatAlarmUtil.repeatAlarm(edcAmsRecord);
        return JsonUtil.toJsonString(MesResult.ok("ok"));
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.RECIPELOG.DATA"})
    public void parseRecipelog(String msg) {
        log.info("recieved message 开始解析{}recipe文件 : {} " + msg);
        List<EdcDskLogRecipe> edcDskLogRecipeList = JsonUtil.from(msg, new TypeReference<List<EdcDskLogRecipe>>() {
        });
        if (edcDskLogRecipeList == null || edcDskLogRecipeList.size() == 0) {
            return;
        }
        //edcDskLogRecipeService.insertBatch(edcDskLogRecipeList);
        edcDskLogRecipeList.forEach(edcDskLogRecipe -> {
            edcDskLogRecipeService.insert(edcDskLogRecipe);
        });
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.TEMPLOG.DATA"})
    public void parseTempHlog(String msg) {
        log.info("recieved message 开始解析{}温度曲线文件 : {} " + msg);
        OvnBatchLot ovnBatchLot = JsonUtil.from(msg, OvnBatchLot.class);
        if (ovnBatchLot == null) {
            return;
        }
        String eqpId = ovnBatchLot.getEqpId();
        if (StringUtil.isNotBlank(eqpId)) {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            ovnBatchLot.setOfficeId(fabEquipment.getOfficeId());
            List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
            ovnBatchLot.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
            ovnBatchLotService.insert(ovnBatchLot);
        }
    }

}
