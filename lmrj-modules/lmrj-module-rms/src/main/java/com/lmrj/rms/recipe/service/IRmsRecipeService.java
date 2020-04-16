package com.lmrj.rms.recipe.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rms.recipe.entity.RmsRecipe;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.recipe.service
* @title: rms_recipe服务接口
* @description: rms_recipe服务接口
* @author: zhangweijiang
* @date: 2019-06-15 01:58:00
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IRmsRecipeService extends ICommonService<RmsRecipe> {
    boolean uploadRecipe(String eqpId, String recipeName);
    RmsRecipe findLastByRecipeCode(RmsRecipe rmsRecipe);
    RmsRecipe findLastByRecipeCode(String id);
    boolean upgrade(RmsRecipe rmsRecipe);
    RmsRecipe selectByIdAndCompareParam(String id);
}