package com.lmrj.rms.recipe.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.mapper.RmsRecipeMapper;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.rms.recipe.mapper.RmsRecipeBodyMapper;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


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

@Slf4j
@Transactional
@Service("rmsRecipeBodyService")
public class RmsRecipeBodyServiceImpl  extends CommonServiceImpl<RmsRecipeBodyMapper,RmsRecipeBody> implements  IRmsRecipeBodyService {

    @Autowired
    private IRmsRecipeService recipeService;

    @Override
    public int copyParaFromExist(String recipeIdNew, String recipeIdOld) {
        return baseMapper.copy(recipeIdNew, recipeIdOld);
    }

    @Override
    public int copyMinValue(String recipeIdNew, String recipeIdOld) {
        return baseMapper.copyMinValue(recipeIdNew, recipeIdOld);
    }

    @Override
    public int copyMaxValue(String recipeIdNew, String recipeIdOld) {
        return baseMapper.copyMaxValue(recipeIdNew, recipeIdOld);
    }

    @Override
    public boolean checkRecipeBody(String recipeCode, String recipeBody, String recipeBodySize) {
        boolean flag = true;
        List<RmsRecipe> rmsRecipes = recipeService.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_code", recipeCode).eq("status", "Y").eq("del_flag", "0"));
        if (rmsRecipes.size() == 0) {
            log.info("未找到[" + recipeCode + "]对应的配方");
            return false;
        }
        List<RmsRecipeBody> recipeBodies = baseMapper.queryRecipeBody(rmsRecipes.get(0).getId());
        int size = Integer.parseInt(recipeBodySize);
        if (size > recipeBodies.size()) {
            log.info("需校验参数个数超出维护参数个数");
            return false;
        }

        for (int i = 0; i < size; i++) {
            String value = recipeBody.substring(i * 10).trim();
            if (i < size - 1) {
                value = recipeBody.substring(i * 10, (i + 1) * 10).trim();
            }
            if (!value.equals(recipeBodies.get(i).getSetValue())) {
                log.info("[" + recipeBodies.get(i).getParaName() + "]的参数值["+ value +"]校验不通过");
                flag = false;
            }
        }
        return flag;
    }
}
