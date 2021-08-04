package com.lmrj.cim.quartz;

import com.lmrj.oven.batchlot.service.IOvnBatchLotDayService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class OvnBatchLotDayTask {
    @Autowired
    private IOvnBatchLotService ovnBatchLotService;
    @Autowired
    private IOvnBatchLotDayService ovnBatchLotDayService;
    private Boolean isRun=Boolean.FALSE;
    //@Scheduled(cron = "0 0 1 * * ?")
    public void run() {
        log.info(" 那日统计设备温度极值开始......................................" + (new Date()));
        if (!isRun) {
            isRun=Boolean.TRUE;
            try {
                //获取当前的时间的前一天并转为YYYY-MM-DD
                Calendar ca = Calendar.getInstance();
                ca.setTime(new Date());
                ca.add(Calendar.DATE, -1);
                Date lastDay = ca.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String periodDate = formatter.format(lastDay);


                Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodDate + " 00:00:00");
                Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodDate + " 23:59:59");
                //获取当前设备列表并逐一写入
                List<String> eqpList = ovnBatchLotService.lastDayEqpList(startTime,endTime);
                for (String eqpId:eqpList) {
                    ovnBatchLotDayService.fParamToDay(eqpId,periodDate);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                isRun=Boolean.FALSE;
            }
        }
    }
}
