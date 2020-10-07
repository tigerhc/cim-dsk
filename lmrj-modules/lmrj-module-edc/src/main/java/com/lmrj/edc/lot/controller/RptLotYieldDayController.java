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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        if (eqpId == null) {
            List<Map> maps = rptLotYieldDayService.pdtChart(beginTime.replace("-", ""), endTime.replace("-", ""), lineNo, stationCode);
            res.put("yield", maps);
        } else {
            List<Map> maps = rptLotYieldDayService.pdtChart(beginTime.replace("-", ""), endTime.replace("-", ""), lineNo, stationCode, eqpId);
            res.put("yield", maps);
        }
        return res;
    }

    @RequestMapping(value = "/searchStand/{lineNo}")
    public List<Map<String, Object>> searchStand(@PathVariable("lineNo") String lineNo) {
        return rptLotYieldDayService.searchStand(lineNo);
    }

    @RequestMapping(value = "/findEqp")
    public List<Map<String, Object>> findEqp(@RequestParam String lineNo, @RequestParam String day, @RequestParam String year, @RequestParam String stationCode) {
        if (StringUtil.isEmpty(stationCode)) {
            stationCode = "DM";
        }
        String head = year.substring(0, 4);
        String date = head.concat(day);
        return rptLotYieldDayService.findEqp(lineNo, stationCode, date);
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
        List<Map<String,Object>> result = new ArrayList<>();
        for (int i =0;i < temp.size();i++){
            Map<String,Object> ele = new HashMap<>();
            ele.put("period_date",temp.get(i).get("period_date"));
            ele.put((String) temp.get(i).get("eqp_id")+"-设备产量",temp.get(i).get("lot_yield_eqp"));
            ele.put((String) temp.get(i).get("eqp_id")+"-MES产量",temp.get(i).get("lot_yield"));

            for (int j =i+1;j < temp.size();j++){
                if(temp.get(i).get("period_date").equals(temp.get(j).get("period_date"))){
                    ele.put((String) temp.get(j).get("eqp_id")+"-MES产量",temp.get(i).get("lot_yield"));
                    ele.put((String) temp.get(j).get("eqp_id")+"-设备产量",temp.get(i).get("lot_yield_eqp"));
                    i=j;
                }
            }
            result.add(ele);
        }

        return result;
    }
}
