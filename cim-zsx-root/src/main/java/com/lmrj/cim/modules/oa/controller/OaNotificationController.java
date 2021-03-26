package com.lmrj.cim.modules.oa.controller;


import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.cim.modules.oa.entity.OaNotificationEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title: 通知公告
 * @Description: 通知公告
 * @author lmrj
 * @date 2017-06-10 17:15:17
 * @version V1.0
 *
 */
//@RestController
//@RequestMapping("oa/oanotification")
//@ViewPrefix("oa/oanotification")
//@RequiresPathPermission("oa:oanotification")
//@LogAspectj(title = "oa/oanotification")
public class OaNotificationController extends BaseCRUDController<OaNotificationEntity> {

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("功能信息", queryable,  propertyPreFilterable,  request,  response);
    }

}
