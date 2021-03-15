package com.lmrj.mes.measure.controller;


import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import com.lmrj.mes.measure.entity.measureSx;
import com.lmrj.mes.measure.mapper.MeasureSxMapper;
import com.lmrj.mes.measure.service.MeasureSxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ms/measuresx")
@ViewPrefix("ms/measuresx")
@RequiresPathPermission("ms/measuresx")
@LogAspectj(title = "ms/measuresx")
public class MeasureSxController {

    @Autowired
    private MeasureSxService measureSxService;

    @RequestMapping(value = "/productionName",method = {RequestMethod.GET, RequestMethod.POST})
    public List<Map<String, String>> productionName(){

        return  measureSxService.findProductionNo();

    }

    @RequestMapping(value = "/findSxNumber",method = {RequestMethod.GET, RequestMethod.POST})
    public List findSxNumber(@RequestParam String productionName,@RequestParam String number,@RequestParam String startDate, @RequestParam String endDate, @RequestParam String type){

        return  measureSxService.findSxNumber(productionName,number, startDate,endDate,type);

    }
}
