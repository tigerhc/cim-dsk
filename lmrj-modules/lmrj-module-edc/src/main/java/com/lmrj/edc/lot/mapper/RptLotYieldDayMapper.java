package com.lmrj.edc.lot.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.edc.lot.entity.RptLotYieldDay;
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

 @Select("SELECT production_no,lot_no,(MAX(lot_yield) - MIN(lot_yield)) AS lot_yield_eqp FROM edc_dsk_log_production WHERE start_time BETWEEN #{startTime} AND #{endTime} GROUP BY production_no,lot_no")
 List<RptLotYieldDay> findDayYeild(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

 @Select("select production_name from aps_plan_pdt_yield where production_no=#{productionNo} LIMIT 1")
 String findProductionName(@Param("productionNo") String productionNo);
 @Select("select lot_yield from mes_lot_track where lot_no=#{lotNo} LIMIT 1")
 Integer findLotYield(@Param("lotNo") String lotNo);
}
