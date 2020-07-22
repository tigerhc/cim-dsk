package com.lmrj.rms.recipe.mapper;

import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.recipe.mapper
 * @title: rms_recipe_body数据库控制层接口
 * @description: rms_recipe_body数据库控制层接口
 * @author: zhangweijiang
 * @date: 2019-06-15 01:58:21
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface RmsRecipeBodyMapper extends BaseMapper<RmsRecipeBody> {
    int copy(@Param("recipeIdNew") String recipeIdNew, @Param("recipeIdOld") String recipeIdOld);
    int copyMinValue(@Param("recipeIdNew") String recipeIdNew, @Param("recipeIdOld") String recipeIdOld);
    int copyMaxValue(@Param("recipeIdNew") String recipeIdNew, @Param("recipeIdOld") String recipeIdOld);
}
