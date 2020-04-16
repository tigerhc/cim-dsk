package com.lmrj.fab.eqp.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.mapper.FabEquipmentModelMapper;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.fab.service.impl
* @title: fab_equipment_model服务实现
* @description: fab_equipment_model服务实现
* @author: kang
* @date: 2019-06-07 22:18:19
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
@Transactional
@Service("fabequipmentmodelService")
public class FabEquipmentModelServiceImpl  extends CommonServiceImpl<FabEquipmentModelMapper,FabEquipmentModel> implements IFabEquipmentModelService {
    @Override
    public List<Map> findLookup() {
        return baseMapper.findLookup();
    }
}
