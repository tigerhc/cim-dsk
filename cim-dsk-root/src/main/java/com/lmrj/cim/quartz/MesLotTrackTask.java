package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
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
public class MesLotTrackTask {
    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;
    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;
    //修复mes_lot_track表中的批量内连番
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void fixLotTrackData() {
        if(!jobenabled){
            return;
        }
        Date startTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        startTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endTime = cal.getTime();
        //查询lotTrack一天内批次信息 修正产量
        List<MesLotTrack> mesLotList= iMesLotTrackService.findCorrectData(startTime,endTime);
        if(mesLotList.size()>0){
            for (int i = 0; i < mesLotList.size(); i++) {
                List<EdcDskLogProduction> proList=new ArrayList<>();
                MesLotTrack mesLotTrack = mesLotList.get(i);
                MesLotTrack nextLotTrack=null;
                if(mesLotTrack.getEndTime()!=null){
                    if(i<mesLotList.size()-1){
                        if(mesLotList.get(i+1).getEqpId().equals(mesLotTrack.getEqpId())){
                            nextLotTrack=mesLotList.get(i+1);
                        }
                    }
                    if(nextLotTrack==null){
                        proList =edcDskLogProductionService.findProByTime(mesLotTrack.getStartTime(),mesLotTrack.getEndTime(),mesLotTrack.getEqpId());
                    }else{
                        proList=edcDskLogProductionService.findProByTime(mesLotTrack.getStartTime(),nextLotTrack.getStartTime(),mesLotTrack.getEqpId());
                    }
                    if(mesLotTrack.getEqpId().contains("SIM-REFLOW") || mesLotTrack.getEqpId().contains("SIM-PRINTER") || mesLotTrack.getEqpId().contains("SIM-TRM")){
                        if(proList.size()>0 && mesLotTrack.getLotYieldEqp()!=(proList.size()*12)){
                            iMesLotTrackService.updateTrackLotYeildEqp(mesLotTrack.getEqpId(),mesLotTrack.getLotNo(),(proList.size()*12));
                        }
                    }else if(proList.size()>0 && mesLotTrack.getLotYieldEqp()!=proList.size()){
                        iMesLotTrackService.updateTrackLotYeildEqp(mesLotTrack.getEqpId(),mesLotTrack.getLotNo(),proList.size());
                    }
                }
            }
        }
    }
}
