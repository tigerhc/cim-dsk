package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipmentBind;
import com.lmrj.fab.eqp.entity.FabSensor;
import com.lmrj.fab.eqp.mapper.FabEquipmentBindMapper;
import com.lmrj.fab.eqp.mapper.FabEquipmentMapper;
import com.lmrj.fab.eqp.mapper.FabSensorMapper;
import com.lmrj.fab.eqp.service.IFabSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 9:21
 */
@Transactional
@Service("fabsensorservice")
public class FabSensorServiceImpl extends CommonServiceImpl<FabSensorMapper, FabSensor> implements IFabSensorService {

    @Autowired
    private FabEquipmentMapper fabEquipmentMapper;
    @Autowired
    private FabEquipmentBindMapper fabEquipmentBindMapper;

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

    /**
     * isBindCreated:Y N 两个值
     * @param isBindCreated
     * @return
     */
    @Override
    public List<Map> AoutAddSensor(String isBindCreated, String eqpId) {
        if(isBindCreated == null || "".equals( isBindCreated ) || isBindCreated == "N"){
            List<Map> list = new ArrayList<>(  );
            list.add( (Map) new HashMap(  ).put( "NO","不生成传感器" ) );
            return list;
        }
        List<Map> maps = fabEquipmentMapper.AoutAddSensor( isBindCreated, eqpId );
        if(maps != null && maps.size() != 0){ //这个地方只能用size来判断，如果判断是否为null的话会报错的，因为list集合本身是不会为null的
            FabSensor fabSensor = new FabSensor();
            fabSensor.setSorId( String.valueOf( maps.get( 0 ).get( "id" ) ) );
            fabSensor.setSorName( maps.get( 0 ).get( "eqp_id" ).toString()+"_"+maps.get( 0 ).get( "model_name" ).toString());
            fabSensor.setOfficeId( maps.get( 0 ).get( "office_id" ).toString().split(",")[maps.get( 0 ).get( "office_id" ).toString().split(",").length-1] );
            fabSensor.setActiveFlag( maps.get( 0 ).get( "active_flag" ).toString() );
            fabSensor.setDelFlag( maps.get( 0 ).get( "del_flag" ).toString() );
            fabSensor.setModelId( maps.get( 0 ).get( "model_id" ).toString() );
            fabSensor.setModelName( maps.get( 0 ).get( "model_name" ).toString() );
            baseMapper.insert( fabSensor );
            FabSensor bind = baseMapper.selectOne( fabSensor );
            if(bind!=null){
                FabEquipmentBind a = new FabEquipmentBind();
//                a.setId( String.valueOf(bind.getId()) );
                a.setEqpId( String.valueOf(bind.getSorId()) );
                a.setParentEqpId(  String.valueOf(maps.get( 0 ).get( "eqp_id" )) );
                a.setOfficeId( String.valueOf(bind.getOfficeId()) );
                a.setTemplateId( String.valueOf(maps.get( 0 ).get( "tLateId" )) );
                a.setTemplateBodyId( String.valueOf(bind.getOfficeId()) );
                a.setDelFlag( String.valueOf(bind.getDelFlag()) );
                fabEquipmentBindMapper.addBind( a );
            }

        }
        return maps;
    }
}
