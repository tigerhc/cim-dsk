package com.lmrj.rms.template.controller;

import com.alibaba.fastjson.JSONObject;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.rms.template.entity.RmsRecipeTemplate;
import com.lmrj.rms.template.service.IRmsRecipeTemplateService;
import com.lmrj.core.log.LogAspectj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.template.controller
 * @title: rms_recipe_template控制器
 * @description: rms_recipe_template控制器
 * @author: zhangweijiang
 * @date: 2019-06-15 01:58:43
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipetemplate")
@ViewPrefix("rms/rmsrecipetemplate")
@RequiresPathPermission("rms:rmsrecipetemplate")
@LogAspectj(title = "rms_recipe_template")
public class RmsRecipeTemplateController extends BaseCRUDController<RmsRecipeTemplate> {

    @Autowired
    private IRmsRecipeTemplateService iRmsRecipeTemplateService;
    @RequestMapping(value = "batchUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Response update(HttpServletRequest request, HttpServletResponse response) {
        String str=request.getParameter("recipeTemplateList");
        List<RmsRecipeTemplate> recipeTemplateList= JSONObject.parseArray(str, RmsRecipeTemplate.class);
         iRmsRecipeTemplateService.updateBatchById(recipeTemplateList);
        return Response.ok("修改成功");
    }

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("配方详情", queryable,  propertyPreFilterable,  request,  response);
    }
}
