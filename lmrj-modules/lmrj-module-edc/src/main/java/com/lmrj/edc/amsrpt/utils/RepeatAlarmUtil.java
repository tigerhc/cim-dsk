package com.lmrj.edc.amsrpt.utils;

import com.google.common.collect.Maps;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.edc.ams.entity.EdcAmsDefine;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsDefineService;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefine;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefineActEmail;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptRecord;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptRecordDtl;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineActEmailService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptRecordDtlService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptRecordService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.impl.EqpApiService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.rw.plan.entity.RwPlan;
import com.lmrj.rw.plan.service.IRwPlanService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author hsg
 */
@Slf4j
@Component
public class RepeatAlarmUtil {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private IEdcAmsRptRecordService edcAmsRptRecordService;
    @Autowired
    private IEdcAmsRptDefineService edcAmsRptDefineService;
    @Autowired
    private IEdcAmsRptRecordDtlService edcAmsRptRecordDtlService;
    @Autowired
    private IEmailSendService emailSendService; //??????????????????
    @Autowired
    private EqpApiService eqptService ; //????????????OPI??????
    @Autowired
    private IFabEquipmentService fabEquipmentService;
    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;
    @Autowired
    private IEdcAmsDefineService edcAmsDefineService;
    @Autowired
    private IEdcAmsRptDefineActEmailService edAmsRptEamilService;
    @Autowired
    private IRwPlanService rwPlanService;

    public void queryAlarmDefine(){
        //?????????alarm???????????????redis???
        if (redisTemplate.opsForList().size("amsRptDefineList") > 0){
        }else {
            List<EdcAmsRptDefine> amsRptDefineList = edcAmsRptDefineService.selectList(new EntityWrapper<>());
            for (EdcAmsRptDefine amsRptDefine:amsRptDefineList) {
                redisTemplate.opsForList().rightPush("amsRptDefineList", amsRptDefine);
            }
        }


        if (redisTemplate.opsForList().size("amsDefineList") > 0){
            return;
        }else {
            List<EdcAmsDefine> amsDefineList = edcAmsDefineService.selectList(new EntityWrapper<>());
            for (EdcAmsDefine amsDefine:amsDefineList) {
                redisTemplate.opsForList().rightPush("amsDefineList", amsDefine);
            }
        }

    }

    //?????????????????????????????????
    public void putEdcAmsRecordInMq(List<EdcAmsRecord> edcAmsRecordList){
        for (EdcAmsRecord edcAmsRecord:edcAmsRecordList) {
            Map<String, String> map = new HashMap<>();
            map.put("alarm", JsonUtil.toJsonString(edcAmsRecord));
            String msg = JsonUtil.toJsonString(map);
            System.out.println(msg);
            Object test = rabbitTemplate.convertSendAndReceive("C2S.Q.ALARMRPT.DATA", msg);
            System.out.println(test);
        }
    }

    public void repeatAlarm(EdcAmsRecord edcAmsRecord){
        log.info("start ??????????????????{} : {}", edcAmsRecord.getEqpId(), edcAmsRecord.getAlarmCode());
        //???????????????????????????alarm
        List<EdcAmsRptDefine> amsRptDefineList = redisTemplate.opsForList().range("amsRptDefineList", 0, -1);
        List<EdcAmsDefine> amsDefineList = redisTemplate.opsForList().range("amsDefineList", 0, -1);//edcAmsDefineService.selectList(new EntityWrapper<>());//
        for (EdcAmsDefine amsDefine:amsDefineList){
            if(amsDefine.getAlarmCode().equals(edcAmsRecord.getAlarmCode())){
                //??????alarm_id????????????
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(edcAmsRecord.getEqpId());
               if (fabEquipment.getModelId().equals(amsDefine.getEqpModelId())){
                    //??????????????????????????????
                   for (EdcAmsRptDefine amsRptDefine:amsRptDefineList) {
                      if(amsRptDefine.getAlarmId().equals(amsDefine.getId())) {
                          //??????????????????????????????

                          //?????????????????????????????????rpt-alarm ???????????????????????????????????????
                          if(!"param_check".equals(edcAmsRecord.getAlarmCode())){
                              resolveRepeatAlarm(amsRptDefine, edcAmsRecord,amsDefine);
                          }else{
                            //????????????
                            //?????????????????????
                            Map<String,Object> map = new HashMap<String,Object>();
                              map.put("rpt_alarm_id",amsRptDefine.getId());
                              List<EdcAmsRptDefineActEmail> edcAmsRptEmailList = edAmsRptEamilService.selectByMap(map);
                              if(edcAmsRptEmailList!=null&&edcAmsRptEmailList.size()>0){
                                  List<String> emailList = new ArrayList<String>();
                                  for (EdcAmsRptDefineActEmail edcAmsRptEmail:edcAmsRptEmailList) {
                                      emailList.add(edcAmsRptEmail.getUserEmail());
                                  }
                                  String[] email = emailList.toArray(new String[emailList.size()]);
                                  Map<String, Object> datas = Maps.newHashMap();
                                  String paramCOde = "";
                                  String recipeCode = "";
                                  String[] detail = edcAmsRecord.getAlarmDetail()==null?null:edcAmsRecord.getAlarmDetail().split(",");
                                  if(detail!=null&&detail.length>=2){
                                      recipeCode = detail[0];
                                      paramCOde = detail[1];
                                  }
                                  datas.put("PARAM_CODE", paramCOde);
                                  datas.put("RECIPE_CODE", recipeCode);
                                  datas.put("EQP_ID", edcAmsRecord.getEqpId());
                                  emailSendService.send(email,"PARAM_CHECK",datas);
                                  EdcAmsRptRecord edcAmsRptRecord = new EdcAmsRptRecord();
                                  edcAmsRptRecord.setAlarmCode(edcAmsRecord.getAlarmCode());
                                  edcAmsRptRecord.setAlarmId(edcAmsRecord.getId());
                                  edcAmsRptRecord.setAlarmName(edcAmsRecord.getAlarmName());
                                  edcAmsRptRecord.setEqpId(edcAmsRecord.getEqpId());
                                  edcAmsRptRecord.setEqpModelId(amsRptDefine.getEqpModelId());
                                  edcAmsRptRecord.setEqpModelName(fabEquipmentModelService.selectList(new EntityWrapper<FabEquipmentModel>().eq("id", amsRptDefine.getEqpModelId())).get(0).getModelName());
                                  edcAmsRptRecord.setLotNo(edcAmsRecord.getLotNo());
                                  edcAmsRptRecord.setRptAlarmId(amsRptDefine.getAlarmId());
                                  //????????????edcAmsRptRecord??????
                                  edcAmsRptRecordService.insert(edcAmsRptRecord);
                              }

                              //?????????????????????????????????edc_ams_rpt_record???edc_ams_rpt_record_dtl??????
                          }

                          return;
                      }
                   }
                }
            }
        }
//        for (EdcAmsRptDefine amsRptDefine:amsRptDefineList) {
////            if (amsRptDefine.getAlarmId().equals(edcAmsRecord.getAlarmCode())){
////                //??????alarm_id????????????
////                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(edcAmsRecord.getEqpId());
////                if (fabEquipment.getModelId().equals(amsRptDefine.getEqpModelId())){
////                    //??????????????????????????????
////                    resolveRepeatAlarm(amsRptDefine, edcAmsRecord);
////                    return;
////                }
////            }
////        }
    }

    private void resolveRepeatAlarm(EdcAmsRptDefine amsRptDefine ,EdcAmsRecord edcAmsRecord,EdcAmsDefine edcAmsDefine){
        String eventId = StringUtil.randomTimeUUID("RPT");
        log.info("start ??????????????????????????????{} : {}", edcAmsRecord.getEqpId(), edcAmsRecord.getAlarmCode());
        String key = edcAmsRecord.getEqpId() + edcAmsRecord.getAlarmCode();
        redisTemplate.opsForList().rightPush(key, edcAmsRecord);
        if (redisTemplate.opsForList().size(key) >= amsRptDefine.getRepeatNum()){
            //?????????????????????????????????????????????????????????
            EdcAmsRecord firstAlarm = (EdcAmsRecord)redisTemplate.opsForList().index(key, 0);
            EdcAmsRecord lastAlarm = (EdcAmsRecord)redisTemplate.opsForList().index(key, redisTemplate.opsForList().size(key)-1);
            if (lastAlarm.getCreateDate().getTime() - firstAlarm.getCreateDate().getTime() < amsRptDefine.getRepeatCycle() * 60 * 1000){
                fabLogService.info(edcAmsRecord.getEqpId(),eventId,"","??????????????????",edcAmsRecord.getLotNo(),"");
                //???????????????????????????????????????????????????????????????????????????????????????????????????
                if(edcAmsDefine.getAlarmCategory()!=null&&"2".equals(edcAmsDefine.getAlarmCategory())){//??????IOT??????????????????  ????????????2??????????????????????????????
                    RwPlan plan = new RwPlan();
                    plan.setPlanId(edcAmsRecord.getId());
                    plan.setEqpId(edcAmsRecord.getEqpId());
                    plan.setAssignedTime(new Date());
                    plan.setAssignedUser("robbort");//???????????????????????????????????????
                    plan.setPlanType("2");//???????????????
                    plan.setPlanStatus("1");//??????????????????????????????
                    rwPlanService.createPlan(plan);
                }else{
                    //????????????
                    eqptService.lockEqpt("1", edcAmsRecord.getEqpId(),"ALARM_LOCK");
                }

                //TODO  ????????????
//                .sendEmail("ALARM","","RepeatAlarm??????",
//                        "??????:" + edcAmsRecord.getEqpId() + ",??????RepeatAlarm,?????????????????????" + "\n"
//                                + "??????id:" + edcAmsRecord.getAlarmCode() + ",????????????:"
//                                + edcAmsRecord.getAlarmName() + "\n" + "???????????????????????????????????????");
                //?????????????????????????????????edc_ams_rpt_record???edc_ams_rpt_record_dtl??????
                EdcAmsRptRecord edcAmsRptRecord = new EdcAmsRptRecord();
                edcAmsRptRecord.setAlarmCode(edcAmsRecord.getAlarmCode());
                edcAmsRptRecord.setAlarmId(edcAmsRecord.getId());
                edcAmsRptRecord.setAlarmName(edcAmsRecord.getAlarmName());
                edcAmsRptRecord.setEqpId(edcAmsRecord.getEqpId());
                edcAmsRptRecord.setEqpModelId(amsRptDefine.getEqpModelId());
                edcAmsRptRecord.setEqpModelName(fabEquipmentModelService.selectList(new EntityWrapper<FabEquipmentModel>().eq("id", amsRptDefine.getEqpModelId())).get(0).getModelName());
                edcAmsRptRecord.setLotNo(edcAmsRecord.getLotNo());
                edcAmsRptRecord.setRptAlarmId("1");
                //????????????edcAmsRptRecord??????
                edcAmsRptRecordService.insert(edcAmsRptRecord);
                List<EdcAmsRptRecordDtl> edcAmsRptRecordDtlList = new ArrayList<>();
                List<EdcAmsRecord> edcAmsRecordList = (List<EdcAmsRecord>)redisTemplate.opsForList().range(key,0,-1);
                for (EdcAmsRecord edcAmsRecord1:edcAmsRecordList) {
                    //?????????????????????????????????????????????
                    redisTemplate.opsForList().leftPop(key);
                    EdcAmsRptRecordDtl edcAmsRptRecordDtl = new EdcAmsRptRecordDtl();
                    edcAmsRptRecordDtl.setAlarmCode(edcAmsRecord1.getAlarmCode());
                    edcAmsRptRecordDtl.setAlarmDetail(edcAmsRecord1.getAlarmDetail());
                    edcAmsRptRecordDtl.setAlarmName(edcAmsRecord1.getAlarmName());
                    edcAmsRptRecordDtl.setAlarmRecordId(edcAmsRecord1.getId());
                    edcAmsRptRecordDtl.setAlarmSwitch(edcAmsRecord1.getAlarmSwitch());
                    edcAmsRptRecordDtl.setEndDate(edcAmsRecord1.getEndDate());
                    edcAmsRptRecordDtl.setEqpId(edcAmsRecord1.getEqpId());
                    edcAmsRptRecordDtl.setRptAlarmId(edcAmsRptRecord.getRptAlarmId());
                    edcAmsRptRecordDtl.setStartDate(edcAmsRecord1.getStartDate());
                    edcAmsRptRecordDtlList.add(edcAmsRptRecordDtl);
                }
                //????????????edcAmsRptRecord?????????????????????edcAmsRptRecordDtl??????
                edcAmsRptRecordDtlService.insertBatch(edcAmsRptRecordDtlList,100);
            }else {
                //?????????????????????alarm????????????
                redisTemplate.opsForList().leftPop(key);
            }
        }
        log.info("end ??????????????????????????????{} : {}", edcAmsRecord.getEqpId(), edcAmsRecord.getAlarmCode());
    }
}
