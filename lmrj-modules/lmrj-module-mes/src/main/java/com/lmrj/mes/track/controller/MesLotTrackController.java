package com.lmrj.mes.track.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.utils.PageRequest;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.entity.MesResult;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.measure.entity.MeasureDm;
import com.lmrj.mes.measure.service.MeasureDmService;
import com.lmrj.mes.track.entity.MesLotMaterial;
import com.lmrj.mes.track.entity.MesLotMaterialInfo;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotMaterialInfoService;
import com.lmrj.mes.track.service.IMesLotMaterialService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.ms.thrust.entity.MsMeasureThrust;
import com.lmrj.ms.thrust.service.IMsMeasureThrustService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.controller
 * @title: mes_lot_track控制器
 * @description: mes_lot_track控制器
 * @author: 张伟江
 * @date: 2020-04-28 14:03:16
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("mes/meslottrack")
@ViewPrefix("mes/meslottrack")
//@RequiresPathPermission("mes:meslottrack")
@LogAspectj(title = "mes_lot_track")
@Slf4j
public class MesLotTrackController extends BaseCRUDController<MesLotTrack> {
    @Autowired
    IMsMeasureThrustService iMsMeasureThrustService;
    @Autowired
    IMesLotTrackService mesLotTrackService;
    @Autowired
    IFabLogService fabLogService;
    @Autowired
    MeasureDmService measureDmService;
    @Autowired
    IMesLotMaterialService mesLotMaterialService;
    @Autowired
    IMesLotMaterialInfoService mesLotMaterialInfoService;
    //@RequestMapping(value = "/trackin/{eqpId}/{lotNo}", method = { RequestMethod.GET, RequestMethod.POST })
    //public MesResult trackin(Model model, @PathVariable String eqpId, @PathVariable String lotNo, @RequestParam String recipeCode, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
    //    return mesLotTrackService.trackIn( eqpId,   lotNo,   recipeCode,   opId);
    //}
    @RequestMapping("/lotTrackQuery")
    public Response lotTrackQuery(@RequestParam String lineNo, @RequestParam String beginTime, @RequestParam String endTime, HttpServletRequest request, HttpServletResponse response) {
        Response res = new Response();
        List<Map> maps = mesLotTrackService.lotTrackQuery(lineNo, beginTime, endTime);
        res.put("lottrack", maps);
        return res;
    }

    //50029150702D 37368342             037368342ED   J.SIM6812M(E)D-URA F2971
    @RequestMapping(value = "/dsktrackin/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String dskTrackin(Model model, @PathVariable String eqpId, @RequestParam String trackinfo, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        log.info("dsktrackin :  {}", trackinfo);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"trackinfo\":\"" + trackinfo + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param6", "MesLotTrackController.dskTrackin", eventDesc, trackinfo, "wangdong");//日志记录参数
            if (trackinfo.length() < 30) {
                return "trackinfo too short";
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1].trim();
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");

            String productionNo = lotNos[0].substring(0, 7); //5002915
            String lotNo = lotNos[0].substring(7, 12); //0702D
            String orderNo = lotNos[1]; //37368342


            String eqpId1 = eqpId;
            if (eqpId.contains("WB")) {
                eqpId1 = eqpId + "A";
            } else if (eqpId.contains("DM")) {
                eqpId1 = "SIM-DM1";
            } else if (eqpId.contains("LF1")) {
                eqpId1 = "SIM-LF1";
            } else if (eqpId.contains("LF2")) {
                eqpId1 = "SIM-LF2";
            }

            //判断批次数据入账是否符合逻辑
            MesLotTrack lastLotTrack = mesLotTrackService.findLotNo1(eqpId1, new Date());
            if (lastLotTrack != null && !eqpId1.contains("SIM-LF")) {
                if (!lastLotTrack.getLotNo().equals(lotNo) && lastLotTrack.getEndTime() == null) {
                    log.error("人员误操作记录，" + eqpId1 + ":" + lastLotTrack.getLotNo() + "批次未结束,无法对" + lotNo + "进行入账");
                    return eqpId1 + "设备" + lastLotTrack.getLotNo() + " is not finished ! Please do track out first";
                }
            }
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.trackin(eqpId, productionNo, productionName, orderNo, lotNo, "", opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.dskTrackin", jo.toString(), trackinfo, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return "Y";
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.dskTrackin", "有异常" + e, trackinfo, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    //三垦干燥炉入帐   有时会出现两个批次同时入帐，将两个批次写在一起，/分隔
    @RequestMapping(value = "/dskOventrackin/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String dskOvenTrackin(Model model, @PathVariable String eqpId, @RequestParam String trackinfo1, @RequestParam String trackinfo2, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        log.info("dsktrackin :  {} , {}", trackinfo1, trackinfo2);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"trackinfo1\":\"" + trackinfo1 + "\",\"trackinfo2\":\"" + trackinfo2 + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param6", "MesLotTrackController.dskOventrackin", eventDesc, trackinfo1 + "/" + trackinfo2, "wangdong");//日志记录参数
            if (trackinfo1.length() < 30) {
                return "trackinfo too short";
            }
            String[] trackinfos = trackinfo1.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1].trim();
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");
            String productionNo = lotNos[0].substring(0, 7); //5002915
            String lotNo = lotNos[0].substring(7, 12); //0702D
            String orderNo = lotNos[1]; //37368342
            if (trackinfo2.length() > 30) {
                String[] trackinfos2 = trackinfo2.split("\\.");
                String lotorder2 = trackinfos2[0];
                String[] lotNos2 = lotorder2.split("_");
                String lotNo2 = lotNos2[0].substring(7, 12);
                lotNo = lotNo + "~" + lotNo2;
            }
            String eqpId1 = eqpId;
            if (eqpId.contains("SIM-OVEN1")) {
                eqpId1 = "SIM-OVEN1";
            } else if (eqpId.contains("SIM-OVEN2")) {
                eqpId1 = "SIM-OVEN2";
            }
            //判断批次数据入账是否符合逻辑
            MesLotTrack lastLotTrack = mesLotTrackService.findLotNo1(eqpId1, new Date());
            if (lastLotTrack != null) {
                if (!lastLotTrack.getLotNo().equals(lotNo) && lastLotTrack.getEndTime() == null) {
                    log.error("人员误操作记录，" + eqpId1 + ":" + lastLotTrack.getLotNo() + "批次未结束,无法对" + lotNo + "进行入账");
                    return eqpId1 + "设备" + lastLotTrack.getLotNo() + " is not finished ! Please do track out first";
                }
            }
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.trackin(eqpId, productionNo, productionName, orderNo, lotNo, "", opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.dskOventrackin", jo.toString(), trackinfo1, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return "Y";
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.dskOventrackin", "有异常" + e, trackinfo1, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/dskapjtrackin/{subLineNo}", method = {RequestMethod.GET, RequestMethod.POST})
    public String dskApjTrackin(Model model, @PathVariable String subLineNo, @RequestParam String trackinfo,@RequestParam String materialInfo, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        log.info("dsktrackin :  {}", trackinfo);
        String eventDesc = "{\"subLineNo\":\"" + subLineNo + "\",\"trackinfo\":\"" + trackinfo+ "\",\"materialInfo\":\"" + materialInfo + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(subLineNo, "Param6", "MesLotTrackController.dskApjTrackin", eventDesc, trackinfo, "wangdong");//日志记录参数
            if (trackinfo.length() < 30) {
                return "trackinfo too short";
            }
            if("RY2".equals(subLineNo)){
                if(materialInfo.length()==0 || "".equals(materialInfo) || materialInfo == null){
                    return "Material infomation must be provided(必须提交芯片物料信息)";
                }
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1].trim();
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");

            String productionNo = lotNos[0].substring(0, 7); //5002915
            String lotNo = lotNos[0].substring(7, 12); //0702D
            String orderNo = lotNos[1]; //37368342

            String eqpId1 = subLineNo;
            if (subLineNo.equals("IGBT")) {
                eqpId1 = "DM-IGBT-SORT1";
            } else if (eqpId1.equals("FRD")) {
                eqpId1 = "DM-FRD-SORT1";
            } else if (eqpId1.equals("IGBTYJH")) {
                eqpId1 = "DM-IGBT-SORT3";
            } else if (eqpId1.equals("FRDYJH")) {
                eqpId1 = "DM-FRD-SORT3";
            } else if (eqpId1.equals("RY1")) {
                eqpId1 = "DM-HB1-SORT2";
            } else if (eqpId1.equals("RY2")) {
                eqpId1 = "DM-HB2-SORT2";
            } else if (eqpId1.equals("ZJ")) {
                eqpId1 = "DM-VI1";
            } else if (eqpId1.equals("TOP")) {
                eqpId1 = "DM-DBCT-SORT2";
            } else if (eqpId1.equals("BOTTOM")) {
                eqpId1 = "DM-DBCB-SORT2";
            } else if (eqpId1.equals("TRM")) {
                eqpId1 = "DM-TRM1";
            } else if (eqpId1.equals("SAT")) {
                eqpId1 = "DM-SAT1";
            } else if (eqpId1.contains("AT") && !eqpId1.contains("S")) {
                eqpId1 = "DM-AT1";
            } else if (eqpId1.equals("LF")) {
                eqpId1 = "DM-LF1";
            } else if (eqpId1.equals("HTRT")) {
                eqpId1 = "DM-HTRT1";
            } else if (eqpId1.equals("XRAY")) {
                eqpId1 = "DM-XRAY1";
            } else if (eqpId1.equals("JET")) {
                eqpId1 = "DM-CLEAN-JET1";
            } else if (eqpId1.equals("US")) {
                eqpId1 = "DM-CLEAN-US1";
            } else if (eqpId1.equals("OVEN1")) {
                eqpId1 = "DM-OVEN1";
            } else if (eqpId1.equals("OVEN2")) {
                eqpId1 = "DM-OVEN2";
            }
            //判断批次数据入账是否符合逻辑
            MesLotTrack lastLotTrack = mesLotTrackService.findLotNo1(eqpId1, new Date());
            if (lastLotTrack != null) {
                if (!lastLotTrack.getLotNo().equals(lotNo) && lastLotTrack.getEndTime() == null) {
                    log.error("人员误操作记录，" + eqpId1 + ":" + lastLotTrack.getLotNo() + "批次未结束,无法对" + lotNo + "进行入账");
                    return eqpId1 + "设备" + lastLotTrack.getLotNo() + " is not finished ! Please do track out first";
                }
            }
            MesResult result = mesLotTrackService.apjTrackin(subLineNo, productionNo, productionName, orderNo, lotNo, "", opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(subLineNo, "Result6", "MesLotTrackController.dskApjTrackin", jo.toString(), trackinfo, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                List<MesLotMaterialInfo> materialInfosList = new ArrayList<>();
                try {
                    //将物料信息存入数据库
                    String materialInfos[] = materialInfo.split("_");
                    MesLotMaterial mesLotMaterial =  mesLotMaterialService.selectMaterialData(eqpId1,lotNo);
                    if(mesLotMaterial == null || "".equals(mesLotMaterial.getEqpId()) || mesLotMaterial.getEqpId() ==null){
                        mesLotMaterial = new MesLotMaterial();
                        mesLotMaterial.setEqpId(eqpId1);
                        mesLotMaterial.setLotNo(lotNo);
                        mesLotMaterial.setCreateBy(opId);
                        mesLotMaterial.setCreateDate(new Date());
                        mesLotMaterialService.insert(mesLotMaterial);
                    }
                    materialInfosList = new ArrayList<>();
                    for (String info : materialInfos) {
                        String materialName = info.split(",")[0];
                        String materiallotNo = info.split(",")[1];
                        MesLotMaterialInfo mesLotMaterialInfo = new MesLotMaterialInfo();
                        mesLotMaterialInfo.setMaterialId(mesLotMaterial.getId());
                        mesLotMaterialInfo.setCreateBy(opId);
                        mesLotMaterialInfo.setMaterialName(materialName);
                        mesLotMaterialInfo.setLotNo(materiallotNo);
                        mesLotMaterialInfo.setCreateDate(new Date());
                        materialInfosList.add(mesLotMaterialInfo);
                    }
                } catch (Exception e) {
                    log.error("mesLotMaterial 解析出错  主表设备："+eqpId1 + "  主表批量："+lotNo  +"  materialInfo:"+materialInfo,e);
                    e.printStackTrace();
                }
                try {
                    if(!mesLotMaterialInfoService.insertBatch(materialInfosList,10)){
                        log.error("materialInfosList 插入失败");
                    }
                } catch (Exception e) {
                    log.error("materialInfosList 插入出错  主表设备："+eqpId1 + "  主表批量："+lotNo,e);
                    e.printStackTrace();
                }
                return "Y";
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(subLineNo, "Error6", "MesLotTrackController.dskApjTrackin", "有异常", trackinfo, "wangdong");//日志记录
            return e.getMessage();
        }
    }


    @RequestMapping(value = "/findRecipeName/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findRecipeName(Model model, @PathVariable String eqpId, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        log.info("findRecipeName :  {}", opId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param6", "MesLotTrackController.findRecipeName", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.findRecipeName(eqpId, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.findRecipeName", jo.toString(), "", "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.findRecipeName", "有异常", "", "wangdong");//日志记录
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/findTemp/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findTemp(Model model, @PathVariable String eqpId, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        log.info("findTemp :  {}", opId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param6", "MesLotTrackController.findTemp", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.findTemp(eqpId, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.findTemp", jo.toString(), "", "wangdong");//日志记录参数
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.findTemp", "有异常", "", "wangdong");//日志记录参数
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/findParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findParam(Model model, @PathVariable String eqpId, @RequestParam String param, @RequestParam String opId, @RequestParam(required = false) String lotNo,
                            @RequestParam(required = false) String productionName,
                            @RequestParam(required = false) String productionNo,
                            @RequestParam(required = false) String index,
                            HttpServletRequest request, HttpServletResponse response) {
        log.info("findParam :  {}, {}, {}, {}", opId, lotNo, productionNo, index);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"lotNo\":\"" + lotNo + "\",\"opId\":\"" + opId + "\",\"param\":\"" + param + "\",\"productionName\":\"" + productionName + "\",\"productionNo\":\"" + productionNo + "\",\"index\":\"" + index + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param6", "MesLotTrackController.findParam", eventDesc, lotNo, "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.findParam(eqpId, param, opId, lotNo, productionNo);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.findParam", jo.toString(), lotNo, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.findParam", "有异常", lotNo, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    //查找APJ-PRINTER设备参数，从产量日志中获取
    @RequestMapping(value = "/findPrinterParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findPrinterParam(Model model, @PathVariable String eqpId, @RequestParam String opId,
                                   HttpServletRequest request, HttpServletResponse response) {
        log.info("findPrinterParam :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param7", "MesLotTrackController.findPrinterParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.equals("TOP")) {
                eqpId = "DM-DBCT-PRINTER1";
            } else if (eqpId.equals("BOTTOM")) {
                eqpId = "DM-DBCB-PRINTER1";
            } else if (eqpId.equals("FRD")) {
                eqpId = "DM-FRD-PRINTER1";
            } else if (eqpId.equals("IGBT")) {
                eqpId = "DM-IGBT-PRINTER1";
            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            String methodName = "FIND_PRINTER_PARAM";
            MesResult result = mesLotTrackService.findApjParam(eqpId, methodName, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result7", "MesLotTrackController.findPrinterParam", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error7", "MesLotTrackController.findPrinterParam", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/findApjRecipeCode/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findApjRecipeCode(Model model, @PathVariable String eqpId, @RequestParam String opId,
                                    HttpServletRequest request, HttpServletResponse response) {
        log.info("findApjRecipeCode :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param8", "MesLotTrackController.findApjRecipeCode", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            MesResult result = mesLotTrackService.findApjRecipeCode(eqpId, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result8", "MesLotTrackController.findApjRecipeCode", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error8", "MesLotTrackController.findApjRecipeCode", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/findReflowParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findReflowParam(Model model, @PathVariable String eqpId, @RequestParam String opId,
                                  HttpServletRequest request, HttpServletResponse response) {
        log.info("findReflowParam :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param9", "MesLotTrackController.findReflowParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.equals("TOP")) {
                eqpId = "DM-DBCT-REFLOW1";
            } else if (eqpId.equals("BOTTOM")) {
                eqpId = "DM-DBCB-REFLOW1";
            } else if (eqpId.equals("FRD")) {
                eqpId = "DM-FRD-REFLOW1";
            } else if (eqpId.equals("IGBT")) {
                eqpId = "DM-IGBT-REFLOW1";
            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            String methodName = "FIND_REFLOW_PARAM";
            MesResult result = mesLotTrackService.findApjParam(eqpId, methodName, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result9", "MesLotTrackController.findReflowParam", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error9", "MesLotTrackController.findReflowParam", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/findSinterParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findSinterParam(Model model, @PathVariable String eqpId, @RequestParam String opId,
                                  HttpServletRequest request, HttpServletResponse response) {
        log.info("findSinterParam :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param10", "MesLotTrackController.findSinterParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.equals("RY1")) {
                eqpId = "DM-HB1-SINTERING1";
            } else if (eqpId.contains("RY2S1")) {//金型温度设定值
                eqpId = "DM-HB2-SINTERING1-1";
            } else if (eqpId.contains("RY2S2")) {//压力、温度设定值
                eqpId = "DM-HB2-SINTERING1-2";
            }/* else if(eqpId.equals("RY2C")){//温度测定值
            eqpId = "APJ-HB2-SINTERING1-3";
        } */ else if (eqpId.contains("RY2DJ")) {//点胶机数据
                eqpId = "DM-HB2-DISPENSING1";
            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            String methodName = "FIND_SINTER_PARAM";
            MesResult result = mesLotTrackService.findApjParam(eqpId, methodName, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result10", "MesLotTrackController.findSinterParam", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error10", "MesLotTrackController.findSinterParam", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    //查找APJ二次热压设备参数，从产量日志中获取
    @RequestMapping(value = "/findViParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findViParam(Model model, @PathVariable String eqpId, @RequestParam String opId,
                              HttpServletRequest request, HttpServletResponse response) {
        log.info("findViParam :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param12", "MesLotTrackController.findViParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.equals("ZJ")) {
                eqpId = "DM-VI1";
            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            String methodName = "FIND_VI_PARAM";
            MesResult result = mesLotTrackService.findApjParam(eqpId, methodName, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result12", "MesLotTrackController.findViParam", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error12", "MesLotTrackController.findViParam", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    //查找APJ二次热压设备参数，从产量日志中获取
    @RequestMapping(value = "/findCleanParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findCleanParam(Model model, @PathVariable String eqpId, @RequestParam String opId,
                                 HttpServletRequest request, HttpServletResponse response) {
        log.info("findCleanParam :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param13", "MesLotTrackController.findCleanParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.equals("JET")) {
                eqpId = "DM-CLEAN-JET1";
            } else if (eqpId.equals("US")) {
                eqpId = "DM-CLEAN-US1";
            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            String methodName = "FIND_CLEAN_PARAM";
            MesResult result = mesLotTrackService.findApjParam(eqpId, methodName, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result13", "MesLotTrackController.findCleanParam", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error13", "MesLotTrackController.findCleanParam", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/findOvenParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findOvenParam(Model model, @PathVariable String eqpId, @RequestParam String lotNo, @RequestParam String param,
                                @RequestParam String opId,
                                HttpServletRequest request, HttpServletResponse response) {
        log.info("findOvenParam :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\",\"lotNo\":\"" + lotNo + "\",\"param\":\"" + param + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param14", "MesLotTrackController.findOvenParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.equals("DM-OVEN1")) {
                eqpId = "DM-OVEN1";
            } else if (eqpId.equals("DM-OVEN2")) {
                eqpId = "DM-OVEN2";
            } else if (eqpId.equals("SIM-OVEN1")) {
                eqpId = "SIM-OVEN1";
            } else if (eqpId.equals("SIM-OVEN2")) {
                eqpId = "SIM-OVEN2";
            } else if (eqpId.equals("SMA-OVEN1")) {
                eqpId = "SMA-OVEN1";
            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            String methodName = "";
            if ("1".equals(param)) {
                methodName = "FIND_OVEN_PARAM_1";
            } else if ("2".equals(param)) {
                methodName = "FIND_OVEN_PARAM_2";
            } else {
                return "param error!:" + param;
            }
            if ("".equals(lotNo) || lotNo == null) {
                lotNo = "test";
            }
            MesResult result = mesLotTrackService.findApjParam(eqpId + "_" + lotNo, methodName, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result14", "MesLotTrackController.findOvenParam", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error14", "MesLotTrackController.findOvenParam", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }


    @RequestMapping(value = "/findLFANDHTRTParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findLFANDHTRTParam(Model model, @PathVariable String eqpId, @RequestParam String opId, @RequestParam String param, @RequestParam String lotNo,
                                     HttpServletRequest request, HttpServletResponse response) {
        log.info("findLFANDHTRTParam :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\",\"lotNo\":\"" + lotNo + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param15", "MesLotTrackController.findLFANDHTRTParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.equals("LF1")) {
                eqpId = "SIM-LF1";
            } else if (eqpId.equals("LF2")) {
                eqpId = "SIM-LF2";
            } else if (eqpId.equals("HTRT1")) {
                eqpId = "SIM-HTRT1";
            } else if (eqpId.equals("HTRT2")) {
                eqpId = "SIM-HTRT2";
            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            String methodName = "findLFANDHTRTParam";
            MesResult result = mesLotTrackService.findParam(eqpId, param, opId, lotNo, "");
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result15", "MesLotTrackController.findLFANDHTRTParam", jo.toString(), lotNo, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error15", "MesLotTrackController.findLFANDHTRTParam", "有异常", lotNo, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    //查找APJ-TRM设备参数，从产量日志和配方日志中获取
    @RequestMapping(value = "/findTrmParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findTrmParam(Model model, @PathVariable String eqpId, @RequestParam String opId,
                               HttpServletRequest request, HttpServletResponse response) {
        log.info("findTrmParam :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param16", "MesLotTrackController.findTrmParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.contains("TRM")) {

            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            String methodName = "FIND_TRM_PARAM";
            MesResult result = mesLotTrackService.findApjParam(eqpId, methodName, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result16", "MesLotTrackController.findViParam", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error16", "MesLotTrackController.findViParam", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    //查找DM-AT设备参数，从产量日志和配方日志中获取
    @RequestMapping(value = "/findAtParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findAtParam(Model model, @PathVariable String eqpId, @RequestParam String opId,
                               HttpServletRequest request, HttpServletResponse response) {
        log.info("findAtParam :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param17", "MesLotTrackController.findAtParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.contains("AT")) {
                eqpId = "DM-AT1";
            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!: " + eqpId;
            }
            String methodName = "FIND_AT_PARAM";
            MesResult result = mesLotTrackService.findApjParam(eqpId, methodName, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result17", "MesLotTrackController.findAtParam", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error17", "MesLotTrackController.findAtParamfindAtParamfindAtParam", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }


    //36916087020DM____0507A5002915J.SIM6812M(E)D-URA_F2971_
    @RequestMapping(value = "/dsktrackin2/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String dskTrackin2(Model model, @PathVariable String eqpId, @RequestParam String trackinfo, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\",\"trackinfo\":\"" + trackinfo + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param6", "MesLotTrackController.dskTrackin2", eventDesc, trackinfo, "wangdong");//日志记录参数
            if (trackinfo.length() < 30) {
                return "trackinfo too short（过账信息不足！）";
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1].trim();
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");
            String lotNo = lotNos[lotNos.length - 1].substring(0, 5);
            String orderNo = lotNos[0].substring(0, 8);
            String productionNo = lotNos[lotNos.length - 1].substring(5, 12); //5002915
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.trackin(eqpId, productionNo, productionName, orderNo, lotNo, "", opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.dskTrackin2", jo.toString(), trackinfo, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return "Y";
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.dskTrackin2", "有异常", trackinfo, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/dsktrackout/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String dmTrackout(Model model, @PathVariable String eqpId, @RequestParam String trackinfo, @RequestParam String yield, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        //36916087020DM____0507A5002915J.SIM6812M(E)D-URA_F2971_
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\",\"trackinfo\":\"" + trackinfo + "\",\"yield\":\"" + yield + "\"}";//日志记录参数
        fabLogService.info(eqpId, "Param6", "MesLotTrackController.dmTrackout", eventDesc, trackinfo, "wangdong");//日志记录参数
        try {
            if (trackinfo.length() < 30) {
                return "trackinfo too short（过账信息不足！）";
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1].trim();
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");
            String productionNo = lotNos[0].substring(0, 7); //5002915
            String lotNo = lotNos[0].substring(7, 12); //0702D
            String orderNo = lotNos[1]; //37368342


            //对当前批次进行判断，若批次结束时间过快，阻止操做
            String eqpId1 = eqpId;
            if (eqpId1.contains("WB")) {
                eqpId1 = eqpId + "A";
            } else if (eqpId1.contains("DM")) {
                eqpId1 = "SIM-DM1";
            } else if (eqpId.contains("LF1")) {
                eqpId1 = "SIM-LF1";
            } else if (eqpId.contains("LF2")) {
                eqpId1 = "SIM-LF2";
            }
            //判断批次数据入账是否符合逻辑
            MesLotTrack nowLotTrack = mesLotTrackService.findLotTrack(eqpId1, lotNo, productionNo);
            if (nowLotTrack == null) {
                log.error("nowLotTrack 为空" + eqpId1 + "  " + lotNo + "  " + productionNo);
                return "lot:" + lotNo + " is not trackin ,please trackin first!";
            }
            Date nowTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowLotTrack.getStartTime());
            calendar.add(Calendar.MINUTE, +5);
            if (nowTime.before(calendar.getTime()) && !eqpId.contains("SIM-GW") && !eqpId.contains("SIM-OVEN")) {
                log.error("操做人员误操作，不允许提前结束批次" + lotNo);
                return "Warning : " + lotNo + " lot Working too short! If it is not misoperation , please contact the administrator（不允许提前结束批次，最短时间五分钟）";
            }
            MesResult result = mesLotTrackService.trackout(eqpId, productionNo, productionName, orderNo, lotNo, yield, "", opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.dmTrackout", jo.toString(), trackinfo, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return "Y";
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.dmTrackout", "有异常", trackinfo, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/dskOventrackout/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String dskOventrackout(Model model, @PathVariable String eqpId, @RequestParam String trackinfo1, @RequestParam String trackinfo2, @RequestParam String yield1, @RequestParam String yield2, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        //36916087020DM____0507A5002915J.SIM6812M(E)D-URA_F2971_
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\",\"trackinfo1\":\"" + trackinfo1 + "\",\"trackinfo2\":\"" + trackinfo2 + "\",\"yield1\":\"" + yield1 + "\",\"yield2\":\"" + yield2 + "\"}";//日志记录参数
        fabLogService.info(eqpId, "Param6", "MesLotTrackController.dskOventrackout", eventDesc, trackinfo1, "wangdong");//日志记录参数
        try {
            if (trackinfo1.length() < 30) {
                return "trackinfo too short（过账信息不足！）";
            }
            String[] trackinfos = trackinfo1.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1].trim();
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");
            String productionNo = lotNos[0].substring(0, 7); //5002915
            String lotNo = lotNos[0].substring(7, 12); //0702D
            String orderNo = lotNos[1]; //37368342
            if (trackinfo2.length() > 30) {
                String[] trackinfos2 = trackinfo2.split("\\.");
                String lotorder2 = trackinfos2[0];
                String[] lotNos2 = lotorder2.split("_");
                String lotNo2 = lotNos2[0].substring(7, 12);
                lotNo = lotNo + "|" + lotNo2;
                yield1 = Integer.parseInt(yield1) + Integer.parseInt(yield2) + "";
            }

            //对当前批次进行判断，若批次结束时间过快，阻止操做
            String eqpId1 = eqpId;
            if (eqpId.contains("SIM-OVEN1")) {
                eqpId1 = "SIM-OVEN1";
            } else if (eqpId.contains("SIM-OVEN2")) {
                eqpId1 = "SIM-OVEN2";
            }
            //判断批次数据入账是否符合逻辑
            MesLotTrack nowLotTrack = mesLotTrackService.findLotTrack(eqpId1, lotNo, productionNo);
            if (nowLotTrack == null) {
                log.error("nowLotTrack 为空" + eqpId1 + "  " + lotNo + "  " + productionNo);
                return "lot:" + lotNo + " is not trackin ,please trackin first!";
            }
            Date nowTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowLotTrack.getStartTime());
            calendar.add(Calendar.MINUTE, +5);
            if (nowTime.before(calendar.getTime()) && !eqpId.contains("SIM-GW")) {
                log.error("操做人员误操作，不允许提前结束批次" + lotNo);
                return "Warning : " + lotNo + " lot Working too short! If it is not misoperation , please contact the administrator（不允许提前结束批次，最短时间五分钟）";
            }

            MesResult result = mesLotTrackService.trackout(eqpId, productionNo, productionName, orderNo, lotNo, yield1, "", opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.dskOventrackout", jo.toString(), trackinfo1 + "/" + trackinfo2, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return "Y";
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.dskOventrackout", "有异常", trackinfo1 + "/" + trackinfo2, "wangdong");//日志记录
            return e.getMessage();
        }
    }


    @RequestMapping(value = "/dskapjtrackout/{subLineNo}", method = {RequestMethod.GET, RequestMethod.POST})
    public String apjTrackout(Model model, @PathVariable String subLineNo, @RequestParam String trackinfo, @RequestParam String yield,@RequestParam String materialInfo,  @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        //36916087020DM____0507A5002915J.SIM6812M(E)D-URA_F2971_
        String eventDesc = "{\"subLineNo\":\"" + subLineNo + "\",\"opId\":\"" + opId + "\",\"trackinfo\":\"" + trackinfo + "\",\"yield\":\"" + yield+"\",\"materialInfo\":\"" + materialInfo + "\"}";//日志记录参数
        fabLogService.info(subLineNo, "Param6", "MesLotTrackController.apjTrackout", eventDesc, trackinfo, "wangdong");//日志记录参数
        try {
            if (trackinfo.length() < 30) {
                return "trackinfo too short（过账信息不足！）";
            }
            if(materialInfo.length()==0 || "".equals(materialInfo) || materialInfo == null){
                return "Material infomation must be provided(必须提交原料物料信息)";
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1].trim();
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");
            String productionNo = lotNos[0].substring(0, 7); //5002915
            String lotNo = lotNos[0].substring(7, 12); //0702D
            String orderNo = lotNos[1]; //37368342

            //对当前批次进行判断，若批次结束时间过快，阻止操做
            String eqpId1 = subLineNo;
            if (subLineNo.equals("IGBT")) {
                eqpId1 = "DM-IGBT-SORT1";
            } else if (eqpId1.equals("FRD")) {
                eqpId1 = "DM-FRD-SORT1";
            } else if (eqpId1.equals("IGBTYJH")) {
                eqpId1 = "DM-IGBT-SORT3";
            } else if (eqpId1.equals("FRDYJH")) {
                eqpId1 = "DM-FRD-SORT3";
            } else if (eqpId1.equals("RY1")) {
                eqpId1 = "DM-HB1-SORT2";
            } else if (eqpId1.equals("RY2")) {
                eqpId1 = "DM-HB2-SORT2";
            } else if (eqpId1.equals("ZJ")) {
                eqpId1 = "DM-VI1";
            } else if (eqpId1.equals("TOP")) {
                eqpId1 = "DM-DBCT-SORT2";
            } else if (eqpId1.equals("BOTTOM")) {
                eqpId1 = "DM-DBCB-SORT2";
            } else if (eqpId1.equals("TRM")) {
                eqpId1 = "DM-TRM1";
            } else if (eqpId1.equals("SAT")) {
                eqpId1 = "DM-SAT1";
            } else if (eqpId1.equals("AT") && !eqpId1.contains("S")) {
                eqpId1 = "DM-AT1";
            } else if (eqpId1.equals("LF")) {
                eqpId1 = "DM-LF1";
            } else if (eqpId1.equals("HTRT")) {
                eqpId1 = "DM-HTRT1";
            } else if (eqpId1.equals("XRAY")) {
                eqpId1 = "DM-XRAY1";
            } else if (eqpId1.equals("JET")) {
                eqpId1 = "DM-CLEAN-JET1";
            } else if (eqpId1.equals("US")) {
                eqpId1 = "DM-CLEAN-US1";
            } else if (eqpId1.equals("OVEN1")) {
                eqpId1 = "DM-OVEN1";
            } else if (eqpId1.equals("OVEN2")) {
                eqpId1 = "DM-OVEN2";
            }
            //判断批次数据入账是否符合逻辑
            MesLotTrack nowLotTrack = mesLotTrackService.findLotTrack(eqpId1, lotNo, productionNo);
            if (nowLotTrack == null) {
                log.error("nowLotTrack 为空" + eqpId1 + "  " + lotNo + "  " + productionNo);
                return "lot:" + lotNo + " is not trackin ,please trackin first!";
            }
            Date nowTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowLotTrack.getStartTime());
            calendar.add(Calendar.MINUTE, +5);
            if (nowTime.before(calendar.getTime())) {
                log.error("操做人员误操作，不允许提前结束批次" + lotNo);
                return "Warning : " + lotNo + " lot Working too short! If it is not misoperation , please contact the administrator（不允许提前结束批次，最短时间五分钟）";
            }

            MesResult result = mesLotTrackService.apjTrackout(subLineNo, productionNo, productionName, orderNo, lotNo, yield, "", opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(subLineNo, "Result6", "MesLotTrackController.apjTrackout", jo.toString(), trackinfo, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                //将物料信息存入数据库
                List<MesLotMaterialInfo> materialInfosList = new ArrayList<>();
                try {
                    String materialInfos[] = materialInfo.split("_");
                    MesLotMaterial mesLotMaterial =  mesLotMaterialService.selectMaterialData(eqpId1,lotNo);
                    if(mesLotMaterial == null || "".equals(mesLotMaterial.getEqpId()) || mesLotMaterial.getEqpId() ==null){
                        mesLotMaterial = new MesLotMaterial();
                        mesLotMaterial.setEqpId(eqpId1);
                        mesLotMaterial.setLotNo(lotNo);
                        mesLotMaterial.setCreateBy(opId);
                        mesLotMaterial.setCreateDate(new Date());
                        mesLotMaterialService.insert(mesLotMaterial);
                    }
                    materialInfosList = new ArrayList<>();
                    for (String info : materialInfos) {
                        String materialName = info.split(",")[0];
                        String materiallotNo = info.split(",")[1];
                        MesLotMaterialInfo mesLotMaterialInfo = new MesLotMaterialInfo();
                        mesLotMaterialInfo.setMaterialId(mesLotMaterial.getId());
                        mesLotMaterialInfo.setCreateBy(opId);
                        mesLotMaterialInfo.setMaterialName(materialName);
                        mesLotMaterialInfo.setLotNo(materiallotNo);
                        mesLotMaterialInfo.setCreateDate(new Date());
                        materialInfosList.add(mesLotMaterialInfo);
                    }
                } catch (Exception e) {
                    log.error("mesLotMaterial 解析出错  主表设备："+eqpId1 + "  主表批量："+lotNo  +"  materialInfo:"+materialInfo,e);
                    e.printStackTrace();
                }
                try {
                    if(!mesLotMaterialInfoService.insertBatch(materialInfosList,10)){
                        log.error("materialInfosList 插入失败");
                    }
                } catch (Exception e) {
                    log.error("materialInfosList 插入出错  主表设备："+eqpId1 + "  主表批量："+lotNo,e);
                    e.printStackTrace();
                }
                return "Y";
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(subLineNo, "Error6", "MesLotTrackController.apjTrackout", "有异常", trackinfo, "wangdong");//日志记录
            log.error("出账错误！", e);
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/dsktrackout2/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String dmTrackout2(Model model, @PathVariable String eqpId, @RequestParam String trackinfo, @RequestParam String yield, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        //36916087020DM____0507A5002915J.SIM6812M(E)D-URA_F2971_
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\",\"trackinfo\":\"" + trackinfo + "\",\"yield\":\"" + yield + "\"}";//日志记录参数
        fabLogService.info(eqpId, "Param6", "MesLotTrackController.dmTrackout2", eventDesc, trackinfo, "wangdong");//日志记录参数
        try {
            if (trackinfo.length() < 30) {
                return "trackinfo too short";
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1].trim();
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");
            String lotNo = lotNos[lotNos.length - 1].substring(0, 5);
            String orderNo = lotNos[0].substring(0, 8);
            String productionNo = lotNos[lotNos.length - 1].substring(5, 12); //5002915
            MesResult result = mesLotTrackService.trackout(eqpId, productionNo, productionName, orderNo, lotNo, yield, "", opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.dsktrackout2", jo.toString(), trackinfo, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return "Y";
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.dsktrackout2", "有异常", trackinfo, "wangdong");//日志记录
            return e.getMessage();
        }
    }

    //允许重复提交推理拉力数据，重复提交时保留最新数据
    @RequestMapping(value = "/thrustAndPullDataUpload/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String thrustAndPullDataUpload(@PathVariable String eqpId, @RequestParam String trackinfo, @RequestParam String thrust, @RequestParam String pull, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        //36916087020DM____0507A5002915J.SIM6812M(E)D-URA_F2971_
        String eventDesc = "{\"thrust\":\"" + thrust + "\",\"pull\":\"" + pull + "\",\"opId\":\"" + opId + "\",\"trackinfo\":\"" + trackinfo + "\"}";//日志记录参数
        fabLogService.info(eqpId, "Param6", "MesLotTrackController.thrustAndPullDataUpload", eventDesc, trackinfo, "wangdong");//日志记录参数
        try {
            if (trackinfo.length() < 30) {
                return "trackinfo too short";
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1].trim();
            productionName = productionName.replace("_", " ").trim();
            String[] lotNos = lotorder.split("_");
            String wbNo = eqpId;
            String productionNo = lotNos[0].substring(0, 7); //5002915
            String lotNo = lotNos[0].substring(7, 12); //0702D
            String orderNo = lotNos[1]; //37368342
            log.info("lotNo:" + lotNo + "  productionNo:" + productionNo + "  productionName:" + productionName + "  thrust:" + thrust + "  pull:" + pull);
            MsMeasureThrust msMeasureThrust = new MsMeasureThrust();
            msMeasureThrust.setEqpId(wbNo);
            msMeasureThrust.setLineNo(productionName.split("-")[0]);
            msMeasureThrust.setLotNo(lotNo);
            msMeasureThrust.setProductionName(productionName);
            msMeasureThrust.setProductionNo(productionNo);
            msMeasureThrust.setPull(pull);
            msMeasureThrust.setThrust(thrust);
            msMeasureThrust.setCreateBy("GXJ");
            msMeasureThrust.setOpId(opId);
            MsMeasureThrust msMeasureThrust1 = null;
            msMeasureThrust1 = iMsMeasureThrustService.findDataByLotNo(lotNo,productionName);
            if(msMeasureThrust1 != null){
                msMeasureThrust1.setPull(pull);
                msMeasureThrust1.setThrust(thrust);
                msMeasureThrust1.setUpdateBy(opId);
                msMeasureThrust1.setUpdateDate(new Date());
                iMsMeasureThrustService.updateById(msMeasureThrust1);
            }else {
                iMsMeasureThrustService.insert(msMeasureThrust);
            }
            fabLogService.info(eqpId, "Result6", "MesLotTrackController.thrustAndPullDataUpload", "数据上传成功", trackinfo, "wangdong");//日志记录
            return "Y";
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.thrustAndPullDataUpload", "数据解析或保存异常" + e, trackinfo, "wangdong");//日志记录
            log.error("MesLotTrackController.thrustAndPullDataUpload.error", e);
            return e.getMessage();
        }
    }

    //@RequestMapping(value = "/trackout/{eqpId}/{lotNo}", method = { RequestMethod.GET, RequestMethod.POST })
    //public MesResult trackout(Model model, @PathVariable String eqpId, @PathVariable String lotNo, @RequestParam String recipeCode, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
    //    return mesLotTrackService.trackOut( eqpId,   lotNo,   recipeCode,   opId);
    //
    //}

    //@GetMapping("export")
    public Response export(HttpServletRequest request) {
        Response response = Response.ok("导出成功");
        try {
            TemplateExportParams params = new TemplateExportParams(
                    "");
            //加入条件
            EntityWrapper<MesLotTrack> entityWrapper = new EntityWrapper<>(MesLotTrack.class);
            // 子查询
            String delFlag = request.getParameter("delFlag");
            if (!StringUtil.isEmpty(delFlag)) {
                entityWrapper.eq("del_flag", delFlag);
            }
            Page pageBean = commonService.selectPage(PageRequest.getPage(), entityWrapper);
            String title = "过账记录";
            Workbook book = ExcelExportUtil.exportExcel(new ExportParams(
                    title, title, ExcelType.XSSF), MesLotTrack.class, pageBean.getRecords());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            book.write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtil.bytesToHexString2(bytes);
            title = title + "-" + DateUtil.getDateTime();
            response.put("bytes", bytesRes);
            response.put("title", title);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(999998, "导出失败");
        }
        return response;
    }

    //2020-11-30
//    @RequestMapping(value = "/chartKongDong", method = {RequestMethod.GET, RequestMethod.POST})
//    public Response chartKongDong(@RequestParam String productionNo,@RequestParam String lineNo,
//                                  @RequestParam String startDate, @RequestParam String endDate) {
//        log.info("MesLotTrackController_chartKongDong : productionNo," + productionNo);
//        Map<String, Object> data = mesLotTrackService.chartKongDong(lineNo, productionNo, startDate, endDate);
//        Response rs = Response.ok();
//        rs.put("kongdong", data);
//        return rs;
//    }

    @RequestMapping(value = "getAllProName", method = {RequestMethod.GET, RequestMethod.POST})
    public Response getAllProName(@RequestParam String productionNo) {
        Response rs = Response.ok();
        rs.putList("allProName", mesLotTrackService.findAllProName(productionNo));
        return rs;
    }

    @RequestMapping(value = "/kongDongBar", method = {RequestMethod.GET, RequestMethod.POST})
    public Response kongDongBar(@RequestParam String productionNo, @RequestParam String lotNo,
                                @RequestParam String startDate, @RequestParam String endDate) {
        log.info("MesLotTrackController_chartKongDong : productionNo," + productionNo);
        List data = mesLotTrackService.kongDongBar(lotNo, productionNo, startDate, endDate);
        List config = mesLotTrackService.getkongDongConfig(productionNo.replace("J.", ""));
        Response rs = Response.ok();
        rs.putList("kongdong", data);
        rs.putList("config", config);
        return rs;
    }

    @RequestMapping(value = "/getKeyence", method = {RequestMethod.GET, RequestMethod.POST})
    public String getKeyence(@RequestParam String mode, @RequestParam String lotNo, @RequestParam String production
    ) throws IOException {
        fabLogService.info("getKeyence", "", "getKeyence", mode + "+" + production, lotNo, "wangdong");//日志记录
        Response rs = new Response();
        String paramStr = production.substring(3, 8);
        String result = mesLotTrackService.getKeyence(mode, lotNo, paramStr);
        fabLogService.info("getKeyence", "", "getKeyence.result", result, lotNo, "wangdong");//日志记录
        if ("".equals(result)) {
            return "data not fond!";
        }
        return result;
    }

    @RequestMapping(value = "/findGI", method = {RequestMethod.GET, RequestMethod.POST})
    public String findGI(@RequestParam String category, @RequestParam String lotNo, @RequestParam String production
    ) throws IOException {
        Response rs = new Response();
        String result = null;
        if (category.equals("5GI")) {
            fabLogService.info("", "findGI", "find5GI", production, lotNo, "jiafuxing");//日志记录
            result = mesLotTrackService.find5GI(lotNo, production);
        } else if (category.equals("6GI")) {
            fabLogService.info("", "findGI", "find6GI", production, lotNo, "jiafuxing");//日志记录
            result = mesLotTrackService.find6GI(lotNo, production);
        }
        fabLogService.info("", "", "findGI.result", result, lotNo, "jiafuxing");//日志记录
        return result;
    }

    @RequestMapping(value = "/findSX", method = {RequestMethod.GET, RequestMethod.POST})
    public String findSX(@RequestParam String production, @RequestParam String lotNo, @RequestParam String flag
    ) throws IOException {
        fabLogService.info("findSX", "Param", "MesLotTrackController.findSX", "production参数:" + production + ",lotNo参数:" + lotNo + ",flag参数:" + flag, lotNo, "jiafuxing");//日志记录参数
        return mesLotTrackService.findSX(production, lotNo, flag);

    }

    //查找DM分离检查参数
    @RequestMapping(value = "/findAPJ/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findApjParam(Model model, @PathVariable String eqpId, @RequestParam String trackinfo,@RequestParam String chipIds,@RequestParam String type, @RequestParam String opId,
                               HttpServletRequest request, HttpServletResponse response) {
        log.info("findAPJParam :  {}, {}", opId, eqpId);
        if (trackinfo.length() < 30) {
            return "trackinfo too short";
        }
        String chipIdColumn[] = chipIds.split(",");
        if(chipIdColumn.length!=5){
            return "chipIds number error(制品码数量错误，必须传输5个制品码！)";
        }
        for (String chIpID : chipIdColumn) {
            if("".equals(chIpID)){
                return "chIpId must be not null(制品码不得为空！)";
            }
        }
        String[] trackinfos = trackinfo.split("\\.");
        String lotorder = trackinfos[0];
        String productionName = trackinfos[1].trim();
        productionName = productionName.replace("_", " ");
        String[] lotNos = lotorder.split("_");

        String productionNo = lotNos[0].substring(0, 7); //5002915
        String lotNo = lotNos[0].substring(7, 12); //0702D
        String orderNo = lotNos[1]; //37368342
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"lotNo\":\"" + lotNo+ "\",\"productionNo\":\"" + productionNo + "\",\"productionName\":\"" + productionName+ "\",\"chipIds\":\"" + chipIds+ "\",\"opId\":\"" + opId+ "\"}";//日志记录参数

        try {
            fabLogService.info(eqpId, "findAPJ", "MesLotTrackController.findAPJ", eventDesc, lotNo, "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.equals("APJLFTest")) {
                eqpId = "DM-LF1";
            }else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            String methodName = "FIND_APJ_LF_PARAM";
            MesResult result = mesLotTrackService.findApjParam(eqpId, methodName, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "findAPJ", "MesLotTrackController.findAPJ", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                //将APJ分离检测数据入库
                String datas = result.getContent().toString();
                List<MeasureDm> measureDmList = null;
                try {
                    String columns[] = datas.split(",");
                    measureDmList = new ArrayList<>();
                    for (int i = 0; i < 70; i=i+14) {
                        String[] paramsValues = Arrays.copyOfRange(columns, i, i+13);
                        String chipId = chipIdColumn[(i%13)];
                        MeasureDm measureDm = new MeasureDm();
                        measureDm.setChipId(chipId);
                        measureDm.setCreateDate(new Date());
                        measureDm.setLineNo("DM");
                        measureDm.setProductionNo(productionName);
                        measureDm.setLotNo(lotNo);
                        measureDm.setMeasureCounter((i%13+1)+"");
                        measureDm.setMeaDate(DateUtil.parseDate(columns[71],"yyyy/M/d H:mm"));
                        measureDm.setMeasureName(opId);
                        measureDm.setMeasureJudgment("Y");
                        measureDm.setMeasureType(type);
                        measureDm.setF0_1(Double.parseDouble(paramsValues[0]));
                        measureDm.setF0_2(Double.parseDouble(paramsValues[1]));
                        measureDm.setF0_3(Double.parseDouble(paramsValues[2]));
                        measureDm.setF1_1(Double.parseDouble(paramsValues[3]));
                        measureDm.setF1_2(Double.parseDouble(paramsValues[4]));
                        measureDm.setF1_3(Double.parseDouble(paramsValues[5]));
                        measureDm.setF3_1(Double.parseDouble(paramsValues[6]));
                        measureDm.setF3_2(Double.parseDouble(paramsValues[7]));
                        measureDm.setF3_3(Double.parseDouble(paramsValues[8]));
                        measureDm.setF3_4(Double.parseDouble(paramsValues[9]));
                        measureDm.setF3_5(Double.parseDouble(paramsValues[10]));
                        measureDm.setF3_6(Double.parseDouble(paramsValues[11]));
                        measureDm.setF3_7(Double.parseDouble(paramsValues[12]));
                        measureDm.setF3_8(Double.parseDouble(paramsValues[13]));
                        measureDmList.add(measureDm);
                    }
                } catch (Exception e) {
                    log.error("DM 分离检查数据解析出错",e);
                    e.printStackTrace();
                }
                if(measureDmList.size()>0){
                    try {
                        measureDmService.insertBatch(measureDmList,5);
                        log.info("DM 分离检查数据导入数据库成功");
                    } catch (Exception e) {
                        log.error("DM 分离检查数据导入数据库失败",e);
                        e.printStackTrace();
                    }
                }
                // 0   13
                //14   27
                //28   41
                //42   55
                //56   69
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "findAPJ", "MesLotTrackController.findAPJ", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }

}



