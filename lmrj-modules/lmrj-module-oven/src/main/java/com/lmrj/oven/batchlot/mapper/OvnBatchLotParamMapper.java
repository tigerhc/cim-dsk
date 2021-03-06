package com.lmrj.oven.batchlot.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
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
 * @title: ovn_batch_lot_param数据库控制层接口
 * @description: ovn_batch_lot_param数据库控制层接口
 * @author: zhangweijiang
 * @date: 2019-06-09 08:55:13
 * @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
 */
@Mapper
public interface OvnBatchLotParamMapper extends BaseMapper<OvnBatchLotParam> {

 @Select("select * from ovn_batch_lot_param where create_date between #{startTime} and #{endTime}")
 List<OvnBatchLotParam> selectTempData(java.util.Date startTime, java.util.Date endTime);

 @Select("select * from ovn_batch_lot_param where batch_id = #{batchId}  order by create_date")
 List<OvnBatchLotParam> selectDataBybatchId(@Param("batchId")String batchId);

 @Select("select * from ovn_batch_lot_param where batch_id in (select id from ovn_batch_lot where start_time < #{endTime}  and end_time > #{endTime}  and eqp_id = #{eqpId}) and  create_date >  #{endTime}  ORDER BY create_date  limit 1")
 OvnBatchLotParam selectDataBytime(@Param("eqpId")String eqpId,@Param( "endTime" ) Date endTime);


 @Select("select * from ovn_batch_lot_param where batch_id in (select id from ovn_batch_lot where end_time > #{endTime}  and eqp_id = #{eqpId}) and  create_date >  #{endTime} and  temp_pv > 109.9  ORDER BY create_date  limit 1")
 OvnBatchLotParam selectDataBytemp(@Param("eqpId")String eqpId,@Param( "endTime" ) Date endTime);

 List<OvnBatchLotParam> selectDataByeqpId(@Param("eqpId")String eqpId,@Param( "beginTime" ) String beginTime ,@Param( "endTime" ) String endTime);

 List<Map> fParamToDayone(@Param("id")String id, @Param( "startTime" ) Date startTime, @Param( "endTime" )Date endTime, @Param( "periodDate" )String periodDate,
                          @Param("eqpId")String eqpId,@Param("eqpTemp")String eqpTemp);

 List<Map> newfParamToDayone(@Param("id")String id, @Param( "startTime" ) Date startTime, @Param( "endTime" )Date endTime, @Param( "periodDate" )String periodDate,
                          @Param("eqpId")String eqpId,@Param("eqpTemp")String eqpTemp);
}