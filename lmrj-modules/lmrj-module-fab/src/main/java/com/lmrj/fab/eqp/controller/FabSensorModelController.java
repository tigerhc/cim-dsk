package com.lmrj.fab.eqp.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.api.entity.EdcparamApi;
import com.lmrj.core.api.service.IEdcparamApiService;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.User;
import com.lmrj.fab.eqp.entity.FabNumType;
import com.lmrj.fab.eqp.entity.FabSensorModel;
import com.lmrj.fab.eqp.service.IFabNumTypeservice;
import com.lmrj.fab.eqp.service.IFabSensorModelService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("fab/fabsensormodel")
@ViewPrefix("eqpmodel/fabsensormodel")
@RequiresPathPermission("FabSensorModel")
@LogAspectj(title = "fab_sensor_model")
public class FabSensorModelController extends BaseCRUDController<FabSensorModel> {

    @Autowired
    private IFabSensorModelService fabEquipmentModelService;
    @Autowired
    private IFabNumTypeservice fabNumTypeservice;
    @Autowired
    private IEdcparamApiService iEdcparamApiService;
    /**
     * 在返回对象之前编辑数据
     *
     * @param entity
     */
    @Override
    public void afterFind(FabSensorModel entity) {
        //创建人
        if(StringUtil.isNotBlank(entity.getCreateBy())){
            User creater = UserUtil.getUser(entity.getCreateBy());
            if(creater != null){
                entity.setCreateByName(creater.getUsername());
            }
        }
        //更新人
        if(StringUtil.isNotBlank(entity.getUpdateBy())){
            User updater = UserUtil.getUser(entity.getUpdateBy());
            if(updater != null){
                entity.setUpdateByName(updater.getUsername());
            }
        }

        //查询对应的示数类型信息以及对应的参数信息
        EdcparamApi edcparamApi = iEdcparamApiService.selectOne(new EntityWrapper<EdcparamApi>().eq("param_define_id",entity.getId()));
        entity.setDefineId(edcparamApi.getId());
        entity.setMaxValue(edcparamApi.getMaxValue());
        entity.setMinValue(edcparamApi.getMinValue());
        entity.setParamCode(edcparamApi.getParamCode());
        entity.setParamName(edcparamApi.getParamName());
        entity.setSetValue(edcparamApi.getSetValue());
        entity.setNumType(fabNumTypeservice.getNumType(edcparamApi.getTypeId()));
    }

    /**
     * 在返回list数据之前编辑数据
     *
     * @param pagejson
     */
    @Override
    public void afterList(DateResponse pagejson, HttpServletRequest request, HttpServletResponse response) {
        List<FabSensorModel> list = (List<FabSensorModel>) pagejson.getResults();
        for(FabSensorModel fabEquipmentModel: list){
            fabEquipmentModel.setModelName(fabEquipmentModel.getManufacturerName()+"-"+fabEquipmentModel.getClassCode());
        }
    }

    /**
     * 测试使用.
     * 仅查询设备类型,同事设备类型由厂家-型号拼接而成
     * 不过建议直接返回Response对象
     *
     */
    @RequestMapping(value = "lookup", method = { RequestMethod.GET, RequestMethod.POST })
    //@PageableDefaults(sort = "id=desc") 不再默认配置
    @RequiresMethodPermissions("list")
    private void lookup() throws IOException {
        List<Map> list = fabEquipmentModelService.findLookup();
        FastJsonUtils.print(list);
    }

    /**
     * 设备厂家下拉框
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/manufacturerNameList", method = { RequestMethod.GET, RequestMethod.POST })
    public void manufacturerNameList(Model model, HttpServletRequest request,
                                     HttpServletResponse response) {
        List<String> manufacturerNameList = fabEquipmentModelService.manufacturerNameList();
        List<Map> list = Lists.newArrayList();
        for (String manufacturerName : manufacturerNameList) {
            Map map = Maps.newHashMap();
            map.put("id", manufacturerName);
            list.add(map);
        }
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    /**
     * 设备类型下拉框
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/classCodeList", method = { RequestMethod.GET, RequestMethod.POST })
    public void classCodeList(Model model, HttpServletRequest request,
                              HttpServletResponse response) {
        List<String> classCodeList = fabEquipmentModelService.classCodeList();
        List<Map> list = Lists.newArrayList();
        for (String classCode : classCodeList) {
            Map map = Maps.newHashMap();
            map.put("id", classCode);
            list.add(map);
        }
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    /**
     * 无模板设备类型下拉框
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/noTemClassCodeList", method = { RequestMethod.GET, RequestMethod.POST })
    public void noTemClassCodeList(Model model, HttpServletRequest request,
                                   HttpServletResponse response) {
        List<String> classCodeList = fabEquipmentModelService.noTemClassCodeList();
        List<Map> list = Lists.newArrayList();
        for (String classCode : classCodeList) {
            Map map = Maps.newHashMap();
            map.put("id", classCode);
            list.add(map);
        }
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }



    /**
     * 设备大类下拉框
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/parentTypeList/{modelId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void parentTypeList(Model model, @PathVariable("modelId") String modelId, HttpServletRequest request,
                               HttpServletResponse response) {
        List<String> classCodeList = fabEquipmentModelService.getTypeList("1",modelId);
        List<Map> list = Lists.newArrayList();
        for (String classCode : classCodeList) {
            Map map = Maps.newHashMap();
            map.put("id", classCode);
            list.add(map);
        }
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }


    /**
     * 设备小类下拉框
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/typeList/{parentType}", method = { RequestMethod.GET, RequestMethod.POST })
    public void typeList(Model model,@PathVariable("parentType") String parentType, HttpServletRequest request,
                         HttpServletResponse response) {
        List<String> classCodeList = fabEquipmentModelService.getTypeList("2",parentType);
        List<Map> list = Lists.newArrayList();
        for (String classCode : classCodeList) {
            Map map = Maps.newHashMap();
            map.put("id", classCode);
            list.add(map);
        }
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    @Override
    public void afterSave(FabSensorModel entity, HttpServletRequest request, HttpServletResponse response) {
        //生成参数并关联示数类型
        FabSensorModel fabSensorModel = fabEquipmentModelService.selectOne(new EntityWrapper<FabSensorModel>().eq("class_code",entity.getClassCode()));
        String typeId = fabNumTypeservice.getTypeId(entity.getClassCode(),entity.getNumType());
        //获取示数类型对应的所需信息
        FabNumType fabNumType = fabNumTypeservice.selectById(typeId);
        EdcparamApi edcparamApi = new EdcparamApi();
        edcparamApi.setParamDefineId(fabSensorModel.getId());//暂存传感器类型ID
        edcparamApi.setModelName(entity.getModelName());
        edcparamApi.setParamCode(entity.getParamCode());
        edcparamApi.setParamName(entity.getParamName());
        edcparamApi.setTypeId(typeId);
        edcparamApi.setSetValue(entity.getSetValue());
        edcparamApi.setMaxValue(entity.getMaxValue());
        edcparamApi.setMinValue(entity.getMinValue());
        edcparamApi.setId(entity.getDefineId());
        edcparamApi.setParamUnit(fabNumType==null?"":fabNumType.getParamUnit());
        //新增或更新对应的参数信息
        iEdcparamApiService.insertOrUpdateAll(edcparamApi);

    }

    @Override
    @RequestMapping(
            value = {"{id}/delete"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Response delete(@PathVariable("id") String id) {
        //删除对应的参数信息
        iEdcparamApiService.delete(new EntityWrapper<EdcparamApi>().eq("param_define_id",id));
        return super.delete(id);

    }

    @Override
    @RequestMapping(
            value = {"batch/delete"},
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    @ResponseBody
    public Response batchDelete(@RequestParam(value = "ids",required = false) String[] ids) {
        List idList = Arrays.asList(ids);
        for (Object id:idList) {
            iEdcparamApiService.delete(new EntityWrapper<EdcparamApi>().eq("param_define_id",id));
        }

        return  super.batchDelete(ids);
    }

}
