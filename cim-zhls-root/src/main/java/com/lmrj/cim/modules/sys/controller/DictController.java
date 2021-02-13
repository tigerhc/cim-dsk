package com.lmrj.cim.modules.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.core.sys.entity.Dict;
import com.lmrj.core.sys.service.IDictService;
import com.lmrj.cim.utils.DictUtils;
import com.lmrj.cim.utils.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@RequestMapping("/sys/dict")
@ViewPrefix("modules/sys/dict")
@RequiresPathPermission("sys:dict")
@LogAspectj(title = "字典管理")
public class DictController extends BaseBeanController<Dict> {

	@Autowired
	private IDictService dictService;

	/**
	 * 字典
	 * @return
	 */
	@GetMapping(value = "")
	public Response get() {
		Response response = new Response();
		try {
			//放入数据字典
			response.putObject(DictUtils.getDict());
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error("获取失败");
		}
		return response;
	}
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
		EntityWrapper<Dict> entityWrapper = new EntityWrapper<>(Dict.class);
		entityWrapper.orderBy("sort",true);
		String keyword=request.getParameter("keyword");
		String gid=request.getParameter("gid");
		if (!StringUtil.isEmpty(gid)&&!StringUtil.isEmpty(keyword)){
			entityWrapper.eq("gid",gid).andNew().like("label",keyword).or().like("value",keyword);
		}else if(!StringUtil.isEmpty(gid)){
			entityWrapper.eq("gid",gid);
		}

		// 预处理
		Page pageBean = dictService.selectPage(PageRequest.getPage(),entityWrapper);
		FastJsonUtils.print(pageBean);
	}

	@PostMapping("add")
	@LogAspectj(logType = LogType.INSERT)
	@RequiresMethodPermissions("add")
	public Response add(Dict entity, BindingResult result,
						   HttpServletRequest request, HttpServletResponse response) {
		// 验证错误
		this.checkError(entity,result);
		dictService.insert(entity);
		return Response.ok("添加成功");
	}

	@PostMapping("{id}/update")
	@LogAspectj(logType = LogType.UPDATE)
	@RequiresMethodPermissions("add")
	public Response update(Dict entity, BindingResult result,
						   HttpServletRequest request, HttpServletResponse response) {
		// 验证错误
		this.checkError(entity,result);
		dictService.insertOrUpdate(entity);
		return Response.ok("更新成功");
	}

	@PostMapping("{id}/delete")
	@LogAspectj(logType = LogType.DELETE)
	public Response delete(@PathVariable("id") String id) {
		dictService.deleteById(id);
		return Response.ok("删除成功");
	}

	@PostMapping("batch/delete")
	@LogAspectj(logType = LogType.DELETE)
	@RequiresMethodPermissions("delete")
	public Response batchDelete(@RequestParam("ids") String[] ids) {
		List<String> idList = java.util.Arrays.asList(ids);
		dictService.deleteBatchIds(idList);
		return Response.ok("删除成功");
	}

	/**
	 * 根据dict code查询字典数据
	 *
	 * @throws IOException
	 */
	@GetMapping(value = "{dictcode}/finddict")
	@LogAspectj(logType = LogType.SELECT)
	@RequiresMethodPermissions("list")
	public void findDict( @PathVariable("dictcode") String dictCode) throws IOException {
		List dicts = dictService.findDictByCode(dictCode);
		FastJsonUtils.print(dicts);
	}

	/**
	 * 根据dict code查询字典数据
	 *
	 * @throws IOException
	 */
	@GetMapping(value = "{dictcode}/finddicts")
	@LogAspectj(logType = LogType.SELECT)
	@RequiresMethodPermissions("list")
	public void finddicts( @PathVariable("dictcode") String dictCode) throws IOException {
		String[] dictCodes = dictCode.split(",");
		Map<String, List> map = Maps.newHashMap();
		for(String temp : dictCodes){
			List dicts = dictService.findDictByCode(temp);
			map.put(temp, dicts);
		}

		FastJsonUtils.print(map);
	}

}
