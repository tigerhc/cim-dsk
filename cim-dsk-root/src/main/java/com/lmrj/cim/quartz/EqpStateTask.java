package com.lmrj.cim.quartz;

import com.google.common.collect.Lists;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.entity.RptEqpStateDay;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.edc.state.service.IRptEqpStateDayService;
import com.lmrj.edc.state.service.impl.EdcEqpStateServiceImpl;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Autowired
    IFabEquipmentService iFabEquipmentService;
    @Autowired
    private IRptEqpStateDayService rptEqpStateDayService;
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
            /*String reflowId = "SIM-REFLOW1";
            boolean flag = true;
            for (String s : eqpIdList) {
                if(reflowId.equals(s)){
                    flag = false;
                }
            }
            if(flag){
                eqpIdList.add(reflowId);
            }*/

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
    //数据补充：为昨日没有生成OEE数据的设备生成一条数据，保证页面可以查到
    @Scheduled(cron = "0 0 1 * * ?")
    public void dataSupplement(){
        Date startTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        startTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endTime = cal.getTime();
        List<String> eqpIdList = new ArrayList<>();
        eqpIdList = iFabEquipmentService.findEqpIdList();
        List<RptEqpStateDay> rptEqpStateDayList = Lists.newArrayList();
        for (String eqpId : eqpIdList) {
            List<EdcEqpState> eqpStateList = edcEqpStateService.getAllByTime(startTime, endTime, eqpId);
            if(eqpStateList.size()==0){
                RptEqpStateDay rptEqpStateDay = new RptEqpStateDay();
                rptEqpStateDay.setEqpId(eqpId);
                rptEqpStateDay.setPeriodDate(DateUtil.formatDate(startTime, "yyyyMMdd"));
                Double run = 24 * 60 * 60 * 1000 * 0.001;
                rptEqpStateDay.setRunTime(run);
                rptEqpStateDay.setDownTime(0.0);
                rptEqpStateDay.setIdleTime(0.0);
                rptEqpStateDay.setPmTime(0.0);
                rptEqpStateDay.setOtherTime(0.0);
                rptEqpStateDayList.add(rptEqpStateDay);
            }
        }
        if(rptEqpStateDayList.size()>0){
            rptEqpStateDayService.insertBatch(rptEqpStateDayList,50);
        }

    }

}
