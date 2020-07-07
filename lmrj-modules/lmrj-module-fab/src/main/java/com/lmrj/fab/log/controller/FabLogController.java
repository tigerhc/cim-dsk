package com.lmrj.fab.log.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.log.entity.FabLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.log.controller
 * @title: fab_log控制器
 * @description: fab_log控制器
 * @author: 张伟江
 * @date: 2020-07-07 16:09:16
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("fab/fablog")
@ViewPrefix("fab/fablog")
@RequiresPathPermission("fab:fablog")
@LogAspectj(title = "fab_log")
public class FabLogController extends BaseCRUDController<FabLog> {

}