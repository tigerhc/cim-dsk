package com.lmrj.mes.measure.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.mes.measure.entity.measureSx;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface MeasureSxService extends ICommonService<measureSx> {
    List<Map<String, String>> findProductionNo(String type);

    List findSxNumber(String productionName, String number,String startDate, String endDate,String type);
}
