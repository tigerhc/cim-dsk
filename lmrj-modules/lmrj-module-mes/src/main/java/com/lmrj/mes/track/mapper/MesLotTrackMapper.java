package com.lmrj.mes.track.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.track.entity.MesLotTrack;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.mapper
 * @title: mes_lot_track数据库控制层接口
 * @description: mes_lot_track数据库控制层接口
 * @author: 张伟江
 * @date: 2020-04-28 14:03:17
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface MesLotTrackMapper extends BaseMapper<MesLotTrack> {
 @Select("select lot_no,start_time,end_time from mes_lot_track where start_time <= #{startTime}  and end_time>= #{startTime} and end_time is not null and eqp_id = #{eqpId} limit 1 ")
 MesLotTrack findLotNo(@Param("startTime") String startTime, @Param("eqpId") String eqpId);

 @Select("select start_time from mes_lot_track where start_time >= #{endTime}  and eqp_id = #{eqpId} order by start_time  limit 1 ")
 MesLotTrack findNextStartTime(@Param("endTime") String endTime, @Param("eqpId") String eqpId);

 /*@Select("select * from mes_lot_track where  (PRODUCTION_NO, LOT_NO, start_time ) in (\n" +
         "SELECT PRODUCTION_NO, LOT_NO ,  max(start_time) FROM mes_lot_track WHERE  ( PRODUCTION_NO, LOT_NO )  NOT IN (\n" +
         "select DISTINCT PRODUCTION_NO, LOT_NO from mes_lot_track where eqp_id LIKE '%TRM%' AND END_TIME IS NOT NULL and create_date between #{startTime} and #{endTime})\n" +
         "and create_date between #{startTime} and #{endTime} \n" +
         "group by PRODUCTION_NO,LOT_NO)")*/
 List<MesLotTrack> findIncompleteLotNo(@Param("startTime") Date startTime,@Param("endTime") Date endTime);

 @Insert("Insert into mes_lot_wip(id,eqp_id,lot_no,production_name,production_no,order_no,lot_yield,lot_yield_eqp,start_time,end_time,remarks,create_by,create_date,update_by,update_date,del_flag) values(" +
         "#{id},#{eqpId},#{lotNo},#{productionName},#{productionNo},#{orderNo},#{lotYield},#{lotYieldEqp},#{startTime},#{endTime},#{remarks},#{createBy},#{createDate},null,null,false" + ")")
 Boolean insterWip(@Param("id") String id, @Param("eqpId") String eqpId, @Param("lotNo") String lotNo, @Param("productionName") String productionName, @Param("productionNo") String productionNo, @Param("orderNo") String orderNo, @Param("lotYield") Integer lotYield
         , @Param("lotYieldEqp") Integer lotYieldEqp, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("remarks") String remarks, @Param("createBy") String createBy, @Param("createDate") Date createDate);

 @Select("select * from mes_lot_wip where eqp_id = #{eqpId} and production_no = #{productionNo}")
 MesLotTrack finddata(@Param("eqpId") String eqpId, @Param("productionNo") String productionNo);

 @Update("update mes_lot_wip set lot_yield=#{lotYield},lot_yield_eqp=#{lotYieldEqp}  where eqp_id = #{eqpId} and production_no = #{productionNo}")
 Boolean updateWip(@Param("lotYield") Integer lotYield, @Param("lotYieldEqp") Integer lotYieldEqp, @Param("eqpId") String eqpId, @Param("productionNo") String productionNo);

 @Select("select DISTINCT PRODUCTION_NO from mes_lot_track where eqp_id LIKE '%SIM-TRM%' AND END_TIME IS NOT NULL and lot_no = #{lotNo} and production_no = #{productionNo}")
 String selectEndData(@Param("lotNo") String lotNo, @Param("productionNo") String productionNo);

 @Delete("delete from mes_lot_wip where lot_no = #{lotNo} and production_no = #{productionNo}")
 Boolean deleteEndData(@Param("lotNo") String lotNo, @Param("productionNo") String productionNo);

 @Select("select * from mes_lot_wip")
 List<MesLotTrack> selectWip();
}