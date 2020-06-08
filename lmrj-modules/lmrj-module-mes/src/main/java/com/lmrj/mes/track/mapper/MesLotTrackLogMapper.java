package com.lmrj.mes.track.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.mapper
 * @title: mes_lot_track_log数据库控制层接口
 * @description: mes_lot_track_log数据库控制层接口
 * @author: 张伟江
 * @date: 2020-04-28 14:03:29
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface MesLotTrackLogMapper extends BaseMapper<MesLotTrackLog> {

    @Select("select * from  mes_lot_track_log where CONCAT(lot_no,create_date) in (select CONCAT(lot_no, max(create_date)) from mes_lot_track_log where create_date > #{startTime}  group by lot_no)")
    List<MesLotTrackLog> findLatestLotEqp(@Param("startTime") Date startTime);
}
