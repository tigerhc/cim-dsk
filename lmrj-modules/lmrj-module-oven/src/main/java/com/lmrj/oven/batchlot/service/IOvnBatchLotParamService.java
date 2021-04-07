package com.lmrj.oven.batchlot.service;


import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;

import java.util.List;

/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.fab.service
* @title: ovn_batch_lot_param服务接口
* @description: ovn_batch_lot_param服务接口
* @author: zhangweijiang
* @date: 2019-06-09 08:55:13
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
public interface IOvnBatchLotParamService extends ICommonService<OvnBatchLotParam> {

    List<OvnBatchLotParam> selectTempData(java.util.Date startTime, java.util.Date endTime);
}