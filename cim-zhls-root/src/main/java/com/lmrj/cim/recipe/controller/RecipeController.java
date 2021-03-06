package com.lmrj.cim.recipe.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.cim.recipe.service.IRecipeService;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("rms2/rmsrecipe")
@RequiresPathPermission("rms:rmsrecipe")
public class RecipeController extends BaseCRUDController<RmsRecipe> {

    @Autowired
    private IRecipeService recipeService;

    /**
     *  查询recipe列表
     * @param request
     */
    @RequestMapping(value = "getRecipeList", method = { RequestMethod.GET, RequestMethod.POST })
    public void selectRecipeList(@RequestParam String eqpId, HttpServletRequest request, HttpServletResponse response) {
        Response res = null;
        try {
            List<String> recipeList = recipeService.selectRecipeList(eqpId);
            if (recipeList.size() == 0){
                res = Response.error(999998, "未查询到配方");
            } else {
                res = DateResponse.ok(recipeList);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            res = Response.error(999998, e.getMessage());
        }

        String content = JSON.toJSONString(res);
        ServletUtils.printJson(response,content);
    }

    /**
     * 上传recipe
     * @param request
     */
    @RequestMapping(value = "uploadrecipe")
    public Response uploadRecipe(@RequestParam String eqpId, @RequestParam String recipeList, HttpServletRequest request) {
        Response response = Response.ok("上传成功");
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
            flag = recipeService.uploadRecipe(eqpId, recipes);
            if (!flag){
                response = Response.error(999998, "上传失败");
            }
        } catch (Exception e) {
            response = Response.error(999998,e.getMessage());
        }
        return response;
    }
}
