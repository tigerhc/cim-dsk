package com.lmrj.oven.batchlot.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.FabEquipmentOvenStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.mapper
 * @title: ovn_batch_lot数据库控制层接口
 * @description: ovn_batch_lot数据库控制层接口
 * @author: zhangweijiang
 * @date: 2019-06-09 08:49:15
 * @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
 */
@Mapper
public interface OvnBatchLotMapper extends BaseMapper<OvnBatchLot> {

     List<FabEquipmentOvenStatus> selectFabStatus(@Param("officeId") String officeId);

     List<Map> selectFabStatusParam(List<FabEquipmentOvenStatus> fabEquipmentOvenStatusList);

    List<Map> selectChartByCase(String officeId);

    int findCountBytime(@Param("eqpId") String eqpId,@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<Map> findDetailBytime(@Param("eqpId") String eqpId,@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<Map> findDetailBytimeOther(@Param("eqpId") String eqpId, @Param("lotNo") String lotNo);

    List<Map<String, Object>> findToEqpId(@Param("dateStr")String dateStr);

    int saveEqp(@Param("eqps")List<Map<String,Object>> eqps);

    int saveTempParam(@Param("temps")List<Map<String,Object>> temps);

    @Select("select * from ovn_batch_lot where eqp_id = #{eqpId} and start_time > #{startTime} ORDER BY create_date desc limit 1")
    OvnBatchLot findBatchData(String eqpId , Date startTime);

    @Select("select * from ovn_batch_lot where eqp_id = #{eqpId} and lot_id = #{lotNo} ORDER BY create_date desc limit 1")
    OvnBatchLot findBatchDataByLot(String eqpId , String lotNo);


    @Select("select * from ovn_batch_lot where start_time between #{beginTime}  and #{endTime} order by eqp_id , create_date")
    List<OvnBatchLot> findDataByTime(@Param("beginTime")String beginTime, @Param("endTime")String endTime);

    List<String> lastDayEqpList(@Param( "startTime" ) Date startTime,@Param( "endTime" )Date endTime);
}
