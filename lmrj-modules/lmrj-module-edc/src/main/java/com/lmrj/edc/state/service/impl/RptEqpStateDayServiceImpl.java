package com.lmrj.edc.state.service.impl;

import com.google.common.collect.Lists;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.state.entity.RptEqpStateDay;
import com.lmrj.edc.state.mapper.RptEqpStateDayMapper;
import com.lmrj.edc.state.service.IRptEqpStateDayService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Map;


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

    @Override
    public List<Map> selectGroupState(String beginTime, String endTime, String officeId, String lineNo, String fab) {
        return baseMapper.selectGroupState(beginTime, endTime, officeId, null, null, "officeId");
    }

    @Override
    public List<Map> selectNowGroupState(String officeId, String lineNo, String fab) {
        String beginTime =DateUtil.getDate("yyyyMMdd");
        String groupName = "";
        if(StringUtil.isNotBlank(officeId)){
            groupName = "office_id";
        }else if(StringUtil.isNotBlank(lineNo)){
            groupName = "line_no";
        }else if(StringUtil.isNotBlank(fab)){
            groupName = "fab";
        }else{
            return null;
        }
        return baseMapper.selectGroupState(beginTime, beginTime, officeId, lineNo, fab, groupName);
    }

    @Override
    public List<Map> selectNowGroupState(String officeId, String lineNo, String fab, String groupName) {
        String beginTime =DateUtil.getDate("yyyyMMdd");
        String[] groupNames = {"office_id","fab","line_no","step_id","step_code"};
        if(Lists.newArrayList(groupNames).contains(groupName)){
            return baseMapper.selectGroupState(beginTime, beginTime, officeId, lineNo, fab, groupName);
        }else{
            return null;
        }
    }

    @Override
    public List<Map> selectEqpStateByPeriod(String officeId, String lineNo, String fab) {
        String endTime =DateUtil.getDate("yyyyMMdd");
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DAY_OF_MONTH, -4);
        String beginTime = DateUtil.formatDate(rightNow.getTime(),"yyyyMMdd");
        return baseMapper.selectEqpStateByPeriod(beginTime, endTime, officeId, lineNo, fab);
    }


}
