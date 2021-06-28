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
    public List<EdcEqpState> getAllByTime(Date startTime,Date endTime,String eqpId){
        return baseMapper.getAllByTime(startTime,endTime,eqpId);
    }
    @Override
    public int syncEqpSate(Date startTime, Date endTime, String eqpId) {
        List<EdcEqpState> eqpStateList = edcEqpStateMapper.getAllByTime(startTime, endTime, eqpId);
        List<EdcEqpState> neweqpStateList = new ArrayList<>();
        //在8点到第一条数据之间新建一条数据
        /*if(eqpStateList.size()==0 && eqpId.equals("SIM-REFLOW1")){
            try {
                EdcEqpState firstData = new EdcEqpState();
                //当天八点前最后一条数据
                EdcEqpState lastData = baseMapper.findLastData(startTime, eqpId);
                if(lastData != null){
                    lastData.setEndTime(startTime);
                    Double state = (double) (startTime.getTime() - lastData.getStartTime().getTime());
                    lastData.setStateTimes(state);
                    this.updateById(lastData);
                }
                firstData.setStartTime(startTime);
                firstData.setEndTime(new Date());
                Double state1 = (double) (new Date().getTime() - startTime.getTime());
                firstData.setStateTimes(state1);
                //把第一条数据的状态值设为当天八点前最后一条数据的状态
                firstData.setState("RUN");
                firstData.setEqpId(eqpId);
                this.insert(firstData);
                log.info("插入记录成功");
            } catch (Exception e) {
                log.error("数据插入失败",e);
                e.printStackTrace();
            }
        } else*/
        if(eqpStateList.size()>0){
            if (eqpStateList.get(0).getStartTime().after(startTime)) {
                try {
                    EdcEqpState firstData = new EdcEqpState();
                    //当天0点前最后一条数据
                    EdcEqpState lastData = baseMapper.findLastData(startTime, eqpId);
                    lastData.setEndTime(startTime);
                    Double state = (double) (startTime.getTime() - lastData.getStartTime().getTime());
                    lastData.setStateTimes(state);
                    this.updateById(lastData);
                    firstData.setStartTime(startTime);
                    firstData.setEndTime(eqpStateList.get(0).getStartTime());
                    Double state1 = (double) (eqpStateList.get(0).getStartTime().getTime() - startTime.getTime());
                    firstData.setStateTimes(state1);
                    //把第一条数据的状态值设为当天八点前最后一条数据的状态
                    firstData.setState(lastData.getState());
                    firstData.setEqpId(eqpId);
                    this.insert(firstData);
                } catch (Exception e) {
                    log.error("日OEE数据解析出错",e);
                    e.printStackTrace();
                }
                log.info("插入记录成功");
            }
        }
        if (eqpStateList.size() >= 2) {
            //若当天最后一条数据结束时间大于当日24点，将数据结束时间更改为当日24点
            EdcEqpState lastEdcEqpState = eqpStateList.get(eqpStateList.size() - 1);
            if (lastEdcEqpState.getEndTime() == null) {
                lastEdcEqpState = eqpStateList.get(eqpStateList.size() - 2);
            }
            if (lastEdcEqpState.getEndTime() != null && lastEdcEqpState.getEndTime().after(endTime)) {
                lastEdcEqpState.setEndTime(endTime);
                Double stateTime1 = (double) (endTime.getTime() - lastEdcEqpState.getStartTime().getTime());
                lastEdcEqpState.setStateTimes(stateTime1);
                neweqpStateList.add(lastEdcEqpState);
            }
        }
        for (int i = 0; i < eqpStateList.size() - 1; i++) {
            //给每条没有endTime的数据加endTime和stateTime
            EdcEqpState edcEqpState = eqpStateList.get(i);
            if (edcEqpState.getEndTime() == null ) {
                EdcEqpState nextedcEqpState = eqpStateList.get(i + 1);
                edcEqpState.setEndTime(nextedcEqpState.getStartTime());
                Double stateTime = (double) (nextedcEqpState.getStartTime().getTime() - edcEqpState.getStartTime().getTime());
                edcEqpState.setStateTimes(stateTime);
                neweqpStateList.add(edcEqpState);
                //如果当前无状态值 将上一条数据状态值赋给它
                /*if(StringUtil.isBlank(edcEqpState.getState())){
                    if (i>0){
                        edcEqpState.setState(eqpStateList.get(i-1).getState());
                    }else{
                        edcEqpState.setState("IDLE");
                    }
                }*/
            }
        }
        if (CollectionUtils.isEmpty(eqpStateList) || eqpStateList.size()==0) {
            EdcEqpState edcEqpState = new EdcEqpState();
            edcEqpState.setEqpId(eqpId);
            edcEqpState.setStartTime(startTime);
            EdcEqpState lastData = baseMapper.findLastData(startTime, eqpId);
            if(lastData == null){
                edcEqpState.setState("IDLE");
            }else {
                edcEqpState.setState(lastData.getState());
            }
            baseMapper.insert(edcEqpState);
            return 0;
        } else {
            if (neweqpStateList.size() > 0) {
                if (this.updateBatchById(neweqpStateList,1000)) {
                    log.info("edc_eqp_state更新成功");
                    String eventId = StringUtil.randomTimeUUID("RPT");
                    fabLogService.info("", eventId, "edc_eqp_state更新", "数据更新成功," + neweqpStateList.size() + "条数据已更新", "", "");
                }
            }
        }
        return eqpStateList.size();
    }

    public int syncOldEqpSate(Date startTime, Date endTime, String eqpId) {
        List<EdcEqpState> eqpStateList = edcEqpStateMapper.getAllByTime(startTime, endTime, eqpId);
        List<EdcEqpState> neweqpStateList = new ArrayList<>();
        //在0点到第一条数据之间新建一条数据
        if(eqpStateList.size()>0){
            if (eqpStateList.get(0).getStartTime().after(startTime)) {
                EdcEqpState firstData = new EdcEqpState();
                //当天0点前最后一条数据
                EdcEqpState lastData = baseMapper.findLastData(startTime, eqpId);
                if(lastData!=null){
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
                    if (this.updateBatchById(neweqpStateList,100)) {
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
            rptEqpStateDay.setPmTime(pm);
            idle = 24 * 60 * 60 * 1000 - run - down  - pm;
            rptEqpStateDay.setIdleTime(idle / 1000);
            //rptEqpStateDay.setOtherTime(other / 1000);
            rptEqpStateDayList.add(rptEqpStateDay);
        }
        String eventId = StringUtil.randomTimeUUID("RPT");
        //先删除day表 按照时间删除 在插入
        if (rptEqpStateDayService.findData(periodDate) == null) {
            rptEqpStateDayService.insertBatch(rptEqpStateDayList,100);
            fabLogService.info("", eventId, "OEE计算更新", "数据更新成功", "", "");
        } else if (rptEqpStateDayService.findData(periodDate) != null && rptEqpStateDayService.deleteByPeriodData(periodDate)) {
            rptEqpStateDayService.insertBatch(rptEqpStateDayList,100);
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
    public List<EdcEqpState> findWrongEqpList(String eqpId, Date startTime, Date endTime) {
        return baseMapper.findWrongEqpList(eqpId, startTime, endTime);
    }

    @Override
    public EdcEqpState findNewData(Date startTime, String eqpId) {
        return baseMapper.findNewData(startTime, eqpId);
    }

    @Override
    public List<Map<String, Object>>  eqpStateTime(String startTime,String endTime,String eqpId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        startTime = startTime+" 00:00:00";
        endTime = endTime+" 23:59:59";
        String[] arr = eqpId.split(",");
        List<HashMap<String, Object>> list =edcEqpStateMapper.eqpStateTime(startTime,endTime,arr);
        List result = new ArrayList();
        for (Map map:list){
            Map element = new HashMap();
            element.put("name",map.get("state"));
            element.put("id",map.get("eqp_id"));
            List element2 = new ArrayList();
            for (int i = 0; i <arr.length ; i++) {
                if (map.get("eqp_id").equals(arr[i])){
                    element2.add(i);
                    break;
                }
            }
            element2.add(DateUtil.formatDate((Date) map.get("start_time"),"yyyy-MM-dd HH:mm:ss.SSS"));
            element2.add(DateUtil.formatDate((Date) map.get("end_time"),"yyyy-MM-dd HH:mm:ss.SSS"));
            element.put("value",element2);
            Map normal = new HashMap();
            Map color = new HashMap();
            if (map.get("state").equals("RUN")){
                color.put("color","#32CD32");
            }else if(map.get("state").equals("DOWN")){
                color.put("color","#B22222");
            }else
                color.put("color","#FFA500");
            normal.put("normal",color);
            element.put("itemStyle",normal);
            result.add(element);
        }
        return result;
    }
}
