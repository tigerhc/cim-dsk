package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabSensor;
import com.lmrj.fab.eqp.mapper.FabSensorMapper;
import com.lmrj.fab.eqp.service.IFabSensorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 9:21
 */
@Transactional
@Service("fabsensorservice")
public class FabSensorServiceImpl extends CommonServiceImpl<FabSensorMapper, FabSensor> implements IFabSensorService {

    @Override
    public List<FabSensor> findWbEqp(String eqpId){
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
    public FabSensor findEqpByCode(String eqpId) {
        List<FabSensor> FabSensorList = baseMapper.selectList(new EntityWrapper<FabSensor>().eq("eqp_id", eqpId));
        if (FabSensorList.size() == 0) {
            return null;
        }
        return FabSensorList.get(0);
    }

    @Override
    public List<FabSensor> findEqpByLine(String lineNo) {
        return baseMapper.selectList(new EntityWrapper<FabSensor>().eq("line_no", lineNo));
    }

    @Override
    public List<FabSensor> findEqpBySubLine(String lineNo) {
        return baseMapper.selectList(new EntityWrapper<FabSensor>().eq("sub_line_no", lineNo));
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

    @Override
    public String findeqpNoInfab(String eqpId) {
        return baseMapper.findeqpNoInfab(eqpId);
    }

    @Override
    public List<Map<String,Object>> findDepartment (String eqpId){
        List<Map<String,Object>> result = baseMapper.findDepartment(eqpId);
        return result;
    }

    @Override
    public List<Map<String,Object>> findEmailALL (String  code){
        List<Map<String,Object>> result = baseMapper.findEmailALL(code);
        return result;
    }

    @Override
    public List<FabSensor> getTempEqpList() {
        return baseMapper.findTempEqpList();
    }

    /**
     * 获取为绑定的设备号列表
     * @param classCode
     * @return
     */
    @Override
    public List<Map> findNoSorMap(String classCode) {
        return baseMapper.findNoSorMap(classCode);
    }
}
