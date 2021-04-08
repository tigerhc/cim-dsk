package com.lmrj.dsk.edc.handler;

import com.alibaba.fastjson.JSONObject;
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
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.util.lang.ArrayUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    IOvnBatchLotService iOvnBatchLotService;
    @Autowired
    IOvnBatchLotParamService iOvnBatchLotParamService;
    static Map<String,String> alarmMap = new HashMap<>();
    static {
        alarmMap.put("A0001","引线框架库空通知");
        alarmMap.put("A0002","引线框架库为空");
        alarmMap.put("A0003","空引线框已满");
        alarmMap.put("A0017","奇数引线框架");
        alarmMap.put("A0018","引线框架消除请求");
        alarmMap.put("A0049"," TABLET为空通知");
        alarmMap.put("A0050","TABLET为空");
        alarmMap.put("A0081","无消隐盒");
        alarmMap.put("A0097","引线框1未准备");
        alarmMap.put("A0098","引线框2未准备");
        alarmMap.put("A0099","引线框1已满");
        alarmMap.put("A0100","引线框2已满");
        alarmMap.put("A0103","引线框检查");
        alarmMap.put("A0129","批量结束");
        alarmMap.put("A0130","批量结束警告开始");
        alarmMap.put("A0145","装载机待机关闭");
        alarmMap.put("A0146","装载机待机关闭");
        alarmMap.put("A0147","卸载机备用关闭");
        alarmMap.put("A0148","清洁剂备用关闭");
        alarmMap.put("A0149","线路自动待机关闭");
        alarmMap.put("A0178","正面安全门2打开");
        alarmMap.put("A0180","系统外侧的安全盖已打开");
        alarmMap.put("A0181","压力机1前上侧的安全盖已打开");
        alarmMap.put("A0182","压力机1后上侧的安全盖已打开");
        alarmMap.put("A0183","压力机2前上侧的安全盖已打开");
        alarmMap.put("A0184","压力机2后上侧的安全盖已打开");
        alarmMap.put("A0185","压力机3前上侧的安全盖已打开");
        alarmMap.put("A0186","压力机3后上侧的安全盖已打开");
        alarmMap.put("A0189","引线框入料口安全盖打开");
        alarmMap.put("A0190","引线框下料口安全盖打开");
        alarmMap.put("A0225","库内升降机服务电机关闭");
        alarmMap.put("A0226","校准伺服电机关闭");
        alarmMap.put("A0227","装载机LW/RW的服务电机关闭");
        alarmMap.put("A0228","装载机FW/BW的服务电机关闭");
        alarmMap.put("A0229","下料机LW/RW的服务电机关闭");
        alarmMap.put("A0230","下料机FW/BW的服务电机关闭");
        alarmMap.put("A0231","托架服务电机关闭");
        alarmMap.put("A0232","压片机服务电机关闭");
        alarmMap.put("A0235","夹持进给服务电机关闭");
        alarmMap.put("A0268","超出机器监视器屏幕上预设除尘器的报警限值");
        alarmMap.put("A0513","P-1的待机模式已关闭");
        alarmMap.put("A0514","P-1 ORG关闭");
        alarmMap.put("A0515","P-1 SERVO关闭");
        alarmMap.put("A0521","P-1 F-M的待机模式已关闭");
        alarmMap.put("A0577","P-2 待机模式已关闭");
        alarmMap.put("A0578","P-2 ORG 关闭");
        alarmMap.put("A0579","P-2 SERVO关闭");
        alarmMap.put("A0585","P-2 F-M的待机模式已关闭");
        alarmMap.put("A0641","P-3 待机模式已关闭");
        alarmMap.put("A0642","P-3 ORG 关闭");
        alarmMap.put("A0643","P-3 SERVO关闭");
        alarmMap.put("A0649","P-3 F-M的待机模式已关闭");
        alarmMap.put("E0003","IN MAGAZINE SEPARETOR超时错误");
        alarmMap.put("E0009","引线框架推进器或过载超时错误");
        alarmMap.put("E0018","引线框架对齐错误");
        alarmMap.put("E0019","引线框架方向错误");
        alarmMap.put("E0050","引线框架对齐错误");
        alarmMap.put("E0097","装载机上升/下降超时错误");
        alarmMap.put("E0098","装载机LW/RW服务电机错误");
        alarmMap.put("E0099","装载机FW/BW服务电机错误");
        alarmMap.put("E0100","装载机引线框架夹持器超时错误");
        alarmMap.put("E0101","装载机 TABLET SHUTTER超时错误");
        alarmMap.put("E0104","引线框架位置错误");
        alarmMap.put("E0105","引线框架丢失");
        alarmMap.put("E0131","TABLET FEED超时");
        alarmMap.put("E0137","TABLET CHUTE LIFTER超时");
        alarmMap.put("E0139","TABLET PUSHER超时");
        alarmMap.put("E0140","TABLET CLOG堵塞");
        alarmMap.put("E0141","TABLET PARTS FEEDER超时");
        alarmMap.put("E0142","TABLET HOPPER错误");
        alarmMap.put("E0170","UNLOADER CLEANER BRUSH UP/DOWN超时");
        alarmMap.put("E0323","气压过低");
        alarmMap.put("E0805","预热器上升和下降超时");
        alarmMap.put("E0834","预热器的加热器温度控制错误");
        alarmMap.put("A0270","14 F-M UNIT 超出系统预设范围");
        alarmMap.put("A0179","TABLET 安全盖打开");
        alarmMap.put("A0644"," P-3(04) 预热器关闭");
        alarmMap.put("A0785"," PH2(01) 预热器关闭");
        alarmMap.put("A0801"," PH3(01) 预热器关闭");
        alarmMap.put("E0002","CLAMPER超时");
        alarmMap.put("E0033","夹持进给服务电机错误");
        alarmMap.put("E0049","ALIGNMENT SERVO MOTOR超时");
        alarmMap.put("E0129","TABLET SEPARATOR超时");
        alarmMap.put("E0162","下料机电机错误");
        alarmMap.put("E0166","下料机未吸附");
        alarmMap.put("E0196","DEGATE超时");
        alarmMap.put("E0210","PICK&PLACE HEAD 1超时");
        alarmMap.put("E0211","PICK&PLACE HEAD 2超时");
        alarmMap.put("E0530","P-1 预热器温度控制错误");
        alarmMap.put("E0594","P-2 预热器温度控制错误");
        alarmMap.put("E0596","P-2 预热器导线断开");
        alarmMap.put("E0658","P-3 预热器温度控制错误");
        alarmMap.put("E0660","P-3 预热器导线断开");
        alarmMap.put("E0802","PH-2 预热器温度控制错误");
        alarmMap.put("E0803","PH-2 温度异常");
        alarmMap.put("E0804","PH-2 预热器断丝");
        alarmMap.put("E0836","PH-3 预热器断丝");
    }
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
        String alarmCode = edcAmsRecord.getAlarmCode();
        if(alarmCode.substring(0,1).equals("1")){
            alarmCode = "A0"+alarmCode.substring(1,alarmCode.length());
        }else if(alarmCode.substring(0,1).equals("2")){
            alarmCode = "E0"+alarmCode.substring(1,alarmCode.length());
        }
        edcAmsRecord.setAlarmCode(alarmCode);
        String alarmName = alarmMap.get(alarmCode);
        if(alarmName!=null && !alarmName.equals("")){
            edcAmsRecord.setAlarmName(alarmName);
        }
        MesLotTrack lotTrack = mesLotTrackService.findLotNo1(edcAmsRecord.getEqpId(),edcAmsRecord.getStartDate());
        if(lotTrack!=null){
            edcAmsRecord.setLotNo(lotTrack.getLotNo());
            edcAmsRecord.setLotYield(lotTrack.getLotYieldEqp());
        }
        edcAmsRecordService.insert(edcAmsRecord);
        fabEquipmentStatusService.updateStatus(edcAmsRecord.getEqpId(),"ALARM", "", "");
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
                Date time= new Date();
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 8);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND,0);
                time = cal.getTime();
                if(pro.getStartTime().before(time) && startTime.after(time)){
                    productionLog.setDayYield(24);
                }else{
                    productionLog.setDayYield(pro.getDayYield()+24);
                }
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

            //生成TRM温度数据
            try {
                List<OvnBatchLotParam> paramList = new ArrayList<>();
                OvnBatchLot ovnBatchLot = new OvnBatchLot();
                ovnBatchLot.setId(StringUtil.randomTimeUUID());
                ovnBatchLot.setEqpId(eqpId);
                ovnBatchLot.setStartTime(productionLog.getStartTime());
                ovnBatchLot.setEndTime(productionLog.getEndTime());
                ovnBatchLot.setStepCode("TRM");
                ovnBatchLot.setRecipeCode(equipmentStatus.getRecipeCode());
                ovnBatchLot.setOtherTempsTitle("模腔1预热器L温度,模腔2预热器L温度,模腔3预热器L温度,模腔1预热器R温度,模腔2预热器R温度,模腔3预热器R温度,模具1温度上型,模具2温度上型,模具3温度上型,模具1温度下型,模具2温度下型,模具3温度下型");
                if(mesLotTrack!=null){
                    ovnBatchLot.setLotId(mesLotTrack.getLotNo());
                }
                String[] a = pro.getParamValue().split(",");
                Long create =  productionLog.getStartTime().getTime()+(1000);
                String temp = null;
                for (int i = 4; i < 15; i++) {
                    if(i == 4 ){
                        temp = a[4]+",150,145,155";
                        //判断温度是否超过范围，超过则发送邮件报警
                        sendAlarmEmail(eqpId,a[4],155,145);
                    }else if(i>4 && i<9){
                        temp = temp +","+ a[i] +",150,145,155";
                        sendAlarmEmail(eqpId,a[i],155,145);
                    }else{
                        temp = temp +","+ a[i] +",185,180,190";
                        sendAlarmEmail(eqpId,a[i],190,180);
                    }
                }
                Date createTime = new Date(create);
                OvnBatchLotParam ovnBatchLotParam = new OvnBatchLotParam();
                ovnBatchLotParam.setBatchId(ovnBatchLot.getId());
                ovnBatchLotParam.setTempPv(a[3]);
                ovnBatchLotParam.setCreateDate(createTime);
                ovnBatchLotParam.setTempMax("155");
                ovnBatchLotParam.setTempMin("145");
                ovnBatchLotParam.setTempSp("150");
                sendAlarmEmail(eqpId,a[3],155,145);
                ovnBatchLotParam.setOtherTempsValue(temp);
                paramList.add(ovnBatchLotParam);
                ovnBatchLot.setOvnBatchLotParamList(paramList);
                //实现主表一个批次只有一条数据
                /*Long time = ovnBatchLot.getStartTime().getTime()-24*60*60*1000;
                Date stime = new Date(time);*/
                OvnBatchLot ovnBatchLot1 = iOvnBatchLotService.findBatchDataByLot(eqpId,mesLotTrack.getLotNo());
                if(ovnBatchLot1!=null){
                    List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                    ovnBatchLot1.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                    iOvnBatchLotService.updateById(ovnBatchLot1);
                    for (OvnBatchLotParam batchLotParam : OvnBatchLotParamList) {
                        batchLotParam.setBatchId(ovnBatchLot1.getId());
                    }
                    log.info("TRM 温度数据插入ovnBatchLot1 成功");
                    iOvnBatchLotParamService.insertBatch(OvnBatchLotParamList);
                }else{
                    iOvnBatchLotService.insert(ovnBatchLot);
                }
            } catch (Exception e) {
                log.error("TRM温度数据插入出错"+ pro.getEqpId()+"  "+pro.getLotNo()+"  "+e.getMessage());
                e.printStackTrace();
            }

            List<EdcDskLogProduction> proList = edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(), mesLotTrack.getEqpId(), mesLotTrack.getProductionNo());
            if (proList.size() > 0) {
                mesLotTrack.setLotYieldEqp(proList.size() * 24);
            } else {
                mesLotTrack.setLotYieldEqp(24);
            }
            boolean updateFlag = mesLotTrackService.updateById(mesLotTrack);
            if (!updateFlag) {
                log.error("TRM设备MesLotTrack批次产量更新出错："+eqpId+"  "+mesLotTrack.getLotNo());
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
        String eventId = "";
        if(evtRecord.getEventId() != null && evtRecord.getEventId().startsWith("11")){
            eventId = "1";
        }else if(evtRecord.getEventId() != null &&evtRecord.getEventId().length()==1){
            eventId = "0";
        } else if(evtRecord.getEventId() != null && (evtRecord.getEventId().startsWith("23") || evtRecord.getEventId().startsWith("21"))){
            eventId = "3";
        }

        edcDskLogOperation.setEventId(eventId);
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
        if("ProcessStateChg_EVENT".equals(evtRecord.getEventDesc())){
            if("0".equals(evtRecord.getEventId())){
                edcEqpState.setState("DOWN");
            }else if("3".equals(evtRecord.getEventId())){
                edcEqpState.setState("RUN");
            }
        }
        if("21049".equals(evtRecord.getEventId()) || "20".equals(evtRecord.getEventId())){
            edcEqpState.setState("RUN");
        }
        if(evtRecord.getEventParams()!= null && evtRecord.getEventId()!=null){
            if(evtRecord.getEventId().startsWith("23")){
                edcEqpState.setState("IDLE");
            }
        }
        equipmentStatus.setEqpStatus(edcEqpState.getState());
        fabEquipmentStatusService.updateById(equipmentStatus);
        if(edcEqpState.getState()!=null){
            EdcEqpState oldEdcEqpState = iEdcEqpStateService.findLastData(evtRecord.getStartDate(),evtRecord.getEqpId());
            oldEdcEqpState.setEndTime(evtRecord.getStartDate());
            Double state = (double) (edcEqpState.getStartTime().getTime() - oldEdcEqpState.getStartTime().getTime());
            oldEdcEqpState.setStateTimes(state);
            iEdcEqpStateService.updateById(oldEdcEqpState);
            iEdcEqpStateService.insert(edcEqpState);
        }
    }
    public Boolean sendAlarmEmail(String eqpId,String tempPv,int tempMax,int tempMin){
        Boolean flag = false;
        double temp = Double.parseDouble(tempPv);
        if(temp < tempMin || temp > tempMax){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("EQP_ID", eqpId);
            jsonObject.put("ALARM_CODE", "E-0009");
            String jsonString = jsonObject.toJSONString();
            log.info(eqpId+"设备---温度不在规定范围之内!将发送邮件通知管理人员");
            try {
                rabbitTemplate.convertAndSend("C2S.Q.MSG.MAIL", jsonString);
            } catch (Exception e) {
                log.error("Exception:", e);
            }
            flag = true;
        }
        return flag;
    }
}
