package com.lmrj.cim.quartz;

import com.lmrj.edc.state.service.IEdcEqpStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    //@Scheduled(cron = "0 0/10 * * * ?")
    public void eqpStateDay() {
        log.info("EqpStateTask定时任务开始执行");
        //当天时间
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(calendar.getTime());
        log.error("定时任务开始执行time{}", time);
        edcEqpStateService.syncEqp(time);
        log.info("EqpStateTask定时任务结束执行");
    }

}
