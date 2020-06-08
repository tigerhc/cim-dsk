package com.lmrj.cim.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Slf4j
@Component
public class ProductionBackUpTask {

    @Autowired
    private IEdcDskLogProductionService edcDskLogProductionService;

    /**
     * 备份数据 edc_dsk_log_production -- >   edc_dsk_log_production_his
     */
    @Scheduled(cron = "0 30 0/1 * * ?")
    public void dskaps() {
        log.error("定时任务开始执行");
        //7天前
        Calendar cal= Calendar.getInstance();
        cal .add(Calendar.DAY_OF_MONTH, -7);
        edcDskLogProductionService.selectList(new EntityWrapper<EdcDskLogProduction>().ge("create_date",cal.getTime() ));
        // TODO: 2020/6/8
        log.error("定时任务开始执行结束");
    }
}
