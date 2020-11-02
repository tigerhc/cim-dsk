package com.lmrj.mes.track.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.entity.MesResult;
import com.lmrj.edc.quartz.MapUtil;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.mapper.MesLotTrackMapper;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.FileUtils;
import com.lmrj.util.file.FileUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;


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
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    @Autowired
    IApsPlanPdtYieldService apsPlanPdtYieldService;

    /**
     * 按照eqp和line获取recipe
     * 当前line写死
     *
     * @param eqpId
     * @param opId
     * @return
     */
    public MesResult findRecipeName(String eqpId, String opId) {
        String bc = "";
        if ("SIM-DM".equals(eqpId)) {
            bc = "SIM-BC1";
        } else {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            if (fabEquipment == null) {
                return MesResult.error(eqpId + "设备不存在");
            }
            bc = fabEquipment.getBcCode();
        }

        MesResult result = MesResult.ok("default");
        String recipe = "";
        if ("SIM-DM".equals(eqpId)) {
            //recipe = "SIM6812M-DI1-1#,SIM6812M-DI2-2#,SIM6812M-MOS2-3#,SIM6812M-MOS2-4#,SIM6812M-MOS2-5#,SIM6812M-MIC1-6#,SIM6812M-MIC2-7#";
            //实时获取值
            Map<String, String> map = Maps.newHashMap();
            map.put("METHOD", "FIND_RECIPE_NAME");
            map.put("EQP_ID", "SIM-DM");
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.getFlag())) {
                    recipe = (String) result.getContent();
                }
            } else {
                return MesResult.error(eqpId + " not reply");
            }


        } else {
            recipe = "RETEST";
        }
        result.setContent(recipe);
        return result;
    }

    public MesResult findTemp(String eqpId, String opId) {
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment == null) {
            return MesResult.error(eqpId + "设备不存在");
        }
        String bc = fabEquipment.getBcCode();
        Map<String, String> map = Maps.newHashMap();
        map.put("EQP_ID", eqpId);
        MesResult result = MesResult.ok("default");
        String temps = "";
        if ("SIM-REFLOW1".equals(eqpId)) {
            //recipe = "151.1,152.1,153.1,154.1,155.1,156.1,157.1,158.1,159.1,160.1,161.1,162.1,163.1,164.1,165.1,166.1,167.1,168.1,169.1";
            map.put("METHOD", "FIND_TEMP");
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.getFlag())) {
                    temps = (String) result.getContent();
                }
            } else {
                return MesResult.error(eqpId + " not reply");
            }
        } else {
            temps = "TEMPTEST";
        }
        result.setContent(temps);
        return result;
    }

    public MesResult findParam(String eqpId, String param, String opId, String lotNo, String productionNo) {
        MesResult result = MesResult.ok("default");
        String value = "";
        Map<String, String> map = Maps.newHashMap();
        map.put("EQP_ID", eqpId);
        map.put("METHOD", "FIND_PARAM");
        if (eqpId.contains("SIM-WT")) {
            map.put("PARAM", param);
            map.put("LOTNO", lotNo);
            map.put("PRODUCTIONNO", productionNo);
            log.info("findParam 参数"+map);
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", "SIM-BC1", JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.getFlag())) {
                    value = (String) result.getContent();
                }
            } else {
                return MesResult.error(eqpId + " not reply");
            }
            //if("10200,10201".equals(param)){
            //    weight = "111.1,222.1";
            //}
            //else if("10300,10301".equals(param)){
            //    weight = "111.2,222.2";
            //}else if("10400,10401".equals(param)){
            //    weight = "111.3,222.3";
            //}
        } else if ("SIM-DM".equals(eqpId)) {
            if ("10300".equals(param)) {
                value = findDmKongdong(eqpId, param, opId, lotNo, productionNo);
                if (value.startsWith("ERROR: ")) {
                    return MesResult.error(value);
                }
            } else {
                return MesResult.error(eqpId + " not reply");
            }

        } else if (eqpId.contains("SIM-TRM")) {
            map.put("PARAM", param);
            map.put("LOTNO", lotNo);
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", "SIM-BC2", JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.getFlag())) {
                    Map<String, String> paramMap = (Map) result.getContent();
                    List<String> vals = Lists.newArrayList();
                    for (String s : param.split(",")) {
                        vals.add(paramMap.get(s));
                    }
                    value = StringUtil.join(vals.toArray(), ",");
                }
            } else {
                return MesResult.error(eqpId + " not reply");
            }
        } else {
            value = "TEMPTEST";
        }
        result.setContent(value);
        return result;
    }

    public String findDmKongdong(String eqpId, String param, String opId, String lotNo, String productionNo) {
        String productionName = apsPlanPdtYieldService.findProductionName(productionNo);
        String line = productionName.split("-")[0].replace("J.","");
        if(line.length()>3){
            line = line.substring(0,3);
        }
        if(line.startsWith("SX")){
            line = "SX";
        }
        //String kongdongDir = "G:\\DSK_SIMULATOR\\8sim\\"+line+"\\" + productionName.replace("J.",""); //本地测试
        String kongdongDir = "D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\"+line+"\\" + productionName.replace("J.","");
        log.info(kongdongDir);
        List<File> kongdongFiles = (List<File>) FileUtil.listFiles(new File(kongdongDir), new String[]{"bmp"}, false);
        Collections.sort(kongdongFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o2.lastModified() > o1.lastModified()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        List<File> lotNoFile = Lists.newArrayList();
        for (File kongdongFile : kongdongFiles) {
            if (kongdongFile.getName().contains(lotNo)) {
                lotNoFile.add(kongdongFile);
                if("SIM".equals(line) && lotNoFile.size() == 7){
                    break;
                }
                if("SX".equals(line) && lotNoFile.size() == 8){
                    break;
                }
            }
        }
        if (lotNoFile.size() == 0) {
            return "ERROR: not found file for " + lotNo;
        }
        String kongdongStr="";
        if ("SIM".equals(line) || "SX".equals(line)) {
            if(lotNoFile.size() == 1){
                return "ERROR:  file quantity need > 1,but found " + lotNoFile.size() + " for " + lotNo;
            }
            String[] kongdongVal = new String[7];
            if("SX".equals(line)){
                kongdongVal = new String[8];
            }
            for (File file : lotNoFile) {
                String[] fileNames = file.getName().split("-");
                String value = fileNames[0].replace(lotNo, "").replace("%", "").trim();
                String index = fileNames[fileNames.length - 1].replace(".bmp", "");
                if(Integer.parseInt(index)>8){
                    kongdongVal[Integer.parseInt(index) - 9] = value;
                }else{
                    kongdongVal[Integer.parseInt(index) - 1] = value;
                }
            }
            kongdongStr = StringUtil.join(kongdongVal, ",");

        }
        if ("5GI".equals(line) ||"6GI".equals(line)) {
            log.info("file name: {}", lotNoFile.get(0).getName());
            if (lotNoFile.size() != 1) {
                return "ERROR:  file quantity need 1,but found " + lotNoFile.size() + " for " + lotNo;
            }
            String[] fileNames = lotNoFile.get(0).getName().split("%");
            kongdongStr = fileNames[0].split("-")[1].trim();
        }
        return kongdongStr;
    }


    // TODO: 2020/7/3 锁表超时后,报错,此时锁的动作还在吗?
    public MesResult trackin(String eqpId, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        saveTrackLog(eqpId, "TRACKIN", productionNo, productionName, orderNo, lotNo, recipeCode, opId);
        if ("SIM-DM1".equals(eqpId)) {
            eqpId = "SIM-DM";
        }
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        // TODO: 2020/7/17 待删除
        if (fabEquipment != null) {
            result = doTrackIn(fabEquipment, productionNo, productionName, orderNo, lotNo, recipeCode, opId, 0);
        } else {
            if(eqpId.contains("WB")){
                result = trackinWB(eqpId, productionNo, productionName, orderNo, lotNo, recipeCode, opId);
            }else{
                result = trackinLine(eqpId, productionNo, productionName, orderNo, lotNo, recipeCode, opId);
            }
        }
        return result;
    }

    /**
     * 针对线别track in,直接更新这条线全部设备
     *
     * @param lineNo
     * @param productionName
     * @param productionNo
     * @param orderNo
     * @param lotNo
     * @param recipeCode
     * @param opId
     * @return String[] eqpids = {"SIM-PRINTER1", "SIM-YGAZO1",
     * "SIM-DM1",
     * "SIM-DM2",
     * "SIM-DM3",
     * "SIM-DM4",
     * "SIM-DM5",
     * "SIM-DM6",
     * "SIM-DM7",
     * "SIM-REFLOW1",
     * "SIM-HGAZO1"
     * };
     */
    public MesResult trackinLine(String lineNo, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        List<FabEquipment> fabEquipmentList = fabEquipmentService.findEqpBySubLine(lineNo);
        if (fabEquipmentList.size() == 0) {
            return MesResult.error("eqp not found");
        }
        int takeTime = 0;
        for (FabEquipment fabEquipment : fabEquipmentList) {
            result = doTrackIn(fabEquipment, productionNo, productionName, orderNo, lotNo, recipeCode, opId, takeTime);
            if (!"Y".equals(result.getFlag())) {
                return result; //失败提前退出
            }
            takeTime = takeTime + fabEquipment.getTakeTime();
        }
        return result;
    }

    public MesResult trackinWB(String eqpId, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        List<FabEquipment> fabEquipmentList = fabEquipmentService.findWbEqp(eqpId);
        if (fabEquipmentList.size() == 0) {
            return MesResult.error("eqp not found");
        }
        int takeTime = 0;
        for (FabEquipment fabEquipment : fabEquipmentList) {
            result = doTrackIn(fabEquipment, productionNo, productionName, orderNo, lotNo, recipeCode, opId, takeTime);
            if (!"Y".equals(result.getFlag())) {
                return result; //失败提前退出
            }
            takeTime = takeTime + fabEquipment.getTakeTime();
        }
        return result;
    }

    public MesResult trackout(String eqpId, String productionNo, String productionName, String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        saveTrackLog(eqpId, "TRACKOUT", productionNo, productionName, orderNo, lotNo, recipeCode, opId);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment != null) {
            result = doTrackout(fabEquipment, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
        } else {
            if(eqpId.contains("WB")){
                result = trackoutWB(eqpId, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
            }else{
                result = trackoutLine(eqpId, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
            }
        }
        return result;
    }

    public MesResult trackoutLine(String lineNo, String productionNo, String productionName, String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        List<FabEquipment> fabEquipmentList = fabEquipmentService.findEqpBySubLine(lineNo);
        if (fabEquipmentList.size() == 0) {
            return MesResult.error("eqp not found");
        }

        for (FabEquipment fabEquipment : fabEquipmentList) {
            result = doTrackout(fabEquipment, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
            if (!"Y".equals(result.getFlag())) {
                return result; //失败提前退出
            }
        }
        return result;
    }

    public MesResult trackoutWB(String eqpId, String productionNo, String productionName, String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        List<FabEquipment> fabEquipmentList = fabEquipmentService.findWbEqp(eqpId);
        if (fabEquipmentList.size() == 0) {
            return MesResult.error("eqp not found");
        }
        for (FabEquipment fabEquipment : fabEquipmentList) {
            result = doTrackout(fabEquipment, productionNo, productionName, orderNo, lotNo, "2448", recipeCode, opId);
            if (!"Y".equals(result.getFlag())) {
                return result; //失败提前退出
            }
        }
        return result;
    }

    public MesResult doTrackIn(FabEquipment fabEquipment, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId, int takeTime) {
        String eqpId = fabEquipment.getEqpId();
        MesResult result = MesResult.ok("default");
        List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo).eq("production_no", productionNo));
        MesLotTrack mesLotTrack = new MesLotTrack();
        if (mesLotTrackList.size() > 0) {
            return MesResult.error(eqpId +" : "+lotNo+"trackin has been completed");
            //mesLotTrack = mesLotTrackList.get(0);
        }else {
            Calendar cal = Calendar.getInstance();
            Calendar.getInstance();
            cal.add(Calendar.MILLISECOND, takeTime);
            mesLotTrack.setStartTime(cal.getTime());
        }
        mesLotTrack.setEqpId(eqpId);
        mesLotTrack.setLotNo(lotNo);
        mesLotTrack.setCreateBy(opId);
        mesLotTrack.setProductionName(productionName);
        mesLotTrack.setProductionNo(productionNo);
        mesLotTrack.setOrderNo(orderNo);
        this.insertOrUpdate(mesLotTrack);

        //发送至EAP客户端 Map
        if ("1".equals(fabEquipment.getClientFlag())) {
            Map<String, String> map = Maps.newHashMap();
            map.put("METHOD", "TRACKIN");
            map.put("LOT_NO", lotNo);
            map.put("EQP_ID", eqpId);
            map.put("ORDER_NO", orderNo);
            map.put("PRODUCTION_NO", productionNo);
            map.put("PRODUCTION_NAME", productionName);
            map.put("OPID", opId);
            map.put("TAKE_TIME", takeTime + "");

            String bc = fabEquipment.getBcCode();
            if (true) {
                String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.MES.COMMAND", bc, JsonUtil.toJsonString(map));
                if (replyMsg != null) {
                    result = JsonUtil.from(replyMsg, MesResult.class);
                    if ("Y".equals(result.getFlag())) {
                    }
                } else {
                    return MesResult.error(eqpId + " not reply");
                }
            }
        }

        log.info("[{}]更新设备状态数据, {}, {}", eqpId, lotNo, recipeCode);
        if (StringUtil.isNotBlank(lotNo) || StringUtil.isNotBlank(recipeCode)) {
            fabEquipmentStatusService.updateYield(eqpId, "RUN", lotNo, recipeCode, 0, 0);
        }
        return result;
    }

    public MesResult doTrackout(FabEquipment fabEquipment, String productionNo, String productionName, String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        String eqpId = fabEquipment.getEqpId();
        List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo).eq("production_no", productionNo));
        if (mesLotTrackList.size() == 0) {
            return MesResult.error("please track in first");
        }
        MesLotTrack mesLotTrack = mesLotTrackList.get(0);
        if(mesLotTrack.getEndTime()!=null){
            return MesResult.error(eqpId+" : "+ lotNo +"trackout has been completed");
        }
        MesLotTrack postTrack = baseMapper.findLastTrack(eqpId, lotNo, mesLotTrack.getStartTime());
        if (postTrack == null) {
            mesLotTrack.setEndTime(new Date());
        } else {
            mesLotTrack.setEndTime(postTrack.getStartTime());
        }
        mesLotTrack.setLotYield(Integer.parseInt(yield));
        mesLotTrack.setUpdateBy(opId);
        this.insertOrUpdate(mesLotTrack);
        return MesResult.ok("default");
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void saveTrackLog(String eqpId, String eventCode, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId) {
        MesLotTrackLog mesLotTrackLog = new MesLotTrackLog();
        mesLotTrackLog.setEqpId(eqpId);
        mesLotTrackLog.setLotNo(lotNo);
        mesLotTrackLog.setProductionName(productionName);
        mesLotTrackLog.setProductionNo(productionNo);
        mesLotTrackLog.setOrderNo(orderNo);
        mesLotTrackLog.setCreateBy(opId);
        mesLotTrackLog.setEventCode(eventCode);
        mesLotTrackLogService.insert(mesLotTrackLog);
    }
    @Override
    public MesLotTrack findNoEndLotNo(String eqpId,Date startTime){
        return baseMapper.findNoEndLotNo(eqpId,startTime);
    }
    @Override
    public List<MesLotTrack> findDataLotNo(String eqpId,Date startTime,Date endTime){
        return baseMapper.findDataLotNo(eqpId,startTime,endTime);
    }

    @Override
    public MesLotTrack findLotNo1(String eqpId,Date startTime){
        return baseMapper.findLotNo1(eqpId,startTime);
    }
    @Override
    public MesLotTrack findLotNo(String startTime, String eqpId) {
        return baseMapper.findLotNo(startTime, eqpId);
    }
    @Override
    public MesLotTrack findLotTrack(String eqpId,String lotNo,String productionNo){
        return baseMapper.findLotTrack(eqpId,lotNo,productionNo);
    }
    @Override
    public MesLotTrack findNextStartTime(String endTime, String eqpId) {
        return baseMapper.findNextStartTime(endTime, eqpId);
    }
    @Override
    public MesLotTrack findLotByStartTime(String eqpId,Date startTime){
        return baseMapper.findLotByStartTime(eqpId,startTime);
    }
    @Override
    public Boolean updateTrackLotYeildEqp(String eqpId,String lotNo,Integer lotYieldEqp){
        return baseMapper.updateTrackLotYeildEqp(eqpId,lotNo,lotYieldEqp);
    }
    @Override
    public MesLotTrack selectEndTime(String eqpId,String lotNo){
        return baseMapper.selectEndTime(eqpId,lotNo);
    }
    @Override
    public List<MesLotTrack> findCorrectData(Date startTime, Date endTime){
        return baseMapper.findCorrectData(startTime,endTime);
    }
    @Override
    public MesLotTrack findLastTrack(String eqpId,String lotNo,Date startTime){
        return baseMapper.findLastTrack(eqpId,lotNo,startTime);
    }
    @Override
    public List<Map> lotTrackQuery(String lineNo,String startTime,String endTime){
        return baseMapper.lotTrackQuery(lineNo,startTime,endTime);
    }

    @Override
    public Map<String, Object> chartKongDong(String proName, String startDate, String endDate) {
        try {
//            String productionName = apsPlanPdtYieldService.findProductionName(productionNo);
            String productionName = baseMapper.findProName(proName);
            String line = productionName.split("-")[0].replace("J.","");
            if(line.length()>3){
                line = line.substring(0,3);
            }
            if(line.startsWith("SX")){
                line = "SX";
            }
//            String kongdongDir = "d:\\backup\\"+line+"\\" + productionName.replace("J.",""); //本地测试
            String kongdongDir = "D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\"+line+"\\" + productionName.replace("J.","");
            log.info(kongdongDir);
            List<Map<String, Object>> files = FileUtils.getFileInfos(kongdongDir, startDate, endDate);
            return _handleFileNameG(files, productionName.replace("J.",""));
        }catch (Exception e){
            log.error("MesLotTrackServiceImpl_chartKongDong:error proName:"+proName);
        }
        return null;
    }

    @Override
    public List<String> findAllProName(String proName) {
        return baseMapper.findAllProName(proName);
    }

    @Override
    public List<Map<String, Object>> chartKongDong(String lotNo,String proName, String startDate, String endDate) {
        try {
//            String productionName = apsPlanPdtYieldService.findProductionName(productionNo);
            String productionName = baseMapper.findProName(proName);
            String line = productionName.split("-")[0].replace("J.","");
            if(line.length()>3){
                line = line.substring(0,3);
            }
            if(line.startsWith("SX")){
                line = "SX";
            }
//            String kongdongDir = "d:\\backup\\"+line+"\\" + productionName.replace("J.",""); //本地测试
            String kongdongDir = "D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\"+line+"\\" + productionName.replace("J.","");
            log.info(kongdongDir);
            List<Map<String, Object>> files = FileUtils.getFileInfos(kongdongDir, startDate, endDate);
            return _handleFileName(files, lotNo);
        }catch (Exception e){
            log.error("MesLotTrackServiceImpl_chartKongDong:error proName:"+proName+",lotNo:"+lotNo);
        }
        return null;
    }

    private Map<String, Object> _handleFileNameG(List<Map<String, Object>> files, String line){
        Map<String, Object> chartOption = new HashMap<>();
        Map<String, Object> allLine = new HashMap<>();
        if(files!=null && files.size()>0){
            List<String> xaxis = new ArrayList<>();
            for(Map<String, Object> item : files){
                String fileName = MapUtils.getString(item, "fileName");
                if(_chkFileName(fileName)){
                    String kongdongVal = fileName.substring(fileName.indexOf(" ")+1,fileName.indexOf("%"));
                    String fileLotNo = fileName.substring(0,fileName.indexOf(" "));
                    String fileNo = fileName.substring(fileName.indexOf("-")+1,fileName.lastIndexOf("."));
                    String fileData = MapUtils.getString(allLine, fileNo);
                    fileData = fileData + "," +kongdongVal;
                    allLine.put(fileNo,fileData);

                    boolean chkFlag = true;
                    if(xaxis.size()>0){
                        for(String chkLotNo : xaxis){
                            if(chkLotNo.equals(fileLotNo)){
                                chkFlag = false;
                                break;
                            }
                        }
                    }
                    if(chkFlag){
                        xaxis.add(fileLotNo);
                    }
                }else{
                    log.warn("MesLotTrackServiceImpl__handleFileName:fileName is error:"+fileName);
                }
            }
            List<Map<String, Object>> allLmt = baseMapper.findkongdongConfig(line);
            List<Map<String, Object>> res = new ArrayList<>();
            List<String> legend = new ArrayList<>();
            int lineLength = 0;
            for(String key : allLine.keySet()){
                String datas = MapUtils.getString(allLine, key);
                datas = datas.substring(5);
                Map<String, Object> lineItem = new HashMap<>();
                lineItem.put("type", "line");
                lineItem.put("name", key);
                List<Double> lineDate = str2Double(datas.split(","));
                if(lineLength<lineDate.size()){
                    lineLength = lineDate.size();
                }
                lineItem.put("data",lineDate);
                res.add(lineItem);
                legend.add(key);
            }
            if(allLmt!=null && allLmt.size()>0){
                for(Map<String, Object> config: allLmt){
                    Map<String, Object> lmtItem = new HashMap<>();
                    lmtItem.put("type", "line");
                    lmtItem.put("name", MapUtils.getString(config, "lineType")+"_lmt");
                    List<Double> list = new ArrayList<>();
                    for(int i=0; i<lineLength; i++){
                        list.add(MapUtils.getDoubleValue(config, "heightLmt"));
                    }
                    lmtItem.put("data", list);
                    res.add(lmtItem);
                    legend.add(MapUtils.getString(config, "lineType")+"_lmt");
                }
            }

            chartOption.put("series",res);
            chartOption.put("xAxis", xaxis);
            chartOption.put("legend",legend);
            return chartOption;
        }
        return null;
    }

    private List<Map<String, Object>> _handleFileName(List<Map<String, Object>> files, String lotNo){
        if(files!=null && files.size()>0){
            List<Map<String, Object>> res = new ArrayList<>();
            for(Map<String, Object> item : files){
                String fileName = MapUtils.getString(item, "fileName");
                if(_chkFileName(fileName)){
                    String kongdongVal = fileName.substring(fileName.indexOf(" ")+1,fileName.indexOf("%"));
                    String fileLotNo = fileName.substring(0,fileName.indexOf(" "));
                    String xAxis = fileName.substring(fileName.indexOf("-")+1,fileName.lastIndexOf("."));
                    item.put("lotNo", fileLotNo);
                    item.put("kongdongVal", kongdongVal);
                    item.put("xAxis", xAxis);
                    if(StringUtil.isEmpty(lotNo)||fileLotNo.equals(lotNo)){
                        res.add(item);
                    }
                }else{
                    log.warn("MesLotTrackServiceImpl__handleFileName:fileName is error:"+fileName);
                }
            }
            return res;
        }
        return files;
    }

    private boolean _chkFileName(String fileName){
        if(fileName.indexOf(" ")<=0){
            return false;
        }
        if(fileName.indexOf("%")<=0){
            return false;
        }
        if(fileName.indexOf("-")<=0){
            return false;
        }
        return true;
    }

    private List<Double> str2Double(String[] strArr){
        List<Double> rs = new ArrayList<>();
        if(strArr.length>0){
            for(String item : strArr){
                rs.add(Double.parseDouble(item));
            }
        }
        return rs;
    }

    @Override
    public List<Map> getKeyence(String mode,String lotno) {
        log.info("getKeyence");
        File pathfile = new File("D:\\DSK1\\IT化データ（二課）\\キエンスー測定機\\SIM\\SIM(IT).csv");
        List<String> lines = null;
        try {
            lines = FileUtil.readLines(pathfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map> maps = new ArrayList<>();
        String[] in = lotno.split("-");
        String lotNoIn  =in[1];
        for (int i = 0; i <lines.size() ; i++) {
            String[] ele = lines.get(i).split(",");
            String[] ele2 = ele[2].split("-");
            if(ele2.length==3){
                if(ele2[1].equals(lotNoIn)){
                    if (mode.equals("0")){
                        if (ele[3].equals("0001-1")){
                            Map<String,Object> map = new HashMap<>();
                            map.put("类型","1-1");
                            map.put("A",ele[7]);
                            map.put("B",ele[8]);
                            map.put("C1",ele[9]);
                            map.put("C21",ele[29]);
                            maps.add(map);
                            System.out.println("1"+map);
                        }else if(ele[3].equals("0001-2")){
                            Map<String,Object> map = new HashMap<>();
                            map.put("类型","1-2");
                            map.put("A",ele[7]);
                            map.put("B",ele[8]);
                            map.put("C1",ele[9]);
                            map.put("C21",ele[29]);
                            maps.add(map);
                        }
                    }
                    if (mode.equals("1")){
                        if (ele[3].equals("0002-1")){
                            Map<String,Object> map = new LinkedHashMap<>();
                            map.put("类型","2-1");
                            map.put("A",ele[7]);
                            map.put("B",ele[8]);
                            map.put("C1",ele[9]);
                            map.put("C21",ele[28]);
                            maps.add(map);
                        }else if(ele[3].equals("0002-2")){
                            Map<String,Object> map = new LinkedHashMap<>();
                            map.put("类型","2-2");
                            map.put("A",ele[7]);
                            map.put("B",ele[8]);
                            map.put("C1",ele[9]);
                            map.put("C21",ele[28]);
                            maps.add(map);
                        }
                    }
                }
            }}
        return maps;

    }



    public static void main(String[] args) {
    //    Map<String , Object> map = Maps.newHashMap();
    //    map.put("1", "2");
    //    map.put("2", "3");
    //    List list = Lists.newArrayList();
    //    list.add("222");
    //    list.add("333");
    //    map.put("3", list);
    //    String value = JsonUtil.toJsonString(map);
    //    System.out.println(JsonUtil.toJsonString(map));
    //    Map map2 = JsonUtil.from(value, new TypeReference<Map<String , Object>>(){});
    //    Map map3 =  JsonUtil.from(value, Map.class);
    //    System.out.println(map2);
//        MesLotTrackServiceImpl cimCommandHandler = new MesLotTrackServiceImpl();
//        Map<String,Object> map =cimCommandHandler.getKeyence("1","6812M-0O21A-#2");
//        System.out.println(map);
    }
    //}
}
