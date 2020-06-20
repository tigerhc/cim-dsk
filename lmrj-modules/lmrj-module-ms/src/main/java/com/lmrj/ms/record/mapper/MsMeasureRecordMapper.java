package com.lmrj.ms.record.mapper;

import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
public interface MsMeasureRecordMapper extends BaseMapper<MsMeasureRecord> {

 List<Map> findDetailBytime(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("eqpId") String eqpId);
}
