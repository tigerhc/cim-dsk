package com.lmrj.rms.log.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rms.log.entity.RmsRecipeLog;
import com.lmrj.rms.recipe.entity.RmsRecipe;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.log.service
* @title: rms_recipe_log服务接口
* @description: rms_recipe_log服务接口
* @author: 张伟江
* @date: 2020-07-07 16:10:43
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IRmsRecipeLogService extends ICommonService<RmsRecipeLog> {

    void downloadLog(RmsRecipe recipe);
}
