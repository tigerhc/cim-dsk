package com.lmrj.core.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.api.entity.EdcparamApi;
import com.lmrj.core.api.service.IEdcparamApiService;
import com.lmrj.core.log.LogAspectj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/edcparamapi")
@ViewPrefix("api/edcparamapi")
@RequiresPathPermission("api:edcparamapi")
@LogAspectj(title = "api_edc_param_api")
public class EdcparamApiController extends BaseCRUDController<EdcparamApi> {

    @Autowired
    private IEdcparamApiService iEdcparamApiService;

    /**
     *设备自带参数
     * @param eqpModelId
     * @return
     */
    @RequestMapping(value = {"list/{eqpModelId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Response findByModelId(@PathVariable String eqpModelId){
        List<EdcparamApi> list = commonService.selectList(new EntityWrapper<EdcparamApi>().eq("model_id",eqpModelId).isNull("param_define_id"));
        if(list == null||list.size()>0){
            return DateResponse.error(eqpModelId+"不存在");
        }
        Response res = DateResponse.ok(list);

        return res;
    }


    /**
     *设备参数总览
     * @param eqpModelId
     * @return
     */
    @RequestMapping(value = {"alllist/{eqpModelId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Response findAllByModelId(@PathVariable String eqpModelId){
        List<EdcparamApi> list = commonService.selectList(new EntityWrapper<EdcparamApi>().eq("model_id",eqpModelId));
        if(list == null||list.size()>0){
            return DateResponse.error(eqpModelId+"不存在");
        }
        Response res = DateResponse.ok(list);

        return res;
    }
}
