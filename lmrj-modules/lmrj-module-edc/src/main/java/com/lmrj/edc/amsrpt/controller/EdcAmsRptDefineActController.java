package com.lmrj.edc.amsrpt.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefineAct;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.amsrpt.controller
 * @title: edc_ams_rpt_define_act控制器
 * @description: edc_ams_rpt_define_act控制器
 * @author: zhangweijiang
 * @date: 2020-02-15 02:46:34
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcamsrptdefineact")
@ViewPrefix("edc/edcamsrptdefineact")
@RequiresPathPermission("edc:edcamsrptdefineact")
@LogAspectj(title = "edc_ams_rpt_define_act")
public class EdcAmsRptDefineActController extends BaseCRUDController<EdcAmsRptDefineAct> {

}
