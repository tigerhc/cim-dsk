package com.lmrj.cim.quartz;

import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    //给tracklog表添加产量
    //@Scheduled(cron = "0 10 0 * * ?")
    public void fixLotTrackLogData() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startTime = cal.getTime();
        List<MesLotTrackLog> trackLogList = iMesLotTrackLogService.findTrackLog(startTime);
        List<MesLotTrackLog> updateLogList = new ArrayList<>();
        int i = 0;
        for (MesLotTrackLog mesLotTrackLog : trackLogList) {
            MesLotTrack mesLotTrack = new MesLotTrack();
            if (mesLotTrackLog.getEqpId().equals("SIM-DM")) {
                mesLotTrack = iMesLotTrackService.findLotTrack("SIM-REFLOW1", mesLotTrackLog.getLotNo(), mesLotTrackLog.getProductionNo());
            } else {
                mesLotTrack = iMesLotTrackService.findLotTrack(mesLotTrackLog.getEqpId(), mesLotTrackLog.getLotNo(), mesLotTrackLog.getProductionNo());
            }
            if (mesLotTrack != null && mesLotTrack.getLotYield() != null) {
                mesLotTrackLog.setLotYield(mesLotTrack.getLotYieldEqp());
                updateLogList.add(mesLotTrackLog);
            }
        }
        if(updateLogList.size()>0){
            Boolean flag = iMesLotTrackLogService.updateBatchById(updateLogList);
            if (flag) {
                String eventId = StringUtil.randomTimeUUID("RPT");
                fabLogService.info("", "fixLotTrackLogData", "更新表中trackout产量成功，更新条数：" + updateLogList.size(), "", "", "gxj");
            }
        }
    }
}
