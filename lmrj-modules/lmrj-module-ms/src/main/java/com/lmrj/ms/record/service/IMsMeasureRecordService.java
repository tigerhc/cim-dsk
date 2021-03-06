package com.lmrj.ms.record.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.ms.record.entity.MsMeasureRecord;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.record.service
 * @title: ms_measure_record服务接口
 * @description: ms_measure_record服务接口
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
public interface IMsMeasureRecordService extends ICommonService<MsMeasureRecord> {
    List<Map> findDetailBytime(String eqpId, String beginTime, String endTime);

    MsMeasureRecord findMsrecordbyfirst(String eqpId, String processNo, String productionNo, String lotNo, String wafer_id);

    List<Map> findDetailBytimeAndPro(String eqpId, String beginTime, String endTime, String productionNo);

    List<MsMeasureRecord> findRecordByRecordId(String recordId);

    List<MsMeasureRecord> findAll();

    List<Map> findWeight(Map<String, Object> param);

    List<Map> getEqpIdOptions(String eqpId);

    List<Map> getLineNoOptions();

    List<String> getAllProductionNo(String productionNo);

    int finddataexist(String eqpId, String lotNo, String productionNo, String rowName, String itemValue);

    List<Map<String, Object>> chkWeight(String startTime, String endTime);
}
