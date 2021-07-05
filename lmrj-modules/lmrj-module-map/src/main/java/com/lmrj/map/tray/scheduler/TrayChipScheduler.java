package com.lmrj.map.tray.scheduler;

import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.service.IMapTrayChipMovePseudoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
public class TrayChipScheduler {
//    @Autowired
//    private IMapTrayChipMoveProcessService mapTrayChipMoveProcessService;
//    @Autowired
//    private IMapTrayChipLogService mpTrayChipLogService;
    @Autowired
    private IMapTrayChipMovePseudoService mapTrayChipMovePseudoService;

    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;

    private boolean pseduoDoingFlag=false;//伪码追溯进行中

//    @Scheduled(cron = "0 0/5 * * * ?") //TODO 试做开始后，确认各个设备日志正确后放开
//    public void TrayChipData(){
//        if(jobenabled){
//            log.info("---------------------------------------------------执行执行TrayChipData开始");
//            MapTrayChipLog traceLog = new MapTrayChipLog();
//            try{
//                //是否正在追溯中【正在追溯中的特点是，在log表中有一条记录只有开始时间，没有结束时间】
//                traceLog.setTrayCode(IMapTrayChipMoveProcessService.processAsynchronous);
//                mapTrayChipMoveProcessService.traceData(traceLog, IMapTrayChipMoveProcessService.processAsynchronous);
//            } catch (Exception e){
//                log.error("正常追溯数据时遇到了一个异常",e);
//                traceLog.setEndTime(new Date());
//                mpTrayChipLogService.updateById(traceLog);
//            }
//        }
//    }

    @Scheduled(cron = "0 0/45 * * * ?") //TODO 试做开始后，确认各个设备日志正确后放开
    public void tracePseudo(){
        if(jobenabled){
            if(!pseduoDoingFlag){
                pseduoDoingFlag = true;
                //追溯伪码(含段尾设备不良品追溯)
                List<MapEquipmentConfig> eqpConfigs = mapTrayChipMovePseudoService.getLineEndEqp();
                for(MapEquipmentConfig item : eqpConfigs){
                    try{
                        log.info("伪码追溯开始:"+item.getEqpId());
                        mapTrayChipMovePseudoService.tracePseudoData(item);
                    } catch (Exception e){
                        log.error("伪码追溯遇到了一个异常"+item.getEqpId(),e);
                    }
                }
                try{//制品码追溯
                    log.info("伪码HB2追溯开始:");
                    mapTrayChipMovePseudoService.traceHB2();
                } catch (Exception e){
                    log.error("伪码HB2追溯遇到了一个异常",e);
                }
                try{//段首设备不良品追溯
                    mapTrayChipMovePseudoService.traceNGData();
                } catch (Exception e){
                    log.error("追溯NG数据时遇到了一个异常",e);
                }
            }
            pseduoDoingFlag = false;
        }
    }
}
