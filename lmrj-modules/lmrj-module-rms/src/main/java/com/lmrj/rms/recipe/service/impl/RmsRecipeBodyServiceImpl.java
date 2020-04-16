package com.lmrj.rms.recipe.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.rms.recipe.mapper.RmsRecipeBodyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.recipe.service.impl
* @title: rms_recipe_body服务实现
* @description: rms_recipe_body服务实现
* @author: zhangweijiang
* @date: 2019-06-15 01:58:21
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rmsRecipeBodyService")
public class RmsRecipeBodyServiceImpl  extends CommonServiceImpl<RmsRecipeBodyMapper,RmsRecipeBody> implements  IRmsRecipeBodyService {

    @Override
    public int copyParaFromExist(String recipeIdNew, String recipeIdOld) {
        return baseMapper.copy(recipeIdNew, recipeIdOld);
    }
}