package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabSensorModel;

import java.util.List;
import java.util.Map;

public interface IFabSensorModelService extends ICommonService<FabSensorModel> {
    public List<Map> findLookup();
    List<String> manufacturerNameList();
    List<String> classCodeList();
    List<String> noTemClassCodeList();

    List<String> getTypeList(String flag, String ID);
    public List<Map> getAlltemplateList();
    String manufacturerName(String modelId);
}
