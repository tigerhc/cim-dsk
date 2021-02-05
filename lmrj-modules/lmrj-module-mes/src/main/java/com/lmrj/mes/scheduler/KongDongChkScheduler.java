package com.lmrj.mes.scheduler;

import com.lmrj.mes.kongdong.service.IMsMeasureKongdongService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
@Slf4j
public class KongDongChkScheduler {
    @Autowired
    private IMsMeasureKongdongService kongdongService;

    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "0 0 0/1 * * ?") //凌晨1点跑一次
    public void chkDataDefect(){
        String yesterday = getYesterDay();
        Map<String, Object> chkParam = new HashMap<>();
        chkParam.put("startTime",yesterday + " 00:00:00");
        chkParam.put("endTime",yesterday + " 23:59:59");
        Map<String, Object> chkKongdongRs = kongdongService.chkDataDefect(chkParam);
        log.error("chkKongdongRs:"+ JsonUtil.toJsonString(chkKongdongRs));
    }

    private String getYesterDay(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,-24);
        return dateFormat.format(calendar.getTime());
    }
}
