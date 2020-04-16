package com.lmrj.edc.state.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.edc.state.entity.RptEqpStateDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.state.mapper
 * @title: rpt_eqp_state_day数据库控制层接口
 * @description: rpt_eqp_state_day数据库控制层接口
 * @author: 张伟江
 * @date: 2020-02-20 01:26:27
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface RptEqpStateDayMapper extends BaseMapper<RptEqpStateDay> {
    List findEqpOee(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("eqpIds") List eqpIds);
    List findEqpsOee(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("eqpIds") List eqpIds);
}
