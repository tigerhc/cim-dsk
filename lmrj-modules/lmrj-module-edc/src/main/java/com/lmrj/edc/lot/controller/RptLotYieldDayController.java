package com.lmrj.edc.lot.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.lot.entity.RptLotYieldDay;
import com.lmrj.edc.lot.service.IRptLotYieldDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public Response findProduction(@RequestParam String lineNo,@RequestParam String beginTime,@RequestParam String endTime, HttpServletRequest request, HttpServletResponse response) {
        Response res=new Response();
        //添加线别和站别产量
        String stationCode="DM";
        String eqpId="SIM-DM7";
        List<Map> maps =  rptLotYieldDayService.pdtChart(beginTime.replace("-",""),endTime.replace("-",""),lineNo,stationCode);
        res.put("yield",maps);
        return res;
    }
}
