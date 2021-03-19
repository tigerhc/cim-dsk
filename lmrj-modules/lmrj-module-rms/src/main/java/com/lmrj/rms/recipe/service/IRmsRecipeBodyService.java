package com.lmrj.rms.recipe.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.rms.recipe.entity.TRXO;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.recipe.service
* @title: rms_recipe_body服务接口
* @description: rms_recipe_body服务接口
* @author: zhangweijiang
* @date: 2019-06-15 01:58:21
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IRmsRecipeBodyService extends ICommonService<RmsRecipeBody> {
    int copyParaFromExist(String recipeIdNew, String recipeIdOld);
    int copyMinValue(String recipeIdNew, String recipeIdOld);
    int copyMaxValue(String recipeIdNew, String recipeIdOld);
    TRXO checkRecipeBody(String eqpId, String recipeCode, String recipeBody, String recipeBodySize);
    boolean setLimitValue(String id, String maxValue, String minValue);
}
