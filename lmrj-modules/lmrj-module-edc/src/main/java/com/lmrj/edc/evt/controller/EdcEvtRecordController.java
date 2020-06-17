package com.lmrj.edc.evt.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.evt.controller
 * @title: edc_evt_record控制器
 * @description: edc_evt_record控制器
 * @author: 张伟江
 * @date: 2019-06-14 16:09:50
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcevtrecord")
@ViewPrefix("edc/edcevtrecord")
@RequiresPathPermission("edc:edcevtrecord")
@LogAspectj(title = "edc_evt_record")
public class EdcEvtRecordController extends BaseCRUDController<EdcEvtRecord> {
    @Autowired
    private IEdcEvtRecordService edcEvtRecordService;
    //@GetMapping("export")
    //public Response export(HttpServletRequest request) {
    //    Response response = Response.ok("导出成功");
    //    try {
    //        TemplateExportParams params = new TemplateExportParams(
    //                "");
    //        //加入条件
    //        EntityWrapper<EdcEvtRecord> entityWrapper = new EntityWrapper<>(EdcEvtRecord.class);
    //        // 子查询
    //        String eqpId = request.getParameter("eqpId");
    //        if (!StringUtil.isEmpty(eqpId)) {
    //            entityWrapper.eq("eqp_id", eqpId);
    //        }
    //        // 子查询
    //        String alarmCode = request.getParameter("alarmCode");
    //        if (!StringUtil.isEmpty(alarmCode)) {
    //            entityWrapper.eq("alarm_code", alarmCode);
    //        }
    //        // 子查询
    //        String startDate = request.getParameter("startDate");
    //        if (!StringUtil.isEmpty(startDate)) {
    //            entityWrapper.ge("start_date", startDate);
    //        }
    //        Page pageBean = edcEvtRecordService.selectPage(PageRequest.getPage(),entityWrapper);
    //        String title = "事件记录";
    //        Workbook book = ExcelExportUtil.exportExcel(new ExportParams(
    //                title, title, ExcelType.XSSF), EdcEvtRecord.class, pageBean.getRecords());
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

    //FileOutputStream fileOutputStream = new FileOutputStream("D:\\创建sheel页2.xlsx");
    //book.write(fileOutputStream);
    //fileOutputStream.close();
}
