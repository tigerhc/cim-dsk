package com.lmrj.dsk.eqplog.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.controller
 * @title: edc_dsk_log_production_his控制器
 * @description: edc_dsk_log_production_his控制器
 * @author: 张伟江
 * @date: 2020-06-08 13:56:07
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("dsk/edcdsklogproductionhis")
@ViewPrefix("dsk/edcdsklogproductionhis")
@RequiresPathPermission("dsk:edcdsklogproductionhis")
@LogAspectj(title = "edc_dsk_log_production_his")
public class EdcDskLogProductionHisController extends BaseCRUDController<EdcDskLogProductionHis> {

}