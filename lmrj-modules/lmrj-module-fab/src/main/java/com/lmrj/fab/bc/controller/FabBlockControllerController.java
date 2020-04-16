package com.lmrj.fab.bc.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.fab.bc.entity.FabBlockController;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.bc.controller
 * @title: fab_block_controller控制器
 * @description: fab_block_controller控制器
 * @author: zhangweijiang
 * @date: 2019-07-15 01:03:01
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("fab/fabblockcontroller")
@ViewPrefix("fab/fabblockcontroller")
@RequiresPathPermission("fab:fabblockcontroller")
@LogAspectj(title = "fab_block_controller")
public class FabBlockControllerController extends BaseCRUDController<FabBlockController> {

}
