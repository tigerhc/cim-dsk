package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionHisService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Slf4j
@Component
public class ProductionBackUpTask {

    @Autowired
    private IEdcDskLogProductionService edcDskLogProductionService;

    @Autowired
    private IEdcDskLogProductionHisService edcDskLogProductionHisService;

    @Autowired
    private IFabEquipmentService fabEquipmentService;

    /**
     * 备份数据 edc_dsk_log_production -- >   edc_dsk_log_production_his
     */
    //@Scheduled(cron = "0 10 * * * ?")
    //@Scheduled(cron = "0 50 9 * * ?")
    public void backupPdt() {
        log.info("ProductionBackUpTask定时任务开始执行");
        //7天前
        Calendar calstart= Calendar.getInstance();
        calstart.add(Calendar.DAY_OF_MONTH, -4);
        Calendar calEnd = Calendar.getInstance();
        calEnd.add(Calendar.DAY_OF_MONTH, -3);
        List<String> eqpIdlist = fabEquipmentService.findEqpIdList();
        for(int i=1;i<30; i++){
            calstart.add(Calendar.DAY_OF_MONTH, -3-i);
            calEnd.add(Calendar.DAY_OF_MONTH, -2-i);
            for (String eqpId : eqpIdlist) {
                int eqpCount = 0;
                log.info("开始备份"+ eqpId + "设备的数据");
                while (true){
                    try{
                        List<EdcDskLogProductionHis> backUpYield = edcDskLogProductionService.findBackUpYield(eqpId,calstart.getTime(), calEnd.getTime());
                        log.info("ProductionBackUpTask 备份行数: {}", backUpYield.size());
                        eqpCount = eqpCount+ backUpYield.size();
                        if (backUpYield.size()==0){
                            break;
                        }
                        edcDskLogProductionHisService.insertBatch(backUpYield,2);
                    }catch (Exception e){
                        log.error("Exception:",e);
                    }
                }
                log.info( "{}产量数据备份完成,{}--{}备份:{}",eqpId, DateUtil.formatTime(calstart.getTime()),DateUtil.formatTime(calEnd.getTime()) ,  eqpCount);
            }
        }

        log.info("ProductionBackUpTask定时任务开始执行结束");
    }

}
