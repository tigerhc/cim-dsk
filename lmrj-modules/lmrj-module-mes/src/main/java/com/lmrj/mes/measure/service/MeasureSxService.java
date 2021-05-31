package com.lmrj.mes.measure.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.mes.measure.entity.MeasureSx;

import java.util.List;
import java.util.Map;

public interface MeasureSxService extends ICommonService<MeasureSx> {
    List<Map<String, String>> findProductionNo(String type, String lineNo);

    List findSxNumber(String productionName, String number,String startDate, String endDate,String type,String local);

    List findSimNumber(String productionName, String number,String startDate, String endDate,String type,String local);

    List findGiNumber(String productionName, String number,String startDate, String endDate,String type,String local);
}
