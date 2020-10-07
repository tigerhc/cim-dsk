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

    List<Map> selectDaypdt(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("lineNo") String lineNo, @Param("stationCode") String stationCode);

    List<Map> selectDaypdtById(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("lineNo") String lineNo, @Param("stationCode") String stationCode,@Param("eqpId") String eqpId);

    @Select("SELECT production_no,lot_no,(MAX(lot_yield) - MIN(lot_yield)) AS lot_yield_eqp FROM edc_dsk_log_production WHERE start_time BETWEEN #{startTime} AND #{endTime} and eqp_id = #{eqpId}  GROUP BY production_no,lot_no")
    List<RptLotYieldDay> findDayYeild(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("eqpId") String eqpId);

    @Select("select eqp_id from fab_equipment where line_no=#{lineNo} and station_code=#{stationCode}")
    List<String> findEqpId(@Param("lineNo") String lineNo, @Param("stationCode") String stationCode);

    @Select("select production_name from aps_plan_pdt_yield where production_no=#{productionNo} LIMIT 1")
    String findProductionName(@Param("productionNo") String productionNo);

    @Select("select lot_yield from mes_lot_track where lot_no=#{lotNo} and start_time or end_time between #{startTime} AND #{endTime} and eqp_Id=#{eqpId} ORDER BY start_time DESC LIMIT 1  ")
    Integer findLotYield(@Param("eqpId") String eqpId, @Param("lotNo") String lotNo, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select distinct station_code from fab_equipment where line_no=#{lineNo}")
    List<Map<String,Object>> searchStand( @Param("lineNo") String lineNo);

    @Select("select sum(lot_yield) as lot_yield,sum(lot_yield_eqp) as lot_yield_eqp,eqp_id from rpt_lot_yield_day where station_code=#{stationId} and period_date=#{date} and line_no =#{lineNo} GROUP BY eqp_id")
    List<Map<String,Object>> findEqp( @Param("lineNo") String lineNo,@Param("stationId") String stationId,@Param("date") String date);

    @Select("select distinct station_code as value,station_code as label from fab_equipment where line_no=#{lineNo} ")
    List<Map<String,Object>> searchStandAndEqp( @Param("lineNo") String lineNo);

    List<Map<String,Object>> findSonEqp( @Param("lineNo") String lineNo,@Param("stationId") List<String> stationId);

    List<Map<String,Object>> findAllEqp(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("lineNo") String lineNo, @Param("stationCode") String stationCode);
}
