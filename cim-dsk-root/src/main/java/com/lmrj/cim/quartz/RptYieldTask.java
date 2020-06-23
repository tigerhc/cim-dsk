package com.lmrj.cim.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.lot.entity.RptLotYield;
import com.lmrj.edc.lot.service.IRptLotYieldService;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Slf4j
@Component
public class RptYieldTask {


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
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateYield() {
        log.info("定时任务开始执行");
        Calendar cal= Calendar.getInstance();
        cal .add(Calendar.DAY_OF_MONTH, -1);
        List<MesLotTrackLog> trackLogList =  mesLotTrackLogService.findLatestLotEqp(cal.getTime());
        for (MesLotTrackLog mesLotTrackLog : trackLogList) {
            String lotNo = mesLotTrackLog.getLotNo();
            String productionNo = mesLotTrackLog.getProductionNo();
            String productionName = mesLotTrackLog.getProductionName();
            String eqpId = mesLotTrackLog.getEqpId();
            if("SIM-DM1".equals(eqpId)){
                eqpId = "SIM-REFLOW1";
            }
            Integer yield = edcDskLogProductionService.findNewYieldByLot(eqpId,productionNo,  lotNo);
            if(yield == null){
                continue;
            }else{
                if("SIM-REFLOW1".equals(eqpId)){
                    yield= yield*12 ;
                }
                boolean updateFlag = rptLotYieldService.updateForSet("lot_yield_eqp="+yield+", eqp_id='"+eqpId+"'", new EntityWrapper().eq("lot_no", lotNo).eq("production_no", productionNo));
                if(!updateFlag){
                    RptLotYield rptLotYield= new RptLotYield();
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
        log.info("定时任务开始执行结束");
    }

}