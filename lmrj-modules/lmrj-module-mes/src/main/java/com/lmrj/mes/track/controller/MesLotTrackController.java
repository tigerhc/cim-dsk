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
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
    IMesLotTrackService mesLotTrackService;
    @Autowired
    IFabLogService fabLogService;

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
            }
            if (eqpId1.contains("DM")) {
                eqpId1 = "SIM-DM1";
            }
            //判断批次数据入账是否符合逻辑
            MesLotTrack lastLotTrack = mesLotTrackService.findLotNo1(eqpId1, new Date());

            if (!lastLotTrack.getLotNo().equals(lotNo) && lastLotTrack.getEndTime() == null) {
                log.error("人员误操作记录，" + eqpId1 + ":" + lastLotTrack.getLotNo() + "批次未结束,无法对" + lotNo + "进行入账");
                return eqpId1 + "设备" + lastLotTrack.getLotNo() + " is not finished ! Please do track out first";
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
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.dskTrackin", "有异常", trackinfo, "wangdong");//日志记录
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
        log.info("findTemp :  {}, {}, {}, {}", opId, lotNo, productionNo, index);
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
            fabLogService.info(eqpId, "Param7", "MesLotTrackController.findParam", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.findPrinterParam(eqpId, opId);
            JSONObject jo = JSONObject.fromObject(result);//日志记录结果
            fabLogService.info(eqpId, "Result7", "MesLotTrackController.findPrinterParam", jo.toString(), eqpId, "wangdong");//日志记录
            if ("Y".equals(result.getFlag())) {
                return result.getContent().toString();
            } else {
                return result.getMsg();
            }
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error6", "MesLotTrackController.findPrinterParam", "有异常", eqpId, "wangdong");//日志记录
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


            //对当前批次进行判断，若批次结束时间过快，阻止操做
            String eqpId1 = eqpId;
            if (eqpId1.contains("WB")) {
                eqpId1 = eqpId + "A";
            }
            if (eqpId1.contains("DM")) {
                eqpId1 = "SIM-DM1";
            }
            //判断批次数据入账是否符合逻辑
            MesLotTrack nowLotTrack = mesLotTrackService.findLotTrack(eqpId1, lotNo, productionNo);
            Date nowTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowLotTrack.getStartTime());
            calendar.add(Calendar.MINUTE, +5);
            if (nowTime.before(calendar.getTime())) {
                log.error("操做人员误操作，提前结束批次" + lotNo);
                return "Warning : " + lotNo + " lot Working too short! If it is not misoperation , please contact the administrator";
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
        fabLogService.info("", "", "getKeyence", mode + "+" + production, lotNo, "wangdong");//日志记录
        Response rs = new Response();
        String paramStr = production.substring(3, 8);
        String result = mesLotTrackService.getKeyence(mode, lotNo, paramStr);
        fabLogService.info("", "", "getKeyence.result", result, lotNo, "wangdong");//日志记录
        return result;
    }

    @RequestMapping(value = "/findGI", method = {RequestMethod.GET, RequestMethod.POST})
    public String findGI(@RequestParam String category, @RequestParam String lotNo, @RequestParam String production
    ) throws IOException {
        Response rs = new Response();
        String result = null;
        if (category.equals("5GI")) {
            fabLogService.info("", "", "find5GI", production, lotNo, "jiafuxing");//日志记录
            result = mesLotTrackService.find5GI(lotNo, production);
        } else if (category.equals("6GI")) {
            fabLogService.info("", "", "find6GI", production, lotNo, "jiafuxing");//日志记录
            result = mesLotTrackService.find6GI(lotNo, production);
        }
        fabLogService.info("", "", "findGI.result", result, lotNo, "jiafuxing");//日志记录
        return result;
    }

    @RequestMapping(value = "/findSX", method = {RequestMethod.GET, RequestMethod.POST})
    public String findSX(@RequestParam String production ,@RequestParam String lotNo,@RequestParam String flag
    ) throws IOException {
        fabLogService.info("findSX", "Param", "MesLotTrackController.findSX", "production参数:"+production+",lotNo参数:"+lotNo+",flag参数:"+flag,lotNo , "jiafuxing");//日志记录参数
        return mesLotTrackService.findSX(production,lotNo,flag);

    }

}



