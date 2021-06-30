package com.lmrj.ms.thrust.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.ms.thrust.entity.MsMeasureThrust;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.record.mapper
 * @title: ms_measure_record数据库控制层接口
 * @description: ms_measure_record数据库控制层接口
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface MsMeasureThrustMapper extends BaseMapper<MsMeasureThrust> {
    @Select("select * from ms_measure_thrust where production_name = #{produictionName}  and create_date between #{startTime} and #{endTime} order by create_date limit 31")
    List<MsMeasureThrust> findDataByTime(@Param("produictionName") String produictionName ,@Param("startTime") String startTime ,@Param("endTime") String endTime);

}
