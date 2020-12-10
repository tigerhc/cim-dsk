package com.lmrj.dsk.edc.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.core.entity.MesResult;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipeBody;
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
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    IMesLotTrackService iMesLotTrackService;
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
    IOvnBatchLotParamService iOvnBatchLotParamService;
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
    @Autowired
    IFabEquipmentService iFabEquipmentService;


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

        List<EdcDskLogProduction> proList = new ArrayList<>();
        List<EdcDskLogProduction> nextproList = new ArrayList<>();

        if (edcDskLogProductionList.size() > 0) {
            EdcDskLogProduction edcDskLogProduction0 = edcDskLogProductionList.get(0);
            String eqpId = edcDskLogProduction0.getEqpId();
            //判断数据是否为同一批次
            MesLotTrack lotTrack = mesLotTrackService.findLotByStartTime(eqpId, edcDskLogProduction0.getStartTime());
            MesLotTrack nextLotTrack = mesLotTrackService.findLastTrack(eqpId, lotTrack.getLotNo(), lotTrack.getStartTime());
            //同一批次
            if (nextLotTrack == null) {
                fixProData(edcDskLogProductionList, lotTrack);
                //不同批次 将开始时间在最新批次之后的数据归为最新批次数据 其他归为旧批次数据
            } else {
                for (EdcDskLogProduction edcDskLogProduction : edcDskLogProductionList) {
                    if (edcDskLogProduction.getStartTime().before(nextLotTrack.getStartTime())) {
                        proList.add(edcDskLogProduction);
                    } else {
                        nextproList.add(edcDskLogProduction);
                    }
                }
                if (proList.size() > 0) {
                    fixProData(proList, lotTrack);
                }
                if (nextproList.size() > 0) {
                    fixProData(nextproList, nextLotTrack);
                }
            }
        } else {

        }
    }

    //修正数据   先把所有数据的批量内连番改为每次加一的顺序 再对特殊设备做特殊处理
    public void fixProData(List<EdcDskLogProduction> proList, MesLotTrack mesLotTrack) {
        int i = 1;
        String eqpId = mesLotTrack.getEqpId();
        List<EdcDskLogProduction> productionList = edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(), mesLotTrack.getEqpId(), mesLotTrack.getProductionNo());
        //修正批量内连番
        if (productionList.size() > 0) {
            i = productionList.size() + 1;
        }
        for (EdcDskLogProduction edcDskLogProduction : proList) {
            edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
            edcDskLogProduction.setLotYield(i);
            i++;
        }
        //如果为REFLOW 或 PRINTER 再乘以12
        if (eqpId.contains("SIM-REFLOW") || eqpId.contains("SIM-PRINTER")) {
            for (EdcDskLogProduction edcDskLogProduction : proList) {
                edcDskLogProduction.setLotYield(edcDskLogProduction.getLotYield() * 12);
            }
        }
        if (StringUtil.isNotBlank(eqpId)) {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            proList.forEach(edcDskLogProduction -> {
                edcDskLogProduction.setEqpNo(fabEquipment.getEqpNo());
                edcDskLogProduction.setEqpModelId(fabEquipment.getModelId());
                edcDskLogProduction.setEqpModelName(fabEquipment.getModelName());
                edcDskLogProduction.setJudgeResult("Y");
            });
        }
        //将重复数据去除
        if (eqpId.contains("SIM-DM")) {
            Iterator it = proList.iterator();
            while (it.hasNext()) {
                EdcDskLogProduction obj = (EdcDskLogProduction) it.next();
                String[] params = obj.getParamValue().split(",");
                if (params.length > 2 && !"1".equals(params[1]))
                    it.remove();
            }
        }
        String eventId = null;
        eventId = StringUtil.randomTimeUUID("RPT");
        EdcDskLogProduction lastPro = null;
        boolean updateFlag = false;
        try {
            if (edcDskLogProductionService.insertBatch(proList,100)) {
                fabLogService.info(eqpId, eventId, "fixProData", "production数据插入结束,共" + proList.size() + "条", mesLotTrack.getLotNo(), "gxj");
            }
            //判断该批次是否为最后一个批次 若不是 查询范围为当前批次开始到下一批次开始
            List<EdcDskLogProduction> allProList = new ArrayList<>();
            //MesLotTrack lastTrack = iMesLotTrackService.findLastTrack(mesLotTrack.getEqpId(), mesLotTrack.getLotNo(), mesLotTrack.getStartTime());
            allProList = edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(),mesLotTrack.getEqpId(),mesLotTrack.getProductionNo());
            lastPro = proList.get(proList.size() - 1);
            mesLotTrack.setLotYieldEqp(allProList.size());
            if (eqpId.contains("SIM-REFLOW") || eqpId.contains("SIM-PRINTER")) {
                mesLotTrack.setLotYieldEqp(allProList.size() * 12);
            }
            mesLotTrack.setUpdateBy("gxj");
            updateFlag = mesLotTrackService.updateById(mesLotTrack);
            log.info("更新设备状态批次产量");
            FabEquipmentStatus fabStatus = fabEquipmentStatusService.findByEqpId(eqpId);
            if(fabStatus!=null){
                fabStatus.setLotYield(allProList.size());
                fabEquipmentStatusService.updateById(fabStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fabLogService.info(eqpId, eventId, "fixProData", "track更新出错" + e + "       ", mesLotTrack.getLotNo(), "gxj");
        }
        if (!updateFlag) {
            System.out.println("修改失败");
            mesLotTrack.setStartTime(new Date());
            if (lastPro.getOrderNo() != null) {
                mesLotTrack.setOrderNo(lastPro.getOrderNo());
            }
            mesLotTrack.setCreateBy("EQP");
            mesLotTrackService.insert(mesLotTrack);
        }
        fabLogService.info(eqpId, eventId, "fixProData", "track批次产量更新为：" + mesLotTrack.getLotYieldEqp(), mesLotTrack.getLotNo(), "gxj");
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
        String eventId1 = StringUtil.randomTimeUUID("EDC");
        fabLogService.info(eqpId, eventId1, "parseOperationlog ", "Operation解析 本次接收数据条数：" + edcDskLogOperationlist.size(), "", "gxj");
        if (StringUtil.isNotBlank(eqpId)) {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            edcDskLogOperationlist.forEach(edcDskLogOperation -> {
                EdcDskLogProduction pro = edcDskLogProductionService.findLastYield(edcDskLogOperation.getEqpId(), edcDskLogOperation.getStartTime());
                if (pro != null) {
                    edcDskLogOperation.setLotYield(pro.getLotYield());
                    edcDskLogOperation.setDayYield(pro.getDayYield());
                }
                edcDskLogOperation.setEqpNo(fabEquipment.getEqpNo());
                edcDskLogOperation.setEqpModelId(fabEquipment.getModelId());
                edcDskLogOperation.setEqpModelName(fabEquipment.getModelName());
            });
        }
        edcDskLogOperationService.insertBatch(edcDskLogOperationlist,100);

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
        List<EdcEqpState> edcEqpStateList = Lists.newArrayList();
        String status = "";
        for (EdcDskLogOperation edcDskLogOperation : edcDskLogOperationlist) {
            String eventId = edcDskLogOperation.getEventId();
            if ((!eqpId.contains("WB") && "2".equals(eventId)) || (eqpId.contains("WB") && "8".equals(eventId))) {
                EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
                edcAmsRecord.setEqpId(edcDskLogOperation.getEqpId());
                String alarmCode = edcDskLogOperation.getAlarmCode();
                edcAmsRecord.setAlarmCode(alarmCode);
                edcAmsRecord.setAlarmName(edcDskLogOperation.getEventDetail());
                edcAmsRecord.setAlarmSwitch("1");
                edcAmsRecord.setLotNo(edcDskLogOperation.getLotNo());
                edcAmsRecord.setLotYield(edcDskLogOperation.getLotYield());
                edcAmsRecord.setStartDate(edcDskLogOperation.getStartTime());
                FabEquipment fabEquipment = iFabEquipmentService.findEqpByCode(edcDskLogOperation.getEqpId());
                if (fabEquipment != null) {
                    edcAmsRecord.setLineNo(fabEquipment.getLineNo());
                    edcAmsRecord.setStationCode(fabEquipment.getStationCode());
                }
                if ("34014801".equals(alarmCode) || "34015212".equals(alarmCode)) {
                    List<Map<String, Object>> users = new ArrayList<>();
                    users = fabEquipmentService.findEmailALL("WBAlarm");
                    Map<String, Object> msgMap = new HashMap<>();
                    if ("34014801".equals(alarmCode)) {
                        msgMap.put("ALARM_CODE", "框架推送错误");
                    } else {
                        msgMap.put("ALARM_CODE", "联接机搬运过载错误");
                    }
                    msgMap.put("EQP_ID", eqpId);
                    List<String> param = new ArrayList<>();
                    if (!users.isEmpty()) {
                        for (Map<String, Object> map : users) {
                            param.add((String) map.get("email"));
                        }
                    }
                    String[] params = new String[param.size()];
                    param.toArray(params);
                    try {
                        emailSendService.blockSend(params, "RTP_ALARM", msgMap);
                    } catch (Exception e) {
                        log.error("WB Alarm 邮件发送出错" + e);
                        e.printStackTrace();
                    }
                }
                edcAmsRecordList.add(edcAmsRecord);
                if ("War04002004".equals(alarmCode) || "War01002013".equals(alarmCode) || "War01002012".equals(alarmCode)) {

                } else if (!"8".equals(eventId)) {
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
                            emailSendService.blockSend(emails,"PARAM_CHANGE",datas);
                            //emailSendService.send(emails, "PARAM_CHANGE", datas);
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
            if (eqpId.contains("WB")) {
                if (edcDskLogOperation.getEventName().equals("2")) {
                    status = "DOWN";
                } else if (edcDskLogOperation.getEventName().equals("4")) {
                    status = "RUN";
                } else if (edcDskLogOperation.getEventName().equals("8")) {
                    status = "ALARM";
                } else if (edcDskLogOperation.getEventName().equals("16") || edcDskLogOperation.getEventName().equals("32")) {
                    status = "IDLE";
                }
            }
            edcEqpState.setState(status);

            if (StringUtil.isNotBlank(status)) {
                /*edcEqpStateList.add(edcEqpState);
                if(edcEqpStateList.size()>0){
                    String stateListJson = JsonUtil.toJsonString(edcEqpStateList);
                    rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateListJson);
                }*/
                String stateJson = JsonUtil.toJsonString(edcEqpState);
                rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateJson);
            }
        }
        /*if(edcEqpStateList.size()>0){
            String stateListJson = JsonUtil.toJsonString(edcEqpStateList);
            rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateListJson);
        }*/
        if (edcEvtRecordList.size() != 0) {
            edcEvtRecordService.insertBatch(edcEvtRecordList,1000);
        }
        if (edcAmsRecordList.size() != 0) {
            edcAmsRecordService.insertBatch(edcAmsRecordList,1000);
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
        log.info("C2S.Q.ALARMRPT.DATA消息接收开始执行");
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
            if(edcDskLogRecipe.getEdcDskLogRecipeBodyList().size()>0){
                for (EdcDskLogRecipeBody recipeBody : edcDskLogRecipe.getEdcDskLogRecipeBodyList()) {
                    recipeBody.setRecipeLogId(edcDskLogRecipe.getId());
                }
            }
            edcDskLogRecipeService.insert(edcDskLogRecipe);
        });
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.TEMPLOG.DATA"})
    public void parseTempHlog(String msg) {
        log.info("recieved message 开始解析{}温度曲线文件 : {} " + msg);
        OvnBatchLot ovnBatchLot = JsonUtil.from(msg, OvnBatchLot.class);
        String eqpId = ovnBatchLot.getEqpId();
        if (StringUtil.isNotBlank(eqpId)) {
            if(!eqpId.equals("")){
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
                ovnBatchLot.setOfficeId(fabEquipment.getOfficeId());
            }
            Long time = ovnBatchLot.getEndTime().getTime()-24*60*60*1000;
            Date startTime = new Date(time);
            OvnBatchLot ovnBatchLot1 = ovnBatchLotService.findBatchData(eqpId,startTime);
            if(ovnBatchLot1!=null){
                List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                ovnBatchLot1.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                ovnBatchLotService.updateById(ovnBatchLot1);
                for (OvnBatchLotParam ovnBatchLotParam : OvnBatchLotParamList) {
                    ovnBatchLotParam.setBatchId(ovnBatchLot1.getId());
                }
                iOvnBatchLotParamService.insertBatch(OvnBatchLotParamList);
            }else{
                List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                ovnBatchLot.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                ovnBatchLotService.insert(ovnBatchLot);
            }
        }
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.MSG.MAIL"})
    public void sendAlarm(String msg) {
        String eqpId = null;
        Map<String, Object> msgMap = JsonUtil.from(msg, Map.class);
        eqpId = (String) msgMap.get("EQP_ID");
        List<Map<String, Object>> users = new ArrayList<>();
        List<Map<String, Object>> department = fabEquipmentService.findDepartment(eqpId);
        if (department.get(0).get("department").equals("YK")) {
            users = fabEquipmentService.findEmailALL("E-0007");
        } else if (department.get(0).get("department").equals("EK")) {
            users = fabEquipmentService.findEmailALL("E-0008");
        }
        List<String> param = new ArrayList<>();
        if (!users.isEmpty()) {
            for (Map<String, Object> map : users) {
                param.add((String) map.get("email"));
            }
        }
        String[] params = new String[param.size()];
        param.toArray(params);
        emailSendService.blockSend(params, "RTP_ALARM", msgMap);
    }
}
