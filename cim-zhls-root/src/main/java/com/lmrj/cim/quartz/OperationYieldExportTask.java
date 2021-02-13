package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.service.impl.EdcDskLogOperationServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.impl.FabEquipmentServiceImpl;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class OperationYieldExportTask {
    @Autowired
    EdcDskLogOperationServiceImpl edcDskLogOperationService;
    @Autowired
    FabEquipmentServiceImpl fabEquipmentService;
    //@Scheduled(cron = "0 30 23 * * ?")
    //跨月怎么办
    public void doExportOperationCsv() throws Exception {
        log.info("修正Operation csv任务执行");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = cal.getTime();
        String filePath="";
        List<String> eqpList = fabEquipmentService.findEqpIdList();
        for (String eqpId : eqpList) {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            filePath = "E:/FTP/EQUIPMENT/SIM/" + DateUtil.getYear() + "/" + fabEquipment.getStepCode() + "/" + eqpId + "/" + DateUtil.getMonth();
            log.info(filePath);
            List<File> fileList= edcDskLogOperationService.getFileList(filePath,startTime);
            for (File file : fileList) {
                edcDskLogOperationService.fixOperationCsvYield(file,filePath);
            }
        }
    }
}
