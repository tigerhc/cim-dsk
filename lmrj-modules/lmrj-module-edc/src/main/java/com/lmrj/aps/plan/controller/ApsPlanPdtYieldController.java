package com.lmrj.aps.plan.controller;

import com.lmrj.aps.plan.entity.ApsPlanPdtYield;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.aps.plan.controller
 * @title: aps_plan_pdt_yield控制器
 * @description: aps_plan_pdt_yield控制器
 * @author: 张伟江
 * @date: 2020-05-17 21:00:52
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("aps/apsplanpdtyield")
@ViewPrefix("aps/apsplanpdtyield")
@RequiresPathPermission("aps:apsplanpdtyield")
public class ApsPlanPdtYieldController extends BaseCRUDController<ApsPlanPdtYield> {

}
