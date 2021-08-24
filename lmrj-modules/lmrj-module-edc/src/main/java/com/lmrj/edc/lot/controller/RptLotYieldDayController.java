package com.lmrj.edc.lot.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.lot.entity.RptLotYieldDay;
import com.lmrj.edc.lot.service.IRptLotYieldDayService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.lot.controller
 * @title: rpt_lot_yield_day控制器
 * @description: rpt_lot_yield_day控制器
 * @author: 张伟江
 * @date: 2020-05-17 21:10:56
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/rptlotyieldday")
@ViewPrefix("edc/rptlotyieldday")
@RequiresPathPermission("edc:rptlotyieldday")
public class RptLotYieldDayController extends BaseCRUDController<RptLotYieldDay> {

    @Autowired
    private IRptLotYieldDayService rptLotYieldDayService;
    // 获取产量

    @RequestMapping("/pdtChart")
    public Response findProduction(@RequestParam String stationCode, @RequestParam String lineNo, @RequestParam String beginTime, @RequestParam String endTime, HttpServletRequest request, HttpServletResponse response) {
        Response res = new Response();

        //添加线别和站别产量
//        String stationCode="DM";
        if (StringUtil.isEmpty(stationCode)) {
            stationCode = "DM";
        }
        String eqpId = null;
        /*if( stationCode.equals("DM")){
            eqpId ="SIM-DM7";
        }*/
        if (eqpId == null) {
            List<Map> maps = rptLotYieldDayService.pdtChart(beginTime.replace("-", ""), endTime.replace("-", ""), lineNo, stationCode);
            List<Map<String,Object>> result = new ArrayList<>();
            for(Map map: maps){
                String temp = (String)map.get("period_date");
                map.put("period_date",temp);
                BigDecimal multiply = new BigDecimal(1000);
                Object lot_yield = map.get("lot_yield");
                BigDecimal ylot_yield = new BigDecimal(String.valueOf(lot_yield));
                Object lot_yield_eqp =map.get("lot_yield_eqp");
                BigDecimal ylot_yield_eqp = new BigDecimal(String.valueOf(lot_yield_eqp));
                Object plan_qty =map.get("plan_qty");
                BigDecimal bigplan_qty = new BigDecimal(String.valueOf(plan_qty));
                map.put("plan_qty",bigplan_qty.divide(multiply,3, RoundingMode.HALF_UP).toString());
                map.put("lot_yield",ylot_yield.divide(multiply,3, RoundingMode.HALF_UP).toString());
                map.put("lot_yield_eqp",ylot_yield_eqp.divide(multiply,3, RoundingMode.HALF_UP).toString());
                result.add(map);
            }
            res.put("yield",result);
        } else {
            List<Map> maps = rptLotYieldDayService.pdtChart(beginTime.replace("-", ""), endTime.replace("-", ""), lineNo, stationCode, eqpId);
            List<Map<String,Object>> result = new ArrayList<>();
            for(Map map: maps){
                String temp = (String)map.get("period_date");
                map.put("period_date",temp);
                BigDecimal multiply = new BigDecimal(1000);
                Object lot_yield = map.get("lot_yield");
                BigDecimal ylot_yield = new BigDecimal(String.valueOf(lot_yield));
                Object lot_yield_eqp =map.get("lot_yield_eqp");
                BigDecimal ylot_yield_eqp = new BigDecimal(String.valueOf(lot_yield_eqp));
                Object plan_qty =map.get("plan_qty");
                BigDecimal bigplan_qty = new BigDecimal(String.valueOf(plan_qty));
                map.put("plan_qty",bigplan_qty.divide(multiply,3, RoundingMode.HALF_UP).toString());
                map.put("lot_yield",ylot_yield.divide(multiply,3, RoundingMode.HALF_UP).toString());
                map.put("lot_yield_eqp",ylot_yield_eqp.divide(multiply,3, RoundingMode.HALF_UP).toString());
                result.add(map);
            }
            res.put("yield",result);
        }
        return res;
    }

    @RequestMapping(value = "/searchStand/{lineNo}")
    public List<Map<String, Object>> searchStand(@PathVariable("lineNo") String lineNo, @RequestParam String dataType) { // TODO dataType:operation(报警统计)/production(生成日报表)
        if("operation".equals(dataType)){
            return rptLotYieldDayService.searchOperStand(lineNo);
        }else if("production".equals(dataType)){
            return rptLotYieldDayService.searchStand(lineNo);
        }else {
            return rptLotYieldDayService.searchStand(lineNo);
        }
    }

    @RequestMapping(value = "/findEqp")
    public List<Map<String, Object>> findEqp(@RequestParam String lineNo, @RequestParam String day, @RequestParam String year, @RequestParam String stationCode) {
        if (StringUtil.isEmpty(stationCode)) {
            stationCode = "DM";
        }
        String head = year.substring(0, 4);
        String date = head.concat(day);
        List<Map<String,Object>> maps =rptLotYieldDayService.findEqp(lineNo, stationCode, date);

        List<Map<String,Object>> result = new ArrayList<>();
        for(Map map: maps){
            BigDecimal multiply = new BigDecimal(1000);
            Object lot_yield = map.get("lot_yield");
            BigDecimal ylot_yield = new BigDecimal(String.valueOf(lot_yield));
            Object lot_yield_eqp =map.get("lot_yield_eqp");
            BigDecimal ylot_yield_eqp = new BigDecimal(String.valueOf(lot_yield_eqp));
            map.put("lot_yield",ylot_yield.divide(multiply,2, RoundingMode.HALF_UP).toString());
            map.put("lot_yield_eqp",ylot_yield_eqp.divide(multiply,2, RoundingMode.HALF_UP).toString());
            result.add(map);
        }
     return  result;
    }

    @RequestMapping(value = "/searchStandAndEqp/{lineNo}")
    public List<Object> searchStandAndEqp(@PathVariable("lineNo") String lineNo) {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> parents = rptLotYieldDayService.searchStandAndEqp(lineNo);
        List<Object> result = new ArrayList<>();
        List<String> parentParams = new ArrayList<>();
        for (Map<String, Object> map : parents) {
            parentParams.add((String) map.get("value"));
        }
        List<Map<String, Object>> sons = rptLotYieldDayService.findSonEqp(lineNo, parentParams);
        for (Map<String, Object> map : parents) {
            List<Object> small = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("value", map.get("value"));
            item.put("label", map.get("value"));

            for (Map<String, Object> mapson : sons) {
                Map<String, Object> temp = new HashMap<>();
                if (map.get("value").equals(mapson.get("parent"))) {
                    temp.put("value", mapson.get("son"));
                    temp.put("label", mapson.get("son"));
                    small.add(temp);
                }
            }
            item.put("children", small);
            result.add(item);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
        return result;
    }

    @RequestMapping(value = "/findAllEqp")
    public List<Map<String, Object>> findAllEqp(@RequestParam String stationCode, @RequestParam String lineNo, @RequestParam String beginTime, @RequestParam String endTime, HttpServletRequest request, HttpServletResponse response) {
        List<Map<String,Object>> temp = rptLotYieldDayService.findAllEqp(beginTime.replace("-", ""), endTime.replace("-", ""), lineNo, stationCode);

        List<Map<String,Object>> result1 = new ArrayList<>();
        for(Map map: temp){
            BigDecimal multiply = new BigDecimal(1000);
            Object lot_yield = map.get("lot_yield");
            BigDecimal ylot_yield = new BigDecimal(String.valueOf(lot_yield));
            Object lot_yield_eqp =map.get("lot_yield_eqp");
            BigDecimal ylot_yield_eqp = new BigDecimal(String.valueOf(lot_yield_eqp));
            map.put("lot_yield",ylot_yield.divide(multiply,3, RoundingMode.HALF_UP).toString());
            map.put("lot_yield_eqp",ylot_yield_eqp.divide(multiply,3, RoundingMode.HALF_UP).toString());
            result1.add(map);
        }

        List<Map<String,Object>> result = new ArrayList<>();
        for (int i =0;i < result1.size();i++){
            Map<String,Object> ele = new LinkedHashMap<>();
            String str = (String) result1.get(i).get("period_date");
            ele.put("period_date",str.substring(4));
            ele.put((String) result1.get(i).get("eqp_id")+"-MES产量",result1.get(i).get("lot_yield"));
            ele.put((String) result1.get(i).get("eqp_id")+"-设备产量",result1.get(i).get("lot_yield_eqp"));

            for (int j =i+1;j <result1.size();j++){
                if(result1.get(i).get("period_date").equals(result1.get(j).get("period_date"))){
                    ele.put((String) result1.get(j).get("eqp_id")+"-MES产量",result1.get(j).get("lot_yield"));
                    ele.put((String) result1.get(j).get("eqp_id")+"-设备产量",result1.get(j).get("lot_yield_eqp"));
                    i=j;
                }
            }
            result.add(ele);
        }
        return result;
    }
}
