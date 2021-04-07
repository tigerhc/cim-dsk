package com.lmrj.cim.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.lot.entity.RptLotYield;
import com.lmrj.edc.lot.service.IRptLotYieldService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class RptYieldTask {

    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    private IMesLotTrackLogService mesLotTrackLogService;
    @Autowired
    private IRptLotYieldService rptLotYieldService;
    @Autowired
    private IEdcDskLogProductionService edcDskLogProductionService;

    /**
     * 计算产量,写入报表 edc_dsk_log_production -- >   rpt_lot_yield
     * <p>
     * rpt_lot_yield: 批次产量,当前站点的产量
     */
    //@Scheduled(cron = "0 0/10 * * * ?")
    public void updateYield() {
        log.info("定时任务开始执行");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY,-8);
        try {
            List<MesLotTrackLog> trackLogList = mesLotTrackLogService.findLatestLotEqp(cal.getTime());
            for (MesLotTrackLog mesLotTrackLog : trackLogList) {
                String lotNo = mesLotTrackLog.getLotNo();
                String productionNo = mesLotTrackLog.getProductionNo();
                String productionName = mesLotTrackLog.getProductionName();
                String eqpId = mesLotTrackLog.getEqpId();
                Integer yield = edcDskLogProductionService.findNewYieldByLot(eqpId, productionNo, lotNo);
                if (yield == null) {
                    continue;
                } else {
                    boolean updateFlag = rptLotYieldService.updateForSet("lot_yield=" + yield+", lot_yield_eqp=" + yield + ", eqp_id='" + eqpId + "'"+ ", update_date='" + DateUtil.formatDateTime(new Date()) + "'", new EntityWrapper().eq("lot_no", lotNo).eq("production_no", productionNo));
                    if (!updateFlag) {
                        RptLotYield rptLotYield = new RptLotYield();
                        rptLotYield.setLotNo(lotNo);
                        rptLotYield.setProductionNo(productionNo);
                        rptLotYield.setProductionName(productionName);
                        rptLotYield.setEqpId(eqpId);
                        rptLotYield.setLotYieldEqp(yield);
                        rptLotYield.setLotYield(0);
                        rptLotYieldService.insert(rptLotYield);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("updateYield():执行异常",e);
        }
        String eventId = StringUtil.randomTimeUUID("RPT");
        fabLogService.info("",eventId,"updateYield","更新批次产量","","");
        log.info("定时任务开始执行结束");
    }

}
