package com.lmrj.fab.eqp.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.api.entity.EdcparamApi;
import com.lmrj.core.api.service.IEdcparamApiService;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.entity.FabModelTemplate;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.fab.eqp.service.IFabModelTemplateBodyService;
import com.lmrj.util.lang.ObjectUtil;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author wdj
 * @date 2021-06-01 8:55
 */
@RestController
@RequestMapping("fab/fabModeltemplate")
@ViewPrefix("fab/fabModeltemplate")
@RequiresPathPermission("fab:fabModeltemplate")
@LogAspectj(title = "fab_model_template")
public class FabModelTemplateController extends BaseCRUDController<FabModelTemplate> {
    @Autowired
    private IFabModelTemplateBodyService fabModelTemplateBodyService;
    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;
    @Autowired
    private IEdcparamApiService edcparamApiService;
    @Override
    @RequestMapping(
            value = {"create"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Response create(Model model, @Valid @ModelAttribute("data") FabModelTemplate entity, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if (this.hasError(entity, result)) {
            String errorMsg = this.errorMsg(result);
            return !StringUtil.isEmpty(errorMsg) ? Response.error(errorMsg) : Response.error("保存失败");
        } else {
            try {
                //this.preSave(entity, request, response);
                //存储头信息
                List<FabModelTemplateBody> list =entity.getFabModelTemplateBodyList();
                if (ObjectUtil.isNullOrEmpty(entity.getId())) {
                    this.commonService.insert(entity);
                    entity = this.commonService.selectOne(new EntityWrapper().eq("class_code", entity.getClassCode()));
                } else {
                    FabModelTemplate oldEntity = this.commonService.selectById(entity.getId());
                    BeanUtils.copyProperties(entity, oldEntity);
                    this.commonService.insertOrUpdate(oldEntity);
                    entity = oldEntity;
                }
                //存储主体信息
                fabModelTemplateBodyService.deleteAndSave(list,entity);
                //生成关联信息

                this.afterSave(entity, request, response);
            } catch (Exception var6) {
                var6.printStackTrace();
                return Response.error("保存失败!<br />原因:" + var6.getMessage());
            }

            return Response.ok("保存成功");
        }
    }


    @Override
    @RequestMapping(
            value = {"{id}/update"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Response update(Model model, @Valid @ModelAttribute("data") FabModelTemplate entity, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if (this.hasError(entity, result)) {
            String errorMsg = this.errorMsg(result);
            return !StringUtil.isEmpty(errorMsg) ? Response.error(errorMsg) : Response.error("保存失败");
        } else {
            try {
                //this.preSave(entity, request, response);
                //存储头信息
                List<FabEquipmentModel>  fabEquipmentModelList =      fabEquipmentModelService.selectList(new EntityWrapper<FabEquipmentModel>().eq("class_code",entity.getClassCode()));
                String modelId = fabEquipmentModelList.get(0).getId();
                    List<EdcparamApi> edcparamApis = entity.getEdcparamApiSelfList();
                    edcparamApiService.delete(new EntityWrapper<EdcparamApi>().eq("model_id",modelId).isNull("param_define_id"));
                    for (EdcparamApi edcparamApi:edcparamApis) {
                        edcparamApi.setModelId(modelId);
                        if(ObjectUtil.isNullOrEmpty(edcparamApi.getId())){
                            edcparamApiService.insert(edcparamApi);
                        }else{
                            edcparamApi.setId("");
                            edcparamApiService.insert(edcparamApi);
                        }
                    }


                List<FabModelTemplateBody> list =entity.getFabModelTemplateBodyList();
                if (ObjectUtil.isNullOrEmpty(entity.getId())) {
                    this.commonService.insert(entity);
                    entity = this.commonService.selectOne(new EntityWrapper().eq("class_code", entity.getClassCode()));
                } else {
                    FabModelTemplate oldEntity = this.commonService.selectById(entity.getId());
                    BeanUtils.copyProperties(entity, oldEntity);
                    this.commonService.insertOrUpdate(oldEntity);
                    entity = oldEntity;
                }
                //存储主体信息
                fabModelTemplateBodyService.deleteAndSave(list,entity);

                this.afterSave(entity, request, response);
            } catch (Exception var6) {
                var6.printStackTrace();
                return Response.error("保存失败!<br />原因:" + var6.getMessage());
            }

            return Response.ok("保存成功");
        }
    }

    @Override
    public void afterFind(FabModelTemplate entity) {
      List<FabEquipmentModel>  fabEquipmentModels = fabEquipmentModelService.selectList(new EntityWrapper<FabEquipmentModel>().eq("class_code",entity.getClassCode()));
        entity.setModelId(fabEquipmentModels.get(0).getId());
        //查询信息自带参数以及总览
        List<EdcparamApi> edcparamApis = edcparamApiService.selectList(new EntityWrapper<EdcparamApi>().eq("model_id",entity.getModelId()).isNull("param_define_id"));
        entity.setEdcparamApiSelfList(edcparamApis);
        edcparamApis = edcparamApiService.selectList(new EntityWrapper<EdcparamApi>().eq("model_id",entity.getModelId()));
        entity.setEdcparamApiAllList(edcparamApis);
    }


}
