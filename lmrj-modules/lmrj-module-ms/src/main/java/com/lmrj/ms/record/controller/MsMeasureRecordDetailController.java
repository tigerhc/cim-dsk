package com.lmrj.ms.record.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.ms.record.entity.MsMeasureRecordDetail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.record.controller
 * @title: ms_measure_record_detail控制器
 * @description: ms_measure_record_detail控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:36:47
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasurerecorddetail")
@ViewPrefix("ms/msmeasurerecorddetail")
@RequiresPathPermission("ms:msmeasurerecorddetail")
@LogAspectj(title = "ms_measure_record_detail")
public class MsMeasureRecordDetailController extends BaseCRUDController<MsMeasureRecordDetail> {

}