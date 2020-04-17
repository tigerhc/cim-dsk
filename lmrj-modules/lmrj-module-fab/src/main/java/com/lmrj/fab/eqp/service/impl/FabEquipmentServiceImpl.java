package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.mapper.FabEquipmentMapper;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.fab.service.impl
* @title: fab_equipment服务实现
* @description: fab_equipment服务实现
* @author: 张伟江
* @date: 2019-06-04 15:42:26
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
@Transactional
@Service("fabequipmentService")
public class FabEquipmentServiceImpl  extends CommonServiceImpl<FabEquipmentMapper,FabEquipment> implements  IFabEquipmentService {

    @Override
    public void inactiveEqp(String id) {
        baseMapper.updateActiveFlag(id, "0");
    }

    @Override
    public FabEquipment findEqpByCode(String eqpId) {
        List<FabEquipment> fabEquipmentList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId));
        if(fabEquipmentList.size() == 0){
            return null;
        }
        return fabEquipmentList.get(0);
    }
}
