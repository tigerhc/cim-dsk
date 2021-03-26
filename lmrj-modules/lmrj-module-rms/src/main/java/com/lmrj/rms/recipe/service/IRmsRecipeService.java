package com.lmrj.rms.recipe.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rms.recipe.entity.RmsRecipe;

import java.util.List;

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
    boolean uploadRecipe(String eqpId, List<String> recipeList) throws Exception;
    boolean downloadRecipe(String eqpId, String recipeName) throws Exception;
    RmsRecipe findLastByRecipeCode(RmsRecipe rmsRecipe, String versionType);
    RmsRecipe findLastByRecipeCode(String id);
    boolean upgrade(RmsRecipe rmsRecipe);
    RmsRecipe selectByIdAndCompareParam(String id);
    RmsRecipe selectByTwoId(String id);
    Integer copyMinValue(String recipeIdNew, String recipeIdOld);
    Integer copyMaxValue(String recipeIdNew, String recipeIdOld);
    List<String> recipeCodeList();
    List<RmsRecipe> recipePermitList(String eqpId,String recipeCode,String startDate,String endDate,String versionType);
    List<RmsRecipe> getRecipePermitList();
    boolean editStatus(String id, String status);
    boolean delete(String id);

}
