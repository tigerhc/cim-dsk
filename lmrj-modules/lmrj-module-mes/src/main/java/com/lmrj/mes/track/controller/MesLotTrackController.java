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
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;


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

    //@RequestMapping(value = "/trackin/{eqpId}/{lotNo}", method = { RequestMethod.GET, RequestMethod.POST })
    //public MesResult trackin(Model model, @PathVariable String eqpId, @PathVariable String lotNo, @RequestParam String recipeCode, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
    //    return mesLotTrackService.trackIn( eqpId,   lotNo,   recipeCode,   opId);
    //}

    //50029150702D 37368342             037368342ED   J.SIM6812M(E)D-URA F2971
    @RequestMapping(value = "/dsktrackin/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String dskTrackin2(Model model, @PathVariable String eqpId, @RequestParam String trackinfo, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        log.info("dsktrackin :  {}", trackinfo);
        try{
            if(trackinfo.length()< 30){
                return "trackinfo too short";
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1];
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");

            String  productionNo  = lotNos[0].substring(0, 7); //5002915
            String  lotNo = lotNos[0].substring(7, 12); //0702D
            String  orderNo= lotNos[1]; //37368342
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.trackin4DSK(eqpId, productionName, productionNo, orderNo, lotNo, "", opId);
            if ("Y".equals(result.flag)) {
                return "Y";
            } else {
                return result.msg;
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/findRecipeName/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findRecipeName(Model model, @PathVariable String eqpId, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        log.info("findRecipeName :  {}", opId);
        try{
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.findRecipeName(eqpId, opId);
            if ("Y".equals(result.flag)) {
                return result.getContent().toString();
            } else {
                return result.msg;
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/findTemp/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findTemp(Model model, @PathVariable String eqpId, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        log.info("findTemp :  {}", opId);
        try{
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.findTemp(eqpId, opId);
            if ("Y".equals(result.flag)) {
                return result.getContent().toString();
            } else {
                return result.msg;
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/findParam/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findParam(Model model, @PathVariable String eqpId, @RequestParam String param, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        log.info("findTemp :  {}", opId);
        try{
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.findParam(eqpId, param, opId);
            if ("Y".equals(result.flag)) {
                return result.getContent().toString();
            } else {
                return result.msg;
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    //36916087020DM____0507A5002915J.SIM6812M(E)D-URA_F2971_
    @RequestMapping(value = "/dsktrackin2/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String dskTrackin(Model model, @PathVariable String eqpId, @RequestParam String trackinfo, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        try{
            if(trackinfo.length()< 30){
                return "trackinfo too short";
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1];
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");
            String lotNo = lotNos[lotNos.length-1].substring(0, 5);
            String orderNo = lotNos[0].substring(0, 8);
            String productionNo = lotNos[lotNos.length-1].substring(5, 12); //5002915
            //String eqpId ="SIM-DM1";
            MesResult result = mesLotTrackService.trackin4DSK(eqpId, productionName, productionNo, orderNo, lotNo, "", opId);
            if ("Y".equals(result.flag)) {
                return "Y";
            } else {
                return result.msg;
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/dsktrackout/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public String dmTrackout(Model model, @PathVariable String eqpId, @RequestParam String trackinfo, @RequestParam String yield, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        //36916087020DM____0507A5002915J.SIM6812M(E)D-URA_F2971_
        if(trackinfo.length()< 30){
            return "trackinfo too short";
        }
        try{
            if(trackinfo.length()< 30){
                return "trackinfo too short";
            }
            String[] trackinfos = trackinfo.split("\\.");
            String lotorder = trackinfos[0];
            String productionName = trackinfos[1];
            productionName = productionName.replace("_", " ");
            String[] lotNos = lotorder.split("_");
            String lotNo = lotNos[lotNos.length-1].substring(0, 5);
            String orderNo = lotNos[0].substring(0, 8);
            String productionNo = lotNos[lotNos.length-1].substring(5, 12); //5002915
            MesResult result = mesLotTrackService.trackout4DSK(eqpId, productionName, productionNo, orderNo, lotNo, yield, "", opId);
            if ("Y".equals(result.flag)) {
                return "Y";
            } else {
                return result.msg;
            }

        }catch (Exception e){
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
            Page pageBean = commonService.selectPage(PageRequest.getPage(),entityWrapper);
            String title = "过账记录";
            Workbook book = ExcelExportUtil.exportExcel(new ExportParams(
                    title, title, ExcelType.XSSF), MesLotTrack.class, pageBean.getRecords());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            book.write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtil.bytesToHexString2(bytes);
            title = title+ "-" + DateUtil.getDateTime();
            response.put("bytes",bytesRes);
            response.put("title",title);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(999998,"导出失败");
        }
        return response;
    }
}
