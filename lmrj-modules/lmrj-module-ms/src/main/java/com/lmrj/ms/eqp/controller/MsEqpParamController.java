package com.lmrj.ms.eqp.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.ms.eqp.entity.MsEqpParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.eqp.controller
 * @title: ms_eqp_param控制器
 * @description: ms_eqp_param控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:19:46
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/mseqpparam")
@ViewPrefix("ms/mseqpparam")
@RequiresPathPermission("ms:mseqpparam")
@LogAspectj(title = "ms_eqp_param")
public class MsEqpParamController extends BaseCRUDController<MsEqpParam> {

}
