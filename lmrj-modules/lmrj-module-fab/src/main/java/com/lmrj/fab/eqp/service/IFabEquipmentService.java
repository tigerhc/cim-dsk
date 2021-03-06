package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabSensor;

import java.util.List;
import java.util.Map;

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

    void activeEqp(String id, String flag);

    List<String> findStationCodeByLineNo(String lineNo);

    FabEquipment findEqpByCode(String eqpId);

    List<FabEquipment> findEqpByLine(String lineNo);

    List<FabEquipment> findEqpBySubLine(String subLineNo);

    List<String> findEqpIdList();

    List<Map> findEqpMap();

    List<FabEquipment> findOeeEqpList();

    List<Map> findOeeEqpMap();

    List<Map> findEqpMapByCode(String classCode);

    //List<String> findEqpIdMsList();
    List<Map> findEqpMsMap();

    String findeqpNoInfab(String eqpId);

    List<Map<String,Object>> findDepartment (String eqpId);

    List<Map<String,Object>> findEmailALL (String code);

    List<FabEquipment> findWbEqp(String eqpId);

    List<FabEquipment> getTempEqpList();

    List<FabEquipment> selectPageByOffId(String offId);

    List<FabSensor> selectFabSensorId(String eqpId);

    List<FabEquipment> selectOfficeName();
}
