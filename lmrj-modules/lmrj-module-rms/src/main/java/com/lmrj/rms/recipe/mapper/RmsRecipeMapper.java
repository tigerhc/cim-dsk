package com.lmrj.rms.recipe.mapper;

import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

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

 RmsRecipe findLastByRecipeCode(@Param("id") String id, @Param("recipeCode") String recipeCode, @Param("eqpModelId") String eqpModelId, @Param("versionType") String versionType);
 String findMaxVersionNo(@Param("recipeCode") String recipeCode, @Param("eqpId") String eqpId, @Param("eqpModelId") String eqpModelId, @Param("versionType") String versionType);
 List<String> recipeCodeList();
 List<RmsRecipe> recipePermitList(@Param("roleIdList") List<String> roleIdList, @Param("eqpId") String eqpId, @Param("recipeCode") String recipeCode, @Param("start") Date start, @Param("end") Date end, @Param("versionType") String versionType);
}
