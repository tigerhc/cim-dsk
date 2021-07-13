package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabNumType;

import java.util.List;

/**
 * @author wdj
 * @date 2021-06-02 16:38
 */
public interface IFabNumTypeservice extends ICommonService<FabNumType> {
    List<String> getNumtypeList(String classCode);

    String getNumType(String id);

    String getTypeId(String classCode,String numType);
}
