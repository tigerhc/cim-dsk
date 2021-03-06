package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabSensor;
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
public class FabEquipmentServiceImpl extends CommonServiceImpl<FabEquipmentMapper, FabEquipment> implements IFabEquipmentService {

    @Override
    public List<FabEquipment> findWbEqp(String eqpId){
        return baseMapper.findWbEqp(eqpId);
    }

    @Override
    public List<String> findStationCodeByLineNo(String lineNo) {
        return baseMapper.findStationCodeByLineNo(lineNo);
    }

    @Override
    public void activeEqp(String id, String flag) {
        baseMapper.updateActiveFlag(id, flag);
    }

    @Override
    public FabEquipment findEqpByCode(String eqpId) {
        List<FabEquipment> fabEquipmentList = baseMapper.selectList(new EntityWrapper<FabEquipment>().eq("eqp_id", eqpId));
        if (fabEquipmentList.size() == 0) {
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
    public List<Map> findOeeEqpMap() {
        return baseMapper.findOeeEqpMap();
    }

    @Override
    public List<FabEquipment> findOeeEqpList(){
        return baseMapper.findOeeEqpList();
    }

    @Override
    public List<Map> findEqpMapByCode(String classCode) {
        return baseMapper.findEqpMapByCode(classCode);
    }

    @Override
    public List<Map> findEqpMsMap() {
        return baseMapper.findEqpMsMap();
    }

    @Override
    public String findeqpNoInfab(String eqpId) {
        return baseMapper.findeqpNoInfab(eqpId);
    }

    public List<Map<String,Object>> findDepartment (String eqpId){
        List<Map<String,Object>> result = baseMapper.findDepartment(eqpId);
       return result;
    }

    public List<Map<String,Object>> findEmailALL (String  code){
        List<Map<String,Object>> result = baseMapper.findEmailALL(code);
        return result;
    }

    @Override
    public List<FabEquipment> getTempEqpList() {
        return baseMapper.findTempEqpList();
    }

    /**
     * 获取所有的设备
     * @param offId
     * @return
     */
    @Override
    public List<FabEquipment>  selectPageByOffId(String offId) {
        return   baseMapper.selectList(new EntityWrapper<FabEquipment>().eq("office_id", offId));
       // page.setRecords(baseMapper.selectPage(page,new EntityWrapper<FabEquipment>().eq("office_id", offId))) ;
      //  return page;
    }

    @Override
    public List<FabSensor> selectFabSensorId(String eqpId) {
        return baseMapper.selectFabSensorId( eqpId );
    }


    @Override
    public List<FabEquipment> selectOfficeName() {
        return baseMapper.selectOfficeName();
    }
}
