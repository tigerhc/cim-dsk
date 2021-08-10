package com.lmrj.oven.batchlot.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.controller
 * @title: ovn_batch_lot_param控制器
 * @description: ovn_batch_lot_param控制器
 * @author: zhangweijiang
 * @date: 2019-06-09 08:55:13
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("oven/ovnbatchlotparam")
@ViewPrefix("modules/OvnBatchLotParam")
@RequiresPathPermission("OvnBatchLotParam")
@LogAspectj(title = "ovn_batch_lot_param")
@Slf4j
public class OvnBatchLotParamController extends BaseCRUDController<OvnBatchLotParam> {
    @Autowired
    IFabLogService fabLogService;
    @Autowired
    IOvnBatchLotParamService ovnBatchLotParamService;

}
