package com.lmrj.oven.batchlot.service;

import com.lmrj.oven.batchlot.entity.OvnBatchLotDay;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IOvnBatchLotDayService {

    List<OvnBatchLotDay> findDetail(String eqpId,String start, String end);

    List<OvnBatchLotDay> selectTime(String periodDate);

    List<OvnBatchLotDay> selectMaxMin(String eqpId,String periodDate);

    List<OvnBatchLotDay> selecTearlyData(String eqpId, String periodDate);

    List<OvnBatchLotDay> selectLateData(String eqpId, String periodDate);

    Integer oldData(String periodDate);

    void fParamToDay(String eqpId, String periodDate);
}
