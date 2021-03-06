package com.lmrj.dsk.edc.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.core.email.entity.EmailSendLog;
import com.lmrj.core.email.service.IEmailSendLogService;
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
import com.lmrj.edc.param.service.impl.EdcEqpLogParamServiceImpl;
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
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.service.impl
 * @title: ovn_batch_lot????????????
 * @description: ovn_batch_lot????????????
 * @author: zhangweijiang
 * @date: 2019-06-09 08:49:15
 * @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
 */
@Service
@Slf4j
public class EdcDskLogHandler {
    static Map<String, String> recipeLimitMap = new HashMap<>();

    static {
        //  2:SIM-2ND BOND
        //  1:SIM-1ST BOND
        recipeLimitMap.put("2ND_???????????????_HMIC-LEAD", "270~150");
        recipeLimitMap.put("2ND_???????????????_HMIC-MOS", "90~70");
        recipeLimitMap.put("2ND_???????????????_BDI-LEAD", "270~150");
        recipeLimitMap.put("2ND_???????????????_HMIC-BDI", "130~110");
        recipeLimitMap.put("2ND_???????????????_LMIC-LEAD", "270~150");
        recipeLimitMap.put("2ND_???????????????_LMIC-MOS", "90~70");
        recipeLimitMap.put("2ND_???????????????_MOS-LEAD", "270~150");
        recipeLimitMap.put("2ND_???????????????_HMIC-HMIC", "130~110");

        recipeLimitMap.put("2ND_???????????????_HMIC-LEAD", "770~600");
        recipeLimitMap.put("2ND_???????????????_HMIC-MOS", "160~140");
        recipeLimitMap.put("2ND_???????????????_BDI-LEAD", "770~600");
        recipeLimitMap.put("2ND_???????????????_HMIC-BDI", "160~140");
        recipeLimitMap.put("2ND_???????????????_LMIC-LEAD", "770~600");
        recipeLimitMap.put("2ND_???????????????_LMIC-MOS", "160~140");
        recipeLimitMap.put("2ND_???????????????_MOS-LEAD", "770~600");
        recipeLimitMap.put("2ND_???????????????_HMIC-HMIC", "160~140");

        recipeLimitMap.put("1ST_???????????????_H...", "260~240");
        recipeLimitMap.put("1ST_???????????????_B...", "260~240");
        recipeLimitMap.put("1ST_???????????????_L...", "260~240");
        recipeLimitMap.put("1ST_???????????????_M...", "90~70");

        recipeLimitMap.put("1ST_???????????????_B...", "520~480");
        recipeLimitMap.put("1ST_???????????????_H...", "420~380");
        recipeLimitMap.put("1ST_???????????????_L...", "420~380");
        recipeLimitMap.put("1ST_???????????????_M...", "390~350");
    }

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
    @Autowired
    EdcEqpLogParamServiceImpl edcEqpLogParamService;
    @Autowired
    IEmailSendLogService iEmailSendLogService;
    StringBuffer alarmEmailLog = new StringBuffer();//??????????????????????????????????????????????????? TODO
    long lastSendMailTime = 0L;//?????????????????????????????????

    String[] paramEdit = {"Pick up pos  Z", "???????????? Z",
            "Pick up press level", "?????????????????????",
            "1st bonding pos  Z", "?????????????????? Z",
            "Bonding press level", "?????????????????????",
            "Pickup search level", "??????????????????",
            "Pickup search speed", "??????????????????",
            "1st plunge up height", "???1????????????",
            "2nd plunge up height", "???2????????????"
    };
    // TODO: 2020/7/8 ???????????????
    String[] emails = {"hanzy@ms5.sanken-ele.co.jp", "suchang@ms5.sanken-ele.co.jp",
            "zhangwj@lmrj.com", "403396835@qq.com"};


    //{"eqpId":"OVEN-F-01","eventId":"ON","eventParams":null,"startDate":"2019-11-12 19:31:33 416"}
    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.PRODUCTIONLOG.DATA"})
    public void parseProductionlog(String msg) {
        //String msg = new String(message, "UTF-8");
        log.info("C2S.Q.PRODUCTIONLOG.DATA??????????????????" + msg);
        try {
            List<EdcDskLogProduction> edcDskLogProductionList = JsonUtil.from(msg, new TypeReference<List<EdcDskLogProduction>>() {
            });
//        if(edcDskLogProductionList.get(0).getEqpId().equals("SIM-HGAZO1")){
//            this.temperatureList(edcDskLogProductionList);}
            List<EdcDskLogProduction> proList = new ArrayList<>();
            List<EdcDskLogProduction> nextproList = new ArrayList<>();

            if (edcDskLogProductionList.size() > 0) {
                EdcDskLogProduction edcDskLogProduction0 = edcDskLogProductionList.get(0);
                String eqpId = edcDskLogProduction0.getEqpId();
                //?????????????????????????????????
                MesLotTrack lotTrack = mesLotTrackService.findLotByStartTime(eqpId, edcDskLogProduction0.getStartTime());
                MesLotTrack nextLotTrack = mesLotTrackService.findLastTrack(eqpId, lotTrack.getLotNo(), lotTrack.getStartTime());
                //????????????
                if (nextLotTrack == null) {
                    fixProData(edcDskLogProductionList, lotTrack);
                    if (eqpId.equals("SIM-YGAZO1")) {
                        this.temperatureList2(edcDskLogProductionList, lotTrack.getLotNo());
                    }
                    //???????????? ????????????????????????????????????????????????????????????????????? ???????????????????????????
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
                if (eqpId.equals("SIM-YGAZO1")) {
                    if (proList.size() > 0) {
                        this.temperatureList2(proList, lotTrack.getLotNo());
                    }
                    if (nextproList.size() > 0) {
                        this.temperatureList2(nextproList, nextLotTrack.getLotNo());
                    }
                }
                try {
                    FabEquipmentStatus fabStatus = fabEquipmentStatusService.findByEqpId(eqpId);
                    if (!"RUN".equals(fabStatus.getEqpStatus()) && eqpId.contains("SIM-")) {
                        EdcDskLogOperation operation = edcDskLogOperationService.findOperationData(eqpId);
                        if(operation != null && edcDskLogProductionList.get(edcDskLogProductionList.size() - 1).getEndTime().after(operation.getStartTime())){
                            fabStatus.setEqpStatus("RUN");
                            fabEquipmentStatusService.updateById(fabStatus);
                            EdcEqpState edcEqpState = new EdcEqpState();
                            edcEqpState.setEqpId(eqpId);
                            edcEqpState.setStartTime(edcDskLogProductionList.get(edcDskLogProductionList.size() - 1).getEndTime());
                            edcEqpState.setState("RUN");
                            String stateJson = JsonUtil.toJsonString(edcEqpState);
                            rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateJson);
                        }else if(operation == null ){
                            fabStatus.setEqpStatus("RUN");
                            fabEquipmentStatusService.updateById(fabStatus);
                            EdcEqpState edcEqpState = new EdcEqpState();
                            edcEqpState.setEqpId(eqpId);
                            edcEqpState.setStartTime(edcDskLogProductionList.get(edcDskLogProductionList.size() - 1).getEndTime());
                            edcEqpState.setState("RUN");
                            String stateJson = JsonUtil.toJsonString(edcEqpState);
                            rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateJson);
                        }
                    }
                } catch (Exception e) {
                    log.error("????????????????????????",e);
                    e.printStackTrace();
                }
            } else {

            }
        } catch (Exception e) {
            log.error("???????????????????????????"+ msg, e);
            e.printStackTrace();
        }


    }

    //?????????????????????
    //????????????   ??????????????????????????????????????????????????????????????? ?????????????????????????????????
    public void fixProData(List<EdcDskLogProduction> proList, MesLotTrack mesLotTrack) {
        int yield = 1;
        int input = 1;
        String eqpId = mesLotTrack.getEqpId();
        //mesLotTrack??????????????????????????????
        List<EdcDskLogProduction> productionList = edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(), mesLotTrack.getEqpId(), mesLotTrack.getProductionNo());
        List<EdcDskLogProductionDefective> efectiveProductionList = iEdcDskLogProductionDefectiveService.findDataBylotNo(mesLotTrack.getLotNo(), mesLotTrack.getEqpId(), mesLotTrack.getProductionNo());
        if (productionList.size() > 0) {
            yield = productionList.size() + 1;
        }
        if (efectiveProductionList.size() > 0) {
            input = efectiveProductionList.size() + 1;
        }
        //??????????????????
        for (EdcDskLogProduction edcDskLogProduction : proList) {
            edcDskLogProduction.setProductionNo(mesLotTrack.getProductionNo());
            edcDskLogProduction.setOrderNo(mesLotTrack.getOrderNo());
            //????????????????????????
            if ("N".equals(edcDskLogProduction.getJudgeResult())) {
                edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
                edcDskLogProduction.setLotYield(yield);
            } else {
                edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
                edcDskLogProduction.setLotYield(yield);
                yield++;
            }
            //????????????????????????
            edcDskLogProduction.setLotInput(input);
            input++;
        }
        //?????????REFLOW ??? PRINTER ?????????12
        if (eqpId.contains("SIM-REFLOW") || eqpId.contains("SIM-PRINTER")) {
            for (EdcDskLogProduction edcDskLogProduction : proList) {
                edcDskLogProduction.setJudgeResult("Y");
                edcDskLogProduction.setLotYield(edcDskLogProduction.getLotYield() * 12);
            }
        }
        if (StringUtil.isNotBlank(eqpId)) {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            proList.forEach(edcDskLogProduction -> {
                edcDskLogProduction.setEqpNo(fabEquipment.getEqpNo());
                edcDskLogProduction.setEqpModelId(fabEquipment.getModelId());
                edcDskLogProduction.setEqpModelName(fabEquipment.getModelName());
                if (!"N".equals(edcDskLogProduction.getJudgeResult())) {
                    edcDskLogProduction.setJudgeResult("Y");
                }
            });
        }
        //???SIM-DM??????????????????
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
            if ("N".equals(edcDskLogProduction.getJudgeResult())) {
                Date startTime = edcDskLogProduction.getStartTime();
                Date endTime = edcDskLogProduction.getEndTime();
                edcDskLogProduction.setStartTime(null);
                edcDskLogProduction.setEndTime(null);
                JSONObject json = JSONObject.fromObject(edcDskLogProduction);
                EdcDskLogProductionDefective defectivePro = JsonUtil.from(json.toString(), EdcDskLogProductionDefective.class);
                defectivePro.setStartTime(startTime);
                defectivePro.setEndTime(endTime);
                defectivePro.setId(null);
                defectiveProList.add(defectivePro);
            } else {
                goodPro.add(edcDskLogProduction);
            }
        }
        if (defectiveProList.size() > 0) {
            iEdcDskLogProductionDefectiveService.insertBatch(defectiveProList, 100);
        }
        if (goodPro.size() > 0) {
            String eventId = null;
            eventId = StringUtil.randomTimeUUID("RPT");
            EdcDskLogProduction lastPro = null;
            boolean updateFlag = false;
            lastPro = goodPro.get(goodPro.size() - 1);
            try {
                if (edcDskLogProductionService.insertBatch(goodPro, 100)) {
                    fabLogService.info(eqpId, eventId, "fixProData", "production??????????????????,???" + proList.size() + "???", mesLotTrack.getLotNo(), "gxj");
                }
                //?????????????????????????????????????????? ????????? ??????????????????????????????????????????????????????
                List<EdcDskLogProduction> allProList = new ArrayList<>();
                //MesLotTrack lastTrack = iMesLotTrackService.findLastTrack(mesLotTrack.getEqpId(), mesLotTrack.getLotNo(), mesLotTrack.getStartTime());
                allProList = edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(), mesLotTrack.getEqpId(), mesLotTrack.getProductionNo());
                mesLotTrack.setLotYieldEqp(allProList.size());
                mesLotTrack.setLotInput(proList.get(proList.size() - 1).getLotInput());
                if (eqpId.contains("SIM-REFLOW") || eqpId.contains("SIM-PRINTER")) {
                    mesLotTrack.setLotYieldEqp(allProList.size() * 12);
                }
                mesLotTrack.setUpdateBy("gxj");
                updateFlag = mesLotTrackService.updateById(mesLotTrack);
                log.info("??????????????????????????????");
                FabEquipmentStatus fabStatus = fabEquipmentStatusService.findByEqpId(eqpId);
                if (fabStatus != null) {
                    fabStatus.setLotYield(allProList.size());
                    fabStatus.setRecipeCode(lastPro.getRecipeCode());
                    fabStatus.setDayYield(lastPro.getDayYield());
                    fabEquipmentStatusService.updateById(fabStatus);
                }
            } catch (Exception e) {
                e.printStackTrace();
                fabLogService.info(eqpId, eventId, "fixProData", "track????????????" + e + "       ", mesLotTrack.getLotNo(), "gxj");
            }
            if (!updateFlag) {
                System.out.println("????????????");
                mesLotTrack.setStartTime(new Date());
                if (lastPro.getOrderNo() != null) {
                    mesLotTrack.setOrderNo(lastPro.getOrderNo());
                }
                mesLotTrack.setCreateBy("EQP");
                mesLotTrackService.insert(mesLotTrack);
            }
            fabLogService.info(eqpId, eventId, "fixProData", "track????????????????????????" + mesLotTrack.getLotYieldEqp(), mesLotTrack.getLotNo(), "gxj");
        }
    }


    public void temperatureList(List<EdcDskLogProduction> proList) {
        try {
            List<OvnBatchLotParam> paramList = new ArrayList<>();
            OvnBatchLot ovnBatchLot = new OvnBatchLot();
            ovnBatchLot.setId(StringUtil.randomTimeUUID());
            ovnBatchLot.setEqpId(proList.get(0).getEqpId());
            ovnBatchLot.setStartTime(proList.get(0).getStartTime());
            ovnBatchLot.setEndTime(proList.get(proList.size() - 1).getStartTime());
            ovnBatchLot.setOtherTempsTitle("0003??????????????????,0003?????????SET,0003?????????MIN,0003?????????MAX,0005??????????????????,0005?????????SET,0005?????????MIN,0005?????????MAX,0006??????????????????,0006?????????SET,0006?????????MIN,0006?????????MAX,0007??????????????????,0007?????????SET,0007?????????MIN,0007?????????MAX,0008??????????????????,0008?????????SET,0008?????????MIN,0008?????????MAX,0009??????????????????,0009?????????SET,0009?????????MIN,0009?????????MAX,0010??????????????????,0010?????????SET,0010?????????MIN,0010?????????MAX,0011??????????????????,0011?????????SET,0011?????????MIN,0011?????????MAX,0012??????????????????,0012?????????SET,0012?????????MIN,0012?????????MAX,0013??????????????????,0013?????????SET,0013?????????MIN,0013?????????MAX,0014??????????????????,0014?????????SET,0014?????????MIN,0014?????????MAX,0015??????????????????,0015?????????SET,0015?????????MIN,0015?????????MAX,0016??????????????????,0016?????????SET,0016?????????MIN,0016?????????MAX,0017??????????????????,0017?????????SET,0017?????????MIN,0017?????????MAX,0018??????????????????,0018?????????SET,0018?????????MIN,0018?????????MAX,0019??????????????????,0019?????????SET,0019?????????MIN,0019?????????MAX,0020??????????????????,0020?????????SET,0020?????????MIN,0020?????????MAX,0021??????????????????,0021?????????SET,0021?????????MIN,0021?????????MAX,0022??????????????????,0022?????????SET,0022?????????MIN,0022?????????MAX,0023??????????????????,0023?????????SET,0023?????????MIN,0023?????????MAX,0024??????????????????,0024?????????SET,0024?????????MIN,0024?????????MAX,0025??????????????????,0025?????????SET,0025?????????MIN,0025?????????MAX,0026??????????????????,0026?????????SET,0026?????????MIN,0026?????????MAX,0027??????????????????,0027?????????SET,0027?????????MIN,0027?????????MAX,0028??????????????????,0028?????????SET,0028?????????MIN,0028?????????MAX,0029??????????????????,0029?????????SET,0029?????????MIN,0029?????????MAX,0030??????????????????,0030?????????SET,0030?????????MIN,0030?????????MAX,0031??????????????????,0031?????????SET,0031?????????MIN,0031?????????MAX,,");
            for (EdcDskLogProduction edcDskLogProduction : proList) {
                OvnBatchLotParam ovnBatchLotParam = new OvnBatchLotParam();
                String[] a = edcDskLogProduction.getParamValue().split(",");
                Long create = edcDskLogProduction.getStartTime().getTime() + (1000);
                StringBuilder temp = new StringBuilder();
                for (int i = 5; i < 116; i++) {
                    if (i == 5) {
                        temp.append(a[6] + ",0," + a[7] + ",0,");
                        i = 7;
                    } else {
                        temp.append(a[i + 1] + ",0," + a[i + 2] + "," + a[i + 3] + ",");
                        i += 3;
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

            //???????????????????????????????????????
            Long time = ovnBatchLot.getStartTime().getTime() - 24 * 60 * 60 * 1000;
            Date stime = new Date(time);
            OvnBatchLot ovnBatchLot1 = iOvnBatchLotService.findBatchData(ovnBatchLot.getEqpId(), stime);
            if (ovnBatchLot1 != null) {
                List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                ovnBatchLot1.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                iOvnBatchLotService.updateById(ovnBatchLot1);
                for (OvnBatchLotParam batchLotParam : OvnBatchLotParamList) {
                    batchLotParam.setBatchId(ovnBatchLot1.getId());
                }
                iOvnBatchLotParamService.insertBatch(OvnBatchLotParamList);
            } else {
                iOvnBatchLotService.insert(ovnBatchLot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void temperatureList2(List<EdcDskLogProduction> proList, String lotNo) {
        log.info("SIM-YGAZO1???????????????????????????");
        try {
            List<OvnBatchLotParam> paramList = new ArrayList<>();
            OvnBatchLot ovnBatchLot = new OvnBatchLot();
            ovnBatchLot.setId(StringUtil.randomTimeUUID());
            ovnBatchLot.setEqpId(proList.get(0).getEqpId());
            ovnBatchLot.setStartTime(proList.get(0).getStartTime());
            ovnBatchLot.setStepCode("DM");
            ovnBatchLot.setLotId(lotNo);
            ovnBatchLot.setEndTime(proList.get(proList.size() - 1).getStartTime());
            ovnBatchLot.setOtherTempsTitle("T102???????????????,T102??????SET,T102??????MIN,T102??????MAX,T103???????????????,T103??????SET,T103??????MIN,T103??????MAX,T104???????????????,T105??????SET,T105??????MIN,T105??????MAX,T106???????????????,T106??????SET,T106??????MIN,T106??????MAX,T107???????????????,T107??????SET,T107??????MIN,T107??????MAX,T108???????????????,T108??????SET,T108??????MIN,T108??????MAX,T109???????????????,T109??????SET,T109??????MIN,T109??????MAX,T110???????????????,T110??????SET,T110??????MIN,T110??????MAX,T111???????????????,T111??????SET,T111??????MIN,T111??????MAX,T112???????????????,T112??????SET,T112??????MIN,T112??????MAX,T113???????????????,T113??????SET,T113??????MIN,T113??????MAX,T114???????????????,T114??????SET,T114??????MIN,T114??????MAX,T115???????????????,T115??????SET,T115??????MIN,T115??????MAX,T116???????????????,T116??????SET,T116??????MIN,T116??????MAX,T117???????????????,T117??????SET,T117??????MIN,T117??????MAX,T118???????????????,T118??????SET,T118??????MIN,T118??????MAX,T119???????????????,T119??????SET,T119??????MIN,T119??????MAX,T120???????????????,T120??????SET,T120??????MIN,T120??????MAX,T121???????????????,T121??????SET,T121??????MIN,T121??????MAX,T122???????????????,T122??????SET,T122??????MIN,T122??????MAX,T123???????????????,T123??????SET,T123??????MIN,T123??????MAX,T124???????????????,T124??????SET,T124??????MIN,T124??????MAX,T125???????????????,T125??????SET,T125??????MIN,T125??????MAX,T126???????????????,T126??????SET,T126??????MIN,T126??????MAX,T127???????????????,T127??????SET,T127??????MIN,T127??????MAX,T128???????????????,T128??????SET,T128??????MIN,T128??????MAX,T129???????????????,T129??????SET,T129??????MIN,T129??????MAX,,");
            FabEquipmentStatus equipmentStatus = fabEquipmentStatusService.findByEqpId(proList.get(0).getEqpId());
            ovnBatchLot.setRecipeCode(equipmentStatus.getRecipeCode());
            for (EdcDskLogProduction edcDskLogProduction : proList) {
                OvnBatchLotParam ovnBatchLotParam = new OvnBatchLotParam();
                String[] a = edcDskLogProduction.getParamValue().split(",");
                if (a.length < 20) {
                    continue;
                }
                Long create = new Date().getTime();
                if (edcDskLogProduction.getStartTime() != null) {
                    create = edcDskLogProduction.getStartTime().getTime() + (1000);
                }
                StringBuilder temp = new StringBuilder();
                for (int i = 6; i < a.length; i++) {
                    temp.append(a[i + 1] + ",0," + a[i + 2] + "," + a[i + 3] + ",");
                    i += 3;
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
            //??????????????????????????????????????????
            OvnBatchLot ovnBatchLot1 = iOvnBatchLotService.findBatchDataByLot(proList.get(0).getEqpId(), lotNo);
            if (ovnBatchLot1 != null) {
                List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                ovnBatchLot1.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                iOvnBatchLotService.updateById(ovnBatchLot1);
                for (OvnBatchLotParam batchLotParam : OvnBatchLotParamList) {
                    batchLotParam.setBatchId(ovnBatchLot1.getId());
                }
                iOvnBatchLotParamService.insertBatch(OvnBatchLotParamList);
            } else {
                iOvnBatchLotService.insert(ovnBatchLot);
            }
        } catch (Exception e) {
            log.error("SIM-YGAZO1???????????????????????????");
            log.error("", e);
            e.printStackTrace();
        }

    }


    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.OPERATIONLOG.DATA"})
    public void parseOperationlog(String msg) {
        log.info("C2S.Q.OPERATIONLOG.DATA recieved message:" + msg);
        //public void cureAlarm(byte[] message) throws UnsupportedEncodingException {
        //    String msg = new String(message, "UTF-8");
        //    System.out.println("??????????????????"+msg);
        List<EdcDskLogOperation> edcDskLogOperationlist = JsonUtil.from(msg, new TypeReference<List<EdcDskLogOperation>>() {
        });
        if (edcDskLogOperationlist == null || edcDskLogOperationlist.size() == 0) {
            return;
        }
        EdcDskLogOperation edcDskLogOperation0 = edcDskLogOperationlist.get(0);
        String eqpId = edcDskLogOperation0.getEqpId();
        String eventId1 = StringUtil.randomTimeUUID("EDC");
        fabLogService.info(eqpId, eventId1, "parseOperationlog ", "Operation?????? ???????????????????????????" + edcDskLogOperationlist.size(), "", "gxj");
        if (StringUtil.isNotBlank(eqpId)) {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            for (EdcDskLogOperation edcDskLogOperation : edcDskLogOperationlist) {
                if(eqpId.contains("XRAY")){
                    if("0".equals(edcDskLogOperation.getEventId())){
                        edcDskLogOperation.setEventId("1");
                    }else if("1".equals(edcDskLogOperation.getEventId())){
                        edcDskLogOperation.setEventId("0");
                    }
                }
                EdcDskLogProduction pro = edcDskLogProductionService.findLastYield(edcDskLogOperation.getEqpId(), edcDskLogOperation.getStartTime());
                if (pro != null) {
                    edcDskLogOperation.setLotYield(pro.getLotYield());
                    edcDskLogOperation.setDayYield(pro.getDayYield());
                    edcDskLogOperation.setDayInput(pro.getDayInput());
                    edcDskLogOperation.setLotInput(pro.getLotInput());
                }
                edcDskLogOperation.setEqpNo(fabEquipment.getEqpNo());
                edcDskLogOperation.setEqpModelId(fabEquipment.getModelId());
                edcDskLogOperation.setEqpModelName(fabEquipment.getModelName());
                if (edcDskLogOperation.getEventName() == null && edcDskLogOperation.getEventDetail() == null) {
                    if ("0".equals(edcDskLogOperation.getEventId())) {
                        edcDskLogOperation.setEventName("????????????????????????");
                        edcDskLogOperation.setEventDetail("????????????????????????");
                    } else if ("1".equals(edcDskLogOperation.getEventId())) {
                        edcDskLogOperation.setEventName("??????????????????");
                        edcDskLogOperation.setEventDetail("??????????????????");
                    } else if ("3".equals(edcDskLogOperation.getEventId())) {
                        edcDskLogOperation.setEventName("IDLE(????????????)");
                        edcDskLogOperation.setEventDetail("IDLE(????????????)");
                    }
                }
            }
        }
        try {
            edcDskLogOperationService.insertBatch(edcDskLogOperationlist, 100);
        } catch (Exception e) {
            log.error("Operation ?????????????????????", e);
            e.printStackTrace();
        }

        //??????event??????alarm???
        //(?????????????????????????????????)
        //0:?????????
        //1:??????????????????(????????????)???
        //2:ALM??????
        //3:????????????/????????????
        //4:??????ON???
        //5:??????OFF???
        //6:???????????????1????????????????????????
        //7:???????????????1????????????????????????
        //?????????4???7????????????????????????????????????
        List<EdcEvtRecord> edcEvtRecordList = Lists.newArrayList();
        List<EdcAmsRecord> edcAmsRecordList = Lists.newArrayList();
        List<EdcEqpState> edcEqpStateList = Lists.newArrayList();
        String status = "";
        String alarmInfo = "";
        for (EdcDskLogOperation edcDskLogOperation : edcDskLogOperationlist) {
            String eventId = edcDskLogOperation.getEventId();
            if(eqpId.contains("XRAY")){
                if("0".equals(eventId)){
                    eventId = "1";
                }else if("1".equals(eventId)){
                    eventId = "0";
                }
            }
            if (("2".equals(eventId))) {
                EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
                edcAmsRecord.setEqpId(edcDskLogOperation.getEqpId());
                String alarmCode = edcDskLogOperation.getAlarmCode();
                edcAmsRecord.setAlarmCode(alarmCode);
                String alarmName = edcDskLogOperation.getEventName();
                if (edcDskLogOperation.getEventDetail() != null && !"".equals(edcDskLogOperation.getEventDetail())) {
                    if (!alarmName.equals(edcDskLogOperation.getEventDetail())) {
                        alarmName = alarmName + ":" + edcDskLogOperation.getEventDetail();
                    }
                }
                edcAmsRecord.setAlarmName(alarmName);

                alarmInfo = alarmName+"("+DateUtil.formatDate(edcDskLogOperation.getStartTime(),"yyyy-MM-dd HH:mm:ss")+")";
                edcAmsRecord.setAlarmSwitch("1");
                edcAmsRecord.setLotNo(edcDskLogOperation.getLotNo());
                edcAmsRecord.setLotYield(edcDskLogOperation.getLotYield());
                edcAmsRecord.setStartDate(edcDskLogOperation.getStartTime());
                FabEquipment fabEquipment = iFabEquipmentService.findEqpByCode(edcDskLogOperation.getEqpId());
                if (fabEquipment != null) {
                    edcAmsRecord.setLineNo(fabEquipment.getLineNo());
                    edcAmsRecord.setStationCode(fabEquipment.getStationCode());
                }
                /*if ("02070651".equals(alarmCode) || "020707EC".equals(alarmCode)) {
                    com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                    jsonObject.put("EQP_ID", eqpId + ":??????"+alarmCode+"   "+edcDskLogOperation.getEventName());
                    jsonObject.put("ALARM_CODE", "E-0003");
                    String jsonString = jsonObject.toJSONString();
                    String queueName = "C2S.Q.MSG.MAIL";
                    log.info(eqpId+"??????---???????????????????????????????????????"+alarmCode+"   "+edcDskLogOperation.getEventName() );
                    try {
                        rabbitTemplate.convertAndSend(queueName, jsonString);
                    } catch (Exception e) {
                        log.error("???????????????????????? Exception:", e);
                    }
                }*/
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
                if ((eventDesc == null || "".equals(eventDesc)) && (eventParams == null || "".equals(eventParams))) {
                    if ("0".equals(eventId)) {
                        eventDesc = "????????????????????????";
                        eventParams = "????????????????????????";
                    } else if ("1".equals(eventId)) {
                        eventDesc = " ??????????????????";
                        eventParams = " ??????????????????";
                    } else if ("3".equals(eventId)) {
                        eventDesc = "IDLE(????????????)";
                        eventParams = "IDLE(????????????)";
                    }
                }
                edcEvtRecord.setEventDesc(eventDesc);
                // TODO: 2020/5/24  ??????????????????????????????
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
                            //emailSendService.send(emails, "PARAM_CHANGE", datas);
                            break;
                        }
                    }
                }
                edcEvtRecord.setEventParams(eventParams);
                edcEvtRecord.setStartDate(edcDskLogOperation.getStartTime());
                edcEvtRecordList.add(edcEvtRecord);
            }
            if (!eqpId.contains("SIM-DM")) {
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
            /*if (eqpId.contains("WB")) {
                if (edcDskLogOperation.getEventName().equals("2")) {
                    status = "DOWN";
                } else if (edcDskLogOperation.getEventName().equals("4")) {
                    status = "RUN";
                } else if (edcDskLogOperation.getEventName().equals("8")) {
                    status = "ALARM";
                } else if (edcDskLogOperation.getEventName().equals("16") || edcDskLogOperation.getEventName().equals("32")) {
                    status = "IDLE";
                }
            }*/
                edcEqpState.setState(status);
                //???????????????????????????????????????
                if (StringUtil.isNotBlank(status)) {
                /*edcEqpStateList.add(edcEqpState);
                if(edcEqpStateList.size()>0){
                    String stateListJson = JsonUtil.toJsonString(edcEqpStateList);
                    rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateListJson);
                }*/
                    String stateJson = JsonUtil.toJsonString(edcEqpState);
                    rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateJson);
                    //????????????????????????
                    fabEquipmentStatusService.updateStatus(eqpId, status, "", "",alarmInfo);
                }
            }
        }
        /*if(edcEqpStateList.size()>0){
            String stateListJson = JsonUtil.toJsonString(edcEqpStateList);
            rabbitTemplate.convertAndSend("C2S.Q.STATE.DATA", stateListJson);
        }*/
        if (edcEvtRecordList.size() != 0) {
            edcEvtRecordService.insertBatch(edcEvtRecordList, 1000);
        }
        if (edcAmsRecordList.size() != 0) {
            edcAmsRecordService.insertBatch(edcAmsRecordList, 1000);
            repeatAlarmUtil.putEdcAmsRecordInMq(edcAmsRecordList);
        }
        // TODO: 2020/8/3 ????????????mq????????????

    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.ALARMRPT.DATA"})
    public String repeatAlarm(String msg) {
        log.info("C2S.Q.ALARMRPT.DATA???????????????????????? " + msg);
        try {
            repeatAlarmUtil.queryAlarmDefine();
            Map<String, String> msgMap = JsonUtil.from(msg, Map.class);
            EdcAmsRecord edcAmsRecord = JsonUtil.from(msgMap.get("alarm"), EdcAmsRecord.class);
            System.out.println(edcAmsRecord);
            repeatAlarmUtil.repeatAlarm(edcAmsRecord);
        } catch (Exception e) {
            log.error("C2S.Q.ALARMRPT.DATA???????????????????????? " + msg,e);
            e.printStackTrace();
        }
        return JsonUtil.toJsonString(MesResult.ok("ok"));
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.RECIPELOG.DATA"})
    public void parseRecipelog(String msg) {
        log.info("C2S.Q.RECIPELOG.DATA  recieved message:" + msg);
        List<EdcDskLogRecipe> edcDskLogRecipeList = JsonUtil.from(msg, new TypeReference<List<EdcDskLogRecipe>>() {
        });
        if (edcDskLogRecipeList == null || edcDskLogRecipeList.size() == 0) {
            return;
        }
        //edcDskLogRecipeService.insertBatch(edcDskLogRecipeList);
        edcDskLogRecipeList.forEach(edcDskLogRecipe -> {
            if (edcDskLogRecipe.getEqpId().contains("WB")) {
                if (edcDskLogRecipe.getEdcDskLogRecipeBodyList().size() > 0) {
                    for (EdcDskLogRecipeBody recipeBody : edcDskLogRecipe.getEdcDskLogRecipeBodyList()) {
                        String paramName = recipeBody.getParaName();
                        try {
                            if (paramName.contains("???????????????") || paramName.contains("???????????????")) {
                                String newValue = recipeBody.getSetValue();
                                String oldValue = recipeBody.getPreValue();
                                double newvalue = Double.parseDouble(newValue);
                                double oldvalue = Double.parseDouble(oldValue);
                                if (recipeLimitMap.get(paramName) != null && !"".equals(recipeLimitMap.get(paramName))) {
                                    String maxLimit = recipeLimitMap.get(paramName).split("~")[0];
                                    String minLimit = recipeLimitMap.get(paramName).split("~")[1];
                                    double max = Double.parseDouble(maxLimit);
                                    double min = Double.parseDouble(minLimit);
                                    String jsonString = null;
                                    if (newvalue < min || newvalue > max) {
                                        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                                        jsonObject.put("EQP_ID", edcDskLogRecipe.getEqpId() + ":??????" + paramName + "????????????, ?????????" + recipeBody.getPreValue() + "  ?????????" + recipeBody.getSetValue() + "   ???????????????" + minLimit + "~" + maxLimit + "   ???????????????" + DateUtil.formatDateTime(edcDskLogRecipe.getStartTime()));
                                        jsonObject.put("ALARM_CODE", "E-0004");
                                        jsonString = jsonObject.toJSONString();
                                    } else if ((oldvalue < min || oldvalue > max) && newvalue > min && newvalue < max) {
                                        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                                        jsonObject.put("EQP_ID", edcDskLogRecipe.getEqpId() + ":??????" + paramName + "????????????, ?????????" + recipeBody.getPreValue() + "  ?????????" + recipeBody.getSetValue() + "   ???????????????" + minLimit + "~" + maxLimit + "   ???????????????" + DateUtil.formatDateTime(edcDskLogRecipe.getStartTime()));
                                        jsonObject.put("ALARM_CODE", "E-0004");
                                        jsonString = jsonObject.toJSONString();
                                    }
                                    String queueName = "C2S.Q.MSG.MAIL";
                                    log.info(edcDskLogRecipe.getEqpId() + "??????---????????????" + paramName + "???????????????????????????!");
                                    try {
                                        rabbitTemplate.convertAndSend(queueName, jsonString);
                                    } catch (Exception e) {
                                        log.error("????????????????????????  Exception:", e);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.error("WB????????????????????????", e);
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (edcDskLogRecipe.getEdcDskLogRecipeBodyList().size() > 0) {
                for (EdcDskLogRecipeBody recipeBody : edcDskLogRecipe.getEdcDskLogRecipeBodyList()) {
                    recipeBody.setRecipeLogId(edcDskLogRecipe.getId());
                }
            }
            edcDskLogRecipeService.insert(edcDskLogRecipe);
            fabEquipmentStatusService.updateYield(edcDskLogRecipe.getEqpId(), "", edcDskLogRecipe.getRecipeCode(), -1, -1);
        });
    }
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.TEMPLOG.DATA"})
    public void parseTempHlog(String msg) {
        log.info("C2S.Q.TEMPLOG.DATA recieved message ????????????{}?????????????????? : {} " + msg);
        try {
            OvnBatchLot ovnBatchLot = JsonUtil.from(msg, OvnBatchLot.class);
            String eqpId = ovnBatchLot.getEqpId();
            if (StringUtil.isNotBlank(eqpId)) {
                if (!eqpId.equals("")) {
                    FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
                    ovnBatchLot.setOfficeId(fabEquipment.getOfficeId());
                    FabEquipmentStatus equipmentStatus = fabEquipmentStatusService.findByEqpId(eqpId);
                    if (equipmentStatus != null) {
                        ovnBatchLot.setRecipeCode(equipmentStatus.getRecipeCode());
                    }
                }
                Long time = ovnBatchLot.getStartTime().getTime() - 24 * 60 * 60 * 1000;
                Date startTime = new Date(time);
                if (eqpId.equals("SIM-PRINTER1")) {
                    ovnBatchLot.setOtherTempsTitle("??????,??????");
                }
                OvnBatchLot ovnBatchLot1 = ovnBatchLotService.findBatchData(eqpId, startTime);
                if (ovnBatchLot1 != null) {
                    List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                    ovnBatchLot1.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                    ovnBatchLotService.updateById(ovnBatchLot1);

                    for (OvnBatchLotParam ovnBatchLotParam : OvnBatchLotParamList) {
                        ovnBatchLotParam.setBatchId(ovnBatchLot1.getId());
                        ovnBatchLotParam.setEqpId(ovnBatchLot1.getEqpId());//??????????????????eqpId,?????????????????????????????????
                    }
                    iOvnBatchLotParamService.insertBatch(OvnBatchLotParamList);
                } else {
                    List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                    ovnBatchLot.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                    ovnBatchLotService.insert(ovnBatchLot);
                }
                if (filterEqpId(eqpId)) {
                    tempFilter(ovnBatchLot, msg);//???????????????????????????????????????????????????
                }
            }
        } catch (Exception e) {
            log.error("C2S.Q.TEMPLOG.DATA recieved message ??????{}???????????????????????? : {} " + msg,e);
            e.printStackTrace();
        }
    }


    private boolean filterEqpId(String eqpId) {
        eqpId = eqpId + ",";
        String allEqp = "DM-CLEAN-US1,DM-RT,DM-HT,DM-OVEN1,";
        return allEqp.contains(eqpId);
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.MSG.MAIL"})
    public void sendAlarm(String msg) {
        log.info("C2S.Q.MSG.MAIL ???????????????" + msg);
        String eqpId = null;
        String alarmCode = null;
        String code = "RTP_ALARM";
        Map<String, Object> msgMap = JsonUtil.from(msg, Map.class);
        eqpId = (String) msgMap.get("EQP_ID");
        alarmCode = (String) msgMap.get("ALARM_CODE");
        if ("E-0071".equals(alarmCode) || "E-0072".equals(alarmCode) || "E-0073".equals(alarmCode) || "E-CSV-S".equals(alarmCode) || "E-CSV-F".equals(alarmCode) || "CSV-LOG-ERROR".equals(alarmCode)) {
            //??????????????????????????????
        } else {
            try {
                EmailSendLog emailSendLog = iEmailSendLogService.selectEmailLog(alarmCode);
                if (emailSendLog != null && emailSendLog.getCreateDate() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.HOUR_OF_DAY, -1);
                    if(alarmCode.equals(":??????????????????!") && cal.getTime().before(emailSendLog.getCreateDate()) && emailSendLog.getSendData().contains(eqpId)){
                        log.info("???????????????1???????????????????????????????????????  " + JsonUtil.toJsonString(msgMap));
                        return;
                    }else if(!alarmCode.equals(":??????????????????!") && cal.getTime().before(emailSendLog.getCreateDate())){
                        log.info("???????????????1???????????????????????????????????????  " + JsonUtil.toJsonString(msgMap));
                        return;
                    }
                }
            } catch (Exception e) {
                log.error("?????????????????????",e);
                e.printStackTrace();
            }
        }
        if (eqpId.contains("TRM") && alarmCode.equals("E-0005")) {
            code = "E-0005";
        }
        List<Map<String, Object>> users = new ArrayList<>();
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        try {
            if(alarmCode.equals(":??????????????????!") || alarmCode.equals(":??????????????????!") ||alarmCode.equals("CSV-LOG-ERROR")){
                eqpId = eqpId.split(":")[0].replace("??????","");
            }
        } catch (Exception e) {
            log.error("??????eqpId????????????",e);
            e.printStackTrace();
        }
        List<Map<String, Object>> department = fabEquipmentService.findDepartment(eqpId);
        if (alarmCode.equals(":??????????????????!")) {
            code = "RTP_ALARM";
            if (eqpId.contains("SIM")) {
                if (department.get(0).get("department").equals("YK")) {
                    users = fabEquipmentService.findEmailALL("E-0007");
                } else if (department.get(0).get("department").equals("EK")) {
                    users = fabEquipmentService.findEmailALL("E-0008");
                }
            } else if (eqpId.contains("DM-")) {
                if (department.get(0).get("department").equals("YK") || department.get(0).get("department").equals("BP")) {
                    users = fabEquipmentService.findEmailALL("E-CSV-F");
                } else if (department.get(0).get("department").equals("EK")) {
                    users = fabEquipmentService.findEmailALL("E-CSV-S");
                }
            }
        } else if (alarmCode.equals(":??????????????????!")) {
            code = "RTP_RECOVER";
            if (department.get(0).get("department").equals("YK")) {
                users = fabEquipmentService.findEmailALL("A-0007");
            } else if (department.get(0).get("department").equals("EK")) {
                users = fabEquipmentService.findEmailALL("A-0008");
            } else if (department.get(0).get("department").equals("DM")) {
                users = fabEquipmentService.findEmailALL("A-0001");
            }
        } else if (alarmCode.equals("CSV-LOG-ERROR")) {
            if (department.get(0).get("department").equals("YK") || department.get(0).get("department").equals("BP")) {
                users = fabEquipmentService.findEmailALL("E-CSV-F");
            } else if (department.get(0).get("department").equals("EK")) {
                users = fabEquipmentService.findEmailALL("E-CSV-S");
            }
        } else {
            users = fabEquipmentService.findEmailALL(alarmCode);
        }
        /*if(!alarmCode.equals(":??????????????????!")){
            users = fabEquipmentService.findEmailALL(alarmCode);
            *//*if("E-0009".equals(alarmCode)){
                code = alarmCode;
            }else if("E-0010".equals(alarmCode)){
                code = alarmCode;
            }else if("E-0011".equals(alarmCode)){
                code = alarmCode;
            }*//*
        }else if (department.get(0).get("department").equals("YK")) {
            users = fabEquipmentService.findEmailALL("E-0007");
        } else if (department.get(0).get("department").equals("EK")) {
            users = fabEquipmentService.findEmailALL("E-0008");
        }else if (department.get(0).get("department").equals("APJ")) {
            users = fabEquipmentService.findEmailALL("E-0001");
        }*/
        List<String> param = new ArrayList<>();
        if (!users.isEmpty()) {
            for (Map<String, Object> map : users) {
                param.add((String) map.get("email"));
                log.info("???????????????" + (String) map.get("email"));
            }
        }
        String[] params = new String[param.size()];
        param.toArray(params);
        if (fabEquipment != null) {
            msgMap.put("EQP_ID", eqpId + "(" + fabEquipment.getEqpName() + ")    ???????????????" + DateUtil.formatDateTime(new Date()));
        }

        emailSendService.send(params, code, msgMap);
    }

    //????????????????????????????????????
    private void tempFilter(OvnBatchLot ovnBatchLot, String dataMsg) {
        if (ovnBatchLot != null && ovnBatchLot.getOvnBatchLotParamList() != null) {
            String eqpId = ovnBatchLot.getEqpId();
            List<OvnBatchLotParam> paramList = ovnBatchLot.getOvnBatchLotParamList();
            String curDesc = "????????????" + eqpId;
            for (OvnBatchLotParam ovnBatchLotParam : paramList) {
                //?????????????????????
                double tempPv = Double.parseDouble(ovnBatchLotParam.getTempPv());
                double tempMax = Double.parseDouble(ovnBatchLotParam.getTempMax());
                double tempMin = Double.parseDouble(ovnBatchLotParam.getTempMin());
                boolean sendFlag = false;
                boolean tempFlag = true;
                if (tempPv > tempMin && tempPv < tempMax) {
                    _handleEmailLog(eqpId, "NORMAL");
                } else if (tempPv > tempMax) {
                    tempFlag = false;
                    sendFlag = _handleEmailLog(eqpId, "HEIGHT");
                    curDesc = curDesc + "?????????????????????????????????:" + tempPv + ",???????????????" + tempMin + "???" + tempMax;
                } else if (tempPv < tempMin) {
                    tempFlag = false;
                    if (!sendFlag) {
                        sendFlag = _handleEmailLog(eqpId, "LOW");
                    }
                    curDesc = curDesc + "?????????????????????????????????:" + tempPv + ",???????????????" + tempMin + "???" + tempMax;
                }
                if (tempFlag) {//???????????????????????????????????????
                    String temp = ovnBatchLotParam.getOtherTempsValue();
                    if(temp != null){
                        String temps[] = temp.split(",");
                        for (int i = 0; i < temps.length; i += 4) {
                            double tempOtherPv = Double.parseDouble(temps[i]);
                            double tempOtherMin = Double.parseDouble(temps[i + 1]);
                            double tempOtherMax = Double.parseDouble(temps[i + 3]);
                            if (tempOtherPv > tempOtherMin && tempOtherPv < tempOtherMax) {
                                _handleEmailLog(eqpId, "NORMAL");
                            } else if (tempOtherPv < tempOtherMin) {
                                if (sendFlag) {
                                    break;
                                } else {
                                    sendFlag = _handleEmailLog(eqpId, "LOW");
                                    curDesc = curDesc + "??????????????????????????????" + (i + 2) + "?????????:" + tempOtherPv + ",???????????????" + tempOtherMin + "???" + tempOtherMax;
                                }
                            } else if (tempOtherPv < tempOtherMax) {
                                if (sendFlag) {
                                    break;
                                } else {
                                    sendFlag = _handleEmailLog(eqpId, "HEIGHT");
                                    curDesc = curDesc + "??????????????????????????????" + (i + 2) + "?????????:" + tempOtherPv + ",???????????????" + tempOtherMin + "???" + tempOtherMax;
                                }
                            }
                        }
                    }
                }
                if (sendFlag) {
                    _sendTempAlarmEmail(curDesc, dataMsg);
                    break;
                }
            }
        }
    }

    /**
     * ?????????????????????????????????
     * ????????? ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @return true ??????????????????????????????????????????????????????????????????????????????
     */
    private boolean _handleEmailLog(String eqpId, String status) {
        String sendLogFlag = alarmEmailLog.toString();
        String[] eqpIds = sendLogFlag.split("@");
        String findLogStatus = "";
        for (String eqpMsg : eqpIds) {
            String[] item = eqpMsg.split(",");
            if (item[0].equals(eqpId)) {
                findLogStatus = item[1];
            }
        }
        if ("NORMAL".equals(status) || status.equals(findLogStatus)) {
            if (!StringUtils.isEmpty(findLogStatus) && "NORMAL".equals(status)) {
                sendLogFlag = sendLogFlag.replace(eqpId + "," + findLogStatus + "@", "");
                alarmEmailLog.setLength(0);
                alarmEmailLog.append(sendLogFlag);
            }
            return false;
        } else {
            sendLogFlag = sendLogFlag.replace(eqpId + "," + findLogStatus + "@", "");
            alarmEmailLog.setLength(0);
            alarmEmailLog.append(sendLogFlag);
            alarmEmailLog.append(eqpId + "," + status + "@");
            return true;
        }
    }

    //???????????????????????????
    private void _sendTempAlarmEmail(String msg, String mqDataMsg) {
        long compareTime = before30Minute();//30?????????
        if (compareTime > lastSendMailTime) {//????????????????????????????????????????????????
            lastSendMailTime = new Date().getTime();
            //Map<String, Object> mailMsg = new HashMap<>();
            /*mailMsg.put("EQP_ID", msg + ",??????????????????????????????" + alarmEmailLog.toString() + "???");
            mailMsg.put("ALARM_CODE", "E-1000");
//            System.out.println(JSONObject.fromObject(mailMsg).toString()); TODO ???????????????main ??????
            rabbitTemplate.convertAndSend("C2S.Q.MSG.MAIL", JSONObject.fromObject(mailMsg).toString());*/
            log.error("????????????????????????    EQP_ID", msg + ",??????????????????????????????" + alarmEmailLog.toString() + "???");
        }
    }

    //??????????????????????????????
    public long before30Minute() {
        Date startTime;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -30);
        startTime = cal.getTime();
        return startTime.getTime();
    }

//    public static void main(String[] args) {
//        String dataMsg = "{\"createDate\":\"2021-02-09 08:34:00 107\",\"delFlag\":\"0\",\"eqpId\":\"APJ-AT1\",\"startTime\":\"2021-02-09 08:34:00\",\"otherTempsTitle\":\",,,,,,,,,???1(1)???2(2)???3(3)???4(4)???5(5)???6(6)???7(7)???8(8)???9(9)???10(10)\",\"ovnBatchLotParamList\":[{\"createDate\":\"2021-02-09 08:34:00 107\",\"delFlag\":\"0\",\"tempPv\":\"1000.0\",\"tempSp\":\"150\",\"tempMin\":\"145\",\"tempMax\":\"155\",\"otherTempsValue\":\"1000.0,150,145,155,1000.0,150,145,155,1000.0,150,145,155,1000.0,150,145,155,1000.0,150,145,155,1000.0,150,145,155,1000.0,150,145,155,1000.0,150,145,155,1000.0,150,145,155,\"}]}";
//        OvnBatchLot ovnBatchLot = JsonUtil.from(dataMsg, OvnBatchLot.class);
//        tempFilter(ovnBatchLot, dataMsg);
//        System.out.println("2");
//        tempFilter(ovnBatchLot, dataMsg);
//    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.EQPLOG.PARAM"})
    public String findLogParam(String msg) {
        log.info("??????????????????????????????" + msg);
        String result = null;
        try {
            result = edcEqpLogParamService.findCsvLogParam();
        } catch (Exception e) {
            log.error("???????????????????????????", e);
            e.printStackTrace();
        }
        if (result == null) {
            log.error("???????????????????????????");
        }
        return result;
    }
}
