package com.lmrj.cim.quartz;

import com.lmrj.edc.lot.service.impl.RptLotYieldDayServiceImpl;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
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

    @Value("${dsk.lineNo}")
    String lineNo;
    /**
     * 计算产量,写入报表 edc_dsk_log_production -- >   rpt_lot_yield_day
     * rpt_lot_yield: 批次产量,当前站点的产量
     */
    //@Scheduled(cron = "0 0/20 * * * ?")
    public void updateDayYield() {
        for (int i = 15; i >0 ; i--) {
            Date endTime=new Date();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY,8);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.add(Calendar.DAY_OF_MONTH,-i);
            endTime=cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH,-1);
            Date startTime=cal.getTime();
            log.info("日产量计算定时任务开始执行");
            SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
            try {
                rptLotYieldDayService.deleteByDate(sim.format(startTime),lineNo);
            } catch (Exception e) {
                log.error("日产量数据删除出错");
                e.printStackTrace();
            }
            try {
                List<String> stationCodeList = iFabEquipmentService.findStationCodeByLineNo(lineNo);
                for (String stationCode : stationCodeList) {
                    rptLotYieldDayService.updateDayYield(startTime,endTime,lineNo,stationCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("updateDayYield():执行异常");
            }
            log.info("日产量计算定时任务执行结束");
        }

    }
}
