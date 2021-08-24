package com.lmrj.edc.state.service.impl;

import com.google.common.collect.Lists;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
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
import java.text.SimpleDateFormat;
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
    @Autowired
    private IEdcEqpStateService edcEqpStateService;

    @Override
    public List<String> findEqpId(Date startTime, Date endTime) {
        return baseMapper.findEqpId(startTime, endTime);
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
    public List<EdcEqpState> getAllByTime(Date startTime, Date endTime, String eqpId) {
        return baseMapper.getAllByTime(startTime, endTime, eqpId);
    }

    @Override
    public EdcEqpState calEqpSateDayByeqpId(Date startTime, Date endTime, String eqpId) {
        return baseMapper.calEqpSateDayByeqpId(startTime, endTime, eqpId);
    }

    @Override
    public int syncEqpSate(Date startTime, Date endTime, String eqpId) {
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(endTime);
        cal.setTime(startTime);
        cal.add(Calendar.MILLISECOND, -1);
        cal1.add(Calendar.MILLISECOND, 1);
        List<EdcEqpState> eqpStateList1 = edcEqpStateMapper.getAllByTime(cal.getTime(), cal1.getTime(), eqpId);
        if (eqpStateList1.size() > 0) {
            for (int i = 0; i < eqpStateList1.size() - 1; i++) {
                EdcEqpState edcEqpState = eqpStateList1.get(i);
                EdcEqpState nextedcEqpState = eqpStateList1.get(i + 1);
                edcEqpState.setEndTime(nextedcEqpState.getStartTime());
                Double stateTime = (double) (nextedcEqpState.getStartTime().getTime() - edcEqpState.getStartTime().getTime());
                edcEqpState.setStateTimes(stateTime);
            }
            edcEqpStateService.updateBatchById(eqpStateList1, 10000);
        }
        List<EdcEqpState> eqpStateList = edcEqpStateMapper.getAllByTime(startTime, endTime, eqpId);
        List<EdcEqpState> lasteqpStateList = new ArrayList<>();
        if (eqpStateList.size() > 0) {
            System.out.println(eqpStateList.get(0).getStartTime());
            //如果第一条数据的开始时间在当日0点只后 创建一条当日0点开始的数据
            if (eqpStateList.get(0).getStartTime().compareTo(startTime) > 0) {
                EdcEqpState firstedcEqpState = edcEqpStateMapper.findNewData2(startTime, eqpId);
                EdcEqpState edcEqpState = eqpStateList.get(0);
                EdcEqpState firstData = new EdcEqpState();
                firstData.setEqpId(eqpId);
                firstData.setStartTime(startTime);
                firstData.setEndTime(edcEqpState.getStartTime());
                Double stateTime = (double) (edcEqpState.getStartTime().getTime() - startTime.getTime());
                firstData.setStateTimes(stateTime);
                firstData.setState(firstedcEqpState.getState());
                try {
                    baseMapper.insert(firstData);
                } catch (Exception e) {
                    log.error("firstData1 insert error!", e);
                    e.printStackTrace();
                }
                //如果第一条数据的开始时间在当日0点之前 创建一条当日0点开始的数据
            } else if (eqpStateList.get(0).getStartTime().compareTo(startTime) < 0) {
                EdcEqpState edcEqpState = eqpStateList.get(0);
                EdcEqpState firstData = new EdcEqpState();
                firstData.setEqpId(eqpId);
                firstData.setStartTime(startTime);
                firstData.setEndTime(edcEqpState.getEndTime());
                Double stateTime = (double) (edcEqpState.getEndTime().getTime() - startTime.getTime());
                firstData.setStateTimes(stateTime);
                firstData.setState(edcEqpState.getState());
                edcEqpState.setEndTime(startTime);
                Double stateTime1 = (double) (startTime.getTime() - edcEqpState.getStartTime().getTime());
                edcEqpState.setStateTimes(stateTime1);
                try {
                    baseMapper.insert(firstData);
                    baseMapper.updateById(edcEqpState);
                } catch (Exception e) {
                    log.error("firstData2 insert error!", e);
                    e.printStackTrace();
                }
            }
            if(eqpStateList.get(eqpStateList.size() - 1).getEndTime()==null){
                EdcEqpState edcEqpState = eqpStateList.get(eqpStateList.size() - 1);
                edcEqpState.setEndTime(endTime);
                Double stateTime = (double) (endTime.getTime() - edcEqpState.getStartTime().getTime());
                edcEqpState.setStateTimes(stateTime);
                try {
                    baseMapper.updateById(edcEqpState);
                } catch (Exception e) {
                    log.error("lastData2 updateById error!", e);
                    e.printStackTrace();
                }
            }
            //最后一条数据的结束时间小于当天24点
            if (eqpStateList.get(eqpStateList.size() - 1).getEndTime().compareTo(endTime) < 0) {
                EdcEqpState edcEqpState = eqpStateList.get(eqpStateList.size() - 1);
                EdcEqpState lastData = new EdcEqpState();
                lastData.setEqpId(eqpId);
                lastData.setStartTime(edcEqpState.getEndTime());
                lastData.setEndTime(endTime);
                Double stateTime = (double) (endTime.getTime() - edcEqpState.getEndTime().getTime());
                lastData.setStateTimes(stateTime);
                lastData.setState(edcEqpState.getState());
                try {
                    baseMapper.insert(lastData);
                } catch (Exception e) {
                    log.error("lastData1 insert error!", e);
                    e.printStackTrace();
                }
                //最后一条数据的结束时间大于当天24点
            } else if (eqpStateList.get(eqpStateList.size() - 1).getEndTime().compareTo(endTime) > 0) {
                EdcEqpState edcEqpState = eqpStateList.get(eqpStateList.size() - 1);
                EdcEqpState lastData = new EdcEqpState();
                lastData.setEqpId(eqpId);
                lastData.setStartTime(edcEqpState.getStartTime());
                lastData.setEndTime(endTime);
                Double stateTime = (double) (endTime.getTime() - edcEqpState.getStartTime().getTime());
                lastData.setStateTimes(stateTime);
                lastData.setState(edcEqpState.getState());
                edcEqpState.setStartTime(endTime);
                Double stateTime1 = (double) (edcEqpState.getEndTime().getTime() - endTime.getTime());
                edcEqpState.setStateTimes(stateTime1);
                try {
                    baseMapper.insert(lastData);
                    baseMapper.updateById(edcEqpState);
                } catch (Exception e) {
                    log.error("lastData2 insert error!", e);
                    e.printStackTrace();
                }
            }
        }
        return eqpStateList.size();
    }

    public int syncOldEqpSate(Date startTime, Date endTime, String eqpId) {
        List<EdcEqpState> eqpStateList = edcEqpStateMapper.getAllByTime(startTime, endTime, eqpId);
        List<EdcEqpState> neweqpStateList = new ArrayList<>();
        //在0点到第一条数据之间新建一条数据
        if (eqpStateList.size() > 0) {
            if (eqpStateList.get(0).getStartTime().after(startTime)) {
                EdcEqpState firstData = new EdcEqpState();
                //当天0点前最后一条数据
                EdcEqpState lastData = baseMapper.findLastData2(startTime, eqpId);
                if (lastData != null) {
                    lastData.setEndTime(startTime);
                    Double state = (double) (startTime.getTime() - lastData.getStartTime().getTime());
                    lastData.setStateTimes(state);
                    this.updateById(lastData);
                    firstData.setStartTime(startTime);
                    firstData.setEndTime(eqpStateList.get(0).getStartTime());
                    Double state1 = (double) (eqpStateList.get(0).getStartTime().getTime() - startTime.getTime());
                    firstData.setStateTimes(state1);
                    //把第一条数据的状态值设为当天0点前最后一条数据的状态
                    firstData.setState(lastData.getState());
                    firstData.setEqpId(eqpId);
                    this.insert(firstData);
                    log.info("插入记录成功");
                }

            }
            EdcEqpState lastEdcEqpState = eqpStateList.get(eqpStateList.size() - 1);
            if (lastEdcEqpState.getEndTime() != null && lastEdcEqpState.getEndTime().after(endTime)) {
                lastEdcEqpState.setEndTime(endTime);
                Double stateTime1 = (double) (endTime.getTime() - lastEdcEqpState.getStartTime().getTime());
                lastEdcEqpState.setStateTimes(stateTime1);
                neweqpStateList.add(lastEdcEqpState);
            }
            for (int i = 0; i < eqpStateList.size() - 1; i++) {
                //给每条没有endTime的数据加endTime和stateTime
                EdcEqpState edcEqpState = eqpStateList.get(i);
                EdcEqpState nextedcEqpState = eqpStateList.get(i + 1);
                if (edcEqpState.getStartTime() != nextedcEqpState.getStartTime() && edcEqpState.getEndTime() != nextedcEqpState.getStartTime()) {
                    edcEqpState.setEndTime(nextedcEqpState.getStartTime());
                    Double stateTime = (double) (nextedcEqpState.getStartTime().getTime() - edcEqpState.getStartTime().getTime());
                    edcEqpState.setStateTimes(stateTime);
                    neweqpStateList.add(edcEqpState);
                }
                //如果当前无状态值 将上一条数据状态值赋给它
                /*if(StringUtil.isBlank(edcEqpState.getState())){
                    if (i>0){
                        edcEqpState.setState(eqpStateList.get(i-1).getState());
                    }else{
                        edcEqpState.setState("IDLE");
                    }
                }*/
            }
            if (CollectionUtils.isEmpty(eqpStateList)) {
                return 0;
            } else {
                if (neweqpStateList.size() > 0) {
                    if (this.updateBatchById(neweqpStateList, 100)) {
                        log.info("edc_eqp_state更正成功");
                    /*String eventId = StringUtil.randomTimeUUID("RPT");
                    fabLogService.info("", eventId, "edc_eqp_state更新", "数据更新成功,"+neweqpStateList.size()+"条数据已更新", "", "");*/
                    }
                }
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
            startTime = DateUtil.parseDate(periodDate + "000000", "yyyyMMddHHmmss");
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
            Double alarm = 0.0;
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
                if ("alarm".equalsIgnoreCase(edcEqpState.getState())) {
                    alarm = alarm + edcEqpState.getStateTimes();
                }
            }
            rptEqpStateDay.setRunTime(run / 1000);
            rptEqpStateDay.setDownTime(down / 1000);
            rptEqpStateDay.setPmTime(pm);
            rptEqpStateDay.setAlarmTime(alarm / 1000);
            idle = 24 * 60 * 60 * 1000 - run - down - pm - alarm;
            rptEqpStateDay.setIdleTime(idle / 1000);
            //rptEqpStateDay.setOtherTime(other / 1000);
            rptEqpStateDayList.add(rptEqpStateDay);
        }
        String eventId = StringUtil.randomTimeUUID("RPT");
        //先删除day表 按照时间删除 在插入
        if (rptEqpStateDayService.findData(periodDate) == null) {
            rptEqpStateDayService.insertBatch(rptEqpStateDayList, 100);
            fabLogService.info("", eventId, "OEE计算更新", "数据更新成功", "", "");
        } else if (rptEqpStateDayService.findData(periodDate) != null && rptEqpStateDayService.deleteByPeriodData(periodDate)) {
            rptEqpStateDayService.insertBatch(rptEqpStateDayList, 100);
            fabLogService.info("", eventId, "OEE计算更新", "数据更新成功", "", "");
        } else {
            fabLogService.info("", eventId, "OEE计算更新", "数据更新失败", "", "");
        }
        return rptEqpStateDayList.size();
    }

    @Override
    public EdcEqpState findLastData(Date startTime, String eqpId) {
        return baseMapper.findLastData(startTime, eqpId);
    }

    @Override
    public EdcEqpState findLastData2(Date startTime, String eqpId) {
        return baseMapper.findLastData2(startTime, eqpId);
    }

    @Override
    public List<EdcEqpState> findWrongEqpList(String eqpId, Date startTime, Date endTime) {
        return baseMapper.findWrongEqpList(eqpId, startTime, endTime);
    }

    @Override
    public EdcEqpState findNewData(Date startTime, String eqpId) {
        return baseMapper.findNewData(startTime, eqpId);
    }

    @Override
    public EdcEqpState findNewData2(Date startTime, String eqpId) {
        return baseMapper.findNewData2(startTime, eqpId);
    }

    @Override
    public List<Map<String, Object>> eqpStateTime(String startTime, String endTime, String eqpId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        startTime = startTime + " 00:00:00";
        endTime = endTime + " 23:59:59";
        String[] arr = eqpId.split(",");
        List<HashMap<String, Object>> list = edcEqpStateMapper.eqpStateTime(startTime, endTime, arr);
        List result = new ArrayList();
        for (Map map : list) {
            Map element = new HashMap();
            element.put("name", map.get("state"));
            element.put("id", map.get("eqp_id"));
            List element2 = new ArrayList();
            for (int i = 0; i < arr.length; i++) {
                if (map.get("eqp_id").equals(arr[i])) {
                    element2.add(i);
                    break;
                }
            }
            element2.add(DateUtil.formatDate((Date) map.get("start_time"), "yyyy-MM-dd HH:mm:ss.SSS"));
            element2.add(DateUtil.formatDate((Date) map.get("end_time"), "yyyy-MM-dd HH:mm:ss.SSS"));
            element.put("value", element2);
            Map normal = new HashMap();
            Map color = new HashMap();
            if (map.get("state").equals("RUN")) {
                color.put("color", "#32CD32");
            } else if (map.get("state").equals("DOWN")) {
                color.put("color", "#B22222");
            } else
                color.put("color", "#FFA500");
            normal.put("normal", color);
            element.put("itemStyle", normal);
            result.add(element);
        }
        return result;
    }
}
