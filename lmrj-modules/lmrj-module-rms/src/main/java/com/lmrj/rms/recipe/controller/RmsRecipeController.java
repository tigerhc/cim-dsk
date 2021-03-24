package com.lmrj.rms.recipe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.core.excel.ImportExcel;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rms.permit.service.IRmsRecipePermitConfigService;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import com.lmrj.util.lang.ObjectUtil;
import com.lmrj.util.lang.StringUtil;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.recipe.controller
 * @title: rms_recipe控制器
 * @description: rms_recipe控制器
 * @author: zhangweijiang
 * @date: 2019-06-15 01:58:00
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipe")
@ViewPrefix("rms/rmsrecipe")
@RequiresPathPermission("rms:rmsrecipe")
@LogAspectj(title = "rms_recipe")
public class RmsRecipeController extends BaseCRUDController<RmsRecipe> {

    @Autowired
    private IRmsRecipeService rmsRecipeService;
    @Autowired
    private IRmsRecipeBodyService rmsRecipeBodyService;
    @Autowired
    private IEmailSendService emailSendService;
    @Autowired
    private IRmsRecipePermitConfigService rmsRecipePermitConfigService;

    /**
     * 保存数据之前,处理细表
     *
     * @param entity
     * @param request
     * @param response
     */
    @Override
    public void preSave(RmsRecipe entity, HttpServletRequest request, HttpServletResponse response) {
        String edcParamRecordDtlListJson = StringEscapeUtils.unescapeHtml4(request.getParameter("_detail"));
        List<RmsRecipeBody> edcParamRecordDtlList = JSONObject.parseArray(edcParamRecordDtlListJson, RmsRecipeBody.class);
        entity.setRmsRecipeBodyDtlList(edcParamRecordDtlList);
    }

    /**
     * 上传recipe
     * @param request
     */
    @RequestMapping(value = "uploadrecipe")
    public Response uploadRecipe(@RequestParam String eqpId, @RequestParam String recipeList, HttpServletRequest request) {
        Response response = Response.ok("上传成功");
        // TODO: 2019/8/26 判断返回结果
        // TODO: 2019/8/26 springmvc异常处理,当前好像已经有此功能了
        boolean flag = false;
        try {
            String[] recipeCodes = recipeList.split("@");
            List<String> recipes = new ArrayList<>();
            for (String recipe : recipeCodes) {
                if (StringUtil.isEmpty(recipe)) {
                    continue;
                }
                recipes.add(recipe);
            }
            flag = rmsRecipeService.uploadRecipe(eqpId, recipes);
            if (!flag){
                response = Response.error(999998, "上传失败");
            }
        } catch (Exception e) {
            response = Response.error(999998,e.getMessage());
        }
        return response;
    }


    /**
     *  下载recipe
     * @param request
     */
    @RequestMapping(value = "downloadrecipe")
    public Response downloadrecipe(@RequestParam String eqpId, @RequestParam String recipeName, HttpServletRequest request) {
        Response response = Response.ok("下载成功");
        boolean flag = false;
        try {
            flag = rmsRecipeService.downloadRecipe(eqpId, recipeName);
            if (!flag){
                response = Response.error(999998, "下载失败");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = Response.error(999998,e.getMessage());
        }
        return response;
    }

    @Override
    @GetMapping("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("配方信息", queryable,  propertyPreFilterable,  request,  response);
    }

    @RequestMapping(value = "import")
    public Response importFile(MultipartFile file, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Response response = Response.ok("导入成功");
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<RmsRecipe> list = ei.getDataList(RmsRecipe.class);
            if(list.size()>5){
                Response.error("导入参数失败！行数超过5条");
            }
            List<RmsRecipe> updateList = Lists.newArrayList();
            for(RmsRecipe param : list){
                RmsRecipe updateParam = new RmsRecipe();
                if(StringUtil.isBlank(param.getId())){
                    continue;
                }
                updateParam.setId(param.getId());
                updateParam.setRecipeCode(param.getRecipeCode());

                updateList.add(updateParam);
            }
            commonService.updateBatchById(updateList,50);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    @RequestMapping(value = "/{id}/findLast", method = { RequestMethod.GET, RequestMethod.POST })
    public void find(Model model, @PathVariable("id") String id, HttpServletRequest request,
                     HttpServletResponse response) {
        RmsRecipe entity = rmsRecipeService.findLastByRecipeCode(id);
        Response res;
        if(entity == null){
            res = Response.error("未查询到数据");
        }else{
            afterFind(entity); //二次处理
            res = DateResponse.ok(entity);
        }
        String content = JSON.toJSONString(res);
        ServletUtils.printJson(response,content);
    }

    @RequestMapping(value = "/{id}/findCompareRecipe", method = { RequestMethod.GET, RequestMethod.POST })
    public void findCompareRecipe(Model model, @PathVariable("id") String id, HttpServletRequest request,
                     HttpServletResponse response) {
        Response res;
        if (ObjectUtil.isNullOrEmpty(id)) {
            res = Response.error("参数为空,无法查询");
        }else{
            RmsRecipe entity = rmsRecipeService.selectByIdAndCompareParam(id);
            if(entity == null){
                res = Response.error("未查询到数据");
            }else{
                afterFind(entity); //二次处理
                res = DateResponse.ok(entity);
            }
        }
        String content = JSON.toJSONString(res);
        ServletUtils.printJson(response,content);
    }

    /**
     * recipe提交审批
     * @param entity
     * @param request
     * @param response
     */
    @RequestMapping(value = "upgrade", method = {RequestMethod.GET, RequestMethod.POST})
    public Response upgrade(RmsRecipe entity, HttpServletRequest request, HttpServletResponse response) {
        Response res = Response.ok("提交审批成功");
        rmsRecipeService.upgrade(entity);
        return res;
    }

    /**
     * 拷贝设定值
     * @param recipeIdNew
     * @param recipeIdOld
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "copySetValue", method = {RequestMethod.GET, RequestMethod.POST})
    public Response copySetValue(@RequestParam String recipeIdNew, @RequestParam String recipeIdOld, HttpServletRequest request, HttpServletResponse response) {
        Response res = Response.ok("提交规格设定值拷贝成功");
        return res;
    }

    /**
     * 拷贝最小值
     * @param recipeIdNew
     * @param recipeIdOld
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "copyMinValue", method = {RequestMethod.GET, RequestMethod.POST})
    public Response copyMinValue(@RequestParam String recipeIdNew, @RequestParam String recipeIdOld, HttpServletRequest request, HttpServletResponse response) {
        Response res = Response.ok("提交规格最小值拷贝成功");
        Integer copyMinValue = rmsRecipeService.copyMinValue(recipeIdNew, recipeIdOld);
        if (copyMinValue == 0){
            res = Response.error("提交规格最小值不需要拷贝");
        }
        return res;
    }

    /**
     * 拷贝最大值
     * @param recipeIdNew
     * @param recipeIdOld
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "copyMaxValue", method = {RequestMethod.GET, RequestMethod.POST})
    public Response copyMaxValue(@RequestParam String recipeIdNew, @RequestParam String recipeIdOld, HttpServletRequest request, HttpServletResponse response) {
        Response res = Response.ok("提交规格最大值拷贝成功");
        Integer copyMaxValue = rmsRecipeService.copyMaxValue(recipeIdNew, recipeIdOld);
        if (copyMaxValue == 0){
            res = Response.error("提交规格最大值不需要拷贝");
        }
        return res;
    }

    /**
     * 程序名称列表下拉框
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/recipeCodeList", method = { RequestMethod.GET, RequestMethod.POST })
    public void recordCodeList(Model model, HttpServletRequest request,
                          HttpServletResponse response) {
        List<String> recipeCodeList = rmsRecipeService.recipeCodeList();
        List<Map> list = Lists.newArrayList();
        for (String recipeCode : recipeCodeList) {
            Map map = Maps.newHashMap();
            map.put("id", recipeCode);
            list.add(map);
        }
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    /**
     * 获取审批配方列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/recipePermitList", method = { RequestMethod.GET, RequestMethod.POST })
    public void recipePermitList(@RequestParam("eqpId") String eqpId,
                                 @RequestParam("recipeCode") String recipeCode, @RequestParam("startDate") String startDate,
                                 @RequestParam("endDate") String endDate,@RequestParam("versionType") String versionType,
                                 HttpServletRequest request, HttpServletResponse response) {
        List<RmsRecipe> rmsRecipes = rmsRecipeService.recipePermitList(eqpId,recipeCode,startDate,endDate,versionType);
        String content = JSON.toJSONString(new DateResponse(rmsRecipes));
        ServletUtils.printJson(response, content);
    }

    /**
     * 获取审批配方列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getRecipePermitList", method = { RequestMethod.GET, RequestMethod.POST })
    public void getRecipePermitList(HttpServletRequest request,
                                 HttpServletResponse response) {
        List<RmsRecipe> rmsRecipes = rmsRecipeService.getRecipePermitList();
        String content = JSON.toJSONString(new DateResponse(rmsRecipes));
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/{id}/find", method = { RequestMethod.GET, RequestMethod.POST })
    public void findById(Model model, @PathVariable("id") String id, HttpServletRequest request,
                     HttpServletResponse response) {
        List<RmsRecipe> recipeList = rmsRecipeService.selectList(new EntityWrapper<RmsRecipe>().eq("id", id));
        RmsRecipe rmsRecipe = null;
        if (recipeList.size() > 0){
            rmsRecipe = recipeList.get(0);
        }
        List<RmsRecipeBody> recipeBodyList = rmsRecipeBodyService.selectList(new EntityWrapper<RmsRecipeBody>().eq("recipe_id", id));
        Response res;
        if(rmsRecipe == null){
            res = Response.error("未查询到数据");
        }else{
            rmsRecipe.setRmsRecipeBodyDtlList(recipeBodyList);
            afterFind(rmsRecipe); //二次处理
            res = DateResponse.ok(rmsRecipe);
        }
        String content = JSON.toJSONString(res);
        ServletUtils.printJson(response,content);
    }

    @PutMapping("/status/{id}/{status}")
    public Response editStatus(@PathVariable String id, @PathVariable String status) {
        rmsRecipeService.editStatus(id, status);
        return Response.ok("修改成功");
    }

    @PutMapping("/delete/{id}")
    public Response delete(@PathVariable String id) {
        rmsRecipeService.delete(id);
        return Response.ok("删除成功");
    }

    /**
     * 提交审批
     * @return
     */
    @RequestMapping(value = "updatePermit", method = {RequestMethod.GET, RequestMethod.POST})
    public Response updatePermit(@RequestParam String id, @RequestParam String versionType, @RequestParam String status,
                                 HttpServletRequest request, HttpServletResponse response) {
        Response res = Response.ok("提交成功");
        RmsRecipe rmsRecipe = rmsRecipeService.selectById(id);
        rmsRecipe.setStatus(status);
        rmsRecipe.setVersionType(versionType);
        rmsRecipe.setApproveResult("");
        String remarks = request.getParameter("remarks");
        if (remarks != null && !"".equals(remarks)){
            rmsRecipe.setRemarks(remarks);
        }
        //发送邮件
        String[] email = rmsRecipePermitConfigService.getEmail(rmsRecipe.getApproveStep());
        Map<String, Object> datas = Maps.newHashMap();
        datas.put("RECIPE_CODE", rmsRecipe.getRecipeCode());
        datas.put("EQP_ID", rmsRecipe.getEqpId());
        datas.put("PERMIT_LEVEL", rmsRecipe.getApproveStep());
        datas.put("VERSION_TYPE", rmsRecipe.getVersionType());
        emailSendService.send(email,"RECIPE_PERMIT",datas);
        boolean flag = rmsRecipeService.updateById(rmsRecipe);
        if (!flag){
            res = Response.error("提交失败");
        }
        return res;
    }

}
