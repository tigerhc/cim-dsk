package com.lmrj.oven.batchlot.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotDay;
import com.lmrj.oven.batchlot.service.IOvnBatchLotDayService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("oven/ovnbatchlotday")
public class OvnBatchLotDatController {
    @Autowired
    private IOvnBatchLotDayService ovnBatchLotDayService;

    @Autowired
    private IOvnBatchLotService ovnBatchLotService;

    @RequestMapping("/findDetail/{eqpId}")
    public void findDetail(@PathVariable String eqpId,HttpServletRequest request, HttpServletResponse response){
        List<OvnBatchLotDay> list = ovnBatchLotDayService.findDetail( eqpId );
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    @RequestMapping("/selectTime/{periodDate}")
    public void selectTime(@PathVariable String periodDate,HttpServletRequest request, HttpServletResponse response){
        List<OvnBatchLotDay> list = ovnBatchLotDayService.selectTime( periodDate );
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }
}
