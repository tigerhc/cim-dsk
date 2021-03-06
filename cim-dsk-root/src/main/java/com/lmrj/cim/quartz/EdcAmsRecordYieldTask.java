package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;
    /**
     * 计算产量,写入操操作日志
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void updateAmsRecordYield() {
        if(!jobenabled){
            return;
        }
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
        List<EdcAmsRecord> edcAmsRecordList1 = new ArrayList<>();
        if(edcAmsRecordList.size()>0){
            for (EdcAmsRecord edcAmsRecord : edcAmsRecordList) {
                if(edcAmsRecord.getLotNo()!=null){
                    //遍历查找时间距离最近的产量日志数据的批次产量
                    EdcDskLogProduction edcDskLogProduction = edcDskLogProductionService.findLastYield(edcAmsRecord.getEqpId(), edcAmsRecord.getStartDate());
                    if(edcDskLogProduction!=null){
                        edcAmsRecord.setLotNo(edcDskLogProduction.getLotNo());
                        edcAmsRecord.setLotYield(edcDskLogProduction.getLotYield());
                        edcAmsRecordList1.add(edcAmsRecord);
                    }
                }
            }
            iEdcAmsRecordService.updateBatchById(edcAmsRecordList1,100);
        }
        log.info("EdcAmsRecordYieldTask定时任务开始执行结束");
    }
}
