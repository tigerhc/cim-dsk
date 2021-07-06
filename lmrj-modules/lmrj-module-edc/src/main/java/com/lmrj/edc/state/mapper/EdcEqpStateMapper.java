package com.lmrj.edc.state.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.edc.state.entity.EdcEqpState;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.HashMap;
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

    List<EdcEqpState> getAllByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime,@Param("eqpId") String eqpId);
    List<EdcEqpState> calEqpSateDay(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
    EdcEqpState findLastData(@Param("startTime") Date startTime,@Param("eqpId") String eqpId);
    @Select("select DISTINCT eqp_id from edc_eqp_state where start_time BETWEEN #{startTime} and #{endTime}")
    List<String> findEqpId(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
    List<EdcEqpState> findWrongEqpList(@Param("eqpId") String eqpId,@Param("startTime") Date startTime, @Param("endTime") Date endTime);
    EdcEqpState findNewData(@Param("startTime") Date startTime,@Param("eqpId") String eqpId);
    List<HashMap<String, Object>> eqpStateTime(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("arr")String[] arr);
    @Select("select * from edc_eqp_state where start_time = #{startTime} and eqp_id = #{eqpId} limit 1")
    EdcEqpState findFirstData(@Param("startTime") Date startTime,@Param("eqpId") String eqpId);
}
