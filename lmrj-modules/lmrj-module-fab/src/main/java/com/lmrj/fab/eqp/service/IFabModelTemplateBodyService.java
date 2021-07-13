package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabModelTemplate;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 9:13
 */
public interface IFabModelTemplateBodyService  extends ICommonService<FabModelTemplateBody> {

    List<Map> getOneTemplateList(String id);

    void deleteAndSave(List<FabModelTemplateBody> list, FabModelTemplate template);

    List<FabModelTemplateBody> getNoBindInfo(String eqpId);

    List<String> getSubAndName(String Id);

    void chageName(String id,String name);
}
