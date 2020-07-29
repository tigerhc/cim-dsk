package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class ProductionYieldExportTask {
    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;

    public String filePath = "E:\\FTP\\EQUIPMENT\\SIM\\2020\\PRINTER\\SIM-PRINTER1\\07";

    @Scheduled(cron = "0 50 23 * * ?")
    public void doExportProductionCsv() throws Exception {
        log.info("开始导出production csv文件");
        Date endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = calendar.getTime();
        edcDskLogProductionService.exportProductionCsv(startTime,endTime);
        log.info("开始导出production csv文件");
    }

}
