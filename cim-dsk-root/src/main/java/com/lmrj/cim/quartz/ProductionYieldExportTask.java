package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class ProductionYieldExportTask {
    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;

    @Autowired
    IMesLotTrackService mesLotTrackService;
    public String filePath = "E:\\FTP\\EQUIPMENT\\SIM\\2020\\PRINTER\\SIM-PRINTER1\\07";

    //@Scheduled(cron = "0 45 14 * * ?")
    public void doExportProductionCsv() throws Exception {
        log.info("开始导出production csv文件");
        Date startTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endTime = calendar.getTime();
        edcDskLogProductionService.exportProductionCsv(startTime, endTime);
        log.info("开始导出production csv文件");
    }

}
