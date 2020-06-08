package com.lmrj.cim.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionHisService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Scheduled(cron = "0 50 0/1 * * ?")
    public void dskaps() {
        log.error("定时任务开始执行");
        //7天前
        Calendar cal= Calendar.getInstance();
        cal .add(Calendar.DAY_OF_MONTH, -7);
        try {
            boolean flag = true;
            while (flag){
                List<EdcDskLogProductionHis> backUpYield = edcDskLogProductionService.findBackUpYield(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-06-08 08:00:00"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-06-09 08:00:00"));
                if (backUpYield.size()==0){
                    flag = false;
                }
                edcDskLogProductionHisService.insert(backUpYield);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // TODO: 2020/6/8
        log.error("定时任务开始执行结束");
    }
}
