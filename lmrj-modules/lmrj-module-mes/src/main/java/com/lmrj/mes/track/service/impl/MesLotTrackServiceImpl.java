package com.lmrj.mes.track.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.entity.MesResult;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import com.lmrj.mes.kongdong.service.IMsMeasureKongdongService;
import com.lmrj.mes.kongdong.service.impl.MsMeasureKongdongServiceImpl;
import com.lmrj.mes.measure.entity.MeasureGi;
import com.lmrj.mes.measure.entity.MeasureSim;
import com.lmrj.mes.measure.entity.MeasureSx;
import com.lmrj.mes.measure.mapper.MeasureGiMapper;
import com.lmrj.mes.measure.mapper.MeasureSimMapper;
import com.lmrj.mes.measure.mapper.MeasureSxMapper;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    @Autowired
    IMsMeasureKongdongService iMsMeasureKongdongService;
    @Autowired
    MeasureSxMapper measureSxMapper;
    @Autowired
    MeasureSimMapper measureSimMapper;
    @Autowired
    MeasureGiMapper measureGiMapper;

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

    public MesResult findPrinterParam(String eqpId, String opId) {
        MesResult result = MesResult.ok("default");
        String value = "";
        Map<String, String> map = Maps.newHashMap();
        if (eqpId.equals("TOP")) {
            eqpId = "APJ-DBCT-PRINTER1";
        } else if (eqpId.equals("BOTTOM")) {
            eqpId = "APJ-DBCB-PRINTER1";
        } else if (eqpId.equals("FRD")) {
            eqpId = "APJ-FRD-PRINTER1";
        } else if (eqpId.equals("IGBT")) {
            eqpId = "APJ-IGBT-PRINTER1";
        } else {
            log.error("设备名称错误！   " + eqpId);
        }
        map.put("EQP_ID", eqpId);
        map.put("METHOD", "FIND_PRINTER_PARAM");
        if (eqpId.contains("PRINTER") && eqpId.contains("APJ")) {
            /*FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
            if (fabEquipment == null) {
                return MesResult.error(eqpId + "设备不存在");
            }*/
            String bc = "";
            if (eqpId.contains("DBC")) {
                bc = "APJ-BC2";
            } else {
                bc = "APJ-BC1";
            }
            /*bc = fabEquipment.getBcCode();
            if(!"APJ-BC1".equals(bc) && !"APJ-BC2".equals(bc)){
                if(eqpId.contains("DBC")){
                    bc = "APJ-BC2";
                }else {
                    bc = "APJ-BC1";
                }
            }*/
            log.info("FIND_PRINTER_PARAM 参数" + map);
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.getFlag())) {
                    value = (String) result.getContent();
                }
                if ("ERROR: NOT FOUND".equals(value)) {
                    log.error("EQP_ID:" + eqpId + "印刷机数据获取失败");
                }
            } else {
                return MesResult.error(eqpId + " not reply");
            }
        } else {
            return MesResult.error(eqpId + "设备名称不正确");
        }
        result.setContent(value);
        return result;
    }

    public MesResult findApjRecipeCode(String eqpId, String opId) {
        MesResult result = MesResult.ok("default");
        String value = "";
        Map<String, String> map = Maps.newHashMap();
        map.put("EQP_ID", eqpId);
        map.put("METHOD", "FIND_APJ_RECIPECODE");
        String bc = "";
        //bcCode暂时写死
        if (eqpId.equals("TOP") || eqpId.equals("BOTTOM")) {
            bc = "APJ-BC2";
        } else if (eqpId.contains("YJH")) {
            bc = "APJ-BC3";
        } else if (eqpId.equals("IGBT") || eqpId.equals("FRD")) {
            bc = "APJ-BC1";
        } else if (eqpId.equals("RY1")) {
            bc = "APJ-BC3";
        } else if (eqpId.equals("RY2")) {
            bc = "APJ-BC4";
        } else if (eqpId.equals("XRAY") || eqpId.equals("US") || eqpId.equals("JET")) {
            bc = "APJ-BC6";
        } else {
            bc = "APJ-BC7";
        }
        log.info("findApjRecipeCode 参数" + map);
        String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
        if (replyMsg != null) {
            result = JsonUtil.from(replyMsg, MesResult.class);
            if ("Y".equals(result.getFlag())) {
                value = (String) result.getContent();
            }
            if ("ERROR: NOT FOUND".equals(value)) {
                log.error("EQP_ID:" + eqpId + "配方名称数据获取失败");
            }
        } else {
            return MesResult.error(eqpId + " connection timed out");
        }
        String smtReplyMsg = null;
        if ("APJ-BC1".equals(bc)) {
            Map<String, String> map1 = Maps.newHashMap();
            String smtValue = "";
            if (eqpId.equals("IGBT")) {
                eqpId = "IGBTSMT";
            } else if (eqpId.equals("FRD")) {
                eqpId = "FRDSMT";
            }
            map1.put("EQP_ID", eqpId);
            map1.put("METHOD", "FIND_APJ_RECIPECODE");
            smtReplyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", "APJ-BC4", JsonUtil.toJsonString(map1));
            if (smtReplyMsg != null) {
                result = JsonUtil.from(smtReplyMsg, MesResult.class);
                if ("Y".equals(result.getFlag())) {
                    smtValue = (String) result.getContent();
                }
                if ("ERROR: NOT FOUND".equals(smtValue)) {
                    log.error("EQP_ID:" + eqpId + "配方名称数据获取失败");
                }
            } else {
                return MesResult.error(eqpId + " connection timed out");
            }
            value = smtValue + "," + value;
        }
        result.setContent(value);
        return result;
    }

    public MesResult findReflowParam(String eqpId, String opId) {
        MesResult result = MesResult.ok("default");
        String value = "";
        Map<String, String> map = Maps.newHashMap();
        if (eqpId.equals("TOP")) {
            eqpId = "APJ-DBCT-REFLOW1";
        } else if (eqpId.equals("BOTTOM")) {
            eqpId = "APJ-DBCB-REFLOW1";
        } else if (eqpId.equals("FRD")) {
            eqpId = "APJ-FRD-REFLOW1";
        } else if (eqpId.equals("IGBT")) {
            eqpId = "APJ-IGBT-REFLOW1";
        } else {
            log.error("设备名称错误！   " + eqpId);
        }
        map.put("EQP_ID", eqpId);
        map.put("METHOD", "FIND_REFLOW_PARAM");
        if (eqpId.contains("REFLOW") && eqpId.contains("APJ")) {
            String bc = "";
            if (eqpId.contains("DBC")) {
                bc = "APJ-BC2";
            } else {
                bc = "APJ-BC1";
            }
            log.info("FIND_REFLOW_PARAM 参数" + map);
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.getFlag())) {
                    value = (String) result.getContent();
                }
                if ("ERROR: NOT FOUND".equals(value)) {
                    log.error("EQP_ID:" + eqpId + "回流焊数据获取失败");
                }
            } else {
                return MesResult.error(eqpId + " not reply");
            }
        } else {
            return MesResult.error(eqpId + "设备名称不正确");
        }
        result.setContent(value);
        return result;
    }

    public MesResult findSinterParam(String eqpId, String opId) {
        MesResult result = MesResult.ok("default");
        String value = "";
        Map<String, String> map = Maps.newHashMap();
        if (eqpId.equals("RY1")) {
            eqpId = "APJ-HB1-SINTERING1";
        } else if (eqpId.contains("RY2S1")) {//金型温度设定值
            eqpId = "APJ-HB2-SINTERING1-1";
        } else if (eqpId.contains("RY2S2")) {//压力、温度设定值
            eqpId = "APJ-HB2-SINTERING1-2";
        }/* else if(eqpId.equals("RY2C")){//温度测定值
            eqpId = "APJ-HB2-SINTERING1-3";
        } */ else if (eqpId.contains("RY2DJ")) {//点胶机数据
            eqpId = "APJ-HB2-DISPENSING1";
        } else {
            log.error("设备名称错误！   " + eqpId);
        }
        map.put("EQP_ID", eqpId);
        map.put("METHOD", "FIND_SINTER_PARAM");
        if (eqpId.contains("HB")) {
            String bc = "";
            if (eqpId.contains("HB1")) {
                bc = "APJ-BC3";
            } else {
                bc = "APJ-BC4";
            }
            log.info("FIND_SINTER_PARAM 参数" + map);
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.getFlag())) {
                    value = (String) result.getContent();
                }
                if ("ERROR: NOT FOUND".equals(value)) {
                    log.error("EQP_ID:" + eqpId + "热压数据获取失败");
                }
            } else {
                return MesResult.error(eqpId + " not reply");
            }
        } else {
            return MesResult.error(eqpId + "设备名称不正确");
        }
        result.setContent(value);
        return result;
    }

    public MesResult findViParam(String eqpId, String opId) {
        MesResult result = MesResult.ok("default");
        String value = "";
        Map<String, String> map = Maps.newHashMap();
        if (eqpId.equals("ZJ")) {
            eqpId = "APJ-HB1-VI1";
        } else {
            log.error("设备名称错误！   " + eqpId);
            return MesResult.error(eqpId + "设备名称不正确");
        }
        map.put("EQP_ID", eqpId);
        map.put("METHOD", "FIND_VI_PARAM");
        String bc = "APJ-BC4";
        log.info("FIND_VI_PARAM 参数" + map);
        String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
        if (replyMsg != null) {
            result = JsonUtil.from(replyMsg, MesResult.class);
            if ("Y".equals(result.getFlag())) {
                value = (String) result.getContent();
            }
            if ("ERROR: NOT FOUND".equals(value)) {
                log.error("EQP_ID:" + eqpId + "中间耐压数据获取失败");
            }
        } else {
            return MesResult.error(eqpId + " not reply");
        }
        result.setContent(value);
        return result;
    }


    public MesResult findCleanParam(String eqpId, String opId) {
        MesResult result = MesResult.ok("default");
        String value = "";
        Map<String, String> map = Maps.newHashMap();
        if (eqpId.equals("JET")) {
            eqpId = "APJ-CLEAN-JET1";
        } else if(eqpId.equals("US")){
            eqpId = "APJ-CLEAN-US1";
        }else {
            log.error("设备名称错误！   " + eqpId);
            return MesResult.error(eqpId + "设备名称不正确");
        }
        map.put("EQP_ID", eqpId);
        map.put("METHOD", "FIND_CLEAN_PARAM");
        String bc = "APJ-BC6";
        log.info("FIND_CLEAN_PARAM 参数" + map);
        String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bc, JsonUtil.toJsonString(map));
        if (replyMsg != null) {
            result = JsonUtil.from(replyMsg, MesResult.class);
            if ("Y".equals(result.getFlag())) {
                value = (String) result.getContent();
            }
            if ("ERROR: NOT FOUND".equals(value)) {
                log.error("EQP_ID:" + eqpId + "洗净机数据数据获取失败");
            }
        } else {
            return MesResult.error(eqpId + " not reply");
        }
        result.setContent(value);
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
        if ("The Productionlog was not updated!".equals(temps)) {
            sendAlarmEmail("SIM-REFLOW1", "E-0010");
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
            log.info("findParam 参数" + map);
            String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", "SIM-BC1", JsonUtil.toJsonString(map));
            if (replyMsg != null) {
                result = JsonUtil.from(replyMsg, MesResult.class);
                if ("Y".equals(result.getFlag())) {
                    value = (String) result.getContent();
                }
                if ("ERROR: NOT FOUND".equals(value)) {
                    com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                    jsonObject.put("EQP_ID", eqpId + ":重量获取失败 批量：" + lotNo + "  品番：" + productionNo);
                    jsonObject.put("ALARM_CODE", "E-9999");
                    String jsonString = jsonObject.toJSONString();
                    log.info(eqpId + "重量数据获取失败!将发送邮件通知管理人员");
                    try {
                        rabbitTemplate.convertAndSend("C2S.Q.MSG.MAIL", jsonString);
                    } catch (Exception e) {
                        log.error("Exception:", e);
                    }
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
        String line = productionName.split("-")[0].replace("J.", "");
        if (line.length() > 3) {
            line = line.substring(0, 3);
        }
        if (line.startsWith("SX")) {
            line = "SX";
        }
        //String kongdongDir = "G:\\DSK_SIMULATOR\\8sim\\"+line+"\\" + productionName.replace("J.",""); //本地测试
        String kongdongDir = "D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\" + line + "\\" + productionName.replace("J.", "");
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
                if ("SIM".equals(line) && lotNoFile.size() == 7) {
                    break;
                }
                if ("SX".equals(line) && lotNoFile.size() == 8) {
                    break;
                }
            }
        }
        if (lotNoFile.size() == 0) {
            return "ERROR: not found file for " + lotNo;
        }
        String kongdongStr = "";
        if ("SIM".equals(line) || "SX".equals(line)) {
            List<MsMeasureKongdong> kongdongList = new ArrayList<>();
            if (lotNoFile.size() == 1) {
                return "ERROR:  file quantity need > 1,but found " + lotNoFile.size() + " for " + lotNo;
            }
            String[] kongdongVal = new String[7];
            if ("SX".equals(line)) {
                kongdongVal = new String[8];
            }
            for (File file : lotNoFile) {
                String[] fileNames = file.getName().split("-");
                String value = fileNames[0].replace(lotNo, "").replace("%", "").trim();
                String index = fileNames[fileNames.length - 1].replace(".bmp", "");
                if (Integer.parseInt(index) > 8) {
                    kongdongVal[Integer.parseInt(index) - 9] = value;
                } else {
                    kongdongVal[Integer.parseInt(index) - 1] = value;
                }
                try {
                    MsMeasureKongdong msMeasureKongdong = new MsMeasureKongdong();
                    String type = file.getName().split("%-")[1].replace(".bmp", "");
                    if (MsMeasureKongdongServiceImpl.typeMap.get(type) != null) {
                        type = MsMeasureKongdongServiceImpl.typeMap.get(type);
                    }
                    msMeasureKongdong.setProductionNo(productionNo);
                    msMeasureKongdong.setProductionName(productionName);
                    msMeasureKongdong.setLineNo(line);
                    msMeasureKongdong.setVoidRatio(Double.parseDouble(value));
                    msMeasureKongdong.setLotNo(lotNo);
                    msMeasureKongdong.setType(type);
                    kongdongList.add(msMeasureKongdong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String types = "";
            for (MsMeasureKongdong measureKongdong : kongdongList) {
                types = types + "," + measureKongdong.getType();
            }
            String msg = MsMeasureKongdongServiceImpl.getUnhaveData(productionName, types);
            if ("".equals(msg)) {
                try {
                    int count = iMsMeasureKongdongService.findKongdongData(line, productionName, lotNo);
                    if (count == 0) {
                        if (kongdongList.size() > 0) {
                            iMsMeasureKongdongService.insertBatch(kongdongList, 100);
                        }
                    }
                } catch (Exception e) {
                    log.error("空洞数据插入失败" + line + "  " + productionName + "  " + lotNo);
                    e.printStackTrace();
                }
                kongdongStr = StringUtil.join(kongdongVal, ",");
            } else {
                return "ERROR: Missing data!  type " + msg + "  Please try again after correction!";
            }

        }
        if ("5GI".equals(line) || "6GI".equals(line)) {
            log.info("file name: {}", lotNoFile.get(0).getName());
            if (lotNoFile.size() != 1) {
                return "ERROR:  file quantity need 1,but found " + lotNoFile.size() + " for " + lotNo;
            }
            String[] fileNames = lotNoFile.get(0).getName().split("%");
            kongdongStr = fileNames[0].split("-")[1].trim();

            MsMeasureKongdong msMeasureKongdong = null;
            try {
                msMeasureKongdong = new MsMeasureKongdong();
                msMeasureKongdong.setLineNo(line);
                msMeasureKongdong.setVoidRatio(Double.parseDouble(kongdongStr));
                msMeasureKongdong.setProductionNo(productionNo);
                msMeasureKongdong.setProductionName(productionName);
                msMeasureKongdong.setLotNo(lotNo);
                int count = iMsMeasureKongdongService.findKongdongData(line, productionName, lotNo);
                if (count == 0) {
                    iMsMeasureKongdongService.insert(msMeasureKongdong);
                }
            } catch (Exception e) {
                log.error("空洞数据插入失败" + line + "  " + productionName + "  " + lotNo);
                e.printStackTrace();
            }
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
            if (eqpId.contains("WB")) {
                result = trackinWB(eqpId, productionNo, productionName, orderNo, lotNo, recipeCode, opId);
            } else if (eqpId.contains("LF")) {
                result = trackinLFAndHTRT(eqpId, productionNo, productionName, orderNo, lotNo, recipeCode, opId);
            } else {
                result = trackinLine(eqpId, productionNo, productionName, orderNo, lotNo, recipeCode, opId);
            }
        }
        return result;
    }


    public MesResult apjTrackin(String subLineNo, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        saveTrackLog(subLineNo, "TRACKIN", productionNo, productionName, orderNo, lotNo, recipeCode, opId);
        result = trackinLine(subLineNo, productionNo, productionName, orderNo, lotNo, recipeCode, opId);
        return result;
    }

    /**
     * 针对线别track in,直接更新这条线全部设备
     *
     * @param sublineNo
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
    public MesResult trackinLine(String sublineNo, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        List<FabEquipment> fabEquipmentList = fabEquipmentService.findEqpBySubLine(sublineNo);
        if (fabEquipmentList.size() == 0) {
            return MesResult.error(sublineNo + " eqp not found");
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
        if (fabEquipmentList.size() != 2) {
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

    public MesResult trackinLFAndHTRT(String eqpId, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        List<FabEquipment> fabEquipmentList = new ArrayList<>();
        if (eqpId.contains("LF1")) {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode("SIM-LF1");
            fabEquipmentList.add(fabEquipment);
            FabEquipment fabEquipment1 = fabEquipmentService.findEqpByCode("SIM-HTRT1");
            if (fabEquipment1 != null) {
                fabEquipmentList.add(fabEquipment1);
            }
        } else if(eqpId.contains("LF2")){
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode("SIM-LF2");
            fabEquipmentList.add(fabEquipment);
            FabEquipment fabEquipment1 = fabEquipmentService.findEqpByCode("SIM-HTRT2");
            if (fabEquipment1 != null) {
                fabEquipmentList.add(fabEquipment1);
            }
        }else {
            return MesResult.error("eqp not found");
        }
        if (fabEquipmentList.size() != 2) {
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
            if (eqpId.contains("WB")) {
                result = trackoutWB(eqpId, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
            } else if(eqpId.contains("LF")){
                trackoutLFAndHTRT(eqpId, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
            }else {
                result = trackoutLine(eqpId, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
            }
        }
        return result;
    }


    public MesResult apjTrackout(String subLineNo, String productionNo, String productionName, String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        saveTrackLog(subLineNo, "TRACKOUT", productionNo, productionName, orderNo, lotNo, recipeCode, opId);
        result = trackoutLine(subLineNo, productionNo, productionName, orderNo, lotNo, yield, recipeCode, opId);
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

    public MesResult trackoutLFAndHTRT(String eqpId, String productionNo, String productionName, String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        MesResult result = MesResult.ok();
        List<FabEquipment> fabEquipmentList = new ArrayList<>();
        if (eqpId.contains("LF1")) {
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode("SIM-LF1");
            fabEquipmentList.add(fabEquipment);
            FabEquipment fabEquipment1 = fabEquipmentService.findEqpByCode("SIM-HTRT1");
            if (fabEquipment1 != null) {
                fabEquipmentList.add(fabEquipment1);
            }
        } else if(eqpId.contains("LF2")){
            FabEquipment fabEquipment = fabEquipmentService.findEqpByCode("SIM-LF2");
            fabEquipmentList.add(fabEquipment);
            FabEquipment fabEquipment1 = fabEquipmentService.findEqpByCode("SIM-HTRT2");
            if (fabEquipment1 != null) {
                fabEquipmentList.add(fabEquipment1);
            }
        }else {
            return MesResult.error("eqp not found");
        }
        if (fabEquipmentList.size() != 2) {
            return MesResult.error("eqp not found");
        }
        for (FabEquipment fabEquipment : fabEquipmentList) {
            if (yield == null || yield.equals("")) {
                yield = "5712";
            }
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
        if (fabEquipmentList.size() != 2) {
            return MesResult.error("eqp not found");
        }
        if (yield == null || yield.equals("")) {
            yield = "5712";
        } else {
            yield = (Integer.parseInt(yield) * 12) + "";
        }
        int yeild1 = Integer.parseInt(yield) / 2;
        for (FabEquipment fabEquipment : fabEquipmentList) {
            String yield2 = "" + yeild1;
            result = doTrackout(fabEquipment, productionNo, productionName, orderNo, lotNo, yield2, recipeCode, opId);
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
            if (fabEquipment.getEqpId().contains("AOI")) {
                MesResult mesResult1 = new MesResult();
                mesResult1.setFlag("Y");
                return mesResult1;
            } else {
                return MesResult.error(eqpId + " : " + lotNo + "trackin has been completed（批次已入账，无法重复入账！）");
            }
            //mesLotTrack = mesLotTrackList.get(0);
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
                if (replyMsg != null ) {
                    result = JsonUtil.from(replyMsg, MesResult.class);
                    //客户端修改成功后插入数据库
                    this.insertOrUpdate(mesLotTrack);
                    if ("Y".equals(result.getFlag())) {
                    }
                } else {
                    return MesResult.error(eqpId + " not reply");
                }
            }
        }
        /*log.info("[{}]更新设备状态数据, {}, {}", eqpId, lotNo, recipeCode);
        if (StringUtil.isNotBlank(lotNo) || StringUtil.isNotBlank(recipeCode)) {
            fabEquipmentStatusService.updateYield(eqpId, "RUN", lotNo, recipeCode, 0, 0);
        }*/
        return result;
    }

    public MesResult doTrackout(FabEquipment fabEquipment, String productionNo, String productionName, String orderNo, String lotNo, String yield, String recipeCode, String opId) {
        String eqpId = fabEquipment.getEqpId();
        List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo).eq("production_no", productionNo));
        /*if(eqpId.contains("WB")){
            String eqpid = eqpId+"A";
            mesLotTrackList= this.selectList(new EntityWrapper().eq("EQP_ID", eqpid).eq("lot_no", lotNo).eq("production_no", productionNo));
        }*/
        if (mesLotTrackList.size() == 0) {
            return MesResult.error("lotNo:" + lotNo + "   please track in first（批次未入账）");
        }
        MesLotTrack mesLotTrack = mesLotTrackList.get(0);
        if (mesLotTrack.getEndTime() != null) {
            if (!mesLotTrack.getEqpId().contains("AOI")) {
                return MesResult.error(eqpId + " : " + lotNo + "trackout has been completed(批次已出帐)");
            }
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
        return MesResult.ok("Y");
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
    public MesLotTrack findNoEndLotNo(String eqpId, Date startTime) {
        return baseMapper.findNoEndLotNo(eqpId, startTime);
    }

    @Override
    public List<MesLotTrack> findDataLotNo(String eqpId, Date startTime, Date endTime) {
        return baseMapper.findDataLotNo(eqpId, startTime, endTime);
    }

    @Override
    public MesLotTrack findLotNo1(String eqpId, Date startTime) {
        return baseMapper.findLotNo1(eqpId, startTime);
    }

    @Override
    public MesLotTrack findLotNo(String startTime, String eqpId) {
        return baseMapper.findLotNo(startTime, eqpId);
    }

    @Override
    public MesLotTrack findLotTrack(String eqpId, String lotNo, String productionNo) {
        return baseMapper.findLotTrack(eqpId, lotNo, productionNo);
    }

    @Override
    public MesLotTrack findNextStartTime(String endTime, String eqpId) {
        return baseMapper.findNextStartTime(endTime, eqpId);
    }

    @Override
    public MesLotTrack findLotByStartTime(String eqpId, Date startTime) {
        return baseMapper.findLotByStartTime(eqpId, startTime);
    }

    @Override
    public Boolean updateTrackLotYeildEqp(String eqpId, String lotNo, Integer lotYieldEqp) {
        return baseMapper.updateTrackLotYeildEqp(eqpId, lotNo, lotYieldEqp);
    }

    @Override
    public MesLotTrack selectEndTime(String eqpId, String lotNo) {
        return baseMapper.selectEndTime(eqpId, lotNo);
    }

    @Override
    public List<MesLotTrack> findCorrectData(Date startTime, Date endTime) {
        return baseMapper.findCorrectData(startTime, endTime);
    }

    @Override
    public MesLotTrack findLastTrack(String eqpId, String lotNo, Date startTime) {
        return baseMapper.findLastTrack(eqpId, lotNo, startTime);
    }

    @Override
    public List<Map> lotTrackQuery(String lineNo, String startTime, String endTime) {
        return baseMapper.lotTrackQuery(lineNo, startTime, endTime);
    }

    @Override
    public List<MesLotTrack> findLotsByTime(String eqpId, Date startTime, Date endTime) {
        return baseMapper.findLotsByTime(eqpId, startTime, endTime);
    }

    @Override
    public MesLotTrack findNowLotByEqp(String eqpId) {
        return baseMapper.findNowLotByEqp(eqpId);
    }

    @Override
    public Map<String, Object> chartKongDong(String lineNo, String proName, String startDate, String endDate) {
        try {
//            String productionName = baseMapper.findProName(proName);
//            String line = productionName.split("-")[0].replace("J.","");
//            if(line.length()>3){
//                line = line.substring(0,3);
//            }
//            if(line.startsWith("SX")){
//                line = "SX";
//            }
//            String kongdongDir = "D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\"+line+"\\" + productionName.replace("J.","");
//            log.info(kongdongDir);
//            List<Map<String, Object>> files = FileUtils.getFileInfos(kongdongDir, startDate, endDate);
//            return _handleFileNameG(files, productionName.replace("J.",""));
            Map<String, Object> param = new HashMap<>();
            param.put("productionName", proName);
            param.put("startTime", startDate);
            param.put("endTime", endDate);
            List<MsMeasureKongdong> data = iMsMeasureKongdongService.getKongdong(param);
            return _handleFileNameG(data, lineNo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("MesLotTrackServiceImpl_chartKongDong:error proName:" + proName);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getkongDongConfig(String productionNo) {
        return baseMapper.findkongdongConfig(productionNo);
    }

    @Override
    public List<String> findAllProName(String proName) {
        return baseMapper.findAllProName(proName);
    }

    @Override
    public List<Map<String, Object>> kongDongBar(String lotNo, String proName, String startDate, String endDate) {
        try {
//            String productionName = apsPlanPdtYieldService.findProductionName(productionNo);
            String productionName = baseMapper.findProName(proName);
            String line = productionName.split("-")[0].replace("J.", "");
            if (line.length() > 3) {
                line = line.substring(0, 3);
            }
            if (line.startsWith("SX")) {
                line = "SX";
            }
//            String kongdongDir = "d:\\backup\\"+line+"\\" + productionName.replace("J.",""); //本地测试
            String kongdongDir = "D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\" + line + "\\" + productionName.replace("J.", "");
            log.info(kongdongDir);
            List<Map<String, Object>> files = FileUtils.getFileInfos(kongdongDir, startDate, endDate);
            return _handleFileName(files, lotNo, line);
        } catch (Exception e) {
            log.error("MesLotTrackServiceImpl_chartKongDong:error proName:" + proName + ",lotNo:" + lotNo);
        }
        return null;
    }

    private Map<String, Object> _handleFileNameG(List<MsMeasureKongdong> data, String line) {
        Map<String, Object> chartOption = new HashMap<>();
        if (data != null && data.size() > 0) {
            List<Map<String, Object>> allLmt = baseMapper.findkongdongConfig(line);
            chartOption.put("Lmt", allLmt);
            Map<String, String> series = new HashMap<>();//type, double,double,
            List<String> xasix = new ArrayList<>();
            String asixStr = "";
            for (MsMeasureKongdong item : data) {
                if (!asixStr.equals(item.getLotNo())) {
                    xasix.add(item.getLotNo());
                    asixStr = item.getLotNo();
                }
                String voidRatio = MapUtils.getString(series, item.getType());
                voidRatio = voidRatio == null ? "" : voidRatio;
                series.put(item.getType(), voidRatio + "," + item.getVoidRatio());
            }
            Map<String, Object> seriesItem = new HashMap<>();
            List<String> legend = new ArrayList<>();
            for (String key : series.keySet()) {
                String doubleRatioStr = MapUtils.getString(series, key);
                doubleRatioStr = doubleRatioStr.substring(1);//删除最开头的逗号
                String[] doubleRatio = doubleRatioStr.split(",");
                List<Double> douItem = new ArrayList<>();
                for (String d : doubleRatio) {
                    if (StringUtil.isEmpty(d)) {
                        douItem.add(null);
                    } else {
                        douItem.add(Double.parseDouble(d));
                    }
                }
                seriesItem.put(key, douItem);
                legend.add(key);
            }
            chartOption.put("data", seriesItem);
            chartOption.put("xAxis", xasix);
            chartOption.put("legend", legend);
        }
        return chartOption;
    }

    private List<Map<String, Object>> _handleFileName(List<Map<String, Object>> files, String lotNo, String line) {
        if (files != null && files.size() > 0) {
            List<Map<String, Object>> res = new ArrayList<>();
            for (Map<String, Object> item : files) {
                String fileName = MapUtils.getString(item, "fileName");
                if (_chkFileNameLine(fileName, line)) {
                    String kongdongVal = "";
                    String fileLotNo = "";
                    String fileNo = "";
                    if (line.contains("5GI") || line.contains("6GI")) {
                        kongdongVal = fileName.substring(fileName.indexOf("-") + 1, fileName.indexOf("%"));
                        fileLotNo = fileName.substring(0, fileName.indexOf("-"));
                        fileNo = "IGBT";
                    } else {
                        kongdongVal = fileName.substring(fileName.indexOf(" ") + 1, fileName.indexOf("%"));
                        fileLotNo = fileName.substring(0, fileName.indexOf(" "));
                        fileNo = fileName.substring(fileName.indexOf("-") + 1, fileName.lastIndexOf("."));
                    }
                    item.put("lotNo", fileLotNo);
                    item.put("kongdongVal", kongdongVal);
                    item.put("xAxis", fileNo);
                    if (StringUtil.isEmpty(lotNo) || fileLotNo.equals(lotNo)) {
                        res.add(item);
                    }
                } else {
                    log.warn("MesLotTrackServiceImpl__handleFileName:fileName is error:" + fileName);
                }
            }
            return res;
        }
        return files;
    }

    private boolean _chkFileNameLine(String fileName, String line) {
        if (!line.contains("5GI") && !line.contains("6GI")) {
            if (fileName.indexOf(" ") <= 0) {
                return false;
            }
            if (fileName.indexOf("%") <= 0) {
                return false;
            }
            if (fileName.indexOf("-") <= 0) {
                return false;
            }
        }
        return true;
    }

    private List<Double> str2Double(String[] strArr) {
        List<Double> rs = new ArrayList<>();
        if (strArr.length > 0) {
            for (String item : strArr) {
                rs.add(Double.parseDouble(item));
            }
        }
        return rs;
    }

    @Override
    public String getKeyence(String mode, String lotNo, String production) {
        log.info("getKeyence");
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;

        File pathfile = new File("D:\\DSK1\\IT化データ（二課）\\キエンスー測定機\\SIM\\SIM(IT).csv");
        List<String> lines = null;
        try {
            lines = FileUtil.readLines(pathfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map> maps = new ArrayList<>();
        List<String> str = new ArrayList<>();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String serialCounter = "";
        for (int i = 0; i < lines.size(); i++) {
            log.info("基恩士:" + lines.get(i));
            String[] ele = lines.get(i).split(",");
            String[] ele2 = ele[2].split("-");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            if (ele2.length == 3) {
                if (ele2[1].equals(lotNo) && ele2[0].equals(production) && ele[4].equals("OK")) {
                    if (mode.equals("0")) {
                        if (ele[3].equals("0001-1") && one == 0) {
                            serialCounter = "1-1";
                            Map<String, Object> map = new HashMap<>();
                            str.add(ele[7]);
                            str.add(ele[8]);
                            str.add(ele[9]);
                            str.add(ele[28]);
                            one = 1;
                        } else if (ele[3].equals("0001-2") && two == 0) {
                            serialCounter = "1-2";
                            str.add(ele[7]);
                            str.add(ele[8]);
                            str.add(ele[9]);
                            str.add(ele[28]);
                            two = 1;
                        }
                    }
                    if (mode.equals("1")) {
                        if (ele[3].equals("0002-1") && three == 0) {
                            serialCounter = "2-1";
                            str.add(ele[7]);
                            str.add(ele[8]);
                            str.add(ele[9]);
                            str.add(ele[28]);
                            three = 1;
                        } else if (ele[3].equals("0002-2") && four == 0) {
                            serialCounter = "2-2";
                            str.add(ele[7]);
                            str.add(ele[8]);
                            str.add(ele[9]);
                            str.add(ele[28]);
                            four = 1;
                        }
                    }
                    try {
                        if (!"".equals(serialCounter)) {
                            MeasureSim measureSim = new MeasureSim();
                            measureSim.setLotNo(lotNo);
                            measureSim.setLineNo("SIM");
                            measureSim.setProductionNo("SIM6812M(E)D-URA_F2971");
                            measureSim.setMeasureType("LF");
                            measureSim.setMeasureCounter(ele[6]);
                            measureSim.setMeasureName(ele[5]);
                            measureSim.setMeasureJudgment(ele[4]);
                            measureSim.setMeaDate(df.parse(ele[1]));
                            measureSim.setA1(Double.valueOf(ele[7]));
                            measureSim.setB1(Double.valueOf(ele[8]));
                            measureSim.setC1(Double.valueOf(ele[9]));
                            measureSim.setC2(Double.valueOf(ele[10]));
                            measureSim.setC3(Double.valueOf(ele[11]));
                            measureSim.setC4(Double.valueOf(ele[12]));
                            measureSim.setC5(Double.valueOf(ele[13]));
                            measureSim.setC6(Double.valueOf(ele[14]));
                            measureSim.setC7(Double.valueOf(ele[15]));
                            measureSim.setC8(Double.valueOf(ele[16]));
                            measureSim.setC9(Double.valueOf(ele[17]));
                            measureSim.setC10(Double.valueOf(ele[18]));
                            measureSim.setC11(Double.valueOf(ele[19]));
                            measureSim.setC12(Double.valueOf(ele[20]));
                            measureSim.setC13(Double.valueOf(ele[21]));
                            measureSim.setC14(Double.valueOf(ele[22]));
                            measureSim.setC15(Double.valueOf(ele[23]));
                            measureSim.setC16(Double.valueOf(ele[24]));
                            measureSim.setC17(Double.valueOf(ele[25]));
                            measureSim.setC19(Double.valueOf(ele[26]));
                            measureSim.setC20(Double.valueOf(ele[27]));
                            measureSim.setC21(Double.valueOf(ele[28]));
                            measureSim.setC23(Double.valueOf(ele[29]));
                            measureSim.setC24(Double.valueOf(ele[30]));
                            measureSim.setC26(Double.valueOf(ele[31]));
                            measureSim.setC28(Double.valueOf(ele[32]));
                            measureSim.setC30(Double.valueOf(ele[33]));
                            measureSim.setC31(Double.valueOf(ele[34]));
                            measureSim.setC35(Double.valueOf(ele[35]));
                            measureSim.setC37(Double.valueOf(ele[36]));
                            measureSim.setC40(Double.valueOf(ele[37]));
                            measureSim.setSerialCounter(serialCounter);
                            EntityWrapper wrapper = new EntityWrapper();
                            wrapper.eq("lot_no", lotNo).eq("production_no", production).eq("measure_type", "IT").eq("serial_counter", serialCounter);
                            Integer nm = measureSimMapper.selectCount(wrapper);
                            if (nm < 1) {
                                measureSimMapper.insert(measureSim);
                            }
                        }
                    } catch (Exception e) {
                        log.error("SIM分离数据解析出错！", e);
                        e.printStackTrace();
                    }
                }
            }
        }
        String result = String.join(",", str);
        return result;
    }


    public String find5GI(String lotNo, String production) {
        String[] temp = production.split("-");
        String pro = temp[1].substring(0, 4);
        String proOther = temp[1].substring(0, 5);
        String productionName = production.replace("_", "");
        File pathfile = new File("D:\\DSK1\\IT化データ（二課）\\キエンスー測定機\\5GI\\5GI(IT)\\5GI(IT).csv");
        List<String> lines = null;
        try {
            lines = FileUtil.readLines(pathfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> str = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (int i = 0; i < lines.size(); i++) {
            String[] ele = lines.get(i).split(",");
            String[] ele2 = ele[2].split("-");
            if (ele2.length == 3) {
                if (ele[4].equals("OK") && ele2[1].equals(lotNo) && (ele2[0].equals(pro) || ele2[0].equals(proOther))) {
                    for (int j = 7; j < ele.length; j++) {
                        str.add(ele[j]);
                    }
                    if (str.size() > 0) {
                        try {
                            MeasureGi measureGi = new MeasureGi();
                            measureGi.setLotNo(lotNo);
                            measureGi.setLineNo("5GI");
                            measureGi.setProductionNo(productionName);//机种名
                            measureGi.setMeasureType("LF");
                            measureGi.setSerialCounter(ele[3].replace("000", ""));//串行计数器
                            measureGi.setMeasureCounter(ele[6]);//测量次数
                            measureGi.setMeasureName(ele[5]);//操作员编号
                            measureGi.setMeasureJudgment("OK");
                            measureGi.setMeaDate(df.parse(ele[1]));
                            measureGi.setBurrf(Double.parseDouble(ele[7]));
                            measureGi.setPinf1(Double.parseDouble(ele[8]));
                            measureGi.setPinf2(Double.parseDouble(ele[9]));
                            measureGi.setPinf3(Double.parseDouble(ele[10]));
                            measureGi.setPinf4(Double.parseDouble(ele[11]));
                            measureGi.setPinf5(Double.parseDouble(ele[12]));
                            measureGi.setPinf6(Double.parseDouble(ele[13]));
                            measureGi.setPinf1f2(Double.parseDouble(ele[14]));
                            measureGi.setPinf2f3(Double.parseDouble(ele[15]));
                            measureGi.setPinf3f4(Double.parseDouble(ele[16]));
                            measureGi.setPinf4f5(Double.parseDouble(ele[17]));
                            measureGi.setPinf5f6(Double.parseDouble(ele[18]));
                            measureGi.setPins1(Double.parseDouble(ele[19]));
                            measureGi.setPins2(Double.parseDouble(ele[20]));
                            measureGi.setPins3(Double.parseDouble(ele[21]));
                            measureGi.setPins4(Double.parseDouble(ele[22]));
                            measureGi.setPins5(Double.parseDouble(ele[23]));
                            measureGi.setPins6(Double.parseDouble(ele[24]));
                            EntityWrapper wrapper = new EntityWrapper();
                            wrapper.eq("lot_no", lotNo).eq("production_no", production).eq("measure_type", "IT").eq("serial_counter", ele[3].replace("000", ""));
                            Integer nm = measureGiMapper.selectCount(wrapper);
                            if (nm < 1) {
                                measureGiMapper.insert(measureGi);
                            }
                        } catch (Exception e) {
                            log.error("5GI分离数据导入失败");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        String result = String.join(",", str);
        return result;
    }

    public String find6GI(String lotNo, String production) {
        String[] temp = production.split("-");
        String pro = temp[1].substring(0, 4);
        String productionName = production.replace("_", "");
        File pathfile = new File("D:\\DSK1\\IT化データ（二課）\\キエンスー測定機\\6GI\\6GI(IT)\\6GI(IT).csv");
        List<String> lines = null;
        try {
            lines = FileUtil.readLines(pathfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map> maps = new ArrayList<>();
        List<String> str = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (int i = 0; i < lines.size(); i++) {
            String[] ele = lines.get(i).split(",");
            String[] ele2 = ele[2].split("-");
            if (ele2.length >= 2) {
                if (ele2[1].equals(lotNo) && ele2[0].equals(pro) && ele[4].equals("OK")) {
                    for (int j = 7; j < ele.length; j++) {
                        str.add(ele[j]);
                    }
                    if (str.size() > 0) {
                        try {
                            MeasureGi measureGi = new MeasureGi();
                            measureGi.setLotNo(lotNo);
                            measureGi.setLineNo("6GI");
                            measureGi.setProductionNo(productionName);//机种名
                            measureGi.setMeasureType("LF");
                            measureGi.setSerialCounter(ele[3].replace("000", ""));//串行计数器
                            measureGi.setMeasureCounter(ele[6]);//测量次数
                            measureGi.setMeasureName(ele[5]);//操作员编号
                            measureGi.setMeasureJudgment("OK");
                            measureGi.setMeaDate(df.parse(ele[1]));
                            measureGi.setBurrf(Double.parseDouble(ele[7]));
                            measureGi.setPinf1(Double.parseDouble(ele[8]));
                            measureGi.setPinf2(Double.parseDouble(ele[9]));
                            measureGi.setPinf3(Double.parseDouble(ele[10]));
                            measureGi.setPinf4(Double.parseDouble(ele[11]));
                            measureGi.setPinf5(Double.parseDouble(ele[12]));
                            measureGi.setPinf6(Double.parseDouble(ele[13]));
                            measureGi.setPinf1f2(Double.parseDouble(ele[14]));
                            measureGi.setPinf2f3(Double.parseDouble(ele[15]));
                            measureGi.setPinf3f4(Double.parseDouble(ele[16]));
                            measureGi.setPinf4f5(Double.parseDouble(ele[17]));
                            measureGi.setPinf5f6(Double.parseDouble(ele[18]));
                            measureGi.setPins1(Double.parseDouble(ele[19]));
                            measureGi.setPins2(Double.parseDouble(ele[20]));
                            measureGi.setPins3(Double.parseDouble(ele[21]));
                            measureGi.setPins4(Double.parseDouble(ele[22]));
                            measureGi.setPins5(Double.parseDouble(ele[23]));
                            measureGi.setPins6(Double.parseDouble(ele[24]));
                            EntityWrapper wrapper = new EntityWrapper();
                            wrapper.eq("lot_no", lotNo).eq("production_no", production).eq("measure_type", "IT").eq("serial_counter", ele[3].replace("000", ""));
                            Integer nm = measureGiMapper.selectCount(wrapper);
                            if (nm < 1) {
                                measureGiMapper.insert(measureGi);
                            }
                        } catch (Exception e) {
                            log.error("6GI分离数据导入失败");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        String result = String.join(",", str);
        return result;
    }

    @SuppressWarnings("unchecked")
    public String findSX(String production, String lotNo, String flag) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        SimpleDateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<String> lines = null;
        String productionName = production.replace("_", "");
        StringBuilder result = new StringBuilder();
        File pathfile = new File("D:\\DSK1");
        if ("LF".equals(flag)) {
            pathfile = new File("D:\\DSK1\\IT化データ（二課）\\キエンスー測定機\\SX\\SX-LF\\SX-LF.csv");
        } else if ("check".equals(flag)) {
            pathfile = new File("D:\\DSK1\\IT化データ（二課）\\キエンスー測定機\\SX\\SX-检查\\SX-检查.csv");
        }
        try {
            lines = FileUtil.readLines(pathfile);
            LocalDate localDate = LocalDate.of(2019, 1, 7);

            if (production.contains("YGD")) {
                production = production.substring(2, 7) + "YGD";
            } else {
                production = production.substring(2, 8);
            }
            for (String rowString : lines) {
                String[] row = rowString.split(",");
                String[] colum2 = row[2].split("-");
                if (production.equals(colum2[0]) && lotNo.equals(colum2[1]) && row[4].equals("OK")) {
                    result.append(row[7] + "," + row[8] + "," + row[9] + "," + row[10] + "," + row[11] + "," + row[12] + "," + row[13] + "," + row[14] + ",");
                    MeasureSx measure = new MeasureSx();
                    measure.setLineNo("SX");
                    measure.setMeaDate(df.parse(row[1]));
                    measure.setLotNo(colum2[1]);
                    measure.setProductionNo(productionName);
                    measure.setSerialCounter(row[3].replaceFirst("^0*", ""));
                    measure.setMeasureJudgment(row[4]);
                    measure.setMeasureName(row[5]);
                    measure.setMeasureCounter(row[6]);
                    measure.setA1(Double.parseDouble(row[7]));
                    measure.setB1(Double.parseDouble(row[8]));
                    measure.setC1(Double.parseDouble(row[9]));
                    measure.setD1(Double.parseDouble(row[10]));
                    measure.setA2(Double.parseDouble(row[11]));
                    measure.setB2(Double.parseDouble(row[12]));
                    measure.setC2(Double.parseDouble(row[13]));
                    measure.setD2(Double.parseDouble(row[14]));
                    measure.setMeasureType(flag);
                    EntityWrapper wrapper = new EntityWrapper();
                    wrapper.eq("lot_no", lotNo).eq("production_no", productionName).eq("measure_type", flag).eq("serial_counter", row[3].replaceFirst("^0*", ""));
                    Integer nm = measureSxMapper.selectCount(wrapper);
                    if (nm < 1) {
                        measureSxMapper.insert(measure);
                    } else {

                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    /*public static void main(String[] args) {
      String a = "SX68122M (AU)YGD-RP";
      String b = a.substring(0,7);
      int d =a.indexOf("c");
        System.out.println(b);
    }
    //}*/
    public Boolean sendAlarmEmail(String eqpId, String alarmCode) {
        Boolean flag = true;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("EQP_ID", eqpId);
        jsonObject.put("ALARM_CODE", alarmCode);
        String jsonString = jsonObject.toJSONString();
        log.info(eqpId + "设备---日志数据未更新!将发送邮件通知管理人员");
        try {
            rabbitTemplate.convertAndSend("C2S.Q.MSG.MAIL", jsonString);
        } catch (Exception e) {
            flag = false;
            log.error("Exception:", e);
        }
        return flag;
    }


}
