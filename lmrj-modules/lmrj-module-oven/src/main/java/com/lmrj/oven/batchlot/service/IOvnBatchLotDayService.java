package com.lmrj.oven.batchlot.service;

import com.lmrj.oven.batchlot.entity.OvnBatchLotDay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IOvnBatchLotDayService {

    List<OvnBatchLotDay> findDetail(String eqpId);

    List<OvnBatchLotDay> selectTime(String periodDate);
}
