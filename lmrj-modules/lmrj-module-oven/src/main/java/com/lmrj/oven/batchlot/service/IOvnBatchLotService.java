package com.lmrj.oven.batchlot.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.oven.batchlot.entity.FabEquipmentOvenStatus;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.fab.service
* @title: ovn_batch_lot服务接口
* @description: ovn_batch_lot服务接口
* @author: zhangweijiang
* @date: 2019-06-09 08:49:15
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
public interface IOvnBatchLotService extends ICommonService<OvnBatchLot> {

    List<FabEquipmentOvenStatus> selectFabStatus(String s);

    List<Map> selectFabStatusParam(List<FabEquipmentOvenStatus> fabEquipmentOvenStatusList);

    List<Map> selectChart(String s);

    OvnBatchLot acquireFile(String eqptId, String fileName) throws Exception;

    boolean resolveTemperatureFile(String eqptId, String fileName) throws Exception;

    void resolveAllTempFile(String eqpId);

    List<Map> findDetailBytime(String eqpId, String beginTime, String endTime);

    List<Map> findDetailBytimeOther(String eqpId,String lotNO);

    List<Map<String, Object>> findTodayEqpIds();

    boolean saveTempData(List<Map<String, Object>> eqpList, List<Map<String, Object>> temp);

    OvnBatchLot findBatchData(String eqpId , Date startTime);

    OvnBatchLot findBatchDataByLot(String eqpId , String lotNo);

    List<OvnBatchLot> findDataByTime(String beginTime, String endTime);
}
