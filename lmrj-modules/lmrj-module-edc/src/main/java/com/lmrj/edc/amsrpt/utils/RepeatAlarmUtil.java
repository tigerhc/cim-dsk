package com.lmrj.edc.amsrpt.utils;

import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.email.service.IEmailSendService;
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
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IEmailSendService emailSendService; //发送邮件使用
    @Autowired
    private EqpApiService eqptService ; //设备操作OPI接口
    @Autowired
    private IFabEquipmentService fabEquipmentService;
    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;

    public void queryAlarmDefine(){
        //先查询alarm配置保存在redis中
        if (redisTemplate.opsForList().size("amsRptDefineList") > 0){
            return;
        }else {
            List<EdcAmsRptDefine> amsRptDefineList = edcAmsRptDefineService.selectList(new EntityWrapper<>());
            for (EdcAmsRptDefine amsRptDefine:amsRptDefineList) {
                redisTemplate.opsForList().rightPush("amsRptDefineList", amsRptDefine);
            }
        }
    }

    //将报警信息放入消息队列
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
        log.info("start 检查报警信息{} : {}", edcAmsRecord.getEqpId(), edcAmsRecord.getAlarmCode());
        //先看是不是配置过的alarm
        List<EdcAmsRptDefine> amsRptDefineList = redisTemplate.opsForList().range("amsRptDefineList", 0, -1);
        for (EdcAmsRptDefine amsRptDefine:amsRptDefineList) {
            if (amsRptDefine.getAlarmId().equals(edcAmsRecord.getAlarmCode())){
                //如果alarm_id是否符合
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(edcAmsRecord.getEqpId());
                if (fabEquipment.getModelId().equals(amsRptDefine.getEqpModelId())){
                    //如果设备类型是否符合
                    resolveRepeatAlarm(amsRptDefine, edcAmsRecord);
                    return;
                }
            }
        }
    }

    private void resolveRepeatAlarm(EdcAmsRptDefine amsRptDefine ,EdcAmsRecord edcAmsRecord){
        String eventId = StringUtil.randomTimeUUID("RPT");
        log.info("start 判断是否触发重复报警{} : {}", edcAmsRecord.getEqpId(), edcAmsRecord.getAlarmCode());
        String key = edcAmsRecord.getEqpId() + edcAmsRecord.getAlarmCode();
        redisTemplate.opsForList().rightPush(key, edcAmsRecord);
        if (redisTemplate.opsForList().size(key) >= amsRptDefine.getRepeatNum()){
            //如果集合长度是否大于配置的重复报警次数
            EdcAmsRecord firstAlarm = (EdcAmsRecord)redisTemplate.opsForList().index(key, 0);
            EdcAmsRecord lastAlarm = (EdcAmsRecord)redisTemplate.opsForList().index(key, redisTemplate.opsForList().size(key)-1);
            if (lastAlarm.getCreateDate().getTime() - firstAlarm.getCreateDate().getTime() < amsRptDefine.getRepeatCycle() * 60 * 1000){
                fabLogService.info(edcAmsRecord.getEqpId(),eventId,"","触发重复报警",edcAmsRecord.getLotNo(),"");
                //如果集合中第一个报警和最后一个报警之间的间隔时间小于配置的报警时间
                //锁住设备
                eqptService.lockEqpt("1", edcAmsRecord.getEqpId(),"ALARM_LOCK");
                //TODO  发送邮件
//                emailSendService.sendEmail("ALARM","","RepeatAlarm信息",
//                        "机台:" + edcAmsRecord.getEqpId() + ",触发RepeatAlarm,报警信息如下：" + "\n"
//                                + "报警id:" + edcAmsRecord.getAlarmCode() + ",报警描述:"
//                                + edcAmsRecord.getAlarmName() + "\n" + "请速联系相关操作人员消警！");
                //清空集合并将数据保存到edc_ams_rpt_record和edc_ams_rpt_record_dtl表中
                EdcAmsRptRecord edcAmsRptRecord = new EdcAmsRptRecord();
                edcAmsRptRecord.setAlarmCode(edcAmsRecord.getAlarmCode());
                edcAmsRptRecord.setAlarmId(edcAmsRecord.getId());
                edcAmsRptRecord.setAlarmName(edcAmsRecord.getAlarmName());
                edcAmsRptRecord.setEqpId(edcAmsRecord.getEqpId());
                edcAmsRptRecord.setEqpModelId(amsRptDefine.getEqpModelId());
                edcAmsRptRecord.setEqpModelName(fabEquipmentModelService.selectList(new EntityWrapper<FabEquipmentModel>().eq("id", amsRptDefine.getEqpModelId())).get(0).getModelName());
                edcAmsRptRecord.setLotNo(edcAmsRecord.getLotNo());
                edcAmsRptRecord.setRptAlarmId("1");
                //保存一条edcAmsRptRecord记录
                edcAmsRptRecordService.insert(edcAmsRptRecord);
                List<EdcAmsRptRecordDtl> edcAmsRptRecordDtlList = new ArrayList<>();
                List<EdcAmsRecord> edcAmsRecordList = (List<EdcAmsRecord>)redisTemplate.opsForList().range(key,0,-1);
                for (EdcAmsRecord edcAmsRecord1:edcAmsRecordList) {
                    //往数据库添加一条记录就移除一个
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
                //保存一条edcAmsRptRecord记录对应的几条edcAmsRptRecordDtl记录
                edcAmsRptRecordDtlService.insertBatch(edcAmsRptRecordDtlList,100);
            }else {
                //否则，将第一个alarm移出集合
                redisTemplate.opsForList().leftPop(key);
            }
        }
        log.info("end 判断是否触发重复报警{} : {}", edcAmsRecord.getEqpId(), edcAmsRecord.getAlarmCode());
    }
}
