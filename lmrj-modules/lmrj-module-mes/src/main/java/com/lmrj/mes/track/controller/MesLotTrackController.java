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
import com.lmrj.core.log.LogAspectj;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
public class MesLotTrackController extends BaseCRUDController<MesLotTrack> {

    @Autowired
    IMesLotTrackService mesLotTrackService;

    @RequestMapping(value = "/trackin/{lotNo}/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void trackin(Model model, @PathVariable String eqpId, @PathVariable String lotNo, @RequestParam String recipeCode, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        mesLotTrackService.trackIn( eqpId,   lotNo,   recipeCode,   opId);

    }

    @RequestMapping(value = "/trackout/{lotNo}/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void trackout(Model model, @PathVariable String eqpId, @PathVariable String lotNo, @RequestParam String recipeCode, @RequestParam String opId, HttpServletRequest request, HttpServletResponse response) {
        mesLotTrackService.trackOut( eqpId,   lotNo,   recipeCode,   opId);

    }

    @GetMapping("export")
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
