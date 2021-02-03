package com.lmrj.dsk.edc.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.core.entity.MesResult;
import com.lmrj.dsk.eqplog.entity.*;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionDefectiveService;
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
import net.sf.json.JSONObject;
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
    @Autowired
    IEdcDskLogProductionDefectiveService iEdcDskLogProductionDefectiveService;
    @Autowired
    IOvnBatchLotService iOvnBatchLotService;


    @Autowired
    IEdcDskLogProductionService iEdcDskLogProductionService;




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


//        if(edcDskLogProductionList.get(0).getEqpId().equals("SIM-HGAZO1")){
//            this.temperatureList(edcDskLogProductionList);}


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
                if(eqpId.equals("SIM-YGAZO1")){
                    this.temperatureList2(edcDskLogProductionList,lotTrack.getLotNo());
                }
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
            if(eqpId.equals("SIM-YGAZO1")){
                if(proList.size() > 0){
                    this.temperatureList2(proList,lotTrack.getLotNo());
                }
                if (nextproList.size() > 0) {
                    this.temperatureList2(nextproList, nextLotTrack.getLotNo());
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
            if("N".equals(edcDskLogProduction.getJudgeResult())){
                edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
                edcDskLogProduction.setLotYield(i);
            }else{
                edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
                edcDskLogProduction.setLotYield(i);
                i++;
            }
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
                if(!"N".equals(edcDskLogProduction.getJudgeResult())){
                    edcDskLogProduction.setJudgeResult("Y");
                }
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
        List<EdcDskLogProductionDefective> defectiveProList = new ArrayList<>();
        List<EdcDskLogProduction> goodPro = new ArrayList<>();
        for (EdcDskLogProduction edcDskLogProduction : proList) {
            if("N".equals(edcDskLogProduction.getJudgeResult())){
                Date startTime = edcDskLogProduction.getStartTime();
                Date endTime = edcDskLogProduction.getEndTime();
                edcDskLogProduction.setStartTime(null);
                edcDskLogProduction.setEndTime(null);
                JSONObject json = JSONObject.fromObject(edcDskLogProduction);
                EdcDskLogProductionDefective defectivePro = JsonUtil.from(json.toString(),EdcDskLogProductionDefective.class);
                defectivePro.setStartTime(startTime);
                defectivePro.setEndTime(endTime);
                defectivePro.setId(null);
                defectiveProList.add(defectivePro);
            }else{
                goodPro.add(edcDskLogProduction);
            }
        }
        if(defectiveProList.size()>0){
            iEdcDskLogProductionDefectiveService.insertBatch(defectiveProList,100);
        }
        String eventId = null;
        eventId = StringUtil.randomTimeUUID("RPT");
        EdcDskLogProduction lastPro = null;
        boolean updateFlag = false;
        try {
            if (edcDskLogProductionService.insertBatch(goodPro,100)) {
                fabLogService.info(eqpId, eventId, "fixProData", "production数据插入结束,共" + proList.size() + "条", mesLotTrack.getLotNo(), "gxj");
            }
            //判断该批次是否为最后一个批次 若不是 查询范围为当前批次开始到下一批次开始
            List<EdcDskLogProduction> allProList = new ArrayList<>();
            //MesLotTrack lastTrack = iMesLotTrackService.findLastTrack(mesLotTrack.getEqpId(), mesLotTrack.getLotNo(), mesLotTrack.getStartTime());
            allProList = edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(),mesLotTrack.getEqpId(),mesLotTrack.getProductionNo());
            lastPro = goodPro.get(goodPro.size() - 1);
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





    public void temperatureList(List<EdcDskLogProduction> proList) {
        try {
            List<OvnBatchLotParam> paramList = new ArrayList<>();
            OvnBatchLot ovnBatchLot = new OvnBatchLot();
            ovnBatchLot.setId(StringUtil.randomTimeUUID());
            ovnBatchLot.setEqpId(proList.get(0).getEqpId());
            ovnBatchLot.setStartTime(proList.get(0).getStartTime());
            ovnBatchLot.setEndTime(proList.get(proList.size()-1).getStartTime());
            ovnBatchLot.setOtherTempsTitle("0003相似度当前值,0003相似度SET,0003相似度MIN,0003相似度MAX,0005相似度当前值,0005相似度SET,0005相似度MIN,0005相似度MAX,0006相似度当前值,0006相似度SET,0006相似度MIN,0006相似度MAX,0007相似度当前值,0007相似度SET,0007相似度MIN,0007相似度MAX,0008相似度当前值,0008相似度SET,0008相似度MIN,0008相似度MAX,0009相似度当前值,0009相似度SET,0009相似度MIN,0009相似度MAX,0010相似度当前值,0010相似度SET,0010相似度MIN,0010相似度MAX,0011相似度当前值,0011相似度SET,0011相似度MIN,0011相似度MAX,0012相似度当前值,0012相似度SET,0012相似度MIN,0012相似度MAX,0013相似度当前值,0013相似度SET,0013相似度MIN,0013相似度MAX,0014相似度当前值,0014相似度SET,0014相似度MIN,0014相似度MAX,0015相似度当前值,0015相似度SET,0015相似度MIN,0015相似度MAX,0016相似度当前值,0016相似度SET,0016相似度MIN,0016相似度MAX,0017相似度当前值,0017相似度SET,0017相似度MIN,0017相似度MAX,0018相似度当前值,0018相似度SET,0018相似度MIN,0018相似度MAX,0019相似度当前值,0019相似度SET,0019相似度MIN,0019相似度MAX,0020相似度当前值,0020相似度SET,0020相似度MIN,0020相似度MAX,0021相似度当前值,0021相似度SET,0021相似度MIN,0021相似度MAX,0022相似度当前值,0022相似度SET,0022相似度MIN,0022相似度MAX,0023相似度当前值,0023相似度SET,0023相似度MIN,0023相似度MAX,0024相似度当前值,0024相似度SET,0024相似度MIN,0024相似度MAX,0025相似度当前值,0025相似度SET,0025相似度MIN,0025相似度MAX,0026相似度当前值,0026相似度SET,0026相似度MIN,0026相似度MAX,0027相似度当前值,0027相似度SET,0027相似度MIN,0027相似度MAX,0028相似度当前值,0028相似度SET,0028相似度MIN,0028相似度MAX,0029相似度当前值,0029相似度SET,0029相似度MIN,0029相似度MAX,0030相似度当前值,0030相似度SET,0030相似度MIN,0030相似度MAX,0031相似度当前值,0031相似度SET,0031相似度MIN,0031相似度MAX,,");
            for (EdcDskLogProduction edcDskLogProduction:proList){
                OvnBatchLotParam ovnBatchLotParam = new OvnBatchLotParam();
                String[] a = edcDskLogProduction.getParamValue().split(",");
                Long create =  edcDskLogProduction.getStartTime().getTime()+(1000);
                StringBuilder temp = new StringBuilder();
                for (int i = 5; i < 116; i++) {
                    if (i==5){
                        temp.append(a[6]+",0,"+a[7]+",0,") ;
                        i=7;
                    }else {
                        temp.append(a[i+1]+",0,"+a[i+2]+","+a[i+3]+",") ;
                        i+=3;
                    }
                }
                Date createTime = new Date(create);
                ovnBatchLotParam.setBatchId(ovnBatchLot.getId());
                ovnBatchLotParam.setTempPv(a[3]);
                ovnBatchLotParam.setCreateDate(createTime);
                ovnBatchLotParam.setTempMax("0");
                ovnBatchLotParam.setTempMin(a[4]);
                ovnBatchLotParam.setTempSp("0");
                ovnBatchLotParam.setRemarks(a[1]);
                ovnBatchLotParam.setOtherTempsValue(temp.toString());
                paramList.add(ovnBatchLotParam);
            }
            ovnBatchLot.setOvnBatchLotParamList(paramList);

            //实现主表一天内只有一条数据
            Long time = ovnBatchLot.getStartTime().getTime()-24*60*60*1000;
            Date stime = new Date(time);
            OvnBatchLot ovnBatchLot1 = iOvnBatchLotService.findBatchData(ovnBatchLot.getEqpId(),stime);
            if(ovnBatchLot1!=null){
                List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                ovnBatchLot1.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                iOvnBatchLotService.updateById(ovnBatchLot1);
                for (OvnBatchLotParam batchLotParam : OvnBatchLotParamList) {
                    batchLotParam.setBatchId(ovnBatchLot1.getId());
                }
                iOvnBatchLotParamService.insertBatch(OvnBatchLotParamList);
            }else{
                iOvnBatchLotService.insert(ovnBatchLot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void temperatureList2(List<EdcDskLogProduction> proList,String lotNo) {
        log.info("SIM-YGAZO1温度数据插入开始！");
        try {
            List<OvnBatchLotParam> paramList = new ArrayList<>();
            OvnBatchLot ovnBatchLot = new OvnBatchLot();
            ovnBatchLot.setId(StringUtil.randomTimeUUID());
            ovnBatchLot.setEqpId(proList.get(0).getEqpId());
            ovnBatchLot.setStartTime(proList.get(0).getStartTime());
            ovnBatchLot.setLotId(lotNo);
            ovnBatchLot.setEndTime(proList.get(proList.size()-1).getStartTime());
            ovnBatchLot.setOtherTempsTitle("T102面积当前值,T102面积SET,T102面积MIN,T102面积MAX,T103面积当前值,T103面积SET,T103面积MIN,T103面积MAX,T104面积当前值,T105面积SET,T105面积MIN,T105面积MAX,T106面积当前值,T106面积SET,T106面积MIN,T106面积MAX,T107面积当前值,T107面积SET,T107面积MIN,T107面积MAX,T108面积当前值,T108面积SET,T108面积MIN,T108面积MAX,T109面积当前值,T109面积SET,T109面积MIN,T109面积MAX,T110面积当前值,T110面积SET,T110面积MIN,T110面积MAX,T111面积当前值,T111面积SET,T111面积MIN,T111面积MAX,T112面积当前值,T112面积SET,T112面积MIN,T112面积MAX,T113面积当前值,T113面积SET,T113面积MIN,T113面积MAX,T114面积当前值,T114面积SET,T114面积MIN,T114面积MAX,T115面积当前值,T115面积SET,T115面积MIN,T115面积MAX,T116面积当前值,T116面积SET,T116面积MIN,T116面积MAX,T117面积当前值,T117面积SET,T117面积MIN,T117面积MAX,T118面积当前值,T118面积SET,T118面积MIN,T118面积MAX,T119面积当前值,T119面积SET,T119面积MIN,T119面积MAX,T120面积当前值,T120面积SET,T120面积MIN,T120面积MAX,T121面积当前值,T121面积SET,T121面积MIN,T121面积MAX,T122面积当前值,T122面积SET,T122面积MIN,T122面积MAX,T123面积当前值,T123面积SET,T123面积MIN,T123面积MAX,T124面积当前值,T124面积SET,T124面积MIN,T124面积MAX,T125面积当前值,T125面积SET,T125面积MIN,T125面积MAX,T126面积当前值,T126面积SET,T126面积MIN,T126面积MAX,T127面积当前值,T127面积SET,T127面积MIN,T127面积MAX,T128面积当前值,T128面积SET,T128面积MIN,T128面积MAX,T129面积当前值,T129面积SET,T129面积MIN,T129面积MAX,,");
            for (EdcDskLogProduction edcDskLogProduction:proList){
                OvnBatchLotParam ovnBatchLotParam = new OvnBatchLotParam();
                String[] a = edcDskLogProduction.getParamValue().split(",");
                if(a.length<20){
                    continue;
                }
                Long create = new Date().getTime();
                if(edcDskLogProduction.getStartTime()!=null){
                    create =  edcDskLogProduction.getStartTime().getTime()+(1000);
                }
                StringBuilder temp = new StringBuilder();
                for (int i = 6; i < a.length; i++) {
                    temp.append(a[i+1]+",0,"+a[i+2]+","+a[i+3]+",") ;
                    i+=3;
                }
                Date createTime = new Date(create);
                ovnBatchLotParam.setBatchId(ovnBatchLot.getId());
                ovnBatchLotParam.setTempPv(a[3]);
                ovnBatchLotParam.setCreateDate(createTime);
                ovnBatchLotParam.setTempMax(a[5]);
                ovnBatchLotParam.setTempMin(a[4]);
                ovnBatchLotParam.setTempSp("0");
                ovnBatchLotParam.setRemarks(a[1]);
                ovnBatchLotParam.setOtherTempsValue(temp.toString());
                paramList.add(ovnBatchLotParam);

            }
            ovnBatchLot.setOvnBatchLotParamList(paramList);
            //实现主表一个批次只有一条数据
            OvnBatchLot ovnBatchLot1 = iOvnBatchLotService.findBatchDataByLot(proList.get(0).getEqpId(),lotNo);
            if(ovnBatchLot1!=null){
                List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                ovnBatchLot1.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                iOvnBatchLotService.updateById(ovnBatchLot1);
                for (OvnBatchLotParam batchLotParam : OvnBatchLotParamList) {
                    batchLotParam.setBatchId(ovnBatchLot1.getId());
                }
                iOvnBatchLotParamService.insertBatch(OvnBatchLotParamList);
            }else{
                iOvnBatchLotService.insert(ovnBatchLot);
            }
        } catch (Exception e) {
            log.error("SIM-YGAZO1温度数据插入失败！");
            log.error("",e);
            e.printStackTrace();
        }

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
            }
            EdcEqpState edcEqpState = new EdcEqpState();
            edcEqpState.setEqpId(edcDskLogOperation.getEqpId());
            edcEqpState.setStartTime(edcDskLogOperation.getStartTime());
            if ("0".equals(eventId) || "7".equals(eventId)) {
                status = "DOWN";
            } else if ("1".equals(eventId) || "6".equals(eventId)) {
                status = "RUN";
            } else if ("3".equals(eventId)) {
                status = "IDLE";
            } else if ("2".equals(eventId)) {
                status = "ALARM";
            }
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
            Long time = ovnBatchLot.getStartTime().getTime()-24*60*60*1000;
            Date startTime = new Date(time);
            if(eqpId.equals("SIM-PRINTER1")){
                ovnBatchLot.setOtherTempsTitle("温度,湿度");
            }
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
            if(eqpId.contains("APJ")){
                List<OvnBatchLotParam> paramList = ovnBatchLot.getOvnBatchLotParamList();
                for (OvnBatchLotParam ovnBatchLotParam : paramList) {
                    sendAlarmEmail(eqpId,ovnBatchLotParam.getTempPv());
                    String temp = ovnBatchLotParam.getOtherTempsValue();
                    String  temps[] = temp.split(",");
                    for (int i = 0; i < temps.length; i+=4) {
                        String nowTemp = temps[i];
                        sendAlarmEmail(eqpId,nowTemp);
                    }
                }
            }

        }
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.MSG.MAIL"})
    public void sendAlarm(String msg) {
        String eqpId = null;
        String alarmCode = null;
        String code = "RTP_ALARM";
        Map<String, Object> msgMap = JsonUtil.from(msg, Map.class);
        eqpId = (String) msgMap.get("EQP_ID");
        alarmCode = (String) msgMap.get("ALARM_CODE");
        List<Map<String, Object>> users = new ArrayList<>();
        List<Map<String, Object>> department = fabEquipmentService.findDepartment(eqpId);
        if(!alarmCode.equals(":网络断开连接!")){
            users = fabEquipmentService.findEmailALL(alarmCode);
            code = alarmCode;
        }else if (department.get(0).get("department").equals("YK")) {
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
        emailSendService.blockSend(params, code, msgMap);
    }



    public Boolean sendAlarmEmail(String eqpId,String tempPv){
        Boolean flag = false;
        double temp = Double.parseDouble(tempPv);
        if(temp > 500){
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("EQP_ID", eqpId+"  温度："+tempPv);
            jsonObject.put("ALARM_CODE", "E-1000");
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
