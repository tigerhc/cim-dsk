package com.lmrj.cim.quartz;

import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.lot.entity.MesLotWip;
import com.lmrj.mes.lot.service.IMesLotWipService;
import com.lmrj.mes.track.entity.MesLotTrack;
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
    IMesLotWipService iMesLotWipService;

    //往mes_lot_wip表中导入数据
    //@Scheduled(cron = "0 10 0 * * ?")
    public void buildWipData() {
        Date endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = calendar.getTime();
        List<MesLotTrack> mesList = iMesLotWipService.findIncompleteLotNo(startTime, endTime);
        for (MesLotTrack mes :
                mesList) {
            MesLotWip mesLotWip=iMesLotWipService.findStep(mes.getEqpId());
            mesLotWip.setEqpId(mes.getEqpId());
            mesLotWip.setLotNo(mes.getLotNo());
            mesLotWip.setStartTime(mes.getStartTime());
            mesLotWip.setLotYield(mes.getLotYield());
            mesLotWip.setLotYieldEqp(mes.getLotYieldEqp());
            mesLotWip.setOrderNo(mes.getOrderNo());
            mesLotWip.setProductionNo(mes.getProductionNo());
            mesLotWip.setProductionName(mes.getProductionName());
            mesLotWip.setEndTime(mes.getEndTime());
            if (iMesLotWipService.finddata(mesLotWip.getLotNo(), mesLotWip.getProductionNo()) == null) {
                //向表中新建数据
                if (iMesLotWipService.insert(mesLotWip)){
                    String eventId = StringUtil.randomTimeUUID("RPT");
                    fabLogService.info(mesLotWip.getEqpId(),eventId,"insterWip","数据插入成功",mes.getLotNo(),"");
                }
                continue;
            } else {
                //更新数据
                if (iMesLotWipService.updateById(mesLotWip)) {
                    log.info("mes_lot_wip表数据更新成功 批次：" + mesLotWip.getEqpId());
                    String eventId = StringUtil.randomTimeUUID("RPT");
                    fabLogService.info(mesLotWip.getEqpId(),eventId,"updateWip","数据更新成功",mesLotWip.getLotNo(),"");
                }
            }
        }
        //更新wip表中数据 将已完成数据删除
        List<MesLotWip> wipList = iMesLotWipService.selectWip();
        for (MesLotWip mesLotWip : wipList) {
            if (iMesLotWipService.selectEndData(mesLotWip.getLotNo(), mesLotWip.getProductionNo()) != null) {
                iMesLotWipService.deleteEndData(mesLotWip.getLotNo(), mesLotWip.getProductionNo());
                String eventId = StringUtil.randomTimeUUID("RPT");
                fabLogService.info(mesLotWip.getEqpId(),eventId,"deleteEndData","数据删除成功",mesLotWip.getLotNo(),"");
            }
        }
    }
}
