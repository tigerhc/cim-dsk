package com.lmrj.cim.modules.oa.controller;


import com.lmrj.common.mvc.annotation.ViewPrefix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.cim.modules.oa.entity.OaNotification;

/**
 * @Title: 通知公告
 * @Description: 通知公告
 * @author lmrj
 * @date 2017-06-10 17:15:17
 * @version V1.0
 *
 */
@Controller
@RequestMapping("oa/oanotification")
@ViewPrefix("modules/oa/oanotification")
@RequiresPathPermission("oa:oanotification")
public class OaNotificationController extends BaseCRUDController<OaNotification> {

}
