package com.lmrj.cim.quartz;

import com.google.common.collect.Lists;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.entity.RptEqpStateDay;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.edc.state.service.IRptEqpStateDayService;
import com.lmrj.edc.state.service.impl.EdcEqpStateServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;

    /**
     * 计算当天的设备OEE数据
     * 每隔10分钟一次
     */
    //@Scheduled(cron = "0 0/10 * * * ?")
    public void eqpStateDay() {
        if (!jobenabled) {
            return;
        }
        log.info("EqpStateTask定时任务开始执行");
        try {
            //当天时间
            Date startTime = new Date();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date endTime = cal.getTime();
            log.info("定时任务开始执行startTime {} --> endTime {}", startTime, endTime);
            List<FabEquipment> eqpIdList = iFabEquipmentService.findOeeEqpList();
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
            for (FabEquipment fabEquipment : eqpIdList) {
                String eqpId = fabEquipment.getEqpId();
                edcEqpStateService.syncEqpSate(startTime, endTime, eqpId);
            }
            edcEqpStateService.calEqpSateDay(DateUtil.formatDate(startTime, "yyyyMMdd"));
        } catch (Exception e) {
            log.error("EqpStateTask; ", e);
        }
        log.info("EqpStateTask定时任务结束执行");
    }

    //计算前一天设备OEE
    @Scheduled(cron = "0 0 3 * * ?")
    public void eqpOldStateDay() {
        if (!jobenabled) {
            return;
        }
        log.info("EqpStateTask定时任务开始执行");
        try {
            //当天时间
            Date endTime = new Date();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            endTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date startTime = cal.getTime();
            log.info("定时任务开始执行startTime {} --> endTime {}", startTime, endTime);
            List<String> eqpIdList = edcEqpStateService.findEqpId(startTime, endTime);
            for (String eqpId : eqpIdList) {
                edcEqpStateService.syncEqpSate(startTime, endTime, eqpId);
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
    public void fixeqpState(Date startTime, Date endTime) {
        if (!jobenabled) {
            return;
        }
        log.info("定时任务开始执行startTime {} --> endTime {}", startTime, endTime);
        List<String> eqpIdList = edcEqpStateService.findEqpId(startTime, endTime);
        for (String eqpId : eqpIdList) {
            edcEqpStateService.syncEqpSate(startTime, endTime, eqpId);
        }
        edcEqpStateService.calEqpSateDay(DateUtil.formatDate(startTime, "yyyyMMdd"));
    }

    //数据补充：为昨日没有生成OEE数据的设备生成一条数据，保证页面可以查到
    @Scheduled(cron = "0 0 2 * * ?")
    public void dataSupplement() {
        if (!jobenabled) {
            return;
        }
        Date startTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date endTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        startTime = cal.getTime();
        List<String> eqpIdList = new ArrayList<>();
        eqpIdList = iFabEquipmentService.findEqpIdList();
        List<RptEqpStateDay> rptEqpStateDayList = Lists.newArrayList();
        List<EdcEqpState> edcEqpStateList = Lists.newArrayList();
        for (String eqpId : eqpIdList) {
            edcEqpStateService.syncEqpSate(startTime, endTime, eqpId);
            EdcEqpState eqpState = edcEqpStateService.calEqpSateDayByeqpId(startTime, endTime, eqpId);
            if (eqpState == null || "".equals(eqpState.getState()) || null == eqpState.getState() ) {
                RptEqpStateDay rptEqpStateDay = new RptEqpStateDay();
                EdcEqpState edcEqpState = new EdcEqpState();
                rptEqpStateDay.setEqpId(eqpId);
                rptEqpStateDay.setPeriodDate(DateUtil.formatDate(startTime, "yyyyMMdd"));
                Boolean flag = false;
                List<MesLotTrack> lotList = iMesLotTrackService.findCorrectData(startTime, endTime);
                if (lotList.size() > 0){
                    for (MesLotTrack mesLotTrack : lotList) {
                        if (mesLotTrack.getEqpId().equals(eqpId)) {
                            flag = true;
                        }
                    }
                }
                if (flag) {
                    Double run = 24 * 60 * 60 * 1000 * 0.001;
                    rptEqpStateDay.setRunTime(run);
                    rptEqpStateDay.setIdleTime(0.0);
                    edcEqpState.setState("RUN");
                } else {
                    Double idle = 24 * 60 * 60 * 1000 * 0.001;
                    rptEqpStateDay.setRunTime(0.0);
                    rptEqpStateDay.setIdleTime(idle);
                    edcEqpState.setState("IDLE");
                }
                rptEqpStateDay.setPmTime(0.0);
                rptEqpStateDay.setAlarmTime(0.0);
                edcEqpState.setEqpId(eqpId);
                edcEqpState.setStartTime(startTime);
                edcEqpState.setEndTime(endTime);
                edcEqpState.setStateTimes(24 * 60 * 60 * 1000000 * 0.001);
                EdcEqpState eqpState1 = edcEqpStateService.findNewData2(startTime,eqpId);
                if(eqpState1.getStartTime().compareTo(startTime)!=0){
                    edcEqpStateList.add(edcEqpState);
                }
            }
        }
        if (rptEqpStateDayList.size() > 0) {
            rptEqpStateDayService.insertBatch(rptEqpStateDayList, 50);
            for (EdcEqpState edcEqpState : edcEqpStateList) {
                EdcEqpState lastedcEqpState = edcEqpStateService.findNewData2(edcEqpState.getStartTime(), edcEqpState.getEqpId());
                if (lastedcEqpState.getEndTime() == null || lastedcEqpState.getEndTime().compareTo(edcEqpState.getStartTime())!=0 ) {
                    lastedcEqpState.setEndTime(edcEqpState.getStartTime());
                    Double state = (double) (edcEqpState.getStartTime().getTime() - lastedcEqpState.getStartTime().getTime());
                    lastedcEqpState.setStateTimes(state);
                    edcEqpStateService.insertOrUpdate(lastedcEqpState);
                }
            }
            edcEqpStateService.insertOrUpdateAllColumnBatch(edcEqpStateList, 50);
        }
    }

    //@Scheduled(cron = "0 0 11 * * ?")
    public void dataFilling() {
        if (!jobenabled) {
            return;
        }
        List<String> epqIdList = new ArrayList<>();
        epqIdList = iFabEquipmentService.findEqpIdList();
        Date startTime = new Date();
        Date endTime = new Date();
        List<RptEqpStateDay> rptEqpStateDayList = new ArrayList<>();
        for (int i = 30; i > 0; i--) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.DAY_OF_MONTH, -i);
            startTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            endTime = cal.getTime();
            String periodDate = DateUtil.formatDate(endTime, "yyyyMMdd");
            for (String eqpId : epqIdList) {
                int dataNo = 0;
                dataNo = rptEqpStateDayService.selectCount(new EntityWrapper<RptEqpStateDay>().eq("eqp_id", eqpId).eq("period_date", periodDate));
                if (dataNo == 0) {
                    RptEqpStateDay newData = new RptEqpStateDay();
                    newData.setEqpId(eqpId);
                    List<MesLotTrack> lotList = iMesLotTrackService.findCorrectData(startTime, endTime);
                    Boolean flag = false;
                    if (lotList.size() > 0) {
                        for (MesLotTrack mesLotTrack : lotList) {
                            if (mesLotTrack.getEqpId().equals(eqpId)) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            Double run = 24 * 60 * 60 * 1000 * 0.001;
                            newData.setRunTime(run);
                            newData.setDownTime(0.0);
                        }
                    } else {
                        Double down = 24 * 60 * 60 * 1000 * 0.001;
                        newData.setRunTime(0.0);
                        newData.setDownTime(down);
                    }
                    newData.setAlarmTime(0.0);
                    newData.setIdleTime(0.0);
                    newData.setPeriodDate(periodDate);
                    newData.setCreateBy("GXJTEST");
                    rptEqpStateDayList.add(newData);
                }
            }
        }
        try {
            rptEqpStateDayService.insertBatch(rptEqpStateDayList, 1000);
        } catch (Exception e) {
            log.info("数据插入失败", e);
            e.printStackTrace();
        }
    }

}
