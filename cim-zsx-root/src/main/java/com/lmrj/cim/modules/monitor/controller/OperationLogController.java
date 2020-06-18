package com.lmrj.cim.modules.monitor.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.cim.modules.monitor.entity.OperationLog;
import com.lmrj.cim.modules.monitor.service.IOperationLogService;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.utils.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.modules.sys.controller
 * @title: 操作日志控制器
 * @description: 操作日志控制器
 * @author: 张飞
 * @date: 2018-09-30 15:53:25
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("monitor/operation/log")
@ViewPrefix("modules/sys/log")
@RequiresPathPermission("monitor:operation:log")
@LogAspectj(title = "操作日志")
public class OperationLogController extends BaseBeanController<OperationLog> {

    @Autowired
    private IOperationLogService operationLogService;

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
        EntityWrapper<OperationLog> entityWrapper = new EntityWrapper<>(OperationLog.class);
        entityWrapper.orderBy("createDate",false);

        // 预处理
        Page pageBean = operationLogService.selectPage(PageRequest.getPage(),entityWrapper);
        FastJsonUtils.print(pageBean);
    }

	@PostMapping("{id}/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
	public Response delete(@PathVariable("id") String id) {
	    operationLogService.deleteById(id);
		return Response.ok("删除成功");
	}

    @GetMapping("{id}/detail")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("detail")
    public ModelAndView detail(Model model,@PathVariable("id") String id) {
        OperationLog operationLog = operationLogService.selectById(id);
        model.addAttribute("operationLog", operationLog);
        return displayModelAndView("operation_detail");
    }

	@PostMapping("batch/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
	public Response batchDelete(@RequestParam("ids") String[] ids) {
		List<String> idList = java.util.Arrays.asList(ids);
		operationLogService.deleteBatchIds(idList);
		return Response.ok("删除成功");
	}
}
