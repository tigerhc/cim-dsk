package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class MesLotTrackTask {
    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;
    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    IMesLotTrackService iMesLotTrackService;

    //修复mes_lot_track表中的批量内连番
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void fixLotTrackData() {
        Date startTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        startTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endTime = cal.getTime();
        List<MesLotTrack> mesLotList= iMesLotTrackService.findCorrectData(startTime,endTime);
        if(mesLotList.size()>0){
            for (MesLotTrack mesLotTrack : mesLotList) {
                List<EdcDskLogProduction> proList=new ArrayList<>();
                if(mesLotTrack.getEndTime()!=null){
                    MesLotTrack lastTrack=iMesLotTrackService.findLastTrack(mesLotTrack.getEqpId(),mesLotTrack.getLotNo(),mesLotTrack.getStartTime());
                    if(lastTrack==null && mesLotTrack.getEndTime()!=null){
                        proList =edcDskLogProductionService.findProByTime(mesLotTrack.getStartTime(),mesLotTrack.getEndTime(),mesLotTrack.getEqpId());
                    }else{
                        proList=edcDskLogProductionService.findProByTime(mesLotTrack.getStartTime(),lastTrack.getStartTime(),mesLotTrack.getEqpId());
                    }
                    if(proList.size()>0 && mesLotTrack.getLotYieldEqp()!=proList.size()){
                        if(mesLotTrack.getEqpId().contains("SIM-REFLOW") || mesLotTrack.getEqpId().contains("SIM-PRINTER")){
                            iMesLotTrackService.updateTrackLotYeildEqp(mesLotTrack.getEqpId(),mesLotTrack.getLotNo(),(proList.size()*12));
                        }else{
                            iMesLotTrackService.updateTrackLotYeildEqp(mesLotTrack.getEqpId(),mesLotTrack.getLotNo(),proList.size());
                        }
                    }
                }
            }
        }
    }
}
