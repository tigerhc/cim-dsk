package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabModelTemplate;

import java.util.List;

/**
 * @author wdj
 * @date 2021-06-01 9:13
 */
public interface IFabModelTemplateService  extends ICommonService<FabModelTemplate> {

    String getName(String id);

    FabModelTemplate insertOrUpdate(FabModelTemplate fabModelTemplate,String a);

    void deleteAll(String classCode);

}
