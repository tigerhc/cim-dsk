package com.lmrj.mes.track.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.track.entity.MesLotTrack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Select("select * from mes_lot_track where start_time >= #{endTime}  and eqp_id = #{eqpId} order by start_time  limit 1 ")
    MesLotTrack findNextStartTime(@Param("endTime") String endTime, @Param("eqpId") String eqpId);

    @Select("select * from mes_lot_track where eqp_id = #{eqpId} and lot_no > #{lotNo} and start_time > #{startTime} order by start_time limit 1")
    MesLotTrack findLastTrack(@Param("eqpId") String eqpId, @Param("lotNo") String lotNo, @Param("startTime") Date startTime);

    List<MesLotTrack> findDataLotNo(@Param("eqpId") String eqpId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select * from mes_lot_track where eqp_id = #{eqpId} and start_time <#{startTime} and end_time is null order by start_time desc limit 1  ")
    MesLotTrack findNoEndLotNo(@Param("eqpId") String eqpId, @Param("startTime") Date startTime);

    @Select("select * from mes_lot_track where eqp_id=#{eqpId} and start_time <= #{startTime} order by start_time desc limit 1")
    MesLotTrack findLotNo1(@Param("eqpId") String eqpId, @Param("startTime") Date startTime);

    @Update("update mes_lot_track set lot_yield_eqp=#{lotYieldEqp} where eqp_id=#{eqpId} and lot_no=#{lotNo} ")
    Boolean updateTrackLotYeildEqp(@Param("eqpId") String eqpId, @Param("lotNo") String lotNo, @Param("lotYieldEqp") Integer lotYieldEqp);

    @Select("select * from mes_lot_track where eqp_id=#{eqpId} AND lot_no>#{lotNo} ORDER BY lot_no limit 1")
    MesLotTrack selectEndTime(@Param("eqpId") String eqpId, @Param("lotNo") String lotNo);

    @Select("select * from mes_lot_track where start_time between #{startTime} and #{endTime} order by eqp_id,lot_no")
    List<MesLotTrack> findCorrectData(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select * from mes_lot_track where eqp_id=#{eqpId} and start_time<=#{startTime} order by start_time desc limit 1")
    MesLotTrack findLotByStartTime(@Param("eqpId") String eqpId, @Param("startTime") Date startTime);

    @Select("select * from mes_lot_track where eqp_id=#{eqpId} and lot_no=#{lotNo} and production_no=#{productionNo}")
    MesLotTrack findLotTrack(@Param("eqpId") String eqpId, @Param("lotNo") String lotNo, @Param("productionNo") String productionNo);

    @Select("select * from mes_lot_track where eqp_id like concat(#{eqpId},'%') and start_time between #{startTime} and #{endTime} group by lot_no order by eqp_id,start_time")
    List<Map> lotTrackQuery(@Param("lineNo") String lineNo, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select(" select  production_name  from aps_plan_pdt_yield " +
            "where production_name like concat('%',#{proName},'%') ORDER BY create_date desc limit 1")
    String findProName(@Param("proName") String proName);

    @Select("SELECT DISTINCT production_name from ms_measure_kongdong " +
            "where production_name like concat('%',#{proName},'%') ORDER BY create_date desc")
    List<String> findAllProName(@Param("proName") String proName);

    @Select(" select  line_type as 'lineType', height_lmt as 'heightLmt' from mes_kongdong_config " +
            "where eqp_id=#{eqpId} and del_flag='0'")
    List<Map<String, Object>> findkongdongConfig(@Param("eqpId") String eqpId);

    @Select("select * from mes_lot_track where eqp_id=#{eqpId} and end_time between #{startTime} and #{endTime} order by start_time ")
    List<MesLotTrack> findLotsByTime(@Param("eqpId") String eqpId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select * from mes_lot_track where eqp_id=#{eqpId} order by start_time desc limit 1")
    MesLotTrack findNowLotByEqp(@Param("eqpId") String eqpId);
}
