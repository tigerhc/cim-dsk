package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.lot.service.IRptLotYieldService;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RptYieldDayTask {


    @Autowired
    private IMesLotTrackLogService mesLotTrackLogService;
    @Autowired
    private IRptLotYieldService rptLotYieldService;
    @Autowired
    private IEdcDskLogProductionService edcDskLogProductionService;

    /**
     * 计算产量,写入报表 edc_dsk_log_production -- >   rpt_lot_yield
     *
     * rpt_lot_yield: 批次产量,当前站点的产量
     */
    //@Scheduled(cron = "0 0/20 * * * ?")
    public void updateYield() {
        log.info("定时任务开始执行");

        log.info("定时任务开始执行结束");
    }

}
