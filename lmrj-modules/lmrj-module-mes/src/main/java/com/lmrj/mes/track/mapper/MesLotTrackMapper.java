package com.lmrj.mes.track.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.track.entity.MesLotTrack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Select("select * from mes_lot_track where eqp_id = #{eqpId} and lot_no > #{lotNo} and start_time > #{startTime} order by start_time limit 1")
    MesLotTrack findLastTrack(@Param("eqpId") String eqpId, @Param("lotNo") String lotNo, @Param("startTime") Date startTime);

    List<MesLotTrack> findDataLotNo(@Param("eqpId") String eqpId,@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select * from mes_lot_track where eqp_id = #{eqpId} and start_time <#{startTime} and end_time is null order by start_time desc limit 1  ")
    MesLotTrack findNoEndLotNo(@Param("eqpId") String eqpId,@Param("startTime") Date startTime);
    @Select("select * from mes_lot_track where eqp_id=#{eqpId} and start_time <= #{startTime} order by start_time desc limit 1")
    MesLotTrack findLotNo1(@Param("eqpId") String eqpId,@Param("startTime") Date startTime);

    @Update("update mes_lot_track set lot_yield_eqp=#{lotYieldEqp} where eqp_id=#{eqpId} and lot_no=#{lotNo} ")
    Boolean updateTrackLotYeildEqp(@Param("eqpId") String eqpId,@Param("lotNo") String lotNo,@Param("lotYieldEqp") Integer lotYieldEqp);

    @Select("select * from mes_lot_track where eqp_id=#{eqpId} AND lot_no>#{lotNo} ORDER BY lot_no limit 1")
    MesLotTrack selectEndTime(@Param("eqpId") String eqpId,@Param("lotNo") String lotNo);

    @Select("select * from mes_lot_track where start_time between #{startTime} and #{endTime} order by lot_no")
    List<MesLotTrack> findCorrectData(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select * from mes_lot_track where eqp_id=#{eqpId} and start_time<=#{startTime} order by start_time desc limit 1")
    MesLotTrack findLotByStartTime(@Param("eqpId") String eqpId,@Param("startTime") Date startTime);

    @Select("select * from mes_lot_track where eqp_id=#{eqpId} and lot_no=#{lotNo} and production_no=#{productionNo}")
    MesLotTrack findLotTrack(@Param("eqpId") String eqpId,@Param("lotNo") String lotNo,@Param("productionNo") String productionNo);
}
