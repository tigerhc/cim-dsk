package com.lmrj.edc.param.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.param.entity.EdcParamDefineModel;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.param.controller
 * @title: edc_param_define_model控制器
 * @description: edc_param_define_model控制器
 * @author: zhangweijiang
 * @date: 2019-06-14 23:14:27
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcparamdefinemodel")
@ViewPrefix("edc/edcparamdefinemodel")
@RequiresPathPermission("edc:edcparamdefinemodel")
@LogAspectj(title = "edc_param_define_model")
public class EdcParamDefineModelController extends BaseCRUDController<EdcParamDefineModel> {

}
