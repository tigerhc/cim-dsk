package com.lmrj.aps.plan.controller;

import com.google.common.collect.Lists;
import com.lmrj.aps.plan.entity.ApsPlanPdtYield;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldDetailService;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.edc.lot.entity.RptLotYield;
import com.lmrj.edc.lot.service.IRptLotYieldService;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.util.calendar.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.aps.plan.controller
 * @title: aps_plan_pdt_yield控制器
 * @description: aps_plan_pdt_yield控制器
 * @author: 张伟江
 * @date: 2020-05-17 21:00:52
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("aps/apsplanpdtyield")
@ViewPrefix("aps/apsplanpdtyield")
@RequiresPathPermission("aps:apsplanpdtyield")
public class ApsPlanPdtYieldController extends BaseCRUDController<ApsPlanPdtYield> {

    @Autowired
    private IFabEquipmentStatusService fabEquipmentStatusService;

    @Autowired
    private IApsPlanPdtYieldDetailService apsPlanPdtYieldDetailService;

    @Autowired
    private IRptLotYieldService rptLotYieldService;

    @GetMapping("indexFour")
    public void indexFour(HttpServletRequest request) throws ParseException {
        //aps_plan_pdt_yield_detail=WHERE production_name like '%SIM%' AND plan_date = '20200509'
        String periodDate = DateUtil.getDate("yyyyMMdd");
        if(DateUtil.getDate("dd").compareTo("08")<0 ){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            periodDate = DateUtil.formatDate(calendar.getTime(),"yyyyMMdd");
        }
        List<ApsPlanPdtYieldDetail> yieldList = apsPlanPdtYieldDetailService.selectList(new com.baomidou.mybatisplus.mapper.EntityWrapper().eq("plan_date", periodDate).like("production_name", "SIM"));
        int yieldQty = 0;
        for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : yieldList) {
            int qty = apsPlanPdtYieldDetail.getPlanQty();
            yieldQty = yieldQty+qty;
        }
        FabEquipmentStatus fabEquipmentStatus=fabEquipmentStatusService.findByEqpId("SIM-REFLOW1");
        List<RptLotYield> lotYieldDaylList = Lists.newArrayList();
        for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : yieldList) {
            String productionNo = apsPlanPdtYieldDetail.getProductionNo();
            String lotNo = apsPlanPdtYieldDetail.getLotNo();
            List<RptLotYield> rptLotYieldList = rptLotYieldService.selectList(new com.baomidou.mybatisplus.mapper.EntityWrapper().eq("production_no", productionNo).eq("lot_no", lotNo));
            lotYieldDaylList.addAll(rptLotYieldList);
        }
        int lotYieldAll = 0;
        for (RptLotYield rptLotYield : lotYieldDaylList) {
            int lotYield = rptLotYield.getLotYield();
            if(lotYield ==0){
                lotYieldAll = lotYieldAll+rptLotYield.getLotYieldEqp();
            }else{
                lotYieldAll += lotYield;
            }
        }

        List<Map> mapList= Lists.newArrayList();
        Map map=new HashMap();
        map.put("name","批次");
        map.put("number",fabEquipmentStatus.getLotNo());
        //map.put("number",111);
        Map map1=new HashMap();
        map1.put("name","目标数");
        map1.put("number",yieldQty);
        Map map2=new HashMap();
        map2.put("name","投入数");
        map2.put("number",lotYieldAll);
        Map map3=new HashMap();
        map3.put("name","达成率");
        map3.put("number",lotYieldAll*100/yieldQty);
        //方案模版
        mapList.add(map);
        mapList.add(map1);
        mapList.add(map2);
        mapList.add(map3);
        FastJsonUtils.print(mapList);
    }
}
