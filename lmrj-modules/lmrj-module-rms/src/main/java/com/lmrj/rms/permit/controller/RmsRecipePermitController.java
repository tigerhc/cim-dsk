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
import com.lmrj.rms.permit.entity.RmsRecipePermit;
import com.lmrj.rms.permit.entity.RmsRecipePermitConfig;
import com.lmrj.rms.permit.service.IRmsRecipePermitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.permit.controller
 * @title: rms_recipe_permit控制器
 * @description: rms_recipe_permit控制器
 * @author: 张伟江
 * @date: 2020-07-15 23:08:38
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipepermit")
@ViewPrefix("rms/rmsrecipepermit")
@RequiresPathPermission("rms:rmsrecipepermit")
@LogAspectj(title = "rms_recipe_permit")
public class RmsRecipePermitController extends BaseCRUDController<RmsRecipePermit> {

    @Autowired
    private IRmsRecipePermitService rmsRecipePermitService;

    @RequestMapping(value = "/getHistory", method = { RequestMethod.GET, RequestMethod.POST })
    public void findByRecipeId(@RequestParam("recipeId") String recipeId, HttpServletRequest request,
                         HttpServletResponse response) {
        List<RmsRecipePermit> recipePermitList = rmsRecipePermitService.selectList(new EntityWrapper<RmsRecipePermit>().eq("recipe_id", recipeId).orderBy("submit_date desc"));
        DateResponse listjson = new DateResponse(recipePermitList);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response,content);
    }

    @RequestMapping(value = "/permit", method = { RequestMethod.GET, RequestMethod.POST })
    public Response recipePermit(@RequestParam("approveStep") String approveStep, @RequestParam("roleName") String roleName,
                             @RequestParam("id") String recipeId, @RequestParam("submitResult") String submitResult,
                             @RequestParam("submitDesc") String submitDesc,HttpServletRequest request) {
        Response res = Response.ok("审批成功");
        try {
            rmsRecipePermitService.recipePermit(approveStep,roleName,recipeId,submitResult,submitDesc);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            res = Response.error(999998,e.getMessage());
        }
        return res;
    }

}
