package com.lmrj.cim.quartz;

import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotDayService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    IFabEquipmentService iFabEquipmentService;
    private Boolean isRun=Boolean.FALSE;
    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;
    //@Scheduled(cron = "0 0 1 * * ?")
    public void run() {
        if(!jobenabled){
            return;
        }
        try {
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
        } catch (Exception e) {
            log.error("K线图数据生成报错");
            e.printStackTrace();
        }
    }

    public void newOvnBatchLotDay(){
        if(!jobenabled){
            return;
        }
        try {
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
                    //获取当前设备列表并逐一写入
                    List<FabEquipment> fabEquipmentList = iFabEquipmentService.getTempEqpList();
                    for (FabEquipment fabEquipment : fabEquipmentList) {
                        String eqpId = fabEquipment.getEqpId();
                        ovnBatchLotDayService.newfParamToDay(eqpId,periodDate);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    isRun=Boolean.FALSE;
                }
            }
        } catch (Exception e) {
            log.error("K线图数据生成报错");
            e.printStackTrace();
        }
    }
}
