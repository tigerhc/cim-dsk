package com.lmrj.edc.lot.service.impl;

import com.google.common.collect.Maps;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldDetailService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.lot.entity.RptLotYieldDay;
import com.lmrj.edc.lot.mapper.RptLotYieldDayMapper;
import com.lmrj.edc.lot.service.IRptLotYieldDayService;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.lot.service.impl
 * @title: rpt_lot_yield_day服务实现
 * @description: rpt_lot_yield_day服务实现
 * @author: 张伟江
 * @date: 2020-05-17 21:10:56
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Transactional
@Service("rptLotYieldDayService")
@Slf4j
public class RptLotYieldDayServiceImpl extends CommonServiceImpl<RptLotYieldDayMapper, RptLotYieldDay> implements IRptLotYieldDayService {
    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    public IApsPlanPdtYieldDetailService apsPlanPdtYieldDetailService;

    @Override
    public int deleteByDate(String periodDate,String lineNo){
        return baseMapper.deleteByDate(periodDate,lineNo);
    }
    @Override
    public List<RptLotYieldDay> selectDayYieldList(Date startTime, Date endTime){
        return baseMapper.selectDayYieldList(startTime,endTime);
    }
    @Override
    public Integer findLotYield(String eqpId, String lotNo, Date startTime, Date endTime) {
        return baseMapper.findLotYield(eqpId, lotNo, startTime, endTime);
    }

    @Override
    public List<String> findEqpId(String lineNo, String stationCode) {
        return baseMapper.findEqpId(lineNo, stationCode);
    }

    @Override
    public RptLotYieldDay findDayYield(String eqpId,Date startTime, Date endTime){
        return baseMapper.findDayYield(eqpId,startTime,endTime);
    }
    /*@Override
    public List<Map> selectDaypdt(String beginTime,String endTime,String lineNo,String stationCode){
        return baseMapper.selectDaypdt(beginTime, endTime, lineNo, stationCode);
    }*/

    public void updateDayYield(Date startTime, Date endTime, String lineNo, String stationCode) {
        //查询站别计算产量时需要计算的eqpID
        List<String> eqpIdlist = new ArrayList<>();
        if(lineNo.equals("SIM") && stationCode.equals("DM")){
            eqpIdlist.add("SIM-REFLOW1");
        }else{
            eqpIdlist = baseMapper.findEqpId(lineNo, stationCode);
        }
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
        for (String eqpid : eqpIdlist) {
            //查询一天内该设备的产量，并新建产量数据
            RptLotYieldDay rptLotYieldDay = baseMapper.findDayYield(eqpid,startTime,endTime);
            //当天该设备无产量则新建无产量数据
            if (Objects.isNull(rptLotYieldDay)) {
                RptLotYieldDay lotYieldDay = new RptLotYieldDay();
                lotYieldDay.setPeriodDate(sim.format(startTime));
                lotYieldDay.setLotYield(0);
                lotYieldDay.setLotYieldEqp(0);
                lotYieldDay.setStationCode(stationCode);
                lotYieldDay.setLineNo(lineNo);
                if(eqpid.contains("WB")){
                    eqpid=eqpid.substring(0,8);
                }
                lotYieldDay.setEqpId(eqpid);
                this.insert(lotYieldDay);
            }else{
                rptLotYieldDay.setPeriodDate(sim.format(startTime));
                rptLotYieldDay.setStationCode(stationCode);
                rptLotYieldDay.setLineNo(lineNo);
                if(eqpid.contains("WB")){
                    eqpid=eqpid.substring(0,8);
                }
                rptLotYieldDay.setEqpId(eqpid);
                this.insert(rptLotYieldDay);
            }
        }
        String eventId = StringUtil.randomTimeUUID("RPT");
        fabLogService.info("", eventId, "updateDayYield", "更新批次日产量", "", "");
    }

    @Override
    public List<Map> pdtChart(String beginTime, String endTime, String lineNo, String stationCode) {
        List<Map> yieldDayList = new ArrayList<>();
        if(stationCode.equals("WireBond")){
            yieldDayList = baseMapper.selectDaypdtWB(beginTime, endTime, lineNo, stationCode);
        }else {
            yieldDayList = baseMapper.selectDaypdt(beginTime, endTime, lineNo, stationCode);
        }
        List<ApsPlanPdtYieldDetail> apsPlanPdtYieldDetails = apsPlanPdtYieldDetailService.selectDayYield(beginTime, endTime, lineNo);
        Map<String, Integer> planYieldmap = Maps.newHashMap();
        for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : apsPlanPdtYieldDetails) {
            //planYieldmap.put(apsPlanPdtYieldDetail.getProductionNo() + apsPlanPdtYieldDetail.getLotNo(), apsPlanPdtYieldDetail.getPlanQty());
            planYieldmap.put(apsPlanPdtYieldDetail.getPlanDate(), apsPlanPdtYieldDetail.getPlanQty());
        }
        for (Map yieldDay : yieldDayList) {
            //String key = yieldDay.get("production_no").toString() + yieldDay.get("lot_no").toString();
            String key = yieldDay.get("period_date") + "";
            int planQty = 0;
            if (planYieldmap.get(key) != null) {
                planQty = planYieldmap.get(key);
            }
            //把计划也送回前端
            if (planQty != 0) {
                double rate = Double.parseDouble(yieldDay.get("lot_yield") + "") * 100 / planQty;
                rate = (double) Math.round(rate * 100) / 100;
                double eqpRate = Double.parseDouble(yieldDay.get("lot_yield_eqp") + "") * 100 / planQty;
                eqpRate = (double) Math.round(eqpRate * 100) / 100;
                yieldDay.put("rate", rate);
                yieldDay.put("eqp_rate", eqpRate);
                yieldDay.put("plan_qty",planQty);
            } else {
                yieldDay.put("rate", 0);
                yieldDay.put("eqp_rate", 0);
                yieldDay.put("plan_qty",0);
            }
            //去除年份
            yieldDay.put("period_date", key.substring(4));
        }
        return yieldDayList;
    }
    @Override
    public List<Map> pdtChart(String beginTime, String endTime, String lineNo, String stationCode,String eqpId) {
        String eqpid[]=eqpId.split(",");
        List<String> params = new ArrayList<>();
        params = Arrays.asList(eqpid);
        List<ApsPlanPdtYieldDetail> apsPlanPdtYieldDetails = apsPlanPdtYieldDetailService.selectDayYield(beginTime, endTime, lineNo);
        Map<String, Integer> planYieldmap = Maps.newHashMap();
        for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : apsPlanPdtYieldDetails) {
            planYieldmap.put(apsPlanPdtYieldDetail.getPlanDate(), apsPlanPdtYieldDetail.getPlanQty());
        }
        List<Map> yieldDayLists=new ArrayList<>();
        List<Map> yieldDayList=new ArrayList<>();
        int test = eqpId.length();
//        for (int i = 0; i < eqpid.length; i++) {
        if(eqpid.length==1){
             yieldDayList = baseMapper.selectDaypdtById(beginTime, endTime, lineNo, stationCode,params);}
        else {
             yieldDayList = baseMapper.selectDaypdtByIds(beginTime, endTime, lineNo, stationCode,params);}
            for (Map yieldDay : yieldDayList) {

                String key = yieldDay.get("period_date") + "";
                int planQty = 0;
                if (planYieldmap.get(key) != null) {
                    planQty = planYieldmap.get(key);
                }
                //把计划也送回前端
                if (planQty != 0) {
                    double rate = Double.parseDouble(yieldDay.get("lot_yield") + "") * 100 / planQty;
                    rate = (double) Math.round(rate * 100) / 100;
                    double eqpRate = Double.parseDouble(yieldDay.get("lot_yield_eqp") + "") * 100 / planQty;
                    eqpRate = (double) Math.round(eqpRate * 100) / 100;
                    yieldDay.put("rate", rate);
                    yieldDay.put("eqp_rate", eqpRate);
                    yieldDay.put("plan_qty",planQty);
                    yieldDay.put("eqp_id",yieldDay.get("eqp_id"));
                } else {
                    yieldDay.put("rate", 0);
                    yieldDay.put("eqp_rate", 0);
                    yieldDay.put("plan_qty",0);
                    yieldDay.put("eqp_id",yieldDay.get("eqp_id"));
                }
                //去除年份
                yieldDay.put("period_date", key.substring(4));
                yieldDayLists.add(yieldDay);
            }
//        }
        return yieldDayLists;
    }

    @Override
    public  List<Map<String,Object>> searchStand(String lineNo){
        return baseMapper.searchStand(lineNo);
    }

    @Override
    public  List<Map<String,Object>> searchOperStand(String lineNo){
        return baseMapper.searchOperStand(lineNo);
    }

    @Override
    public  List<Map<String,Object>> findEqp(String lineNo,String stationId,String date){
        return baseMapper.findEqp(lineNo,stationId,date);
    }

    @Override
    public  List<Map<String,Object>> searchStandAndEqp(String lineNo){
        return baseMapper.searchStandAndEqp(lineNo);

    }

    @Override
    public  List<Map<String,Object>> findSonEqp(String lineNo,List<String> stationId){
        return baseMapper.findSonEqp(lineNo,stationId);

    }

    @Override
    public  List<Map<String,Object>> findAllEqp(String beginTime, String endTime, String lineNo, String stationCode){
        return baseMapper.findAllEqp(beginTime, endTime, lineNo, stationCode);

    }
}
