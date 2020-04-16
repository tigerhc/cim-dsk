package com.lmrj.rms.recipe.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.recipe.controller
 * @title: rms_recipe_body控制器
 * @description: rms_recipe_body控制器
 * @author: zhangweijiang
 * @date: 2019-06-15 01:58:21
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipebody")
@ViewPrefix("rms/rmsrecipebody")
@RequiresPathPermission("rms:rmsrecipebody")
@LogAspectj(title = "rms_recipe_body")
public class RmsRecipeBodyController extends BaseCRUDController<RmsRecipeBody> {

}
