package com.lmrj.cim.quartz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.lot.entity.MesLotWip;
import com.lmrj.mes.lot.service.IMesLotWipService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.lang.StringUtil;
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
public class MesLotWipTask {
    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    IMesLotWipService iMesLotWipService;
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    @Value("${dsk.lineNo}")
    String lineNo;

    //往mes_lot_wip表中导入数据
    //@Scheduled(cron = "0 10 0 * * ?")
    public void buildWipData() {
        Date endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = calendar.getTime();
        //查找未完成批量
        List<MesLotTrack> mesList = new ArrayList<>();
        if ("SIM".equals(lineNo)) {
            mesList = iMesLotWipService.findIncompleteLotNo(startTime, endTime);
            try {
                wipDataBuild(mesList);
            } catch (Exception e) {
                log.error("仕挂数据生成出错！", e);
                e.printStackTrace();
            }
        } else if("DM".equals(lineNo)){
            List<String> wipSubLineNos = new ArrayList<>();
            wipSubLineNos.add("IGBT");
            wipSubLineNos.add("FRD");
            wipSubLineNos.add("DBCT");
            wipSubLineNos.add("DBCB");
            wipSubLineNos.add("HGC");
            String startEqpId = "";
            String endEqpId = "";
            for (String wipSubLineNo : wipSubLineNos) {
                String eqpIDs = getWipEqpId(wipSubLineNo);
                startEqpId = eqpIDs.split("_")[0];
                endEqpId = eqpIDs.split("_")[1];
                mesList = iMesLotWipService.findDMIncompleteLotNo(startTime, endTime, startEqpId, endEqpId);
            }
            try {
                wipDataBuild(mesList);
            } catch (Exception e) {
                log.error("仕挂数据生成出错！", e);
                e.printStackTrace();
            }
        }
        if (mesList.size() > 0) {
            //更新wip表中数据 将已完成数据删除
            List<MesLotWip> wipList = iMesLotWipService.selectWip();
            for (MesLotWip mesLotWip : wipList) {
                if ("SIM".equals(lineNo)) {
                    if (iMesLotWipService.selectEndData(mesLotWip.getLotNo(), mesLotWip.getProductionNo()) != null) {
                        iMesLotWipService.deleteEndData(mesLotWip.getLotNo(), mesLotWip.getProductionNo());
                        String eventId = StringUtil.randomTimeUUID("RPT");
                        fabLogService.info(mesLotWip.getEqpId(), eventId, "Wip表数据更新结束", "删除已结束批次数据", mesLotWip.getLotNo(), "");
                    }
                } else if("DM".equals(lineNo)){
                    String wipSubLineNo = mesLotWip.getWipSubLineNo();
                    String eqpIDs = getWipEqpId(wipSubLineNo);
                    //String startEqpId = eqpIDs.split("_")[0];
                    String endEqpId = eqpIDs.split("_")[1];
                    if(iMesLotWipService.selectDMEndData(mesLotWip.getLotNo(), mesLotWip.getProductionNo(),endEqpId) != null){
                        iMesLotWipService.deleteEndData(mesLotWip.getLotNo(), mesLotWip.getProductionNo());
                    }
                }
            }
            if ("SIM".equals(lineNo)) {
                //判断SIM-OVEN处双批次数据是否已进行至下一设备
                wipList = iMesLotWipService.selectWip();
                for (MesLotWip mesLotWip : wipList) {
                    if (mesLotWip.getLotNo().contains("~") && mesLotWip.getEqpId().contains("OVEN")) {
                        String lotNos[] = mesLotWip.getLotNo().split("~");
                        String lotNo1 = lotNos[0];
                        String lotNo2 = lotNos[1];
                        MesLotTrack lotTrack1 = iMesLotTrackService.selectOne(new EntityWrapper<MesLotTrack>().like("eqp_id", "SIM-HTRT").eq("lot_no", lotNo1));
                        MesLotTrack lotTrack2 = iMesLotTrackService.selectOne(new EntityWrapper<MesLotTrack>().like("eqp_id", "SIM-HTRT").eq("lot_no", lotNo2));
                        if (lotTrack1 != null && lotTrack2 != null) {
                            if (iMesLotWipService.deleteById(mesLotWip)) {
                                log.info("SIM-OVEN已结束批次仕挂数据删除完毕！    mesLotWip:" + mesLotWip.getLotNo());
                            } else {
                                log.error("SIM-OVEN已结束批次仕挂数据删除失败！    mesLotWip:" + mesLotWip.getLotNo());
                            }
                        }
                    }
                }
            }
        }
    }

    public void wipDataBuild(List<MesLotTrack> mesList) {
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
            mesLotWip.setWipSubLineNo(mesLotWip1.getWipSubLineNo());
            mesLotWip.setEqpId(eqpId);
            mesLotWip.setLotNo(mes.getLotNo());
            mesLotWip.setStartTime(mes.getStartTime());
            mesLotWip.setLotYield(mes.getLotYield());
            mesLotWip.setLotYieldEqp(mes.getLotYieldEqp());
            mesLotWip.setOrderNo(mes.getOrderNo());
            mesLotWip.setProductionNo(mes.getProductionNo());
            mesLotWip.setProductionName(mes.getProductionName());
            mesLotWip.setEndTime(mes.getEndTime());
            //判断该批量在仕挂表里是否存在
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
                String eqpid = mes.getEqpId();
                if (eqpid.contains("SIM-WB") && eqpid.contains("A")) {
                    eqpid = eqpid.replace("A", "B");
                }
                int newSortNo = iMesLotWipService.findSortNo(eqpid);
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
                    if (mes.getEndTime() == null) {
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
    }

    public String getWipEqpId(String wipSubLine) {
        String startEqpId = "";
        String endEqpId = "";
        if ("IGBT".equals(wipSubLine)) {
            startEqpId = "DM-IGBT-SORT1";
            endEqpId = "DM-VI1";
        } else if ("FRD".equals(wipSubLine)) {
            startEqpId = "DM-FRD-SORT1";
            endEqpId = "DM-VI1";
        } else if ("DBCT".equals(wipSubLine)) {
            startEqpId = "DM-DBCT-SORT1";
            endEqpId = "DM-DBCT-SORT2";
        } else if ("DBCB".equals(wipSubLine)) {
            startEqpId = "DM-DBCB-SORT1";
            endEqpId = "DM-DBCB-SORT2";
        } else if ("HGC".equals(wipSubLine)) {
            startEqpId = "DM-HB2-SORT1";
            endEqpId = "DM-HTRT1";
        }
        return startEqpId + "_" + endEqpId;
    }
}
