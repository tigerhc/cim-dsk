package com.lmrj.edc.amsrpt.utils;

import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefine;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptRecord;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptRecordDtl;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptRecordDtlService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptRecordService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.impl.EqpApiService;
import com.lmrj.fab.log.service.IFabLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    private IEdcAmsRptRecordService edcAmsRptRecordService;
    @Autowired
    private IEdcAmsRptDefineService edcAmsRptDefineService;
    @Autowired
    private IEdcAmsRptRecordDtlService edcAmsRptRecordDtlService;
    @Autowired
    private IEmailSendService emailSendService; //发送邮件使用
    @Autowired
    private EqpApiService eqptService ; //设备操作OPI接口
    @Autowired
    private IFabEquipmentService fabEquipmentService;
    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;
    @Autowired
    private static IEdcDskLogProductionService edcDskLogProductionService;

    public void queryAlarmDefine(){
        //先查询alarm配置保存在redis中
        List<EdcAmsRptDefine> amsRptDefineList = edcAmsRptDefineService.selectList(new EntityWrapper<>());
        for (EdcAmsRptDefine amsRptDefine:amsRptDefineList) {
            redisTemplate.opsForList().rightPush("amsRptDefineList", amsRptDefine);
        }
    }

    public void repeatAlarm(EdcAmsRecord alarm){
        log.info("start 检查报警信息");
        //先看是不是配置过的alarm
        List<EdcAmsRptDefine> amsRptDefineList = redisTemplate.opsForList().range("amsRptDefineList", 0, -1);
        for (EdcAmsRptDefine amsRptDefine:amsRptDefineList) {
            if (amsRptDefine.getAlarmId().equals(alarm.getAlarmCode())){
                //如果alarm_id是否符合
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(alarm.getEqpId());
                if (fabEquipment.getModelId().equals(amsRptDefine.getEqpModelId())){
                    //如果设备类型是否符合
                    resolveRepeatAlarm(amsRptDefine, alarm);
                    return;
                }
            }
        }
    }

    private void resolveRepeatAlarm(EdcAmsRptDefine amsRptDefine ,EdcAmsRecord alarm){
        String key = alarm.getEqpId() + alarm.getAlarmCode();
        redisTemplate.opsForList().rightPush(key, alarm);
        if (redisTemplate.opsForList().size(key) >= amsRptDefine.getRepeatNum()){
            //如果集合长度是否大于配置的重复报警次数
            EdcAmsRecord firstAlarm = (EdcAmsRecord)redisTemplate.opsForList().index(key, 0);
            EdcAmsRecord lastAlarm = (EdcAmsRecord)redisTemplate.opsForList().index(key, redisTemplate.opsForList().size(key)-1);
            if (lastAlarm.getCreateDate().getTime() - firstAlarm.getCreateDate().getTime() < amsRptDefine.getRepeatCycle() * 60 * 1000){
                //如果集合中第一个报警和最后一个报警之间的间隔时间小于配置的报警时间
                //锁住设备
                eqptService.lockEqpt("1", alarm.getEqpId(),"ALARM_LOCK");
                //发送邮件
                emailSendService.sendEmail("ALARM","","RepeatAlarm信息",
                        "机台:" + alarm.getEqpId() + ",触发RepeatAlarm,报警信息如下：" + "\n"
                                + "报警id:" + alarm.getAlarmCode() + ",报警描述:"
                                + alarm.getAlarmName() + "\n" + "请速联系相关操作人员消警！");
                //清空集合并将数据保存到edc_ams_rpt_record和edc_ams_rpt_record_dtl表中
                EdcAmsRptRecord edcAmsRptRecord = new EdcAmsRptRecord();
                edcAmsRptRecord.setAlarmCode(alarm.getAlarmCode());
                edcAmsRptRecord.setAlarmId(alarm.getId());
                edcAmsRptRecord.setAlarmName(alarm.getAlarmName());
                edcAmsRptRecord.setEqpId(alarm.getEqpId());
                edcAmsRptRecord.setEqpModelId(amsRptDefine.getEqpModelId());
                edcAmsRptRecord.setEqpModelName(fabEquipmentModelService.selectList(new EntityWrapper<FabEquipmentModel>().eq("id", amsRptDefine.getEqpModelId())).get(0).getModelName());
                edcAmsRptRecord.setLotNo(alarm.getLotNo());
                edcAmsRptRecord.setProductionNo(edcDskLogProductionService.selectList(new EntityWrapper<EdcDskLogProduction>().eq("lot_no", alarm.getLotNo())).get(0).getProductionNo());
                edcAmsRptRecord.setRptAlarmId("1");
                edcAmsRptRecordService.insert(edcAmsRptRecord);
                List<EdcAmsRptRecordDtl> edcAmsRptRecordDtlList = new ArrayList<>();
                List<EdcAmsRecord> edcAmsRecordList = (List<EdcAmsRecord>)redisTemplate.opsForList().range(key,0,-1);
                for (EdcAmsRecord edcAmsRecord:edcAmsRecordList) {
                    EdcAmsRptRecordDtl edcAmsRptRecordDtl = new EdcAmsRptRecordDtl();
                    edcAmsRptRecordDtl.setAlarmCode(edcAmsRecord.getAlarmCode());
                    edcAmsRptRecordDtl.setAlarmDetail(edcAmsRecord.getAlarmDetail());
                    edcAmsRptRecordDtl.setAlarmName(edcAmsRecord.getAlarmName());
                    edcAmsRptRecordDtl.setAlarmRecordId(edcAmsRecord.getId());
                    edcAmsRptRecordDtl.setAlarmSwitch(edcAmsRecord.getAlarmSwitch());
                    edcAmsRptRecordDtl.setEndDate(edcAmsRecord.getEndDate());
                    edcAmsRptRecordDtl.setEqpId(edcAmsRecord.getEqpId());
                    edcAmsRptRecordDtl.setRptAlarmId(edcAmsRptRecord.getRptAlarmId());
                    edcAmsRptRecordDtl.setStartDate(edcAmsRecord.getStartDate());
                    edcAmsRptRecordDtlList.add(edcAmsRptRecordDtl);
                }
                edcAmsRptRecordDtlService.insertBatch(edcAmsRptRecordDtlList);
            }else {
                //否则，将第一个alarm移出集合
                redisTemplate.opsForList().leftPop(key);
            }
        }
    }
}
