package com.lmrj.map.tray.scheduler;

import com.lmrj.map.tray.entity.MapTrayChipLog;
import com.lmrj.map.tray.service.IMapTrayChipLogService;
import com.lmrj.map.tray.service.IMapTrayChipMoveProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
@Slf4j
public class TrayChipScheduler {
    @Autowired
    private IMapTrayChipMoveProcessService mapTrayChipMoveProcessService;
    @Autowired
    private IMapTrayChipLogService mpTrayChipLogService;

//    @Scheduled(cron = "0 0/5 * * * ?") //TODO 试做开始后，确认各个设备日志正确后放开
    public void TrayChipData(){
        log.info("---------------------------------------------------执行执行TrayChipData开始");
        MapTrayChipLog traceLog = new MapTrayChipLog();
        try{
            //是否正在追溯中【正在追溯中的特点是，在log表中有一条记录只有开始时间，没有结束时间】
            traceLog.setTrayCode(IMapTrayChipMoveProcessService.processAsynchronous);
            mapTrayChipMoveProcessService.traceData(traceLog, IMapTrayChipMoveProcessService.processAsynchronous);
        } catch (Exception e){
            log.error("正常追溯数据时遇到了一个异常",e);
            traceLog.setEndTime(new Date());
            mpTrayChipLogService.updateById(traceLog);
        }
    }

//    @Scheduled(cron = "0 0/5 * * * ?") //TODO 试做开始后，确认各个设备日志正确后放开
    public void TrayChipErrData(){
        log.info("---------------------------------------------------执行执行TrayChipErrData开始");
        MapTrayChipLog traceLog = new MapTrayChipLog();
        try{
            traceLog.setTrayCode(IMapTrayChipMoveProcessService.processErrDataFlag);
            mapTrayChipMoveProcessService.traceData(traceLog, IMapTrayChipMoveProcessService.processErrDataFlag);
        } catch (Exception e){
            log.error("追溯残缺数据时遇到了一个异常",e);
            traceLog.setEndTime(new Date());
            mpTrayChipLogService.updateById(traceLog);
        }
    }

    @Scheduled(cron = "0 0/2 * * * ?") //TODO 试做开始后，确认各个设备日志正确后放开
    public void TrayChipNG(){
        log.info("---------------------------------------------------执行执行TrayChipNG开始");
        MapTrayChipLog traceLog = new MapTrayChipLog();
        try{
            //是否正在追溯中【正在追溯中的特点是，在log表中有一条记录只有开始时间，没有结束时间】
            traceLog.setTrayCode(IMapTrayChipMoveProcessService.processNgDataFlag);
            mapTrayChipMoveProcessService.traceData(traceLog, IMapTrayChipMoveProcessService.processNgDataFlag);
        } catch (Exception e){
            log.error("追溯NG数据时遇到了一个异常",e);
            traceLog.setEndTime(new Date());
            mpTrayChipLogService.updateById(traceLog);
        }
    }
}
