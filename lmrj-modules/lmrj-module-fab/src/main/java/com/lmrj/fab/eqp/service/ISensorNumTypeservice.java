package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.SensorNumType;

import java.util.List;

/**
 * @author wdj
 * @date 2021-06-02 16:38
 */
public interface ISensorNumTypeservice extends ICommonService<SensorNumType> {
    List<String> getNumtypeList(String classCode);

    String getNumType(String id);

    String getTypeId(String classCode,String numType);
}
