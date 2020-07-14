package com.lmrj.rms.log.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rms.log.entity.RmsRecipeLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.log.controller
 * @title: rms_recipe_log控制器
 * @description: rms_recipe_log控制器
 * @author: 张伟江
 * @date: 2020-07-07 16:10:43
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("rms/rmsrecipelog")
@ViewPrefix("rms/rmsrecipelog")
@RequiresPathPermission("rms:rmsrecipelog")
@LogAspectj(title = "rms_recipe_log")
public class RmsRecipeLogController extends BaseCRUDController<RmsRecipeLog> {

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("配方日志", queryable,  propertyPreFilterable,  request,  response);
    }
}
