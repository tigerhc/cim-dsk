package com.lmrj.edc.ams.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.ams.entity.EdcAmsDefineI18n;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.ams.controller
 * @title: edc_ams_define_i18n控制器
 * @description: edc_ams_define_i18n控制器
 * @author: zhangweijiang
 * @date: 2020-02-15 02:42:19
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcamsdefinei18n")
@ViewPrefix("edc/edcamsdefinei18n")
@RequiresPathPermission("edc:edcamsdefinei18n")
@LogAspectj(title = "edc_ams_define_i18n")
public class EdcAmsDefineI18nController extends BaseCRUDController<EdcAmsDefineI18n> {

}
