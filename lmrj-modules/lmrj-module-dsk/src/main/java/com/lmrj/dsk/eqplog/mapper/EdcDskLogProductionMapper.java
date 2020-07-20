package com.lmrj.dsk.eqplog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
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
 * @title: edc_dsk_log_production数据库控制层接口
 * @description: edc_dsk_log_production数据库控制层接口
 * @author: 张伟江
 * @date: 2020-04-14 10:10:00
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface EdcDskLogProductionMapper extends BaseMapper<EdcDskLogProduction> {

 @Select("select * from edc_dsk_log_production where  eqp_id= #{eqpId} and start_time  >= #{startTime} limit 1")
 EdcDskLogProduction findNextYield(@Param("eqpId") String eqpId, @Param("startTime") Date startTime);

 @Select("select * from edc_dsk_log_production where eqp_id = #{eqpId} and start_time  between #{startTime} and #{endTime}  order by start_time limit 1000")
 List<EdcDskLogProduction> findYields(@Param("eqpId") String eqpId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

 @Select("select max(lot_yield) lot_yield  from edc_dsk_log_production where eqp_id=#{eqpId} and production_no=#{productionNo} and lot_no=#{lotNo}")
 Integer findNewYieldByLot(String eqpId, String productionNo, String lotNo);

 @Select("select lot_no,start_time,end_time from mes_lot_track where start_time <= #{startTime}  and end_time>= #{startTime} and end_time is not null and eqp_id = #{eqpId} ")
 EdcDskLogProduction findLotNo(@Param("startTime") String startTime,@Param("eqpId") String eqpId);

 @Select("select lot_no from edc_dsk_log_production where start_time  between #{startTime} and #{endTime} ")
 String findLotNo1(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
