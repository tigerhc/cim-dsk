package com.lmrj.dsk.eqplog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipeBody;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.mapper
 * @title: edc_dsk_log_recipe_body数据库控制层接口
 * @description: edc_dsk_log_recipe_body数据库控制层接口
 * @author: 张伟江
 * @date: 2020-04-17 17:21:46
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface EdcDskLogRecipeBodyMapper extends BaseMapper<EdcDskLogRecipeBody> {
 @Select("select * from edc_dsk_log_recipe_body where recipe_log_id = #{recipeLogId} order by sort_no")
 List<EdcDskLogRecipeBody> selectParamList(@Param("recipeLogId") String recipeLogId);
}