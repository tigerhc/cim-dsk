package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import com.lmrj.mes.track.entity.MesLotTrack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ProductionYieldExportTask {
    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;

    //@Scheduled(cron = "0 30 23 * * ?")
    public void doExportProductionCsv() throws Exception {
        log.info("导出production csv任务执行");
        try{
            Date endTime = new Date();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            endTime = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date startTime = cal.getTime();
            //更正表中批次品番
            List<MesLotTrack> wrongList = edcDskLogProductionService.updateProductionData(startTime, endTime);
            //导出数据生成文件
            if(wrongList.size()>0){
                edcDskLogProductionService.printProductionCsv(wrongList);
                log.info("开始导出production csv文件");
            }else{
                log.info("production csv文件正确 无需导出");
            }
        }catch (Exception e){
            log.error("ProductionYieldExportTask; ", e);
        }
    }

}
