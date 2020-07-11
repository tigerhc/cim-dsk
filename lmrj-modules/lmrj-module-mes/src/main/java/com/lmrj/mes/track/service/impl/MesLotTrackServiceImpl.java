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

    /**
     * 按照eqp和line获取recipe
     * 当前line写死
     * @param eqpId
     * @param opId
     * @return
     */
    public MesResult findRecipeName(String eqpId, String opId) {
        String bc = "";
        if("SIM-DM".equals(eqpId)){
            bc = "SIM-BC1";
        }else{
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            if (fabEquipment == null){
                return MesResult.error(eqpId+"设备不存在");
            }
            bc = fabEquipment.getBcCode();
        }

        MesResult result = MesResult.ok("default");
        String recipe ="";
        if("SIM-DM".equals(eqpId)){
            //recipe = "SIM6812M-DI1-1#,SIM6812M-DI2-2#,SIM6812M-MOS2-3#,SIM6812M-MOS2-4#,SIM6812M-MOS2-5#,SIM6812M-MIC1-6#,SIM6812M-MIC2-7#";
            //实时获取值
            Map<String, String> map = Maps.newHashMap();
            map.put("METHOD", "FIND_RECIPE_NAME");
            map.put("EQP_ID", "SIM-DM");
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.flag)) {
                    recipe = (String) result.getContent();
                }
            }else{
                return MesResult.error(eqpId+" not reply");
            }


        }else{
            recipe = "RETEST";
        }
        result.setContent(recipe);
        return result;
    }

    public MesResult findTemp(String eqpId, String opId) {
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment == null){
            return MesResult.error(eqpId+"设备不存在");
        }
        String bc = fabEquipment.getBcCode();
        Map<String, String> map = Maps.newHashMap();
        map.put("EQP_ID", eqpId);
        MesResult result = MesResult.ok("default");
        String temps ="";
        if("SIM-REFLOW1".equals(eqpId)){
            //recipe = "151.1,152.1,153.1,154.1,155.1,156.1,157.1,158.1,159.1,160.1,161.1,162.1,163.1,164.1,165.1,166.1,167.1,168.1,169.1";
            map.put("METHOD", "FIND_TEMP");
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.flag)) {
                    temps = (String) result.getContent();
                }
            }else{
                return MesResult.error(eqpId+" not reply");
            }
        }else{
            temps = "TEMPTEST";
        }
        result.setContent(temps);
        return result;
    }

    public MesResult findParam(String eqpId, String param, String opId) {
        MesResult result = MesResult.ok("default");
        String weight ="";
        Map<String, String> map = Maps.newHashMap();
        map.put("EQP_ID", eqpId);
        map.put("METHOD", "FIND_PARAM");
        if("SIM-WT1".equals(eqpId)){
            map.put("PARAM", param);
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", "SIM-BC1", JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.flag)) {
                    weight = (String) result.getContent();
                }
            }else{
                return MesResult.error(eqpId+" not reply");
            }
            //if("10200,10201".equals(param)){
            //    weight = "111.1,222.1";
            //}
            //else if("10300,10301".equals(param)){
            //    weight = "111.2,222.2";
            //}else if("10400,10401".equals(param)){
            //    weight = "111.3,222.3";
            //}
        }else{
            weight = "TEMPTEST";
        }
        result.setContent(weight);
        return result;
    }

    // TODO: 2020/7/3 锁表超时后,报错,此时锁的动作还在吗?
    public MesResult trackin4DSK(String eqpId, String productionName,String productionNo,String orderNo, String lotNo, String recipeCode, String opId) {
        MesResult result = MesResult.ok("default");
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if(fabEquipment == null){
            return MesResult.error("eqp not found");
        }

        MesLotTrackLog mesLotTrackLog = new MesLotTrackLog();
        mesLotTrackLog.setEqpId(eqpId);
        mesLotTrackLog.setLotNo(lotNo);
        mesLotTrackLog.setProductionName(productionName);
        mesLotTrackLog.setProductionNo(productionNo);
        mesLotTrackLog.setOrderNo(orderNo);
        mesLotTrackLog.setCreateBy(opId);
        mesLotTrackLog.setEventCode("TRACKIN");
        mesLotTrackLogService.insert(mesLotTrackLog);

        if("SIM-DM1".equals(eqpId) || "SIM-FP".equals(eqpId) ){
            return trackin4DSKLine( eqpId,  productionName, productionNo, orderNo,  lotNo,  recipeCode,  opId);
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

        //发送至EAP客户端 Map
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "TRACKIN");
        map.put("LOT_NO", lotNo);
        map.put("EQP_ID", eqpId);
        map.put("ORDER_NO", orderNo);
        map.put("PRODUCTION_NO", productionNo);
        map.put("PRODUCTION_NAME", productionName);
        map.put("OPID",opId);

        String bc = "SIM-BC2";
        if(eqpId.contains("DM") || eqpId.contains("TRM") ){
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.MES.COMMAND", bc, JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.flag)) {
                }
            }
        }else{
            return MesResult.error(eqpId+" not reply");
        }

        //直接更新fab status
        log.info("更新SIM-DM线状态数据, {}, {}", lotNo, recipeCode);
        if(StringUtil.isNotBlank(lotNo) || StringUtil.isNotBlank(recipeCode)) {
            //fabEquipmentStatusService.updateStatus(eqpId, "RUN", lotNo, recipeCode);
            fabEquipmentStatusService.updateYield(eqpId, "RUN", lotNo, recipeCode,0,0);
        }
        return result;
    }

    /**
     * 针对线别track in,直接更新这条线全部设备
     * @param lineId
     * @param productionName
     * @param productionNo
     * @param orderNo
     * @param lotNo
     * @param recipeCode
     * @param opId
     * @return
     */
    public MesResult trackin4DSKLine(String lineId, String productionName,String productionNo,String orderNo, String lotNo, String recipeCode, String opId){

        MesResult result = MesResult.ok("default");
        String[] eqpids = {"SIM-PRINTER1", "SIM-YGAZO1",
                "SIM-DM1",
                "SIM-DM2",
                "SIM-DM3",
                "SIM-DM4",
                "SIM-DM5",
                "SIM-DM6",
                "SIM-DM7",
                "SIM-REFLOW1",
                "SIM-HGAZO1"
        };

        for(String eqpId: eqpids){
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

            //直接更新fab status
            log.info("[{}]更新SIM-DM线状态数据, {}, {}", eqpId, lotNo, recipeCode);
            if(StringUtil.isNotBlank(lotNo) || StringUtil.isNotBlank(recipeCode)){
                fabEquipmentStatusService.updateStatus(eqpId,"RUN", lotNo, recipeCode);
            }

            //发送至EAP客户端 Map
            Map<String, String> map = Maps.newHashMap();
            map.put("METHOD", "TRACKIN");
            map.put("LOT_NO", lotNo);
            map.put("EQP_ID", eqpId);
            map.put("ORDER_NO", orderNo);
            map.put("PRODUCTION_NO", productionNo);
            map.put("PRODUCTION_NAME", productionName);
            map.put("OPID",opId);
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
            }else{
                return MesResult.error(eqpId+" not reply");
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

        if("SIM-DM1".equals(eqpId) || "SIM-FP".equals(eqpId)){
            return trackout4DSKLine(  eqpId,  productionName, productionNo, orderNo,  lotNo,  yield,  recipeCode,  opId);
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
        return result;
    }

    public MesResult trackout4DSKLine(String lineId, String productionName,String productionNo,String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        MesResult result = MesResult.ok("default");
        String[] eqpids = {"SIM-PRINTER1", "SIM-YGAZO1",
                "SIM-DM1",
                "SIM-DM2",
                "SIM-DM3",
                "SIM-DM4",
                "SIM-DM5",
                "SIM-DM6",
                "SIM-DM7",
                "SIM-REFLOW1",
                "SIM-HGAZO1"
        };
        for(String eqpId: eqpids){
            List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo));

            if (mesLotTrackList.size() == 0) {
                return MesResult.error("please track in first");
            }
            MesLotTrack mesLotTrack = mesLotTrackList.get(0);
            mesLotTrack.setEndTime(new Date());
            mesLotTrack.setEqpId(eqpId);
            mesLotTrack.setLotNo(lotNo);
            mesLotTrack.setLotYield(Integer.parseInt(yield));
            mesLotTrack.setUpdateBy(opId);
            mesLotTrack.setProductionName(productionName);
            mesLotTrack.setProductionNo(productionNo);
            mesLotTrack.setOrderNo(orderNo);
            this.insertOrUpdate(mesLotTrack);

        }
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
