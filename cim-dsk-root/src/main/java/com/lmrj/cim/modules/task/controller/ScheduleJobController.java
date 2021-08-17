package com.lmrj.cim.modules.task.controller;

import com.lmrj.common.utils.BeanUtils;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.util.lang.ObjectUtil;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.cim.modules.task.service.IScheduleJobService;
import com.lmrj.cim.modules.task.entity.ScheduleJob;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.util.lang.StringUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.utils.PageRequest;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/task/schedule/job")
@ViewPrefix("modules/task/schedule/job")
@RequiresPathPermission("task:schedule:job")
@LogAspectj(title = "计划任务")
public class ScheduleJobController extends BaseBeanController<ScheduleJob> {
	@Value("${mapping.jobenabled}")
	private Boolean jobenabled;
	@Autowired
	private IScheduleJobService scheduleJobService;


	@GetMapping(value = "list")
	@LogAspectj(logType = LogType.SELECT)
	@RequiresMethodPermissions("list")
	public void list( HttpServletRequest request) throws IOException {
		//加入条件
		EntityWrapper<ScheduleJob> entityWrapper = new EntityWrapper<>(ScheduleJob.class);
		entityWrapper.orderBy("createDate",false);
		String jobName= request.getParameter("jobName");
		if (!StringUtil.isEmpty(jobName)){
			entityWrapper.like("jobName",jobName);
		}
		// 预处理
		Page pageBean = scheduleJobService.selectPage(PageRequest.getPage(),entityWrapper);
		FastJsonUtils.print(pageBean,ScheduleJob.class,"id,jobName,cronExpression,executeClass,methodName,methodParams,misfirePolicy,loadWay,isConcurrent,description,jobStatus,jobGroup");
	}

	@PostMapping("add")
	@LogAspectj(logType = LogType.INSERT)
	@RequiresMethodPermissions("add")
	public Response add(ScheduleJob entity, BindingResult result,
						   HttpServletRequest request, HttpServletResponse response) {
		if(!jobenabled){
			return Response.error("定时任务被暂停");
		}
		// 验证错误
		this.checkError(entity,result);
		if (!CronExpression.isValidExpression(entity.getCronExpression())) {
			return Response.error("cron表达式格式不对");
		}
		scheduleJobService.insert(entity);
		return Response.ok("添加成功");
	}

	@PostMapping("{id}/update")
	@LogAspectj(logType = LogType.UPDATE)
	@RequiresMethodPermissions("update")
	public Response update(ScheduleJob entity, BindingResult result,
						   HttpServletRequest request, HttpServletResponse response) {
		if(!jobenabled){
			return Response.error("定时任务被暂停");
		}
		// 验证错误
		this.checkError(entity,result);
		if (!CronExpression.isValidExpression(entity.getCronExpression())) {
			return Response.error("cron表达式格式不对");
		}
		scheduleJobService.insertOrUpdate(entity);
		return Response.ok("更新成功");
	}

	@PostMapping("{id}/delete")
	@LogAspectj(logType = LogType.DELETE)
	@RequiresMethodPermissions("delete")
	public Response delete(@PathVariable("id") String id) {
		scheduleJobService.deleteById(id);
		return Response.ok("删除成功");
	}

	@PostMapping(value = "/saveScheduleJob")
	public Response saveScheduleJob(ScheduleJob scheduleJob, HttpServletRequest request, HttpServletResponse response) {
		if(!jobenabled){
			return Response.error("定时任务被暂停");
		}
		if (!CronExpression.isValidExpression(scheduleJob.getCronExpression())) {
			return Response.error("cron表达式格式不对");
		}
		try {
			if (ObjectUtil.isNullOrEmpty(scheduleJob.getId())) {
				scheduleJobService.insert(scheduleJob);
			} else {
				// FORM NULL不更新
				ScheduleJob oldEntity = scheduleJobService.selectById(scheduleJob.getId());
				BeanUtils.copyProperties(scheduleJob,oldEntity);
				scheduleJobService.insertOrUpdate(oldEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error("保存失败"+e.getMessage());
		}
		return Response.ok("保存成功");
	}

	@PostMapping("batch/delete")
	@LogAspectj(logType = LogType.DELETE)
	@RequiresMethodPermissions("delete")
	public Response batchDelete(@RequestParam("ids") String[] ids) {
		if(!jobenabled){
			return Response.error("定时任务被暂停");
		}
		List<String> idList = java.util.Arrays.asList(ids);
		scheduleJobService.deleteBatchIds(idList);
		return Response.ok("删除成功");
	}

	@PostMapping(value = "{id}/changeJobStatus")
	@LogAspectj(logType = LogType.OTHER,title = "任务状态")
	@RequiresMethodPermissions("change:job:status")
	public Response changeJobStatus(@PathVariable("id") String id, HttpServletRequest request,
									HttpServletResponse response) {
		if(!jobenabled){
			return Response.error("定时任务被暂停");
		}
		String cmd = request.getParameter("cmd");
		String label = "停止";
		if (cmd.equals("start")) {
			label = "启动";
		} else {
			label = "停止";
		}
		try {
			scheduleJobService.changeStatus(id, cmd);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error("任务" + label + "失败" + e.getMessage());
		}
		return 	Response.ok("任务" + label + "成功");
	}

	@PostMapping(value = "{id}/updateCron")
	@LogAspectj(logType = LogType.OTHER,title = "任务更新")
	@RequiresMethodPermissions("update:cron")
	public Response updateCron(@PathVariable("id") String id) {
		if(!jobenabled){
			return Response.error("定时任务被暂停");
		}
		scheduleJobService.updateCron(id);
		return Response.ok("任务更新成功");
	}

	@PostMapping(value = "/runAJobNow")
	@LogAspectj(logType = LogType.OTHER,title = "执行一次")
	@RequiresMethodPermissions("run:ajob:now")
	public Response runAJobNow(ScheduleJob scheduleJob, HttpServletRequest request,
							   HttpServletResponse response) {
		if(!jobenabled){
			return Response.error("定时任务被暂停");
		}
	    scheduleJobService.runAJobNow(scheduleJob.getId());
		return Response.ok("任务启动成功");
	}

	/**
	 * 刷新任务
	 * @return
	 */
	@PostMapping(value = "/refreshJob")
	@LogAspectj(logType = LogType.OTHER,title = "刷新任务")
	@RequiresMethodPermissions("refresh:job")
	public Response refreshJob() {
		if(!jobenabled){
			return Response.error("定时任务被暂停");
		}
		scheduleJobService.refreshTask();
		return Response.ok("刷新任务成功");
	}
}
