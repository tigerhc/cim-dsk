package com.lmrj.fab.eqp.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.1.service
 * @title: fab_equipment_status服务接口
 * @description: fab_equipment_status服务接口
 * @author: zhangweijiang
 * @date: 2019-06-18 20:41:20
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
public interface IFabEquipmentStatusService extends ICommonService<FabEquipmentStatus> {

    int updateStatus(String eqpID, String status, String lotNo, String recipeCode, String alarmname);

    int updateYield(String eqpID, String lotNo, String recipeCode, int lotYield, int dayYield);

    int increaseYield(String eqpID, int increasedYield);

    int updateLot(String eqpID, String lotId);

    boolean initStatus(List idList);

    List<FabEquipmentStatus> selectEqpStatus(String officeId, String lineNo, String fab,String department);

    List<Map> selectEqpStatusChart();

    List<Map> selectYield(String lineNo,String department);

    FabEquipmentStatus findByEqpId(String eqpId);

    List<Map> selectAlarmInfo(String lineNo,String department);

}
