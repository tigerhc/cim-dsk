package com.lmrj.rms.recipe.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.rms.recipe.service.impl.RmsRecipeBodyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.recipe.controller
 * @title: rms_recipe_body控制器
 * @description: rms_recipe_body控制器
 * @author: zhangweijiang
 * @date: 2019-06-15 01:58:21
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipebody")
@ViewPrefix("rms/rmsrecipebody")
@RequiresPathPermission("rms:rmsrecipebody")
@LogAspectj(title = "rms_recipe_body")
public class RmsRecipeBodyController extends BaseCRUDController<RmsRecipeBody> {

    @Autowired
    private IRmsRecipeBodyService rmsRecipeBodyService;

    @RequestMapping(value = "checkRecipeBody")
    public Response checkRecipeBody(@RequestParam String eqpId, @RequestParam String recipeCode, @RequestParam String recipeBody, @RequestParam String recipeBodySize, HttpServletRequest request) {
        Response response = Response.ok("参数校验通过");
        boolean flag = false;
        try {
            flag = "Y".equals(rmsRecipeBodyService.checkRecipeBody(eqpId, recipeCode, recipeBody, recipeBodySize).getResult());
            if (!flag){
                response = Response.error(999998, "参数校验失败");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = Response.error(999998,e.getMessage());
        }
        return response;
    }

}
