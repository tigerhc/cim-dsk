package com.lmrj.edc.ams.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.ams.controller
 * @title: edc_ams_record控制器
 * @description: edc_ams_record控制器
 * @author: 张伟江
 * @date: 2019-06-14 15:51:23
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcamsrecord")
@ViewPrefix("edc/edcamsrecord")
@RequiresPathPermission("edc:edcamsrecord")
@LogAspectj(title = "edc_ams_record")
public class EdcAmsRecordController extends BaseCRUDController<EdcAmsRecord> {

    @Autowired
    private IEdcAmsRecordService iEdcAmsRecordService;

    @RequestMapping("/selectAlarmCountByLine")
    public Response selectAlarmCountByLine(@RequestParam String lineNo, @RequestParam String beginTime, @RequestParam String endTime, @RequestParam String stationCode, HttpServletRequest request, HttpServletResponse response) {
        beginTime = beginTime+" 00:00:00";
        endTime = endTime+" 23:59:59";
        Response res = new Response();
        if (StringUtil.isEmpty(stationCode)) {
            List<Map> maps = iEdcAmsRecordService.selectAlarmCountByLine(beginTime, endTime, lineNo);
            res.put("record", maps);
        } else {
            List<Map> maps = iEdcAmsRecordService.selectAlarmCountByLineOther(stationCode, beginTime, endTime, lineNo);
            res.put("record", maps);
        }
        return res;
    }

    @RequestMapping("/selectAlarmCountByEqp")
    public Response selectAlarmCountByEqp(@RequestParam String eqpId, @RequestParam String beginTime, @RequestParam String endTime, HttpServletRequest request, HttpServletResponse response) {
        Response res = new Response();
        List<Map> maps = iEdcAmsRecordService.selectAlarmCountByEqp(beginTime, endTime, eqpId);
        res.put("record", maps);
        return res;
    }

    @RequestMapping("/selectAlarmCountByStation")
    public Response selectAlarmCountByStation(@RequestParam String lineNo, @RequestParam String stationCode, @RequestParam String beginTime, @RequestParam String endTime, HttpServletRequest request, HttpServletResponse response) {
        Response res = new Response();
        List<Map> maps = iEdcAmsRecordService.selectAlarmCountByStation(beginTime, endTime, lineNo, stationCode);
        res.put("record", maps);
        return res;
    }

    //@GetMapping("export")
    //public Response export(HttpServletRequest request) {
    //    Response response = Response.ok("导出成功");
    //    try {
    //        TemplateExportParams params = new TemplateExportParams(
    //                "");
    //        //加入条件
    //        EntityWrapper<EdcAmsRecord> entityWrapper = new EntityWrapper<>(EdcAmsRecord.class);
    //        // 子查询
    //        String eqpId = request.getParameter("eqpId");
    //        if (!StringUtil.isEmpty(eqpId)) {
    //            entityWrapper.eq("eqp_id", eqpId);
    //        }
    //        // 子查询
    //        String eventId = request.getParameter("eventId");
    //        if (!StringUtil.isEmpty(eventId)) {
    //            entityWrapper.eq("event_id", eventId);
    //        }
    //        // 子查询
    //        String startDate = request.getParameter("startDate");
    //        if (!StringUtil.isEmpty(startDate)) {
    //            entityWrapper.ge("start_date", startDate);
    //        }
    //        // 子查询
    //        String endDate = request.getParameter("endDate");
    //        if (!StringUtil.isEmpty(endDate)) {
    //            entityWrapper.le("end_date", endDate);
    //        }
    //        Page pageBean = iEdcAmsRecordService.selectPage(PageRequest.getPage(),entityWrapper);
    //        String title = "报警信息";
    //        Workbook book = ExcelExportUtil.exportExcel(new ExportParams(
    //                title, title, ExcelType.XSSF), EdcAmsRecord.class, pageBean.getRecords());
    //        ByteArrayOutputStream bos = new ByteArrayOutputStream();
    //        book.write(bos);
    //        byte[] bytes = bos.toByteArray();
    //        String bytesRes = StringUtil.bytesToHexString2(bytes);
    //        title = title+ "-" + DateUtil.getDateTime();
    //        response.put("bytes",bytesRes);
    //        response.put("title",title);
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        return Response.error(999998,"导出失败");
    //    }
    //    return response;
    //}

}
