package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class ProductionYieldExportTask {
    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;

    //@Scheduled(cron = "0 30 23 * * ?")
    public void doExportProductionCsv() throws Exception {
        log.info("开始导出production csv文件");
        Date endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = calendar.getTime();
        //导出一天之内的数据
        edcDskLogProductionService.exportProductionCsv(startTime, endTime);
        log.info("开始导出production csv文件");
    }

}
