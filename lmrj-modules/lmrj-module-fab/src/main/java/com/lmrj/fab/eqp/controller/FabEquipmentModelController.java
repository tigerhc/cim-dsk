package com.lmrj.fab.eqp.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.entity.FabModelTemplate;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.fab.eqp.service.IFabModelTemplateBodyService;
import com.lmrj.fab.eqp.service.IFabModelTemplateService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.User;
import com.lmrj.cim.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.controller
 * @title: fab_equipment_model控制器
 * @description: fab_equipment_model控制器
 * @author: kang
 * @date: 2019-06-07 22:18:19
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("fab/fabequipmentmodel")
@ViewPrefix("eqpmodel/fabequipmentmodel")
@RequiresPathPermission("FabEquipmentModel")
@LogAspectj(title = "fab_equipment_model")
public class FabEquipmentModelController extends BaseCRUDController<FabEquipmentModel> {

    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;
    @Autowired
    private IFabModelTemplateService fabModelTemplateService;
    @Autowired
    private IFabModelTemplateBodyService fabModelTemplateBodyService;
    /**
     * 在返回对象之前编辑数据
     *
     * @param entity
     */
    @Override
    public void afterFind(FabEquipmentModel entity) {
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
        //查询模板名称以及模板ID
      FabModelTemplate fabModelTemplate =  fabModelTemplateService.selectOne(new EntityWrapper<FabModelTemplate>().eq("class_code",entity.getClassCode()));
        entity.setTemplateId(fabModelTemplate.getId());
        entity.setTemplateName(fabModelTemplate.getName());
    }

    /**
     * 在返回list数据之前编辑数据
     *
     * @param pagejson
     */
    @Override
    public void afterList(DateResponse pagejson, HttpServletRequest request, HttpServletResponse response) {
        List<FabEquipmentModel> list = (List<FabEquipmentModel>) pagejson.getResults();
        for(FabEquipmentModel fabEquipmentModel: list){
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
    public void afterSave(FabEquipmentModel entity, HttpServletRequest request, HttpServletResponse response) {
    //保存完主表信息后将自动生成对应的模板信息
        //此时无需生成body表
        FabModelTemplate fabModelTemplate = new FabModelTemplate();
        fabModelTemplate.setClassCode(entity.getClassCode());
        fabModelTemplate.setActiveFlag("Y");
        fabModelTemplate.setDelFlag("0");
        fabModelTemplate.setName(entity.getTemplateName());
        fabModelTemplate.setManufacturerName(entity.getManufacturerName());
        fabModelTemplate.setId(entity.getTemplateId());
        FabModelTemplate fabModelTemplateAfter =  fabModelTemplateService.insertOrUpdate(fabModelTemplate,"");
//如果变更了模板名称则修改  否则无操作
        fabModelTemplateBodyService.chageName(fabModelTemplateAfter.getId(),fabModelTemplateAfter.getName());

    }

    @Override
    @RequestMapping(
            value = {"{id}/delete"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Response delete(@PathVariable("id") String id) {
        //删除对应的模板
      String classCode =  fabEquipmentModelService.selectById(id).getClassCode();
      fabModelTemplateService.deleteAll(classCode);
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
        //删除对应的模板
        for (Object id:idList) {
            String classCode =  fabEquipmentModelService.selectById(String.valueOf(id)).getClassCode();
            fabModelTemplateService.deleteAll(classCode);
        }
        return  super.batchDelete(ids);
    }







}
