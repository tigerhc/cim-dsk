package com.lmrj.mes.measure.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.mes.measure.entity.MeasureSma;

import java.util.List;
import java.util.Map;

public interface MeasureSmaService extends ICommonService<MeasureSma> {
    List<Map<String, String>> findProductionNo(String type);

    List findSmaNumber(String productionName, String number, String startDate, String endDate, String type, String local);
}
