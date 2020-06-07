package com.lmrj.ms.config.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.ms.config.entity.MsMeasureConfigDetail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.config.controller
 * @title: ms_measure_config_detail控制器
 * @description: ms_measure_config_detail控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:33:20
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasureconfigdetail")
@ViewPrefix("ms/msmeasureconfigdetail")
@RequiresPathPermission("ms:msmeasureconfigdetail")
@LogAspectj(title = "ms_measure_config_detail")
public class MsMeasureConfigDetailController extends BaseCRUDController<MsMeasureConfigDetail> {

}