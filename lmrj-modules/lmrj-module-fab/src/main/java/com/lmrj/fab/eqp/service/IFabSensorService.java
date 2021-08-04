package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabSensor;
import com.lmrj.fab.eqp.entity.FabSensor;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 9:14
 */
public interface IFabSensorService extends ICommonService<FabSensor> {

    void activeEqp(String id, String flag);

    List<String> findStationCodeByLineNo(String lineNo);

    FabSensor findEqpByCode(String eqpId);

    List<FabSensor> findEqpByLine(String lineNo);

    List<FabSensor> findEqpBySubLine(String subLineNo);

    List<String> findEqpIdList();

    List<Map> findEqpMap();

    //List<String> findEqpIdMsList();
    List<Map> findEqpMsMap();

    String findeqpNoInfab(String eqpId);

    List<Map<String,Object>> findDepartment (String eqpId);

    List<Map<String,Object>> findEmailALL (String code);

    List<FabSensor> findWbEqp(String eqpId);

    List<FabSensor> getTempEqpList();

    List<Map> findNoSorMap(String classCode);

    List<Map> aoutAddSensor(String isBindCreated, String eqpId);
}
