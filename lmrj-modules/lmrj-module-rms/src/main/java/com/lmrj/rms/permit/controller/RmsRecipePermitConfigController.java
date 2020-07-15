package com.lmrj.rms.permit.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rms.permit.entity.RmsRecipePermitConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.permit.controller
 * @title: rms_recipe_permit_config控制器
 * @description: rms_recipe_permit_config控制器
 * @author: 张伟江
 * @date: 2020-07-15 23:08:59
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipepermitconfig")
@ViewPrefix("rms/rmsrecipepermitconfig")
@RequiresPathPermission("rms:rmsrecipepermitconfig")
@LogAspectj(title = "rms_recipe_permit_config")
public class RmsRecipePermitConfigController extends BaseCRUDController<RmsRecipePermitConfig> {

}