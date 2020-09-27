package com.lmrj.dsk.eqplog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.mapper
 * @title: edc_dsk_log_recipe数据库控制层接口
 * @description: edc_dsk_log_recipe数据库控制层接口
 * @author: 张伟江
 * @date: 2020-04-17 17:21:17
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface EdcDskLogRecipeMapper extends BaseMapper<EdcDskLogRecipe> {
 @Select("select id from edc_dsk_log_recipe where eqp_id=#{eqpId} and start_time< #{startTime} order by start_time desc limit 1")
 String findOldRecipeId(@Param("eqpId") String eqpId, @Param("startTime") Date startTime);

 List<Map<String,String>> findData(@Param("eqpId") String eqpId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

 String findOldData(@Param("eqpId") String eqpId, @Param("startTime") String startTime, @Param("paramName") String paramName);
}