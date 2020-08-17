package com.lmrj.cim.quartz;

import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class EqpStateTask {

    //@Autowired
    //private IEdcDskLogOperationService edcDskLogOperationService;
    @Autowired
    private IEdcEqpStateService edcEqpStateService;

    /**
     * 计算当天的设备OEE数据
     * 每隔10分钟一次
     */
    //@Scheduled(cron = "0 0/10 * * * ?")
    public void eqpStateDay() {
        log.info("EqpStateTask定时任务开始执行");
        try {
            //当天时间
            Date startTime = new Date();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND,0);
            startTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date endTime = cal.getTime();
            log.info("定时任务开始执行startTime {} --> endTime {}", startTime, endTime);
            List<String> eqpIdList=edcEqpStateService.findEqpId(startTime, endTime);
            for (String eqpId : eqpIdList) {
                edcEqpStateService.syncEqpSate(startTime, endTime,eqpId);
            }
            edcEqpStateService.calEqpSateDay(DateUtil.formatDate(startTime, "yyyyMMdd"));
        } catch (Exception e) {
            log.error("EqpStateTask; ", e);
        }
        log.info("EqpStateTask定时任务结束执行");
    }
    /**
     * 修正前天的设备OEE数据
     * 每天八点执行
     */
    //@Scheduled(cron = "0 0 8 * * ?")
    public void  fixeqpState(){
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        endTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = cal.getTime();
        log.info("定时任务开始执行startTime {} --> endTime {}", startTime, endTime);
        List<String> eqpIdList=edcEqpStateService.findEqpId(startTime, endTime);
        for (String eqpId : eqpIdList) {
            edcEqpStateService.syncEqpSate(startTime, endTime,eqpId);
        }
    }

}
