package com.lmrj.cim.modules.oa.controller;


import com.lmrj.cim.modules.oa.service.IOaNotificationService;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.cim.modules.oa.entity.OaNotification;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @Title: 通知公告
 * @Description: 通知公告
 * @author lmrj
 * @date 2017-06-10 17:15:17
 * @version V1.0
 *
 */
@RestController
@RequestMapping("oa/oanotification")
@ViewPrefix("modules/oa/oanotification")
@RequiresPathPermission("oa:oanotification")

public class OaNotificationController extends BaseCRUDController<OaNotification> {
    @Autowired
    private IOaNotificationService iOaNotificationService;
    @RequestMapping("/findList")
    public List<Map<String,Object>> findList(){
        List<Map<String,Object>> result = iOaNotificationService.findList();
       for(Map map:result){
           Date time =(Date)map.get("create_date");
//           Date date =new Date(time);
           SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
           String temp =df.format(time);
           map.put("create_date",temp);
       }
        return result;
    }

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("公告信息", queryable,  propertyPreFilterable,  request,  response);
    }


}
