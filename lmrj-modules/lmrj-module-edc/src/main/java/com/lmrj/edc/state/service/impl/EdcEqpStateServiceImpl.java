package com.lmrj.edc.state.service.impl;

import com.google.common.collect.Lists;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.edc.quartz.MapUtil;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.entity.RptEqpStateDay;
import com.lmrj.edc.state.mapper.EdcEqpStateMapper;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.edc.state.service.IRptEqpStateDayService;
import com.lmrj.util.calendar.DateUtil;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.state.service.impl
* @title: edc_eqp_state服务实现
* @description: edc_eqp_state服务实现
* @author: 张伟江
* @date: 2020-02-20 01:26:46
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcEqpStateService")
public class EdcEqpStateServiceImpl  extends CommonServiceImpl<EdcEqpStateMapper,EdcEqpState> implements  IEdcEqpStateService {

    @Autowired
    private EdcEqpStateMapper edcEqpStateMapper;

    @Autowired
    private IRptEqpStateDayService rptEqpStateDayService;

    /**
     * 更新end_time, state_times
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public int syncEqpSate(Date startTime, Date endTime) {
        String periodDate =  DateUtil.formatDate(startTime, "yyyyMMdd");
        List<EdcEqpState> eqpStateList=edcEqpStateMapper.getAllByTime(startTime, endTime);
        if(CollectionUtils.isEmpty(eqpStateList)){
            return 0;
        }
        //更新end_time, state_times
        //for (EdcEqpState edcEqpState : eqpStateList) {
        //
        //}


        List<EdcEqpState> eqpDayStateList = Lists.newArrayList();



        return eqpStateList.size();
    }

    /**
     * 计算日OEE
     * @param periodDate
     * @return
     */
    @Override
    public int calEqpSateDay(String periodDate) {
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = DateUtil.parseDate(periodDate+"080000", "yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            cal.add(Calendar.DAY_OF_MONTH,1);
            endTime = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //计算日OEE
        List<EdcEqpState> eqpDayStateList=edcEqpStateMapper.calEqpSateDay(startTime, endTime);
        if(CollectionUtils.isEmpty(eqpDayStateList)){
            return 0;
        }

        List<RptEqpStateDay> rptEqpStateDayList= Lists.newArrayList();
        //日OEE分组
        Map<String, List<EdcEqpState>> eqpStatetMap = MapUtil.listToMapList(eqpDayStateList, e -> e.getEqpId());
        //计算日OEE
        for (List<EdcEqpState> list : eqpStatetMap.values()) {
            RptEqpStateDay rptEqpStateDay=new RptEqpStateDay();
            rptEqpStateDay.setEqpId(list.get(0).getEqpId());
            rptEqpStateDay.setPeriodDate(DateUtil.formatDate(startTime, "yyyyMMdd"));
            //Double idel=0.0;
            //Double run=0.0;
            //Double down=0.0;
            //for(EdcEqpState edcEqpState:list){
            //    if("run".equalsIgnoreCase(edcEqpState.getState())){
            //        run=run+edcEqpState.getStateTimes();
            //    }
            //    if("down".equalsIgnoreCase(edcEqpState.getState())){
            //        down=down+edcEqpState.getStateTimes();
            //    }
            //    if("idel".equalsIgnoreCase(edcEqpState.getState())){
            //        idel=idel+edcEqpState.getStateTimes();
            //    }
            //}
            //rptEqpStateDay.setRunTime(run);
            //rptEqpStateDay.setDownTime(down);
            //rptEqpStateDay.setIdleTime(idel);
            //rptEqpStateDayList.add(rptEqpStateDay);
        }

        //先删除day表 按照时间删除 在插入
        rptEqpStateDayService.delete(new EntityWrapper<RptEqpStateDay>().eq("period_date",periodDate));
        rptEqpStateDayService.insertBatch(rptEqpStateDayList);
        return rptEqpStateDayList.size();
    }
}
