package com.lmrj.cim.quartz;

import com.lmrj.edc.lot.service.impl.RptLotYieldDayServiceImpl;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class RptYieldDayTask {
    @Autowired
    RptLotYieldDayServiceImpl rptLotYieldDayService;
    @Autowired
    IFabEquipmentService iFabEquipmentService;
    /**
     * 计算产量,写入报表 edc_dsk_log_production -- >   rpt_lot_yield_day
     *
     * rpt_lot_yield: 批次产量,当前站点的产量
     */
    //@Scheduled(cron = "0 0/20 * * * ?")
    public void updateDayYield() {
        Date endTime=new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        endTime=cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        Date startTime=cal.getTime();
        log.info("日产量计算定时任务开始执行");
        String lineNo="SIM";
        List<String> stationCodeList = iFabEquipmentService.findStationCodeByLineNo(lineNo);
        for (String stationCode : stationCodeList) {
            rptLotYieldDayService.updateDayYield(startTime,endTime,lineNo,stationCode);
        }
        log.info("日产量计算定时任务执行结束");
    }
}
