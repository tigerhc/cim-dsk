package com.lmrj.dsk.dashboard.service;

import com.lmrj.dsk.dashboard.entity.FbpbistolO;
import com.lmrj.dsk.dashboard.entity.FipinqtoolO;
import com.lmrj.dsk.dashboard.entity.ToolGroupInfo;

import java.util.List;
import java.util.Map;

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
public interface IDashboardService {
    ToolGroupInfo findOrgGroupInfo(String fab);

    FipinqtoolO findAlarmByFab(String fab);

    FipinqtoolO findEqpStateByFab(String fab);
    FipinqtoolO findEqpStateByStep(String step);
    FbpbistolO findEqpStatusByFabId(String fab);

    FipinqtoolO findEqpStateByPeriod(String fab);

    List<Map> findCurStateByPeriod(String fab);

    List<Map> dayYield(String line, String stationCode,String eqpId);
}
