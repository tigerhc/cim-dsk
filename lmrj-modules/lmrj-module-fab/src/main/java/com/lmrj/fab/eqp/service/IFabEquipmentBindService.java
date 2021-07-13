package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import com.lmrj.fab.eqp.entity.FabEquipmentBind;

import java.util.List;

/**
 * @author wdj
 * @date 2021-06-01 17:24
 */
public interface IFabEquipmentBindService extends ICommonService<FabEquipmentBind> {

    List<FabEquipmentBind> getIotEquipmenetBindList(String parentEqpId);

    void saveBindList(List<FabEquipmentBind> inList);

    void insertBlankInfo(List<FabModelTemplateBody> list,String eqpId);
}
