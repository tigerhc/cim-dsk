package com.lmrj.rms.config.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rms.config.entity.RmsRecipeDownloadConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.config.controller
 * @title: rms_recipe_download_config控制器
 * @description: rms_recipe_download_config控制器
 * @author: 何思国
 * @date: 2020-07-29 16:21:03
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipedownloadconfig")
@ViewPrefix("rms/rmsrecipedownloadconfig")
@RequiresPathPermission("rms:rmsrecipedownloadconfig")
@LogAspectj(title = "rms_recipe_download_config")
public class RmsRecipeDownloadConfigController extends BaseCRUDController<RmsRecipeDownloadConfig> {

}