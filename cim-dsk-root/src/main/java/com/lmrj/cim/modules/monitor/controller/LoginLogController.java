package com.lmrj.cim.modules.monitor.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.cim.common.response.ResponseError;
import com.lmrj.cim.modules.monitor.entity.LoginLog;
import com.lmrj.cim.modules.monitor.service.ILoginLogService;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.utils.PageRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.modules.sys.controller
 * @title: 登陆日志控制器
 * @description: 登陆日志控制器
 * @author: sys
 * @date: 2018-09-28 11:35:45
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("monitor/login/log")
@ViewPrefix("modules/sys/log")
@RequiresPathPermission("monitor:login:log")
@LogAspectj(title = "登陆日志")
public class LoginLogController extends BaseBeanController<LoginLog> {

    @Autowired
    private ILoginLogService loginLogService;


    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void list( HttpServletRequest request) throws IOException {
        //加入条件
        EntityWrapper<LoginLog> entityWrapper = new EntityWrapper<>(LoginLog.class);
        entityWrapper.orderBy("loginTime",false);
        String status = request.getParameter("status");
        if (!StringUtil.isEmpty(status)){
            entityWrapper.eq("status",status);
        }

        // 预处理
        Page pageBean = loginLogService.selectPage(PageRequest.getPage(),entityWrapper);
        FastJsonUtils.print(pageBean);
    }

	@PostMapping("{id}/delete")
    @RequiresMethodPermissions("delete")
	public Response delete(@PathVariable("id") String id) {
	    loginLogService.deleteById(id);
		return Response.ok("删除成功");
	}

	@PostMapping("batch/delete")
    @RequiresMethodPermissions("delete")
	public Response batchDelete(@RequestParam("ids") String[] ids) {
		List<String> idList = java.util.Arrays.asList(ids);
		loginLogService.deleteBatchIds(idList);
		return Response.ok("删除成功");
	}

    @GetMapping("export")
    @LogAspectj(logType = LogType.EXPORT)
    @RequiresMethodPermissions("export")
    public Response export(HttpServletRequest request) {
        Response response = Response.ok("导出成功");
        try {
            TemplateExportParams params = new TemplateExportParams(
                    "");
            //加入条件
            EntityWrapper<LoginLog> entityWrapper = new EntityWrapper<>(LoginLog.class);
            entityWrapper.orderBy("loginTime",false);
            Page pageBean = loginLogService.selectPage(PageRequest.getPage(),entityWrapper);
            String title = "登陆日志";
            Workbook book = ExcelExportUtil.exportExcel(new ExportParams(
                    title, title, ExcelType.XSSF), LoginLog.class, pageBean.getRecords());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            book.write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtil.bytesToHexString2(bytes);
            title = title+ "-" + DateUtil.getDateTime();
            response.put("bytes",bytesRes);
            response.put("title",title);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResponseError.NORMAL_ERROR,"导出失败");
        }
        return response;
    }
}
