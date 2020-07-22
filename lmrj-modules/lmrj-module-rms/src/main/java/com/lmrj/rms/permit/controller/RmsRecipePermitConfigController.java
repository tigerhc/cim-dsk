package com.lmrj.rms.permit.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rms.permit.entity.RmsRecipePermitConfig;
import com.lmrj.rms.permit.service.IRmsRecipePermitConfigService;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.permit.controller
 * @title: rms_recipe_permit_config控制器
 * @description: rms_recipe_permit_config控制器
 * @author: 张伟江
 * @date: 2020-07-15 23:08:59
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipepermitconfig")
@ViewPrefix("rms/rmsrecipepermitconfig")
@RequiresPathPermission("rms:rmsrecipepermitconfig")
@LogAspectj(title = "rms_recipe_permit_config")
public class RmsRecipePermitConfigController extends BaseCRUDController<RmsRecipePermitConfig> {

    @Autowired
    private IRmsRecipePermitConfigService rmsRecipePermitConfigService;

    @RequestMapping(value = "/getPermitConfig", method = { RequestMethod.GET, RequestMethod.POST })
    public void getPermitConfig(@RequestParam("approveStep") String submitLevel, HttpServletRequest request,
                         HttpServletResponse response) {
        List<RmsRecipePermitConfig> recipePermitConfigList = rmsRecipePermitConfigService.selectList(new EntityWrapper<RmsRecipePermitConfig>().eq("submit_level", Integer.parseInt(submitLevel)));
        RmsRecipePermitConfig recipePermitConfig = null;
        if (recipePermitConfigList.size() > 0){
            recipePermitConfig = recipePermitConfigList.get(0);
        }
        Response res;
        if(recipePermitConfig == null){
            res = Response.error("未查询到数据");
        }else{
            afterFind(recipePermitConfig); //二次处理
            res = DateResponse.ok(recipePermitConfig);
        }
        String content = JSON.toJSONString(res);
        ServletUtils.printJson(response,content);
    }

    @RequestMapping(value = "/getPermitConfigList", method = { RequestMethod.GET, RequestMethod.POST })
    public void getPermitConfigList(HttpServletRequest request,
                                HttpServletResponse response) {
        List<RmsRecipePermitConfig> recipePermitConfigList = rmsRecipePermitConfigService.selectList(new EntityWrapper<RmsRecipePermitConfig>());
        DateResponse listjson = new DateResponse(recipePermitConfigList);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/updatePermitConfig", method = { RequestMethod.GET, RequestMethod.POST })
    public Response updatePermitConfig(@RequestParam("roleName") String submitterRoleName, @RequestParam("submitLevel") String submitLevel,
                                   HttpServletRequest request, HttpServletResponse response) {
        Response res = Response.ok("修改成功");
        Integer i = rmsRecipePermitConfigService.updateRoleNameBySubmitLevel(submitterRoleName, submitLevel);
        if(i > 0){
            return res;
        }else{
            return Response.error(999998,"导出失败");
        }
    }

}
