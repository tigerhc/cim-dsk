package com.lmrj.edc.param.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.edc.param.entity.EdcEqpLogParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.param.mapper
* @title: edc_eqp_log_param数据库控制层接口
* @description: edc_eqp_log_param数据库控制层接口
 * @author: 高雪君
 * @date: 2021-03-30 09:05:34
 * @copyright: 2020 www.lmrj.com Inc. All rights reserved.
*/
@Mapper
public interface EdcEqpLogParamMapper extends BaseMapper<EdcEqpLogParam> {
    @Select("select * from edc_eqp_log_param order by eqp_id")
    List<EdcEqpLogParam> findLogParamList();

    @Select("select DISTINCT eqp_id from edc_eqp_log_param order by eqp_id")
    List<String> findParamEqpId();
}