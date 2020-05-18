package com.lmrj.edc.lot.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.lot.entity.RptLotYield;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.lot.controller
 * @title: rpt_lot_yield控制器
 * @description: rpt_lot_yield控制器
 * @author: 张伟江
 * @date: 2020-05-17 21:10:40
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/rptlotyield")
@ViewPrefix("edc/rptlotyield")
@RequiresPathPermission("edc:rptlotyield")
public class RptLotYieldController extends BaseCRUDController<RptLotYield> {



}
