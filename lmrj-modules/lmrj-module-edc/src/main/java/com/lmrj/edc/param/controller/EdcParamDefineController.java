package com.lmrj.edc.param.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.param.entity.EdcParamDefine;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.param.controller
 * @title: edc_param_define控制器
 * @description: edc_param_define控制器
 * @author: 张伟江
 * @date: 2019-06-14 23:05:34
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcparamdefine")
@ViewPrefix("edc/edcparamdefine")
@RequiresPathPermission("edc:edcparamdefine")
@LogAspectj(title = "edc_param_define")
public class EdcParamDefineController extends BaseCRUDController<EdcParamDefine> {

}
