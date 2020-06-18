package com.lmrj.cim.modules.sys.controller;

import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.cim.common.helper.VueTreeHelper;
import com.lmrj.cim.common.response.ResponseError;
import com.lmrj.core.sys.entity.Menu;
import com.lmrj.core.sys.entity.Role;
import com.lmrj.core.log.LogType;
import com.lmrj.core.sys.service.IMenuService;
import com.lmrj.cim.modules.sys.service.IRoleMenuService;
import com.lmrj.core.sys.service.IRoleService;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.util.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.utils.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.modules.sys.controller
 * @title: 消息模版控制器
 * @description: 消息模版控制器
 * @author: 张飞
 * @date: 2018-09-03 15:10:10
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/sys/role")
@ViewPrefix("modules/sys/role")
//@RequiresPathMenu("sys:role")
@LogAspectj(title = "角色管理")
public class RoleController extends BaseBeanController<Role> {

	@Autowired
	private IRoleService roleService;
	@Autowired
	private IMenuService menuService;
	@Autowired
	private IRoleMenuService roleMenuService;

	/**
	 * 根据页码和每页记录数，以及查询条件动态加载数据
	 *
	 * @param request
	 * @throws IOException
	 */
	@GetMapping(value = "list")
	@LogAspectj(logType = LogType.SELECT)
	//@RequiresMethodMenus("list")
	public void list( HttpServletRequest request) throws IOException {
		//加入条件
		EntityWrapper<Role> entityWrapper = new EntityWrapper<>(Role.class);
		entityWrapper.orderBy("createDate",false);
		String code= request.getParameter("code");
		if (!StringUtil.isEmpty(code)){
			entityWrapper.like("code",code);
		}
		String name=request.getParameter("name");
		if (!StringUtil.isEmpty(name)){
			entityWrapper.like("name",name);
		}
		String projectCode= request.getParameter("projectId");
		if (!StringUtil.isEmpty(projectCode)){
			entityWrapper.eq("project_id",projectCode);
		}
		// 预处理
		Page pageBean = roleService.selectPage(PageRequest.getPage(),entityWrapper);
		FastJsonUtils.print(pageBean,Role.class,"id,name,code,isSys,usable");
	}

	/**
	 * 获取可用的用户列表
	 *
	 * @throws IOException
	 */
	@GetMapping(value = "usable/list")
	public List<Role> usableLst() throws IOException {
		EntityWrapper<Role> entityWrapper = new EntityWrapper<Role>(Role.class);
		entityWrapper.orderBy("createDate",false);
		List<Role> usableLst = roleService.selectList(entityWrapper);
		return usableLst;
	}

	@PostMapping("add")
	@LogAspectj(logType = LogType.INSERT)
	//@RequiresMethodMenus("add")
	public Response add(Role entity, BindingResult result) {
		// 验证错误
		this.checkError(entity,result);
		roleService.insert(entity);
		return Response.ok("添加成功");
	}

	@PostMapping("{id}/update")
	@LogAspectj(logType = LogType.UPDATE)
	//@RequiresMethodMenus("update")
	public Response update(Role entity, BindingResult result) {
		// 验证错误
		this.checkError(entity,result);
		roleService.insertOrUpdate(entity);
		return Response.ok("更新成功");
	}

	@PostMapping("{id}/delete")
	@LogAspectj(logType = LogType.DELETE)
	//@RequiresMethodMenus("delete")
	public Response delete(@PathVariable("id") String id) {
		roleService.deleteById(id);
		return Response.ok("删除成功");
	}

	@PostMapping("batch/delete")
	@LogAspectj(logType = LogType.DELETE)
	//@RequiresMethodMenus("delete")
	public Response batchDelete(@RequestParam("ids") String[] ids) {
		List<String> idList = java.util.Arrays.asList(ids);
		roleService.deleteBatchIds(idList);
		return Response.ok("删除成功");
	}

	/**
	 * 通过用户ID获得角色
	 * @param uid
	 * @return
	 */
	@PostMapping(value = "{uid}/findListByUserId")
	public List<Role> findListByUserId(@PathVariable("uid") String uid) {
		try {
			return roleService.findListByUserId(uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@GetMapping(value = "{roleId}/menu")
	public void menu(@PathVariable("roleId") String roleId,
						   HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> dataMap = new HashMap<>();
		EntityWrapper<Menu> entityWrapper = new EntityWrapper<>(Menu.class);
		entityWrapper.setTableAlias("t.");
		//加入条件
		List<Menu> treeNodeList = menuService.selectTreeList(entityWrapper);
		List<VueTreeHelper.VueTreeNode> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
		dataMap.put("menus", vueTreeNodes);
		// 获得选择的
		List<Menu> menuList = menuService.findMenuByRoleId(roleId);
		List<String> menuIdList = new ArrayList<>();
		for (Menu menu:menuList) {
			menuIdList.add(menu.getId());
		}
		dataMap.put("selectMenuIds", menuIdList);
		String content = JSON.toJSONString(dataMap);
		ServletUtils.printJson(response, content);
	}

	@PostMapping(value = "/setMenu")
	@LogAspectj(logType = LogType.OTHER,title = "菜单授权")
	//@RequiresMethodMenus("authMenu")
	public Response setMenu(@RequestParam("roleId") String roleId,
										@RequestParam("menuIds") String menuIds) {
		try {
			// 权限设置
			roleMenuService.setMenu(roleId,menuIds);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error(ResponseError.NORMAL_ERROR,"保存失败!<br />原因:" + e.getMessage());
		}
		return Response.ok("保存成功");
	}
}
