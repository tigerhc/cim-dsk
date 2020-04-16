package com.lmrj.edc.ams.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.cim.utils.PageRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;


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
    @GetMapping("export")
    public Response export(HttpServletRequest request) {
        Response response = Response.ok("导出成功");
        try {
            TemplateExportParams params = new TemplateExportParams(
                    "");
            //加入条件
            EntityWrapper<EdcAmsRecord> entityWrapper = new EntityWrapper<>(EdcAmsRecord.class);
            // 子查询
            String eqpId = request.getParameter("eqpId");
            if (!StringUtil.isEmpty(eqpId)) {
                entityWrapper.eq("eqp_id", eqpId);
            }
            // 子查询
            String eventId = request.getParameter("eventId");
            if (!StringUtil.isEmpty(eventId)) {
                entityWrapper.eq("event_id", eventId);
            }
            // 子查询
            String startDate = request.getParameter("startDate");
            if (!StringUtil.isEmpty(startDate)) {
                entityWrapper.ge("start_date", startDate);
            }
            // 子查询
            String endDate = request.getParameter("endDate");
            if (!StringUtil.isEmpty(endDate)) {
                entityWrapper.le("end_date", endDate);
            }
            Page pageBean = iEdcAmsRecordService.selectPage(PageRequest.getPage(),entityWrapper);
            String title = "报警信息";
            Workbook book = ExcelExportUtil.exportExcel(new ExportParams(
                    title, title, ExcelType.XSSF), EdcAmsRecord.class, pageBean.getRecords());
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
