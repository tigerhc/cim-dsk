package com.lmrj.dsk.eqplog.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;

import java.util.Date;
import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service
* @title: edc_dsk_log_production服务接口
* @description: edc_dsk_log_production服务接口
* @author: 张伟江
* @date: 2020-04-14 10:10:00
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcDskLogProductionService extends ICommonService<EdcDskLogProduction> {
    EdcDskLogProduction findNextYield(String eqpId, Date startTime);

    List<EdcDskLogProductionHis> findBackUpYield(String eqpId,Date startTime, Date endTime);

    Integer findNewYieldByLot(String eqpId,String productionNo, String lotNo);

    EdcDskLogProduction findLotNo(String startTime,String eqpId);

    String findLotNo1(String startTime, String endTime);

    List<EdcDskLogProduction> findProductionlog(String startTime, String endTime);
}
