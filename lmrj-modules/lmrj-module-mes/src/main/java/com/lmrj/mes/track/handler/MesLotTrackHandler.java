package com.lmrj.mes.track.handler;

import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Component
public class MesLotTrackHandler {
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    //往mes_lot_wip表中导入数据
    public void buildWipData(Date startTime,Date endTime) {
        List<MesLotTrack> mesList = iMesLotTrackService.findIncompleteLotNo(startTime,endTime);
        for (MesLotTrack mes:
                mesList) {
            if (iMesLotTrackService.finddata(mes.getEqpId(), mes.getProductionNo()) == null) {
                //向表中新建数据
                if (iMesLotTrackService.insterWip(mes.getId(), mes.getEqpId(), mes.getLotNo(), mes.getProductionName(), mes.getProductionNo(), mes.getOrderNo(), mes.getLotYield(), mes.getLotYieldEqp(), mes.getStartTime(), mes.getEndTime(), mes.getRemarks(), mes.getCreateBy(), mes.getCreateDate())) {
                    log.info("mes_lot_wip表数据插入成功 批次：" + mes.getEqpId());
                }
                continue;
            } else {
                //更新数据
                if (iMesLotTrackService.updateWip(mes.getLotYield(), mes.getLotYieldEqp(), mes.getLotNo(), mes.getProductionNo())) {
                    log.info("mes_lot_wip表数据更新成功 批次：" + mes.getEqpId());
                }
            }
        }
    }

    //更新wip表中数据 将已完成数据删除
    public void deleteWip(Date startTime,Date endTime) {
        iMesLotTrackService.deleteWip(startTime, endTime);
    }
}
