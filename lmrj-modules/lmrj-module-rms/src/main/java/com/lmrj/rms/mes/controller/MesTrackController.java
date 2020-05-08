package com.lmrj.rms.mes.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.rms.mes.service.MesTrackService;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.entity.MesResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
@RequestMapping("mes/track")
@ViewPrefix("rms/rmsrecipe")
@RequiresPathPermission("mes:track")
@LogAspectj(title = "mes_track")
public class MesTrackController extends BaseCRUDController<RmsRecipe> {

    @Autowired
    private MesTrackService mesTrackService;

    @RequestMapping(value = "model", method = { RequestMethod.GET, RequestMethod.POST })
    public void test1(Model model, HttpServletRequest request, HttpServletResponse response) {
        preList(model, request, response);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String content = JSON.toJSONString(MesResult.ok("before track in success"));
        ServletUtils.printJson(response,content);
    }

    @RequestMapping(value = "test2", method = { RequestMethod.GET, RequestMethod.POST })
    public void test2(Model model, HttpServletRequest request, HttpServletResponse response) {
        String content = "111";
        ServletUtils.printJson(response,content);
    }

    @RequestMapping(value = "test3", method = { RequestMethod.GET, RequestMethod.POST })
    public void test3(Model model, HttpServletRequest request, HttpServletResponse response) {
        preList(model, request, response);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("before track 成功");
        String content = JSON.toJSONString(MesResult.ok("before track 成功"));
        ServletUtils.printJson(response,content);
    }

    @RequestMapping(value = "beforein", method = { RequestMethod.GET, RequestMethod.POST })
    public void beforein(Model model, @RequestParam String EQP_ID,@RequestParam String STEP_ID,@RequestParam String RECIPE_NAME,@RequestParam String LOT_ID, @RequestParam String USER_ID,@RequestParam(required=false) String PARAMS,
                         HttpServletRequest request, HttpServletResponse response) {
        //if("CM-EC-04".equals(EQP_ID)){
        String result = mesTrackService.beforeTrackIn(EQP_ID, RECIPE_NAME, LOT_ID, USER_ID, PARAMS);
        ServletUtils.printJson(response,result);
        //}else{
        //    String result =  mesTrackService.afterTrackIn(EQP_ID, RECIPE_NAME, LOT_ID, USER_ID, PARAMS);
        //    ServletUtils.printJson(response,result);
        //}

    }
    @RequestMapping(value = "afterin", method = { RequestMethod.GET, RequestMethod.POST })
    public void afterin(Model model, @RequestParam String EQP_ID,@RequestParam String STEP_ID,@RequestParam String RECIPE_NAME,@RequestParam String LOT_ID, @RequestParam String USER_ID,@RequestParam(required=false) String PARAMS,
                        HttpServletRequest request, HttpServletResponse response) {
        String result =  mesTrackService.afterTrackIn(EQP_ID, RECIPE_NAME, LOT_ID, USER_ID, PARAMS);
        ServletUtils.printJson(response,result);
    }

    @RequestMapping(value = "stopeqp", method = { RequestMethod.GET, RequestMethod.POST })
    public void stopeqp(Model model, @RequestParam String EQP_ID, @RequestParam String STEP_ID, @RequestParam String USER_ID,@RequestParam(required=false) String PARAMS,
                        HttpServletRequest request, HttpServletResponse response) {
        String result =  mesTrackService.stopEqp(EQP_ID, STEP_ID,USER_ID, PARAMS);
        ServletUtils.printJson(response,result);
    }
}
