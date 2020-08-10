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
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;


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
@Slf4j
public class EdcEqpStateServiceImpl extends CommonServiceImpl<EdcEqpStateMapper, EdcEqpState> implements IEdcEqpStateService {

    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    private EdcEqpStateMapper edcEqpStateMapper;
    @Autowired
    private IRptEqpStateDayService rptEqpStateDayService;

    @Override
    public List<String> findEqpId(Date startTime,Date endTime){
        return baseMapper.findEqpId(startTime,endTime);
    }
    /**
     * 更新end_time, state_times
     *
     * @param startTime
     * @param endTime
     * @return
     */
    //给edc_eqp_state表里的数据添加end_time
    @Override
    public int syncEqpSate(Date startTime, Date endTime,String eqpId) {
        List<EdcEqpState> eqpStateList = edcEqpStateMapper.getAllByTime(startTime,endTime,eqpId);
        List<EdcEqpState> neweqpStateList = new ArrayList<>();
        //在8点到第一条数据之间新建一条数据
        if (eqpStateList.get(0).getStartTime()!=(startTime)) {
            EdcEqpState lastData = this.findLastData(startTime, eqpId);
            EdcEqpState firstData = new EdcEqpState();
            firstData.setStartTime(startTime);
            firstData.setEndTime(eqpStateList.get(0).getStartTime());
            Double state = (double) (eqpStateList.get(0).getStartTime().getTime() - startTime.getTime());
            firstData.setStateTimes(state);
            firstData.setState(lastData.getState());
            firstData.setEqpId(lastData.getEqpId());
            this.insert(firstData);
            log.info("插入记录成功");
        }
        for (int i = 0; i < eqpStateList.size() - 1; i++) {
            //将最后一条数据的endtime改为当天8点 并计算StateTime
            if (i == eqpStateList.size() - 2) {
                EdcEqpState lastEdcEqpState = eqpStateList.get(eqpStateList.size() - 1);
                lastEdcEqpState.setEndTime(endTime);
                Double stateTime1 = (double) (endTime.getTime() - lastEdcEqpState.getStartTime().getTime());
                lastEdcEqpState.setStateTimes(stateTime1);
                neweqpStateList.add(lastEdcEqpState);
            }
            //给每条数据加endTime和stateTime
            EdcEqpState edcEqpState = eqpStateList.get(i);
            EdcEqpState nextedcEqpState = eqpStateList.get(i + 1);
            edcEqpState.setEndTime(nextedcEqpState.getStartTime());
            Double stateTime = (double) (nextedcEqpState.getStartTime().getTime() - edcEqpState.getStartTime().getTime());
            edcEqpState.setStateTimes(stateTime);
            neweqpStateList.add(edcEqpState);
        }
        if (CollectionUtils.isEmpty(eqpStateList)) {
            return 0;
        } else {
            if (this.updateBatchById(neweqpStateList)) {
                log.info("edc_eqp_state更新成功");
                String eventId = StringUtil.randomTimeUUID("RPT");
                fabLogService.info("", eventId, "edc_eqp_state更新", "数据更新成功", "", "");
            }
        }
        return eqpStateList.size();
    }

    /**
     * 计算日OEE
     *
     * @param periodDate
     * @return
     */
    @Override
    public int calEqpSateDay(String periodDate) {
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = DateUtil.parseDate(periodDate + "080000", "yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            endTime = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取数据
        List<EdcEqpState> eqpDayStateList = edcEqpStateMapper.calEqpSateDay(startTime, endTime);
        if (CollectionUtils.isEmpty(eqpDayStateList)) {
            return 0;
        }
        List<RptEqpStateDay> rptEqpStateDayList = Lists.newArrayList();
        //日OEE分组
        Map<String, List<EdcEqpState>> eqpStatetMap = MapUtil.listToMapList(eqpDayStateList, e -> e.getEqpId());
        //计算日OEE
        for (List<EdcEqpState> list : eqpStatetMap.values()) {
            RptEqpStateDay rptEqpStateDay = new RptEqpStateDay();
            rptEqpStateDay.setEqpId(list.get(0).getEqpId());
            rptEqpStateDay.setPeriodDate(DateUtil.formatDate(startTime, "yyyyMMdd"));
            Double idle = 0.0;
            Double run = 0.0;
            Double down = 0.0;
            Double pm = 0.0;
            for (EdcEqpState edcEqpState : list) {
                if ("run".equalsIgnoreCase(edcEqpState.getState())) {
                    run = run + edcEqpState.getStateTimes();
                }
                if ("down".equalsIgnoreCase(edcEqpState.getState())) {
                    down = down + edcEqpState.getStateTimes();
                }
                if ("idle".equalsIgnoreCase(edcEqpState.getState())) {
                    idle = idle + edcEqpState.getStateTimes();
                }
                if ("pm".equalsIgnoreCase(edcEqpState.getState())) {
                    pm = pm + edcEqpState.getStateTimes();
                }
            }
            rptEqpStateDay.setRunTime(run / 1000);
            rptEqpStateDay.setDownTime(down / 1000);
            rptEqpStateDay.setIdleTime(idle / 1000);
            rptEqpStateDay.setPmTime(pm);
            Double other = 24 * 60 * 60 * 1000 - run - down - idle - pm;
            rptEqpStateDay.setOtherTime(other / 1000);
            rptEqpStateDayList.add(rptEqpStateDay);
        }
        //先删除day表 按照时间删除 在插入
        if (rptEqpStateDayService.delete(new EntityWrapper<RptEqpStateDay>().eq("period_date", periodDate))
                && rptEqpStateDayService.insertBatch(rptEqpStateDayList)) {
            String eventId = StringUtil.randomTimeUUID("RPT");
            fabLogService.info("", eventId, "OEE计算更新", "数据更新成功", "", "");
        }
        ;
        return rptEqpStateDayList.size();
    }

    @Override
    public EdcEqpState findLastData(Date startTime, String eqpId) {
        return baseMapper.findLastData(startTime, eqpId);
    }
}
