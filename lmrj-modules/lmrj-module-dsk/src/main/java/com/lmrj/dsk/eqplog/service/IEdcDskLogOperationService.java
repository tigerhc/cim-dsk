package com.lmrj.dsk.eqplog.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;

import java.util.Date;
import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service
* @title: edc_dsk_log_operation服务接口
* @description: edc_dsk_log_operation服务接口
* @author: 张伟江
* @date: 2020-04-14 10:10:16
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcDskLogOperationService extends ICommonService<EdcDskLogOperation> {
    List<EdcDskLogOperation> findDataByTimeAndEqpId(String eqpId,Date startTime,Date endTime);

    List<String> findEqpId(Date startTime,Date endTime);

    EdcDskLogOperation findOperationData(String eqpId);

    Boolean exportTrmOperationFile(String eqpId,Date startTime,Date endTime);

    int insertList(List<EdcDskLogOperation> list);
}