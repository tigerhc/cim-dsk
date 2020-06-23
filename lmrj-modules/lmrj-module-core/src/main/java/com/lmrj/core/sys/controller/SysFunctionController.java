package com.lmrj.core.sys.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.SysFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.core.sys.controller
 * @title: sys_function控制器
 * @description: sys_function控制器
 * @author: 张伟江
 * @date: 2020-06-23 09:55:27
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("core/sysfunction")
@ViewPrefix("core/sysfunction")
@RequiresPathPermission("core:sysfunction")
@LogAspectj(title = "sys_function")
public class SysFunctionController extends BaseCRUDController<SysFunction> {

}