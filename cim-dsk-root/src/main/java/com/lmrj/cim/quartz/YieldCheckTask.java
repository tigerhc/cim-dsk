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
public class YieldCheckTask {
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
    //@Scheduled(cron = "0 0 1 * * ?")
    public void yieldCheck() {
        Date endTime=new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY,-1);
        endTime=cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        Date startTime=cal.getTime();
        log.info("各个批次产量异常判定定时任务开始执行");
        List<MesLotTrack> list = mesLotTrackService.findCorrectData(startTime,endTime);
        for (MesLotTrack lotTrack : list) {
            if(lotTrack.getEndTime()!=null){
                if(lotTrack.getUpdateDate()==null){
                    
                }else{

                }
                Date lastUpdateTime = lotTrack.getUpdateDate();
            }
        }
    }
}
