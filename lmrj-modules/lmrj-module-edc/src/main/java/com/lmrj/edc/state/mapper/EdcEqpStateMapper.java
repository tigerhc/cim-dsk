package com.lmrj.edc.state.mapper;

import com.lmrj.edc.state.entity.EdcEqpState;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.state.mapper
 * @title: edc_eqp_state数据库控制层接口
 * @description: edc_eqp_state数据库控制层接口
 * @author: 张伟江
 * @date: 2020-02-20 01:26:46
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface EdcEqpStateMapper extends BaseMapper<EdcEqpState> {

     List<EdcEqpState> getAllByTime(String time);
 }