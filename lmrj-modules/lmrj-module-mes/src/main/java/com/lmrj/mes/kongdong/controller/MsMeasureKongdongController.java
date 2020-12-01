package com.lmrj.mes.kongdong.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import com.lmrj.mes.kongdong.service.IMsMeasureKongdongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.kongdong.controller
 * @title: ms_measure_kogndong控制器
 * @description: ms_measure_kogndong控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasurekongdong")
@ViewPrefix("ms/msmeasurekongdong")
@RequiresPathPermission("ms:msmeasurekongdong")
@LogAspectj(title = "ms_measure_kongdong")
public class MsMeasureKongdongController extends BaseCRUDController<MsMeasureKongdong> {
    @Autowired
    private IMsMeasureKongdongService kongdongService;

//    @RequestMapping(value = "saveBeforeKongdong",method = {RequestMethod.GET, RequestMethod.POST})
//    public Response saveBeforeKongdong(@RequestParam("filePath") int index){
//        Response rs = Response.ok();
//        rs.putList("data",kongdongService.saveBeforeFile(index));
//        return rs;
//    }

    @RequestMapping(value = "kongdongChart",method = {RequestMethod.GET, RequestMethod.POST})
    public Response kongdongChart(@RequestParam String productionName, @RequestParam String startDate, @RequestParam String endDate){
        Map<String, Object> param = new HashMap<>();
        param.put("productionName", productionName.replace("J.",""));
        param.put("startTime", startDate);
        param.put("endTime", endDate);
        Response rs = Response.ok();
        rs.put("data",kongdongService.kongdongChart(param));
        return rs;
    }

    @RequestMapping(value = "/kongDongBar", method = {RequestMethod.GET, RequestMethod.POST})
    public Response kongDongBar(@RequestParam String productionName, @RequestParam String lotNo) {
        Map<String, Object> param = new HashMap<>();
        param.put("productionName", productionName.replace("J.", ""));
        param.put("lotNo", lotNo);
        Response rs = Response.ok();
        rs.put("kongdong", kongdongService.kongDongBar(param));
        return rs;
    }
}
