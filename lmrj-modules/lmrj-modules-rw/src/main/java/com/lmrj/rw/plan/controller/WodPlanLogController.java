package com.lmrj.rw.plan.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rw.plan.entity.WodPlanLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wdj
 * @date 2021-05-18 8:49
 */
@RestController
@RequestMapping("rw/wodplanlog")
@ViewPrefix("rw/wodplanlog")
@RequiresPathPermission("rw:wodplanlog")
@LogAspectj(title = "wod_plan_log")
public class WodPlanLogController extends BaseCRUDController<WodPlanLog> {
}
