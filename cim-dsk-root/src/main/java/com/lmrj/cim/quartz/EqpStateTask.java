package com.lmrj.cim.quartz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

//@Slf4j
//@Component
public class EqpStateTask {

    //@Autowired
    //private IEdcDskLogOperationService edcDskLogOperationService;

    /**
     * 计算当天的设备OEE数据
     * 每隔10分钟一次
     *
     */
//    @Scheduled(cron = "0 0/10 * * * ?")
//    public void eqpStateDay() {
//        log.info("EqpStateTask定时任务开始执行");
//        //当天时间
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        //cal.add(Calendar.DAY_OF_MONTH, -1);
//
//        log.info("EqpStateTask定时任务结束执行");
//    }

}
