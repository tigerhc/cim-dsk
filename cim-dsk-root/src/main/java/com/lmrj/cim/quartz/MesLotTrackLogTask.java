package com.lmrj.cim.quartz;

import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class MesLotTrackLogTask {
    @Autowired
    IMesLotTrackLogService iMesLotTrackLogService;
    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    IMesLotTrackService iMesLotTrackService;

    //往mes_lot_wip表中导入数据
    //@Scheduled(cron = "0 10 0 * * ?")
    public void fixLotTrackLogData() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        Date startTime = cal.getTime();
        List<MesLotTrackLog> trackLogList = iMesLotTrackLogService.findTrackLog(startTime);
        for (MesLotTrackLog mesLotTrackLog : trackLogList) {
            MesLotTrack mesLotTrack=iMesLotTrackService.findLotTrack(mesLotTrackLog.getEqpId(),mesLotTrackLog.getLotNo(),mesLotTrackLog.getProductionNo());
            if(mesLotTrack!=null && mesLotTrack.getLotYield()!=null){
                mesLotTrackLog.setLotYield(mesLotTrack.getLotYield());
            }
        }
        iMesLotTrackLogService.updateBatchById(trackLogList);
    }
}
