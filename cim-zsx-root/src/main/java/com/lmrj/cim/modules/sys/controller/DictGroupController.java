package com.lmrj.cim.modules.sys.controller;

import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.core.sys.entity.DictGroup;
import com.lmrj.core.sys.entity.Role;
import com.lmrj.cim.modules.sys.service.IDictGroupService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/sys/dict/group")
@ViewPrefix("modules/sys/dict/group")
@RequiresPathPermission("sys:dict")
@LogAspectj(title = "字典分组")
public class DictGroupController extends BaseBeanController<DictGroup> {

	@Autowired
	private IDictGroupService dictGroupService;


	/**
	 * 根据页码和每页记录数，以及查询条件动态加载数据
	 *
	 * @param request
	 * @throws IOException
	 */
	@GetMapping(value = "list")
	@LogAspectj(logType = LogType.SELECT)
	@RequiresMethodPermissions("group:list")
	public void list( HttpServletRequest request) throws IOException {
		//加入条件
		EntityWrapper<DictGroup> entityWrapper = new EntityWrapper<>(DictGroup.class);
		entityWrapper.orderBy("createDate",false);
		String keyword=request.getParameter("keyword");
		if (!StringUtil.isEmpty(keyword)){
			entityWrapper.like("name",keyword).or().like("code",keyword);
		}
		// 预处理
		Page pageBean = dictGroupService.selectPage(PageRequest.getPage(),entityWrapper);
		FastJsonUtils.print(pageBean,Role.class,"id,name,code,remarks,usable");
	}

	@PostMapping("add")
	@LogAspectj(logType = LogType.INSERT)
	@RequiresMethodPermissions("group:add")
	public Response add(DictGroup entity, BindingResult result,
						   HttpServletRequest request, HttpServletResponse response) {
		// 验证错误
		this.checkError(entity,result);
		dictGroupService.insert(entity);
		return Response.ok("添加成功");
	}

	@PostMapping("{id}/update")
	@LogAspectj(logType = LogType.UPDATE)
	@RequiresMethodPermissions("group:update")
	public Response update(DictGroup entity, BindingResult result,
						   HttpServletRequest request, HttpServletResponse response) {
		// 验证错误
		this.checkError(entity,result);
		dictGroupService.insertOrUpdate(entity);
		return Response.ok("更新成功");
	}

	@PostMapping("{id}/delete")
	@RequiresMethodPermissions("group:delete")
	public Response delete(@PathVariable("id") String id) {
		dictGroupService.deleteById(id);
		return Response.ok("删除成功");
	}

	@RequestMapping(value = "/forceRefresh", method = RequestMethod.POST)
	@ResponseBody
	@LogAspectj(logType = LogType.OTHER,title = "字典刷新")
	@RequiresMethodPermissions("force:refresh")
	public Response forceRefresh(HttpServletRequest request, HttpServletResponse response) {
		try {
			// DictUtils.clear();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error("字典刷新失败" + e.getMessage());
		}
		return Response.ok("字典刷新成功");
	}

}
