package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;

import java.util.List;
import java.util.Map;

/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.fab.service
* @title: fab_equipment_model服务接口
* @description: fab_equipment_model服务接口
* @author: kang
* @date: 2019-06-07 22:18:19
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
public interface IFabEquipmentModelService extends ICommonService<FabEquipmentModel> {
    public List<Map> findLookup();
}
