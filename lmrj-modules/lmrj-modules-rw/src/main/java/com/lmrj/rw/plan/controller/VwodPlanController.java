package com.lmrj.rw.plan.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rw.plan.entity.VwodPlan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rw/vwodplan")
@ViewPrefix("rw/vwodplan")
@RequiresPathPermission("rw:vwodplan")
@LogAspectj(title = "v_wod_plan")
public class VwodPlanController  extends BaseCRUDController<VwodPlan> {
}
