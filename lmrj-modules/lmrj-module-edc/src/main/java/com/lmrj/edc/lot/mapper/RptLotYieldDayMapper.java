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

    List<Map> selectDaypdtById(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("lineNo") String lineNo, @Param("stationCode") String stationCode,@Param("eqpId") List<String> eqpId);

    List<Map> selectDaypdtByIds(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("lineNo") String lineNo, @Param("stationCode") String stationCode,@Param("eqpId") List<String> eqpId);

    @Select("select * from rpt_lot_yield_day where create_date between #{startTime} AND #{endTime} ORDER BY eqp_id")
    List<RptLotYieldDay> selectDayYieldList(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select eqp_id from fab_equipment where line_no=#{lineNo} and station_code=#{stationCode} and step_yield_flag='1' order by eqp_id ")
    List<String> findEqpId(@Param("lineNo") String lineNo, @Param("stationCode") String stationCode);

    @Select("select lot_yield from mes_lot_track where lot_no=#{lotNo} and start_time or end_time between #{startTime} AND #{endTime} and eqp_Id=#{eqpId} ORDER BY start_time DESC LIMIT 1  ")
    Integer findLotYield(@Param("eqpId") String eqpId, @Param("lotNo") String lotNo, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select distinct station_code from fab_equipment where line_no=#{lineNo}")
    List<Map<String,Object>> searchStand( @Param("lineNo") String lineNo);

    @Select("select sum(lot_yield) as lot_yield,sum(lot_yield_eqp) as lot_yield_eqp,eqp_id from rpt_lot_yield_day where station_code=#{stationId} and period_date=#{date} and line_no =#{lineNo} GROUP BY eqp_id")
    List<Map<String,Object>> findEqp( @Param("lineNo") String lineNo,@Param("stationId") String stationId,@Param("date") String date);

    @Select("select distinct station_code as value,station_code as label from fab_equipment where line_no=#{lineNo} ")
    List<Map<String,Object>> searchStandAndEqp( @Param("lineNo") String lineNo);

    @Select("select distinct eqp_id,production_name,production_no,sum(lot_yield) as lot_yield, sum(lot_yield_eqp) AS lot_yield_eqp from mes_lot_track where eqp_id=#{eqpId} and end_time between #{startTime} and #{endTime}")
    RptLotYieldDay findDayYield(@Param("eqpId") String eqpId,@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<Map<String,Object>> findSonEqp( @Param("lineNo") String lineNo,@Param("stationId") List<String> stationId);

    List<Map<String,Object>> findAllEqp(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("lineNo") String lineNo, @Param("stationCode") String stationCode);
}
