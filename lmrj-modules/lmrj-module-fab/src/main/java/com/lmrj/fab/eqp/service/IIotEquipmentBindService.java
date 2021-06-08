package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import com.lmrj.fab.eqp.entity.IotEquipmentBind;

import java.util.List;

/**
 * @author wdj
 * @date 2021-06-01 17:24
 */
public interface IIotEquipmentBindService extends ICommonService<IotEquipmentBind> {

    List<IotEquipmentBind> getIotEquipmenetBindList(String parentEqpId);

    void saveBindList(List<IotEquipmentBind> inList);

    void insertBlankInfo(List<FabModelTemplateBody> list,String eqpId);
}
