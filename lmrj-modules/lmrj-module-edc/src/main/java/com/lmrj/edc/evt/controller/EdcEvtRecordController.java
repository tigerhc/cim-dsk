package com.lmrj.edc.evt.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import com.lmrj.util.mapper.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    @Value("${dsk.lineNo}")
    private String lineNo;
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

    @RequestMapping("selectEqpList")
    public Response tempEqpList(){
        Response rs = Response.ok();
        if(lineNo.equals("SIM")){//应苏科长要求,REFLOW1 显示REFLOW1,个别设备冰箱等显示描述
            String str = "[{\"id\":\"SIM-PRINTER1\",\"name\":\"SIM-印刷机1\"},\n" +
                    "{\"id\":\"SIM-YGAZO1\",\"name\":\"SIM-印刷后画像1#\"},\n" +
                    "{\"id\":\"SIM-DM1\",\"name\":\"SIM-DM1#\"},\n" +
                    "{\"id\":\"SIM-DM2\",\"name\":\"SIM-DM2#\"},\n" +
                    "{\"id\":\"SIM-DM3\",\"name\":\"SIM-DM3#\"},\n" +
                    "{\"id\":\"SIM-DM4\",\"name\":\"SIM-DM4#\"},\n" +
                    "{\"id\":\"SIM-DM5\",\"name\":\"SIM-DM5#\"},\n" +
                    "{\"id\":\"SIM-DM6\",\"name\":\"SIM-DM6#\"},\n" +
                    "{\"id\":\"SIM-DM7\",\"name\":\"SIM-DM7#\"},\n" +
                    "{\"id\":\"SIM-REFLOW1\",\"name\":\"SIM-回流焊1#\"},\n" +
                    "{\"id\":\"SIM-HGAZO1\",\"name\":\"SIM-回流焊后画像1#\"},\n" +
                    "{\"id\":\"SIM-WB-1A\",\"name\":\"SIM-WB焊线机1A\"},\n" +
                    "{\"id\":\"SIM-WB-1B\",\"name\":\"SIM-WB焊线机1B\"},\n" +
                    "{\"id\":\"SIM-WB-2A\",\"name\":\"SIM-WB焊线机2A\"},\n" +
                    "{\"id\":\"SIM-WB-2B\",\"name\":\"SIM-WB焊线机2B\"},\n" +
                    "{\"id\":\"SIM-WB-3A\",\"name\":\"SIM-WB焊线机3A\"},\n" +
                    "{\"id\":\"SIM-WB-3B\",\"name\":\"SIM-WB焊线机3B\"},\n" +
                    "{\"id\":\"SIM-WB-4A\",\"name\":\"SIM-WB焊线机4A\"},\n" +
                    "{\"id\":\"SIM-WB-4B\",\"name\":\"SIM-WB焊线机4B\"},\n" +
                    "{\"id\":\"SIM-WB-5A\",\"name\":\"SIM-WB焊线机5A\"},\n" +
                    "{\"id\":\"SIM-WB-5B\",\"name\":\"SIM-WB焊线机5B\"},\n" +
                    "{\"id\":\"SIM-WB-6A\",\"name\":\"SIM-WB焊线机6A\"},\n" +
                    "{\"id\":\"SIM-WB-6B\",\"name\":\"SIM-WB焊线机6B\"},\n" +
                    "{\"id\":\"SIM-TRM1\",\"name\":\"SIM-TRM塑封机1\"},\n" +
                    "{\"id\":\"SIM-TRM2\",\"name\":\"SIM-TRM塑封机2\"},\n" +
                    "{\"id\":\"SIM-LF1\",\"name\":\"SIM-分离1\"},\n" +
                    "{\"id\":\"SIM-HTRT1\",\"name\":\"SIM-检查1\"},\n" +
                    "{\"id\":\"SIM-LF2\",\"name\":\"SIM-分离2\"},\n" +
                    "{\"id\":\"SIM-HTRT2\",\"name\":\"SIM-检查2\"}]";
            List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
            rs.put("id", list);
        } else {
            String str = "[{\"id\":\"APJ-IGBT-SMT1\",\"name\":\"APJ-IGBT印刷贴片机\"},\n" +
                    "{\"id\":\"APJ-IGBT-PRINTER1\",\"name\":\"APJ-IGBT印刷机\"},\n" +
                    "{\"id\":\"APJ-IGBT-2DAOI1\",\"name\":\"APJ-IGBT印刷 2D画像检查机\"},\n" +
                    "{\"id\":\"APJ-IGBT-REFLOW1\",\"name\":\"APJ-IGBT印刷立式固化炉\"},\n" +
                    "{\"id\":\"APJ-IGBT-3DAOI1\",\"name\":\"APJ-IGBT印刷-3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-IGBT-SORT1\",\"name\":\"APJ-IGBT印刷下料机\"},\n" +
                    "{\"id\":\"APJ-IGBT-SORT2\",\"name\":\"APJ-IGBT预结合上料机\"},\n" +
                    "{\"id\":\"APJ-IGBT-DM1\",\"name\":\"APJ-IGBT预结合晶圆贴片机\"},\n" +
                    "{\"id\":\"APJ-IGBT-SORT3\",\"name\":\"APJ-IGBT预结合下料机\"},\n" +
                    "{\"id\":\"APJ-FRD-SMT1\",\"name\":\"APJ-IGBT印刷贴片机\"},\n" +
                    "{\"id\":\"APJ-FRD-PRINTER1\",\"name\":\"APJ-FRD印刷机\"},\n" +
                    "{\"id\":\"APJ-FRD-2DAOI1\",\"name\":\"APJ-FRD印刷 2D画像检查机\"},\n" +
                    "{\"id\":\"APJ-FRD-REFLOW1\",\"name\":\"APJ-FRD印刷立式固化炉\"},\n" +
                    "{\"id\":\"APJ-FRD-3DAOI1\",\"name\":\"APJ-FRD印刷-3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-FRD-SORT1\",\"name\":\"APJ-FRD印刷下料机\"},\n" +
                    "{\"id\":\"APJ-FRD-SORT2\",\"name\":\"APJ-FRD预结合上料机\"},\n" +
                    "{\"id\":\"APJ-FRD-DM1\",\"name\":\"APJ-FRD预结合晶圆贴片机\"},\n" +
                    "{\"id\":\"APJ-FRD-SORT3\",\"name\":\"APJ-FRD预结合下料机\"},\n" +
                    "{\"id\":\"APJ-HB1-SORT1\",\"name\":\"APJ-一次热压上料机\"},\n" +
                    "{\"id\":\"APJ-HB1-SINTERING1\",\"name\":\"APJ-一次热压机\"},\n" +
                    "{\"id\":\"APJ-HB1-3DAOI1\",\"name\":\"APJ-一次热压3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-HB1-SORT2\",\"name\":\"APJ-一次热压下料机\"},\n" +
                    "{\"id\":\"APJ-VI1\",\"name\":\"APJ-中间耐压检查一体机\"},\n" +
                    "{\"id\":\"APJ-DBCT-SORT1\",\"name\":\"APJ-上基板印刷上料机   \"},\n" +
                    "{\"id\":\"APJ-DBCT-PRINTER1\",\"name\":\"APJ-上基板印刷机\"},\n" +
                    "{\"id\":\"APJ-DBCT-2DAOI1\",\"name\":\"APJ-上基板印刷2D画像检查机\"},\n" +
                    "{\"id\":\"APJ-DBCT-REFLOW1\",\"name\":\"APJ-上基板印刷立式固化炉\"},\n" +
                    "{\"id\":\"APJ-DBCT-3DAOI1\",\"name\":\"APJ-上基板3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-DBCT-SORT2\",\"name\":\"APJ-上基板印刷下料机\"},\n" +
                    "{\"id\":\"APJ-DBCB-SORT1\",\"name\":\"APJ-下基板印刷上料机   \"},\n" +
                    "{\"id\":\"APJ-DBCB-PRINTER1\",\"name\":\"APJ-下基板印刷机\"},\n" +
                    "{\"id\":\"APJ-DBCB-2DAOI1\",\"name\":\"APJ-下基板印刷2D画像检查机\"},\n" +
                    "{\"id\":\"APJ-DBCB-REFLOW1\",\"name\":\"APJ-下基板印刷立式固化炉\"},\n" +
                    "{\"id\":\"APJ-DBCB-3DAOI1\",\"name\":\"APJ-下基板3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-DBCB-SORT2\",\"name\":\"APJ-下基板印刷下料机\"},\n" +
                    "{\"id\":\"APJ-HB2-SORT1\",\"name\":\"APJ-二次热压上料机\"},\n" +
                    "{\"id\":\"APJ-HB2-DISPENSING1\",\"name\":\"APJ-二次热压点胶机1\"},\n" +
                    "{\"id\":\"APJ-HB2-DISPENSING2\",\"name\":\"APJ-二次热压点胶机2\"},\n" +
                    "{\"id\":\"APJ-HB2-DISPENSING3\",\"name\":\"APJ-二次热压点胶机3\"},\n" +
                    "{\"id\":\"APJ-HB2-2DAOI1\",\"name\":\"APJ-二次热压2D画像检查机1\"},\n" +
                    "{\"id\":\"APJ-HB2-SMT1\",\"name\":\"APJ-二次热压贴片机1\"},\n" +
                    "{\"id\":\"APJ-HB2-SMT2\",\"name\":\"APJ-二次热压贴片机2\"},\n" +
                    "{\"id\":\"APJ-HB2-2DAOI2\",\"name\":\"APJ-二次热压2D画像检查机2\"},\n" +
                    "{\"id\":\"APJ-HB2-ASSEMBLY1\",\"name\":\"APJ-二次热压组立机\"},\n" +
                    "{\"id\":\"APJ-HB2-SINTERING1\",\"name\":\"APJ-二次热压机\"},\n" +
                    "{\"id\":\"APJ-HB2-SORT2\",\"name\":\"APJ-二次热压下料机\"},\n" +
                    "{\"id\":\"APJ-XRAY1\",\"name\":\"APJ-X射线检查机\"},\n" +
                    "{\"id\":\"APJ-CLEAN-US1\",\"name\":\"APJ-US1洗净机\"},\n" +
                    "{\"id\":\"APJ-CLEAN-JET1\",\"name\":\"APJ-JET洗净机\"},\n" +
                    "{\"id\":\"APJ-TRM1\",\"name\":\"APJ-TRM1\"},\n" +
                    "{\"id\":\"APJ-AT1\",\"name\":\"APJ-耐老化试验搬送机\"},\n" +
                    "{\"id\":\"APJ-LF1\",\"name\":\"APJ-分离检查机\"},\n" +
                    "{\"id\":\"APJ-HTRT1\",\"name\":\"APJ-高温室温检查机\"}]";
            List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
            rs.put("id", list);
        }
        return rs;
    }

    //FileOutputStream fileOutputStream = new FileOutputStream("D:\\创建sheel页2.xlsx");
    //book.write(fileOutputStream);
    //fileOutputStream.close();
}
