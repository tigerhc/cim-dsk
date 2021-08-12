package com.lmrj.oven.batchlot.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.oven.batchlot.entity.OvnBatchLotDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OvnBatchLotDayMapper extends BaseMapper<OvnBatchLotDay>{
    /*一天一条数据*/
    List<OvnBatchLotDay> findDetail(@Param("eqpId") String eqpId,@Param( "start" ) String start,@Param( "end" ) String end);
    /*当天所有数据*/
    List<OvnBatchLotDay> selectTime(@Param( "periodDate" ) String periodDate);
    /*当天的最大最小值*/
    List<OvnBatchLotDay> selectMaxMin(@Param("eqpId") String eqpId,@Param( "periodDate" ) String periodDate);
    /*当天最早的数据*/
    List<OvnBatchLotDay> selecTearlyData(@Param("eqpId") String eqpId,@Param( "periodDate" ) String periodDate);
    /*当天最晚的数据*/
    List<OvnBatchLotDay> selectLateData(@Param("eqpId") String eqpId,@Param( "periodDate" ) String periodDate);
    /*前一天的数据汇总*/
    Integer oldData(@Param( "createDate" ) String createDate);

    List<Map> fParamToDay(@Param( "list" )List<Map> list,@Param( "startTime" ) Date startTime,@Param( "endTime" )Date endTime,@Param( "periodDate" )String periodDate);
<<<<<<< .merge_file_a13852
=======

    List<String> selectTitle(String eqpId);
>>>>>>> .merge_file_a23464
}
