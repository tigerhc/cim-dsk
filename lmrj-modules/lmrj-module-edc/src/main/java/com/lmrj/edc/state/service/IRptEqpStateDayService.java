package com.lmrj.edc.state.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.state.entity.RptEqpStateDay;

import java.util.List;

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
}
