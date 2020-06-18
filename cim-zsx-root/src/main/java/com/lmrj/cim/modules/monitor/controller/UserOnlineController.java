package com.lmrj.cim.modules.monitor.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.controller.BaseController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.cim.modules.sso.service.IOAuthService;
import com.lmrj.cim.utils.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Title: 在线用户
 * @Description: 在线用户
 * @author lmrj
 * @date 2017-05-15 08:18:21
 * @version V1.0
 *
 */
@RestController
@RequestMapping(value = "/monitor/user/online")
@RequiresPathPermission("monitor:user:online")
@LogAspectj(title = "在线用户")
public class UserOnlineController extends BaseController{

	@Autowired
	private IOAuthService oAuthService;

	/**
	 * 根据页码和每页记录数，以及查询条件动态加载数据
	 *
	 * @throws IOException
	 */
	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })

	@LogAspectj(logType = LogType.SELECT)
	@RequiresMethodPermissions("list")
	public void list() throws IOException {
		// 预处理
		com.baomidou.mybatisplus.plugins.Page pageBean = oAuthService.activePrincipal(PageRequest.getPage());
		FastJsonUtils.print(pageBean);
	}

	@PostMapping("/forceLogout")
	@LogAspectj(title = "用户强制退出")
	public Response forceLogout(@RequestParam(value = "ids") String[] ids) {
		for (String id : ids) {
			// 强制退出，不想频繁的请求redis没想好怎么处理
		}
		return Response.ok("强制退出成功");
	}

}
