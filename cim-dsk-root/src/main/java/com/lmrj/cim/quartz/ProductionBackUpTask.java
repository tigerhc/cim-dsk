package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionHisService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
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

    /**
     * 备份数据 edc_dsk_log_production -- >   edc_dsk_log_production_his
     */
    //@Scheduled(cron = "0 50 0/1 * * ?")
    @Scheduled(cron = "0 50 1 * * ?")
    public void backupPdt() {
        log.error("backupPdt定时任务开始执行");
        //7天前
        Calendar calstart= Calendar.getInstance();
        calstart.add(Calendar.DAY_OF_MONTH, -9);

        Calendar calEnd = Calendar.getInstance();
        calEnd.add(Calendar.DAY_OF_MONTH, -7);
        boolean flag = true;
        while (flag){
            List<EdcDskLogProductionHis> backUpYield = edcDskLogProductionService.findBackUpYield(calstart.getTime(), calEnd.getTime());
            if (backUpYield.size()==0){
                flag = false;
            }
            edcDskLogProductionHisService.insertBatch(backUpYield);
        }
        // TODO: 2020/6/8
        log.error("backupPdt定时任务开始执行结束");
    }
}
