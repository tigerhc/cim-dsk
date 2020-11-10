package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TrmCsvTask {
    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;
    @Autowired
    IMesLotTrackService mesLotTrackService;
    @Autowired
    IEdcDskLogOperationService iEdcDskLogOperationService;
    /**
     * 生成TRM前一天的表格
     * 每隔10分钟一次
     */
    //@Scheduled(cron = "0 0/10 * * * ?")
    public void trmProductionCsv() {
        Date endTime=new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        endTime=cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        Date startTime=cal.getTime();
        log.info("TRM日志生成定时任务开始执行");
        String eqpIds[] = {"SIM-TRM1","SIM-TRM2"};
        for (String eqpId : eqpIds) {
            List<MesLotTrack> lotTrackList = mesLotTrackService.findLotsByTime(eqpId,startTime,endTime);
            Boolean flag = edcDskLogProductionService.exportProductionFile(lotTrackList,"PRODUCTION");
            if(flag){
                log.info("TRM Production日志生成完毕");
            }
            Boolean flag1 = iEdcDskLogOperationService.exportOperationFile(eqpId,startTime,endTime);
            if(flag1){
                log.info("TRM Operation日志生成完毕");
            }
        }
    }
}
