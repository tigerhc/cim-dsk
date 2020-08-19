package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class EdcAmsRecordYieldTask {
    @Autowired
    private IEdcAmsRecordService iEdcAmsRecordService;
    @Autowired
    private IEdcDskLogProductionService edcDskLogProductionService;

    /**
     * 计算产量,写入操操作日志
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void updateAmsRecordYield() {
        log.info("EdcAmsRecordYieldTask定时任务开始执行");
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        endTime=cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, -2);
        Date startTime = cal.getTime();
        //获取两小时以内所有AmsRecord
        List<EdcAmsRecord> edcAmsRecordList=iEdcAmsRecordService.findAmsRecordByTime(startTime,endTime);
        if(edcAmsRecordList.size()>0){
            for (EdcAmsRecord edcAmsRecord : edcAmsRecordList) {
                if(edcAmsRecord.getLotNo()!=null){
                    //遍历查找最近的批次
                    EdcDskLogProduction edcDskLogProduction = edcDskLogProductionService.findLastYield(edcAmsRecord.getEqpId(),edcAmsRecord.getLotNo(), edcAmsRecord.getStartDate());
                    if(edcDskLogProduction!=null){
                        edcAmsRecord.setLotNo(edcDskLogProduction.getLotNo());
                        edcAmsRecord.setLotYield(edcDskLogProduction.getLotYield());
                        iEdcAmsRecordService.updateById(edcAmsRecord);
                    }
                }
            }
        }
        log.info("EdcAmsRecordYieldTask定时任务开始执行结束");
    }
}
