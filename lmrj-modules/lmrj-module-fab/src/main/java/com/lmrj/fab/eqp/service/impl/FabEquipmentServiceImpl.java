package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.mapper.FabEquipmentMapper;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


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
    public void activeEqp(String id, String flag) {
        baseMapper.updateActiveFlag(id, flag);
    }

    @Override
    public FabEquipment findEqpByCode(String eqpId) {
        List<FabEquipment> fabEquipmentList = baseMapper.selectList(new EntityWrapper<FabEquipment>().eq("eqp_id", eqpId));
        if(fabEquipmentList.size() == 0){
            return null;
        }
        return fabEquipmentList.get(0);
    }

    @Override
    public List<FabEquipment> findEqpByLine(String lineNo) {
        return baseMapper.selectList(new EntityWrapper<FabEquipment>().eq("line_no", lineNo));
    }

    @Override
    public List<FabEquipment> findEqpBySubLine(String lineNo) {
        return baseMapper.selectList(new EntityWrapper<FabEquipment>().eq("sub_line_no", lineNo));
    }


    @Override
    public List<String> findEqpIdList() {
        return baseMapper.findEqpIdList();
    }

    @Override
    public List<Map> findEqpMap() {
        return baseMapper.findEqpMap();
    }

    @Override
    public List<Map> findEqpMsMap() {
        return baseMapper.findEqpMsMap();
    }

}
