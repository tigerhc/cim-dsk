package com.lmrj.edc.state.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.state.entity.RptEqpStateDay;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.state.service
 * @title: rpt_eqp_state_day服务接口
 * @description: rpt_eqp_state_day服务接口
 * @author: 张伟江
 * @date: 2020-02-20 01:26:27
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
public interface IRptEqpStateDayService extends ICommonService<RptEqpStateDay> {
    List findEqpOee(String beginTime, String endTime, List eqpIds);

    List findEqpsOee(String beginTime, String endTime, List eqpIds);

    List<Map> selectGroupState(String beginTime, String endTime, String officeId, String lineNo, String fab);

    List<Map> selectNowGroupState(String officeId, String lineNo, String fab);

    List<Map> selectNowGroupState(String officeId, String lineNo, String fab, String groupName);

    List<Map> selectEqpStateByPeriod(String officeId, String lineNo, String fab);

    Boolean deleteByPeriodData(String periodDate);

    RptEqpStateDay findData(String periodDate);

    List<Map> curPeriodData(String fab);
}
