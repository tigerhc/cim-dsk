package com.lmrj.cim.quartz;

import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    //往mes_lot_wip表中导入数据
    //@Scheduled(cron = "0 10 0 * * ?")
    public void fixLotTrackData() {
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        endTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = cal.getTime();
        List<MesLotTrack> mesLotList= iMesLotTrackService.findCorrectData(startTime,endTime);
        if(mesLotList.size()>0){
            for (MesLotTrack mesLotTrack : mesLotList) {
                List<EdcDskLogProduction> proList =edcDskLogProductionService.findDataBylotNo(mesLotTrack.getLotNo(),mesLotTrack.getEqpId(),mesLotTrack.getProductionNo());
                if(proList.size()>0){
                    mesLotTrack.setLotYieldEqp(proList.get(proList.size()-1).getLotYield());
                }
            }
            iMesLotTrackService.updateBatchById(mesLotList);
        }
    }
}
