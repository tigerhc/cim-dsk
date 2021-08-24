package com.lmrj.oven.batchlot.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.oven.batchlot.entity.OvnBatchLotDay;

import java.util.List;

public interface IOvnBatchLotDayService extends ICommonService<OvnBatchLotDay> {

    List<OvnBatchLotDay> findDetail(String eqpId,String start, String end);

    List<OvnBatchLotDay> selectTime(String periodDate);

    List<OvnBatchLotDay> selectMaxMin(String eqpId,String periodDate);

    List<OvnBatchLotDay> selecTearlyData(String eqpId, String periodDate);

    List<OvnBatchLotDay> selectLateData(String eqpId, String periodDate);

    Integer oldData(String periodDate);

    void fParamToDay(String eqpId, String periodDate);

    void newfParamToDay(String eqpId, String periodDate);

    List<String> getTitleByEqpId(String eqpId);
}
