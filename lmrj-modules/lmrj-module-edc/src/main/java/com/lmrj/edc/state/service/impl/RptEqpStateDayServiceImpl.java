package com.lmrj.edc.state.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.state.entity.RptEqpStateDay;
import com.lmrj.edc.state.mapper.RptEqpStateDayMapper;
import com.lmrj.edc.state.service.IRptEqpStateDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.state.service.impl
* @title: rpt_eqp_state_day服务实现
* @description: rpt_eqp_state_day服务实现
* @author: 张伟江
* @date: 2020-02-20 01:26:27
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rptEqpStateDayService")
public class RptEqpStateDayServiceImpl  extends CommonServiceImpl<RptEqpStateDayMapper,RptEqpStateDay> implements  IRptEqpStateDayService {

    @Override
    public List findEqpOee(String beginTime, String endTime, List eqpIds) {
        List list = (List) baseMapper.findEqpOee( beginTime, endTime,eqpIds);
        return list;
    }

    @Override
    public List findEqpsOee(String beginTime, String endTime, List eqpIds) {
        List list = (List) baseMapper.findEqpsOee( beginTime, endTime,eqpIds);
        return list;
    }
}
