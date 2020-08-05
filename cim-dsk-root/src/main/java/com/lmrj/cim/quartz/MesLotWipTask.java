package com.lmrj.cim.quartz;

import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class MesLotWipTask {
    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    IMesLotTrackService iMesLotTrackService;

    //往mes_lot_wip表中导入数据
    //@Scheduled(cron = "0 10 0 * * ?")
    public void buildWipData() {
        Date endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = calendar.getTime();
        List<MesLotTrack> mesList = iMesLotTrackService.findIncompleteLotNo(startTime, endTime);
        for (MesLotTrack mes :
                mesList) {
            if (iMesLotTrackService.finddata(mes.getLotNo(), mes.getProductionNo()) == null) {
                //向表中新建数据
                if (iMesLotTrackService.insterWip(mes.getId(), mes.getEqpId(), mes.getLotNo(), mes.getProductionName(), mes.getProductionNo(), mes.getOrderNo(), mes.getLotYield(), mes.getLotYieldEqp(), mes.getStartTime(), mes.getEndTime(), mes.getRemarks(), mes.getCreateBy(), mes.getCreateDate())) {
                    log.info("mes_lot_wip表数据插入成功 批次：" + mes.getEqpId());
                    String eventId = StringUtil.randomTimeUUID("RPT");
                    fabLogService.info(mes.getEqpId(),eventId,"insterWip","数据插入成功",mes.getLotNo(),"");
                }
                continue;
            } else {
                //更新数据
                if (iMesLotTrackService.updateWip(mes.getLotYield(), mes.getLotYieldEqp(), mes.getLotNo(), mes.getProductionNo())) {
                    log.info("mes_lot_wip表数据更新成功 批次：" + mes.getEqpId());
                    String eventId = StringUtil.randomTimeUUID("RPT");
                    fabLogService.info(mes.getEqpId(),eventId,"updateWip","数据更新成功",mes.getLotNo(),"");
                }
            }
        }
        //更新wip表中数据 将已完成数据删除
        List<MesLotTrack> wipList = iMesLotTrackService.selectWip();
        for (MesLotTrack mesLotTrack : wipList) {
            if (iMesLotTrackService.selectEndData(mesLotTrack.getLotNo(), mesLotTrack.getProductionNo()) != null) {
                iMesLotTrackService.deleteEndData(mesLotTrack.getLotNo(), mesLotTrack.getProductionNo());
                String eventId = StringUtil.randomTimeUUID("RPT");
                fabLogService.info(mesLotTrack.getEqpId(),eventId,"deleteEndData","数据删除成功",mesLotTrack.getLotNo(),"");
            }
        }
    }
}
