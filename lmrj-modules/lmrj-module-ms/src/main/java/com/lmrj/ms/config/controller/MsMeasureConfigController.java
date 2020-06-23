package com.lmrj.ms.config.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.ms.config.entity.MsMeasureConfig;
import com.lmrj.ms.config.entity.MsMeasureConfigDetail;
import com.lmrj.ms.config.service.IMsMeasureConfigDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.config.controller
 * @title: ms_measure_config控制器
 * @description: ms_measure_config控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:32:57
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasureconfig")
@ViewPrefix("ms/msmeasureconfig")
@RequiresPathPermission("ms:msmeasureconfig")
@LogAspectj(title = "ms_measure_config")
public class MsMeasureConfigController extends BaseCRUDController<MsMeasureConfig> {

    @Autowired
    private IFabEquipmentService fabEquipmentService;

    @Autowired
    private IMsMeasureConfigDetailService msMeasureConfigDetailService;
    ///**
    // * 设备列表下拉框
    // * @param request
    // * @param response
    // * @return
    // */
    //@RequestMapping(value = "/listByEqp/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    //public Response eqpIdlist(@PathVariable String eqpId, HttpServletRequest request,
    //                          HttpServletResponse response) {
    //    Response res= new Response();
    //    FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
    //    List<MsMeasureConfig> detail = commonService.selectList(new EntityWrapper<MsMeasureConfig>().eq("eqp_model_id" ,fabEquipment.getModelId()));
    //    //String content = JSON.toJSONString(detail);
    //    //ServletUtils.printJson(response, content);
    //    res.put("data",detail);
    //    return res;
    //}

    /**
     * 设备列表下拉框
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/listByEqp/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void eqpIdlist(@PathVariable String eqpId, HttpServletRequest request,
                              HttpServletResponse response) {
        Response res= new Response();
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        List<MsMeasureConfig> list = commonService.selectList(new EntityWrapper<MsMeasureConfig>().eq("eqp_model_id" ,fabEquipment.getModelId()));
        list.forEach(msMeasureConfig -> {
            String id = msMeasureConfig.getId();
            List<MsMeasureConfigDetail> edcParamRecordDtlList = msMeasureConfigDetailService.selectList(new com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper<MsMeasureConfigDetail>(MsMeasureConfigDetail.class).eq("ms_config_id",id));
            msMeasureConfig.setDetail(edcParamRecordDtlList);
        });
        String content = JSON.toJSONString(list);
        ServletUtils.printJson(response, content);
        //res.put("data",detail);
        //return res;
    }

    /**
     * 设备列表下拉框
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/listProByEqp/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void listProByEqp(@PathVariable String eqpId, HttpServletRequest request,
                          HttpServletResponse response) {
        Response res= new Response();
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        List<MsMeasureConfig> list = commonService.selectList(new EntityWrapper<MsMeasureConfig>().eq("eqp_model_id" ,fabEquipment.getModelId()));
        String content = JSON.toJSONString(list);
        ServletUtils.printJson(response, content);
        //res.put("data",detail);
        //return res;
    }
}
