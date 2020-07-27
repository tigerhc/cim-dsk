package com.lmrj.rms.log.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rms.log.service.IRmsRecipeLogService;
import com.lmrj.rms.log.entity.RmsRecipeLog;
import com.lmrj.rms.log.mapper.RmsRecipeLogMapper;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.log.service.impl
* @title: rms_recipe_log服务实现
* @description: rms_recipe_log服务实现
* @author: 张伟江
* @date: 2020-07-07 16:10:43
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rmsRecipeLogService")
public class RmsRecipeLogServiceImpl  extends CommonServiceImpl<RmsRecipeLogMapper,RmsRecipeLog> implements  IRmsRecipeLogService {

    @Override
    public void addLog(RmsRecipe recipe, String eventCode, String eqpId) {
        RmsRecipeLog recipeLog = new RmsRecipeLog();
        recipeLog.setEventCode(eventCode);
        recipeLog.setEqpId(eqpId);
        recipeLog.setEqpModelId(recipe.getEqpModelId());
        recipeLog.setEqpModelName(recipe.getEqpModelName());
        recipeLog.setRecipeCode(recipe.getRecipeCode());
        recipeLog.setRecipeName(recipe.getRecipeName());
        recipeLog.setVersionNo(recipe.getVersionNo());
        recipeLog.setVersionType(recipe.getVersionType());
        baseMapper.insert(recipeLog);
    }
}
