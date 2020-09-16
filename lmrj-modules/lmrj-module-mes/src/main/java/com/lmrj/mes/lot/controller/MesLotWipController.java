package com.lmrj.mes.lot.controller;

import com.google.common.collect.Lists;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldDetailService;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.mes.lot.entity.MesLotWip;
import com.lmrj.mes.lot.service.IMesLotWipService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.calendar.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.lot.controller
 * @title: mes_lot_wip控制器
 * @description: mes_lot_wip控制器
 * @author: 张伟江
 * @date: 2020-08-05 10:32:55
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("mes/meslotwip")
@ViewPrefix("mes/meslotwip")
@RequiresPathPermission("mes:meslotwip")
@LogAspectj(title = "mes_lot_wip")
public class MesLotWipController extends BaseCRUDController<MesLotWip> {
    @Autowired
    private IMesLotWipService iMesLotWipService;
    @Autowired
    private IFabEquipmentStatusService fabEquipmentStatusService;

    @Autowired
    private IApsPlanPdtYieldDetailService apsPlanPdtYieldDetailService;

    @Autowired
    private IMesLotTrackService mesLotTrackService;

    //批次在制品(仕掛)批次状态图
    @RequestMapping("/findLotYield")
    public Response findLotYield(@RequestParam String lineNo, HttpServletRequest request, HttpServletResponse response) {
        Response res=new Response();
        List<Map> maps =  iMesLotWipService.findLotYield(lineNo);
        res.put("yield",maps);
        return res;
    }

    @GetMapping("indexFour")
    public void indexFour(HttpServletRequest request) throws ParseException {
        //aps_plan_pdt_yield_detail=WHERE production_name like '%SIM%' AND plan_date = '20200509'
        String periodDate = DateUtil.getDate("yyyyMMdd");
        if(DateUtil.getDate("HH").compareTo("08")<0 ){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            periodDate = DateUtil.formatDate(calendar.getTime(),"yyyyMMdd");
        }
        FabEquipmentStatus fabEquipmentStatus=fabEquipmentStatusService.findByEqpId("SIM-REFLOW1");

        //按照当日产量来算,比较复杂
        //List<ApsPlanPdtYieldDetail> yieldList = apsPlanPdtYieldDetailService.selectList(new com.baomidou.mybatisplus.mapper.EntityWrapper().eq("plan_date", periodDate).like("production_name", "SIM"));
        //int yieldQty = 0;
        //for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : yieldList) {
        //    int qty = apsPlanPdtYieldDetail.getPlanQty();
        //    yieldQty = yieldQty+qty;
        //}
        List<ApsPlanPdtYieldDetail> yieldList = apsPlanPdtYieldDetailService.selectList(new com.baomidou.mybatisplus.mapper.EntityWrapper().eq("plan_date", periodDate).eq("production_no", fabEquipmentStatus.getProductionNo()));
        //int yieldQty = yieldList.get(0).getPlanQty();
        //改为当日目标产量
        int yieldQty = apsPlanPdtYieldDetailService.findCurrentDayPlan(fabEquipmentStatus.getProductionNo(),periodDate);
        List<MesLotTrack> lotYieldDaylList = Lists.newArrayList();
        for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : yieldList) {
            String productionNo = apsPlanPdtYieldDetail.getProductionNo();
            String lotNo = apsPlanPdtYieldDetail.getLotNo();
            List<MesLotTrack> rptLotYieldList = mesLotTrackService.selectList(new com.baomidou.mybatisplus.mapper.EntityWrapper().eq("production_no", productionNo).eq("lot_no", lotNo).eq("eqp_id","SIM-PRINTER1"));
            lotYieldDaylList.addAll(rptLotYieldList);
        }
        int lotYieldAll = 0;
        for (MesLotTrack rptLotYield : lotYieldDaylList) {
            int lotYield = rptLotYield.getLotYield();
            if(lotYield ==0){
                lotYieldAll = lotYieldAll+rptLotYield.getLotYieldEqp();
            }else{
                lotYieldAll += lotYield;
            }
        }
        //改为日产量, 从towa上获取

        List<Map> mapList= Lists.newArrayList();
        Map map=new HashMap();
        map.put("name","批次");
        map.put("number",fabEquipmentStatus.getLotNo());
        //map.put("number",111);
        Map map1=new HashMap();
        map1.put("name","当天目标数");
        if(yieldQty==0){
            map1.put("number",0);
        }else{
            map1.put("number",yieldQty);
        }
        Map map2=new HashMap();
        map2.put("name","当天投入数");
        if(lotYieldAll==0){
            map2.put("number",0);
        }else{
            map2.put("number",lotYieldAll);
        }
        Map map3=new HashMap();
        map3.put("name","达成率");
        if(yieldQty==0){
            map3.put("number",100);
        }else{
            map3.put("number",lotYieldAll*100/yieldQty);
        }

        //方案模版
        mapList.add(map);
        mapList.add(map1);
        mapList.add(map2);
        mapList.add(map3);
        FastJsonUtils.print(mapList);
    }
}
