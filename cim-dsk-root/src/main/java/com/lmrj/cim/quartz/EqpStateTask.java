package com.lmrj.cim.quartz;

import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.edc.state.service.impl.EdcEqpStateServiceImpl;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    @Autowired
    EdcEqpStateServiceImpl edcEqpStateServiceImpl;
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
            //当SIM-REFLOW1没有数据时补充数据
            String reflowId = "SIM-REFLOW1";
            boolean flag = true;
            for (String s : eqpIdList) {
                if(reflowId.equals(s)){
                    flag = false;
                }
            }
            if(flag){
                eqpIdList.add(reflowId);
            }

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
    public void  fixeqpState(Date startTime , Date endTime){
        log.info("定时任务开始执行startTime {} --> endTime {}", startTime, endTime);
        //List<String> eqpIdList=edcEqpStateService.findEqpId(startTime, endTime);
        List<String> eqpIdList= new ArrayList<>();
        eqpIdList.add("SIM-WB-3A");
        eqpIdList.add("SIM-WB-4B");
        eqpIdList.add("SIM-WB-5A");
        eqpIdList.add("SIM-WB-5B");
        eqpIdList.add("SIM-WB-6A");
        for (String eqpId : eqpIdList) {
            edcEqpStateServiceImpl.syncOldEqpSate(startTime, endTime,eqpId);
        }
        edcEqpStateService.calEqpSateDay(DateUtil.formatDate(startTime, "yyyyMMdd"));
    }

}
