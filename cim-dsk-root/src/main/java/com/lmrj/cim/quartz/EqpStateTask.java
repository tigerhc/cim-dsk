package com.lmrj.cim.quartz;

import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

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
     *
     */
    //@Scheduled(cron = "0 * * * * ?")
    public void eqpStateDay() {
        log.info("EqpStateTask定时任务开始执行");
        //当天时间
        Date endTime=new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        endTime=cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        Date startTime=cal.getTime();
        log.error("定时任务开始执行startTime {} --> endTime {}", startTime, endTime);
        edcEqpStateService.syncEqpSate(startTime, endTime);
        edcEqpStateService.calEqpSateDay(DateUtil.formatDate(startTime, "yyyyMMdd"));
        log.info("EqpStateTask定时任务结束执行");
    }

}
