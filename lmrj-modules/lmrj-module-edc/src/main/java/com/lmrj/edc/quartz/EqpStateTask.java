package com.lmrj.edc.quartz;

import com.lmrj.edc.state.service.IEdcEqpStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
@Component
public class EqpStateTask {

    @Autowired
    private IEdcEqpStateService edcEqpStateService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void sync() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(calendar.getTime());
        log.error("定时任务开始执行time{}", time);
        int size = edcEqpStateService.syncEqp(time);
        log.error("定时任务开始执行结束size{}", size);
    }


}
