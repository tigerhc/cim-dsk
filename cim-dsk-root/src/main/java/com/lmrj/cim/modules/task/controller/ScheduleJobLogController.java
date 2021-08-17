package com.lmrj.cim.modules.task.controller;

import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.cim.modules.task.entity.ScheduleJobLog;
import com.lmrj.cim.modules.task.service.IScheduleJobLogService;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.util.lang.StringUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.utils.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.modules.task.controller
 * @title: 任务日志控制器
 * @description: 任务日志控制器
 * @author: 张飞
 * @date: 2018-09-17 14:25:19
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/task/schedule/job/log")
@ViewPrefix("modules/task/schedule/job")
@RequiresPathPermission("task:schedule:joblog")
@LogAspectj(title = "计划任务日志")
public class ScheduleJobLogController extends BaseBeanController<ScheduleJobLog> {
    @Autowired
    private IScheduleJobLogService scheduleJobLogService;
    @Value("${mapping.jobenabled}")
    private Boolean jobenabled;

    @GetMapping(value = "list")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void list( HttpServletRequest request) throws IOException {
        if(!jobenabled){
            return;
        }
        //加入条件
        EntityWrapper<ScheduleJobLog> entityWrapper = new EntityWrapper<>(ScheduleJobLog.class);
        entityWrapper.orderBy("createTime",false);
        String jobName= request.getParameter("jobName");
        if (!StringUtil.isEmpty(jobName)){
            entityWrapper.like("jobName",jobName);
        }
        String executeClass= request.getParameter("executeClass");
        if (!StringUtil.isEmpty(executeClass)){
            entityWrapper.eq("executeClass",executeClass);
        }
        String status=request.getParameter("status");
        if (!StringUtil.isEmpty(status)){
            entityWrapper.eq("status",status);
        }
        // 预处理
        Page pageBean = scheduleJobLogService.selectPage(PageRequest.getPage(),entityWrapper);
        FastJsonUtils.print(pageBean,ScheduleJobLog.class,"id,jobName,executeClass,jobGroup,methodName,methodParams,jobMessage,status,exceptionInfo,createTime");
    }

    @PostMapping("{id}/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response delete(@PathVariable("id") String id) {
        scheduleJobLogService.deleteById(id);
        return Response.ok("删除成功");
    }

    @PostMapping("batch/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response batchDelete(@RequestParam("ids") String[] ids) {
        List<String> idList = java.util.Arrays.asList(ids);
        scheduleJobLogService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
