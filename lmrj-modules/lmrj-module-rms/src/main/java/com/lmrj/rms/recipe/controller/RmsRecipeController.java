package com.lmrj.rms.recipe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import com.lmrj.util.lang.ObjectUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.excel.ImportExcel;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


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
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @RequestMapping(value = "uploadrecipe")
    //@LogAspectj(logType = LogType.SELECT)
    //@RequiresMethodPermissions("list")
    public Response uploadRecipe(@RequestParam String eqpId, @RequestParam String recipeName, HttpServletRequest request) {
        Response response = Response.ok("上传成功");
        // TODO: 2019/8/26 判断返回结果
        // TODO: 2019/8/26 springmvc异常处理,当前好像已经有此功能了
        rmsRecipeService.uploadRecipe(eqpId, recipeName);
        return response;
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
            commonService.updateBatchById(updateList);

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
        Response res = Response.ok("提交规格最小值拷贝成功");
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
        Response res = Response.ok("提交规格最小值拷贝成功");
        return res;
    }

}
