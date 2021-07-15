package com.lmrj.oven.batchlot.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.oven.batchlot.entity.OvnBatchLotDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OvnBatchLotDayMapper {

    List<OvnBatchLotDay> findDetail(@Param("eqpId") String eqpId);

    List<OvnBatchLotDay> selectTime(@Param( "periodDate" ) String periodDate);

}
