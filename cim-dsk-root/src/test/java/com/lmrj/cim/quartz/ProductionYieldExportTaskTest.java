package com.lmrj.cim.quartz;

import com.lmrj.cim.CimBootApplication;
import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ProductionYieldExportTaskTest {

    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;

    public String filePath = "E:\\FTP\\EQUIPMENT\\SIM\\2020\\PRINTER\\SIM-PRINTER1\\07";
    @Test
    //@Scheduled(cron = "0 45 14 * * ?")
    public void doExportProductionCsv() throws Exception {
        log.info("开始导出production csv文件");
        String stime="2019-12-29 11:35:34";
        String etime="2020-04-16 11:58:10";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime =format.parse(stime);
        Date endTime =format.parse(etime);
        edcDskLogProductionService.exportProductionCsv(startTime,endTime);
        log.info("开始导出production csv文件");
    }
}
