package com.lmrj.rms.recipe.mapper;

import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.recipe.mapper
 * @title: rms_recipe数据库控制层接口
 * @description: rms_recipe数据库控制层接口
 * @author: zhangweijiang
 * @date: 2019-06-15 01:58:00
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface RmsRecipeMapper extends BaseMapper<RmsRecipe> {

 RmsRecipe findLastByRecipeCode(@Param("id") String id, @Param("recipeCode") String recipeCode, @Param("eqpModelId") String eqpModelId);
 String findMaxVersionNo(@Param("recipeCode") String recipeCode, @Param("eqpId") String eqpId, @Param("eqpModelId") String eqpModelId, @Param("versionType") String versionType);
}