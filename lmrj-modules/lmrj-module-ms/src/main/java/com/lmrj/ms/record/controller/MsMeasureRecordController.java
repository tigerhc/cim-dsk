package com.lmrj.ms.record.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.record.controller
 * @title: ms_measure_record控制器
 * @description: ms_measure_record控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasurerecord")
@ViewPrefix("ms/msmeasurerecord")
@RequiresPathPermission("ms:msmeasurerecord")
@LogAspectj(title = "ms_measure_record")
public class MsMeasureRecordController extends BaseCRUDController<MsMeasureRecord> {

}