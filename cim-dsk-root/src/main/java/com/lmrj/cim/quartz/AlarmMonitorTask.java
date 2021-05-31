package com.lmrj.cim.quartz;

import com.alibaba.fastjson.JSONObject;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class AlarmMonitorTask {
    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;
    @Autowired
    IMesLotTrackService mesLotTrackService;
    @Autowired
    IEdcDskLogOperationService iEdcDskLogOperationService;
    @Autowired
    IFabEquipmentService iFabEquipmentService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 监控设备报警持续时间，
     * 每隔10分钟一次
     */
    //@Scheduled(cron = "0 10 * * * ?")
    public void AlarmMonitor() {
        List<String> eqpIdList = new ArrayList<>();
        eqpIdList = iFabEquipmentService.findEqpIdList();
        for (String eqpId : eqpIdList) {
            EdcDskLogOperation operation = iEdcDskLogOperationService.findOperationData(eqpId);
            if ("2".equals(operation.getEventId())) {
                Calendar calNow = Calendar.getInstance();
                calNow.add(Calendar.HOUR_OF_DAY, -1);
                Date date1 = calNow.getTime();
                calNow.add(Calendar.HOUR_OF_DAY, -1);
                Date date2 = calNow.getTime();
                MesLotTrack lotTrack = mesLotTrackService.findNowLotByEqp(eqpId);
                JSONObject jsonObject = new JSONObject();
                String jsonString = "";
                //报警状态持续2小时
                if (date2.getTime() > operation.getStartTime().getTime() && lotTrack.getEndTime() == null) {
                    calNow.setTime(new Date());
                    calNow.add(Calendar.HOUR_OF_DAY, -2);
                    calNow.add(Calendar.MINUTE, -19);
                    //邮件只发送一次
                    if (operation.getStartTime().getTime() > calNow.getTimeInMillis()) {
                        jsonObject.put("EQP_ID", "设备：" + eqpId + " 报警Code：" + operation.getAlarmCode() + " 报警内容：" + operation.getEventName() + " 设备报警持续时间超过2小时，请尽快确认设备情况！");
                        jsonObject.put("ALARM_CODE", "A-0002");
                        jsonString = jsonObject.toJSONString();
                    }
                    //报警状态持续1小时
                } else if (date1.getTime() > operation.getStartTime().getTime() && lotTrack.getEndTime() == null) {
                    calNow.setTime(new Date());
                    calNow.add(Calendar.HOUR_OF_DAY, -1);
                    calNow.add(Calendar.MINUTE, -19);
                    if (operation.getStartTime().getTime() > calNow.getTimeInMillis()) {
                        jsonObject.put("EQP_ID", "设备：" + eqpId + " 报警Code：" + operation.getAlarmCode() + " 报警内容：" + operation.getEventName() + " 设备报警持续时间超过1小时，请尽快确认设备情况！");
                        jsonObject.put("ALARM_CODE", "A-0001");
                        jsonString = jsonObject.toJSONString();
                    }
                }
                if (!"".equals(jsonString)) {
                    log.info(eqpId + "设备---报警持续时间过长，邮件通知相关人员！");
                    try {
                        rabbitTemplate.convertAndSend("C2S.Q.MSG.MAIL", jsonString);
                    } catch (Exception e) {
                        log.error("邮件发送失败 Exception:", e);
                    }
                }
            }
        }
    }
}
