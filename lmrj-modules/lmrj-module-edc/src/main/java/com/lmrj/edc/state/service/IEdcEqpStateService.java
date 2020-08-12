package com.lmrj.edc.state.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.state.entity.EdcEqpState;

import java.util.Date;
import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.state.service
* @title: edc_eqp_state服务接口
* @description: edc_eqp_state服务接口
* @author: 张伟江
* @date: 2020-02-20 01:26:46
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcEqpStateService extends ICommonService<EdcEqpState> {

    int syncEqpSate(Date startTime, Date endTime,String eqpId);

    int calEqpSateDay(String periodDate);

    EdcEqpState findLastData(Date startTime,String eqpId);

    List<String> findEqpId(Date startTime,Date endTime);

    List<EdcEqpState> findWrongEqpList(String eqpId,Date startTime,Date endTime);

    EdcEqpState findNextData(Date startTime,String eqpId);
}
