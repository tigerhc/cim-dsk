package com.lmrj.ms.scheduler;

import com.lmrj.ms.record.service.IMsMeasureRecordService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Configuration
@EnableScheduling
@Slf4j
public class CheckWeightScheduler {
    @Autowired
    IMsMeasureRecordService msMeasureRecordService;

    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "0 0 1 * * ?") //凌晨1点跑一次
    public void chkDataDefect(){
        String yesterday = getYesterDay();
        log.error("每日检测重量的结果:"+ JsonUtil.toJsonString(msMeasureRecordService.chkWeight(yesterday + " 00:00:00", yesterday + " 23:59:59")));
    }

    private String getYesterDay(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,-24);
        return dateFormat.format(calendar.getTime());
    }
}
