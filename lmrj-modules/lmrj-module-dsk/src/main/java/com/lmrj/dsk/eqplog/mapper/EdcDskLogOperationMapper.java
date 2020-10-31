package com.lmrj.dsk.eqplog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.mapper
 * @title: edc_dsk_log_operation数据库控制层接口
 * @description: edc_dsk_log_operation数据库控制层接口
 * @author: 张伟江
 * @date: 2020-04-14 10:10:16
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface EdcDskLogOperationMapper extends BaseMapper<EdcDskLogOperation> {
    @Select("select * from edc_dsk_log_operation where eqp_id=#{eqpId} and start_time between #{startTime} and #{endTime} order by start_time")
    List<EdcDskLogOperation> findDataByTimeAndEqpId(@Param("eqpId") String eqpId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select distinct eqp_id from edc_dsk_log_operation where start_time between #{startTime} and #{endTime}")
    List<String> findEqpId(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int insertList(@Param("list") List<EdcDskLogOperation> list);
}