package com.lmrj.edc.lot.service.impl;

import com.google.common.collect.Maps;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldDetailService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.lot.entity.RptLotYieldDay;
import com.lmrj.edc.lot.mapper.RptLotYieldDayMapper;
import com.lmrj.edc.lot.service.IRptLotYieldDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


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
public class RptLotYieldDayServiceImpl  extends CommonServiceImpl<RptLotYieldDayMapper,RptLotYieldDay> implements  IRptLotYieldDayService {

    @Autowired
    public IApsPlanPdtYieldDetailService apsPlanPdtYieldDetailService;

    @Override
    public List<Map> pdtChart(String beginTime, String endTime, String line) {
        List<Map> yieldDayList = baseMapper.selectDaypdt(beginTime, endTime, "SIM");
        List<ApsPlanPdtYieldDetail> apsPlanPdtYieldDetails = apsPlanPdtYieldDetailService.selectDayYield(beginTime, endTime,"SIM");
        Map<String, Integer> planYieldmap = Maps.newHashMap();
        for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : apsPlanPdtYieldDetails) {
            //planYieldmap.put(apsPlanPdtYieldDetail.getProductionNo() + apsPlanPdtYieldDetail.getLotNo(), apsPlanPdtYieldDetail.getPlanQty());
            planYieldmap.put(apsPlanPdtYieldDetail.getPlanDate(), apsPlanPdtYieldDetail.getPlanQty());
        }
        for (Map yieldDay : yieldDayList) {
            //String key = yieldDay.get("production_no").toString() + yieldDay.get("lot_no").toString();
            String key = yieldDay.get("period_date") + "";
            int planQty = planYieldmap.get(key);
            if (planQty != 0) {
                double rate = Double.parseDouble(yieldDay.get("lot_yield") + "") * 100 / planQty;
                rate = (double) Math.round(rate * 100) / 100;

                double eqpRate = Double.parseDouble(yieldDay.get("lot_yield_eqp") + "") * 100 / planQty;
                eqpRate = (double) Math.round(eqpRate * 100) / 100;
                yieldDay.put("rate", rate);
                yieldDay.put("eqp_rate", eqpRate);
            }else{
                yieldDay.put("rate", 0);
                yieldDay.put("eqp_rate", 0);
            }
            //去除年份
            yieldDay.put("period_date", key.substring(4));
        }
        return yieldDayList;
    }

}
