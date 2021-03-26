package com.lmrj.rms.log.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rms.log.entity.RmsRecipeCheckLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wdj
 * @date 2021-03-25 15:33
 */
@RestController
@RequestMapping("rms/rmsrecipechecklog")
@ViewPrefix("rms/rmsrecipechecklog")
@RequiresPathPermission("rms:rmsrecipechecklog")
@LogAspectj(title = "rms_recipe_check_log")
public class RmsRecipeCheckController extends BaseCRUDController<RmsRecipeCheckLog>{

}
