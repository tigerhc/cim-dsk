package com.lmrj.mes.track.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.entity.MesResult;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.mapper.MesLotTrackMapper;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.service.impl
 * @title: mes_lot_track服务实现
 * @description: mes_lot_track服务实现
 * @author: 张伟江
 * @date: 2020-04-28 14:03:17
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Transactional
@Service("mesLotTrackService")
@Slf4j
public class MesLotTrackServiceImpl extends CommonServiceImpl<MesLotTrackMapper, MesLotTrack> implements IMesLotTrackService {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    IMesLotTrackLogService mesLotTrackLogService;
    @Autowired
    IFabEquipmentService fabEquipmentService;
    @Autowired
    IFabEquipmentStatusService fabEquipmentStatusService;

    public MesResult trackin4DSK(String eqpId, String productionName,String productionNo,String orderNo, String lotNo, String recipeCode, String opId) {
        MesResult result = MesResult.ok("default");
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if(fabEquipment == null){
            return MesResult.error("eqp not found");
        }
        List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo));
        MesLotTrack mesLotTrack = new MesLotTrack();
        if (mesLotTrackList.size() > 0) {
            mesLotTrack = mesLotTrackList.get(0);
            //mesLotTrack.setStartTime(new Date());
        } else {
            mesLotTrack.setStartTime(new Date());
        }
        mesLotTrack.setEqpId(eqpId);
        mesLotTrack.setLotNo(lotNo);
        mesLotTrack.setCreateBy(opId);
        mesLotTrack.setProductionName(productionName);
        mesLotTrack.setProductionNo(productionNo);
        mesLotTrack.setOrderNo(orderNo);


        this.insertOrUpdate(mesLotTrack);

        MesLotTrackLog mesLotTrackLog = new MesLotTrackLog();
        mesLotTrackLog.setEqpId(eqpId);
        mesLotTrackLog.setLotNo(lotNo);
        mesLotTrackLog.setProductionName(productionName);
        mesLotTrackLog.setProductionNo(productionNo);
        mesLotTrackLog.setOrderNo(orderNo);
        mesLotTrackLog.setCreateBy(opId);
        mesLotTrackLog.setEventCode("TRACKIN");
        mesLotTrackLogService.insert(mesLotTrackLog);

        //发送至EAP客户端 Map
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "TRACKIN");
        map.put("LOT_NO", lotNo);
        map.put("EQP_ID", eqpId);
        String bc = "SIM-BC1";
        String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.MES.COMMAND", bc, JsonUtil.toJsonString(map));

        if (replyMsg != null) {
            result = JsonUtil.from(replyMsg, MesResult.class);
            if ("Y".equals(result.flag)) {
                //Map<String, String> content = Maps.newHashMap();
                //content.put("RECIPE_NAME", recipeCode);
                //result.setContent(content);
                //简单处理
                //result.setContent(recipeCode);
            }
        }

        //直接更新fab status
        log.info("更新SIM-DM线状态数据, {}, {}", lotNo, recipeCode);
        if(StringUtil.isNotBlank(lotNo) || StringUtil.isNotBlank(recipeCode)){
            if("SIM-DM1".equals(eqpId)){
                fabEquipmentStatusService.updateStatus("SIM-DM1","RUN", lotNo, recipeCode);
                fabEquipmentStatusService.updateStatus("SIM-DM2","RUN", lotNo, recipeCode);
                fabEquipmentStatusService.updateStatus("SIM-DM3","RUN", lotNo, recipeCode);
                fabEquipmentStatusService.updateStatus("SIM-DM4","RUN", lotNo, recipeCode);
                fabEquipmentStatusService.updateStatus("SIM-DM5","RUN", lotNo, recipeCode);
                fabEquipmentStatusService.updateStatus("SIM-DM6","RUN", lotNo, recipeCode);
                fabEquipmentStatusService.updateStatus("SIM-DM7","RUN", lotNo, recipeCode);
            }
        }
        return result;
    }

    public MesResult trackout4DSK(String eqpId, String productionName,String productionNo,String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        MesResult result = MesResult.ok("default");
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if(fabEquipment == null){
            return MesResult.error("eqp not found");
        }
        List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo));
        MesLotTrack mesLotTrack = new MesLotTrack();
        if (mesLotTrackList.size() > 0) {
            mesLotTrack = mesLotTrackList.get(0);
        }else{
            return MesResult.error("please track in first");
        }
        mesLotTrack.setEndTime(new Date());
        mesLotTrack.setEqpId(eqpId);
        mesLotTrack.setLotNo(lotNo);
        mesLotTrack.setLotYield(Integer.parseInt(yield));
        mesLotTrack.setUpdateBy(opId);
        mesLotTrack.setProductionName(productionName);
        mesLotTrack.setProductionNo(productionNo);
        mesLotTrack.setOrderNo(orderNo);
        this.insertOrUpdate(mesLotTrack);

        MesLotTrackLog mesLotTrackLog = new MesLotTrackLog();
        mesLotTrackLog.setEqpId(eqpId);
        mesLotTrackLog.setLotNo(lotNo);
        mesLotTrackLog.setLotYield(Integer.parseInt(yield));
        mesLotTrackLog.setProductionName(productionName);
        mesLotTrackLog.setProductionNo(productionNo);
        mesLotTrackLog.setOrderNo(orderNo);
        mesLotTrackLog.setCreateBy(opId);
        mesLotTrackLog.setEventCode("TRACKOUT");
        mesLotTrackLogService.insert(mesLotTrackLog);

        //发送至EAP客户端 Map
        //Map<String, String> map = Maps.newHashMap();
        //map.put("METHOD", "TRACKOUT");
        //map.put("LOT_NO", lotNo);
        //map.put("EQP_ID", eqpId);
        //String bc = "SIM-BC1";
        //String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.MES.COMMAND", bc, JsonUtil.toJsonString(map));
        //
        //if (replyMsg != null) {
        //    result = JsonUtil.from(replyMsg, MesResult.class);
        //    if ("Y".equals(result.flag)) {
        //        //Map<String, String> content = Maps.newHashMap();
        //        //content.put("RECIPE_NAME", recipeCode);
        //        //result.setContent(content);
        //        //简单处理
        //        //result.setContent(recipeCode);
        //    }
        //}
        return result;
    }

    //public MesResult trackIn(String eqpId, String lotNo, String recipeCode, String opId) {
    //    MesResult result = MesResult.ok("default");
    //    FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
    //    if(fabEquipment == null){
    //        return MesResult.error("eqp not found");
    //    }
    //    List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo));
    //    MesLotTrack mesLotTrack = new MesLotTrack();
    //    if (mesLotTrackList.size() > 0) {
    //        mesLotTrack = mesLotTrackList.get(0);
    //        mesLotTrack.setStartTime(new Date());
    //        mesLotTrack.setCreateBy(opId);
    //    } else {
    //        mesLotTrack.setEqpId(eqpId);
    //        mesLotTrack.setLotNo(lotNo);
    //        mesLotTrack.setCreateBy(opId);
    //        mesLotTrack.setStartTime(new Date());
    //    }
    //    this.insertOrUpdate(mesLotTrack);
    //
    //    MesLotTrackLog mesLotTrackLog = new MesLotTrackLog();
    //    mesLotTrackLog.setEqpId(eqpId);
    //    mesLotTrackLog.setLotNo(lotNo);
    //    mesLotTrackLog.setCreateBy(opId);
    //    mesLotTrackLog.setEventCode("TRACKIN");
    //    mesLotTrackLogService.insert(mesLotTrackLog);
    //
    //    //发送至EAP客户端 Map
    //    Map<String, String> map = Maps.newHashMap();
    //    map.put("METHOD", "TRACKIN");
    //    map.put("LOT_NO", lotNo);
    //    map.put("EQP_ID", eqpId);
    //    String bc = "SIM-BC1";
    //    String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.MES.COMMAND", bc, JsonUtil.toJsonString(map));
    //
    //    if (replyMsg != null) {
    //        result = JsonUtil.from(replyMsg, MesResult.class);
    //        if ("Y".equals(result.flag)) {
    //            //Map<String, String> content = Maps.newHashMap();
    //            //content.put("RECIPE_NAME", recipeCode);
    //            //result.setContent(content);
    //            //简单处理
    //            //result.setContent(recipeCode);
    //        }
    //    }
    //    return result;
    //}

    //public MesResult trackOut(String eqpId, String lotNo, String recipeCode, String opId) {
    //    List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo));
    //    MesLotTrack mesLotTrack = new MesLotTrack();
    //    if (mesLotTrackList.size() > 0) {
    //        mesLotTrack = mesLotTrackList.get(0);
    //        mesLotTrack.setEndTime(new Date());
    //        mesLotTrack.setCreateBy(opId);
    //    } else {
    //        mesLotTrack = new MesLotTrack();
    //        mesLotTrack.setEqpId(eqpId);
    //        mesLotTrack.setLotNo(lotNo);
    //        mesLotTrack.setCreateBy(opId);
    //        mesLotTrack.setEndTime(new Date());
    //    }
    //    this.insertOrUpdate(mesLotTrack);
    //    MesLotTrackLog mesLotTrackLog = new MesLotTrackLog();
    //    mesLotTrackLog.setEqpId(eqpId);
    //    mesLotTrackLog.setLotNo(lotNo);
    //    mesLotTrackLog.setCreateBy(opId);
    //    mesLotTrackLog.setEventCode("TRACKOUT");
    //    mesLotTrackLogService.insert(mesLotTrackLog);
    //    return MesResult.ok("default");
    //}
}
