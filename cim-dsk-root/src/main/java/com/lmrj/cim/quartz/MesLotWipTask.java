package com.lmrj.cim.quartz;

import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.lot.entity.MesLotWip;
import com.lmrj.mes.lot.service.IMesLotWipService;
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
    IMesLotWipService iMesLotWipService;
    @Autowired
    IMesLotTrackService iMesLotTrackService;

    //往mes_lot_wip表中导入数据
    //@Scheduled(cron = "0 10 0 * * ?")
    public void buildWipData() {
        Date endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = calendar.getTime();
        List<MesLotTrack> mesList = iMesLotWipService.findIncompleteLotNo(startTime, endTime);
        if(mesList.size()>0){
            for (MesLotTrack mes :
                    mesList) {
                MesLotWip mesLotWip1 = iMesLotWipService.findStep(mes.getEqpId());
                MesLotWip mesLotWip = new MesLotWip();
                String eqpId = mes.getEqpId();
                if (eqpId.contains("WB")) {
                    eqpId = eqpId.substring(0, 8);
                }
                mesLotWip.setStepId(mesLotWip1.getStepId());
                mesLotWip.setStepCode(mesLotWip1.getStepCode());
                mesLotWip.setStationCode(mesLotWip1.getStationCode());
                mesLotWip.setEqpId(eqpId);
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
                    if (eqpId.contains("WB")) {
                        MesLotTrack wbLotTrack = iMesLotWipService.findWByYield(eqpId, mesLotWip.getLotNo(), mesLotWip.getProductionNo());
                        mesLotWip.setLotYield(wbLotTrack.getLotYield());
                        mesLotWip.setLotYieldEqp(wbLotTrack.getLotYieldEqp());
                    }
                    if (iMesLotWipService.insert(mesLotWip)) {
                        String eventId = StringUtil.randomTimeUUID("RPT");
                        fabLogService.info(mesLotWip.getEqpId(), eventId, "insterWip", "批次实时产量更新成功", mesLotWip.getLotNo(), "");
                    }
                    continue;
                } else {
                    MesLotWip oldmesLotWip = iMesLotWipService.finddata(mesLotWip.getLotNo(), mesLotWip.getProductionNo());
                    //判断设备前后顺序 若新数据站点在已存在数据站点之后 则更新 否则 不更新
                    int oldSortNo = iMesLotWipService.findSortNo(oldmesLotWip.getEqpId());
                    int newSortNo = iMesLotWipService.findSortNo(mes.getEqpId());
                    if (newSortNo >= oldSortNo) {
                        if (eqpId.contains("WB")) {
                            MesLotTrack wbLotTrack = iMesLotWipService.findWByYield(eqpId, mesLotWip.getLotNo(), mesLotWip.getProductionNo());
                            oldmesLotWip.setLotYield(wbLotTrack.getLotYield());
                            oldmesLotWip.setLotYieldEqp(wbLotTrack.getLotYieldEqp());
                        } else {
                            oldmesLotWip.setLotYield(mes.getLotYield());
                            oldmesLotWip.setLotYieldEqp(mes.getLotYieldEqp());
                        }
                        oldmesLotWip.setStartTime(mes.getStartTime());
                        oldmesLotWip.setEndTime(mes.getEndTime());
                        if(mes.getEndTime()==null){
                            iMesLotWipService.updateEndTime(oldmesLotWip.getId());
                        }
                        oldmesLotWip.setEqpId(eqpId);
                        oldmesLotWip.setStepId(mesLotWip1.getStepId());
                        oldmesLotWip.setStepCode(mesLotWip1.getStepCode());
                        oldmesLotWip.setStationCode(mesLotWip1.getStationCode());
                        //更新数据
                        if (iMesLotWipService.updateById(oldmesLotWip)) {
                            log.info("mes_lot_wip表数据更新成功 批次：" + mesLotWip.getEqpId());
                            String eventId = StringUtil.randomTimeUUID("RPT");
                            fabLogService.info(mesLotWip.getEqpId(), eventId, "updateWip", "数据更新成功", mesLotWip.getLotNo(), "");
                        }
                    }
                }
            }
            //更新wip表中数据 将已完成数据删除
            List<MesLotWip> wipList = iMesLotWipService.selectWip();
            for (MesLotWip mesLotWip : wipList) {
                if (iMesLotWipService.selectEndData(mesLotWip.getLotNo(), mesLotWip.getProductionNo()) != null) {
                    iMesLotWipService.deleteEndData(mesLotWip.getLotNo(), mesLotWip.getProductionNo());
                    String eventId = StringUtil.randomTimeUUID("RPT");
                    fabLogService.info(mesLotWip.getEqpId(), eventId, "Wip表数据更新结束", "删除已结束批次数据", mesLotWip.getLotNo(), "");
                }
            }
        }
    }
}
