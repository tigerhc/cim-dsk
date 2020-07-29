package com.lmrj.cim.quartz;

import com.lmrj.mes.track.handler.MesLotTrackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class MesLotWipTask {
    @Autowired
    private MesLotTrackHandler mesLotTrackHandler;
    /*@Scheduled(cron = "0 13 18 * * ?")*/
    public void WipTask(){
        Date startTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        Date endTime = calendar.getTime();
        // 删除创建时间在当前时间到两天前范围内 已完成的数据记录
        mesLotTrackHandler.deleteWip(startTime,endTime);
        // 查询创建时间在当前时间到两天前范围内的数据导入wip表
        mesLotTrackHandler.buildWipData(startTime,endTime);
    }
}
