package com.lmrj.edc.lot.mapper;

import com.lmrj.edc.lot.entity.RptLotYieldDay;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.lot.mapper
 * @title: rpt_lot_yield_day数据库控制层接口
 * @description: rpt_lot_yield_day数据库控制层接口
 * @author: 张伟江
 * @date: 2020-05-17 21:10:56
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface RptLotYieldDayMapper extends BaseMapper<RptLotYieldDay> {

 List<Map> selectlotpdt(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("officeId") String lineNo);
 List<Map> selectDaypdt(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("officeId") String lineNo);

}