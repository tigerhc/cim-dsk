package com.lmrj.mes.measure.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.mes.measure.entity.MeasureSim;

import java.util.List;
import java.util.Map;

public interface MeasureSimService extends ICommonService<MeasureSim> {
    List<Map<String, String>> findProductionNo(String type);

    List findSimNumber(String productionName, String number, String startDate, String endDate, String type, String local);
}
