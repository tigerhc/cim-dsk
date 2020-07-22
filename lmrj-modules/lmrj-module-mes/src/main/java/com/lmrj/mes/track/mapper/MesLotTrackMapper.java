package com.lmrj.mes.track.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.track.entity.MesLotTrack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

 @Select("select lot_no from edc_dsk_log_production where start_time  between #{startTime} and #{endTime} ")
 String findLotNo1(@Param("startTime") String startTime, @Param("endTime") String endTime);

}
