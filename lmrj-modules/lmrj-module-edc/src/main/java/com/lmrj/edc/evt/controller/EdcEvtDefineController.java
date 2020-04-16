package com.lmrj.edc.evt.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.evt.entity.EdcEvtDefine;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.evt.controller
 * @title: edc_evt_define控制器
 * @description: edc_evt_define控制器
 * @author: 张伟江
 * @date: 2019-06-14 16:01:31
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcevtdefine")
@ViewPrefix("edc/edcevtdefine")
@RequiresPathPermission("edc:edcevtdefine")
@LogAspectj(title = "edc_evt_define")
public class EdcEvtDefineController extends BaseCRUDController<EdcEvtDefine> {

}
