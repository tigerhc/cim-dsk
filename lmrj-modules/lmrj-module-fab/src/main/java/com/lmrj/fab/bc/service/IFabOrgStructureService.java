package com.lmrj.fab.bc.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.bc.entity.FabOrgStructure;

public interface IFabOrgStructureService extends ICommonService<FabOrgStructure> {
    String synchro(String orgCode);
}
