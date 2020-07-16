package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionHisService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Scheduled(cron = "0 0 1 * * ?")
    public void backupPdt() {
        log.error("backupPdt定时任务开始执行");
        //7天前
        Calendar calstart= Calendar.getInstance();
        //calstart.add(Calendar.DAY_OF_MONTH, -9);
        calstart.add(Calendar.DAY_OF_MONTH, -40);
        Calendar calEnd = Calendar.getInstance();
        calEnd.add(Calendar.DAY_OF_MONTH, -3);
        List<String> eqpIdlist = fabEquipmentService.findEqpIdList();
        for (String eqpId : eqpIdlist) {
            log.info("开始备份"+ eqpId + "设备的数据");
            while (true){
                try{
                    List<EdcDskLogProductionHis> backUpYield = edcDskLogProductionService.findBackUpYield(eqpId,calstart.getTime(), calEnd.getTime());
                    if (backUpYield.size()==0){
                        break;
                    }
                    edcDskLogProductionHisService.insertBatch(backUpYield,2);
                }catch (Exception e){
                    log.error("Exception:",e);
                }

            }
            log.info(eqpId + "设备的数据备份完成");
        }
        // TODO: 2020/6/8
        log.error("backupPdt定时任务开始执行结束");
    }
}
