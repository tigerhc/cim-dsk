package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabEquipment;

/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.fab.service
* @title: fab_equipment服务接口
* @description: fab_equipment服务接口
* @author: 张伟江
* @date: 2019-06-04 15:42:26
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
public interface IFabEquipmentService extends ICommonService<FabEquipment> {

    void inactiveEqp(String id);

    FabEquipment findEqpByCode(String eqpId);
}