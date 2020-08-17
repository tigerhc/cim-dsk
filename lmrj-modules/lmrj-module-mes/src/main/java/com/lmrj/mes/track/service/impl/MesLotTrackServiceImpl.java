package com.lmrj.mes.track.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldService;
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
import com.lmrj.util.file.FileUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
                if (lotNoFile.size() == 7) {
                    break;
                }
            }
        }
        if (lotNoFile.size() == 0) {
            return "ERROR: not found file for " + lotNo;
        }
        String kongdongStr="";
        if ("SIM".equals(line)) {
            if(lotNoFile.size() == 1){
                return "ERROR:  file quantity need > 1,but found " + lotNoFile.size() + " for " + lotNo;
            }
            String[] kongdongVal = new String[7];
            for (File file : lotNoFile) {
                String[] fileNames = file.getName().split("-");
                String value = fileNames[0].replace(lotNo, "").replace("%", "").trim();
                String index = fileNames[fileNames.length - 1].replace(".bmp", "");
                kongdongVal[Integer.parseInt(index) - 1] = value;
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
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        // TODO: 2020/7/17 待删除
        if ("SIM-DM1".equals(eqpId)) {
            eqpId = "SIM-DM";
        }
        if (fabEquipment != null) {
            result = doTrackIn(fabEquipment, productionNo, productionName, orderNo, lotNo, recipeCode, opId, 0);
        } else {
            result = trackinLine(eqpId, productionNo, productionName, orderNo, lotNo, recipeCode, opId);
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

    public MesResult trackout(String eqpId, String productionNo, String productionName, String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        saveTrackLog(eqpId, "TRACKOUT", productionNo, productionName, orderNo, lotNo, recipeCode, opId);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment != null) {
            result = doTrackout(fabEquipment, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
        } else {
            result = trackoutLine(eqpId, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
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

    public MesResult doTrackIn(FabEquipment fabEquipment, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId, int takeTime) {
        String eqpId = fabEquipment.getEqpId();
        MesResult result = MesResult.ok("default");
        List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo).eq("production_no", productionNo));
        MesLotTrack mesLotTrack = new MesLotTrack();
        if (mesLotTrackList.size() > 0) {
            mesLotTrack = mesLotTrackList.get(0);
        } else {
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
    public List<MesLotTrack> findLotByStartTime(String eqpId,Date startTime){
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

    //public static void main(String[] args) {
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
    //}
}
