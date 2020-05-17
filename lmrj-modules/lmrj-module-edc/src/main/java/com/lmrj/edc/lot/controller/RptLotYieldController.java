package com.lmrj.edc.lot.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.edc.lot.entity.RptLotYield;
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
 * @title: rpt_lot_yield控制器
 * @description: rpt_lot_yield控制器
 * @author: 张伟江
 * @date: 2020-05-17 21:10:40
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/rptlotyield")
@ViewPrefix("edc/rptlotyield")
@RequiresPathPermission("edc:rptlotyield")
public class RptLotYieldController extends BaseCRUDController<RptLotYield> {
    @Autowired
    private IRptLotYieldDayService rptLotYieldDayService;

    // 获取产量
    @RequestMapping("/pdtChart")
    public void findProduction(@RequestParam String bay_id, HttpServletRequest request, HttpServletResponse response) {
        List<Map> maps =  rptLotYieldDayService.pdtChart2("");
        ServletUtils.printJson(response, maps);
    }
}
