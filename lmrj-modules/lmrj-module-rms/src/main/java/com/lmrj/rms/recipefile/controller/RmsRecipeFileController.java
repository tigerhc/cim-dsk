package com.lmrj.rms.recipefile.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.rms.recipefile.entity.RmsRecipeFile;
import com.lmrj.rms.recipefile.service.IRmsRecipeFileService;
import com.lmrj.core.log.LogAspectj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.recipefile.controller
 * @title: rms_recipe_file控制器
 * @description: rms_recipe_file控制器
 * @author: zhangweijiang
 * @date: 2019-07-14 02:57:51
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipefile")
@ViewPrefix("rms/rmsrecipefile")
@RequiresPathPermission("rms:rmsrecipefile")
@LogAspectj(title = "rms_recipe_file")
public class RmsRecipeFileController extends BaseCRUDController<RmsRecipeFile> {
    @Autowired
    private IRmsRecipeFileService rmsRecipeFileService;

    @RequestMapping(value = "/{id}/findFileByRecipeId", method = { RequestMethod.GET, RequestMethod.POST })
    private void findFileByRecipeId(@PathVariable("id") String recipeId, HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        List<RmsRecipeFile> rmsRecipeFileList = rmsRecipeFileService.findFileByRecipeId(recipeId);
        String content = JSON.toJSONString(DateResponse.ok(rmsRecipeFileList));
        ServletUtils.printJson(response,content);
    }
}
