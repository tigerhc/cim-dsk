package com.lmrj.oven.batchlot.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.mapper.OvnBatchLotParamMapper;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.fab.service.impl
* @title: ovn_batch_lot_param服务实现
* @description: ovn_batch_lot_param服务实现
* @author: zhangweijiang
* @date: 2019-06-09 08:55:13
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
@Slf4j
@Transactional
@Service("ovnbatchlotparamService")
public class OvnBatchLotParamServiceImpl  extends CommonServiceImpl<OvnBatchLotParamMapper,OvnBatchLotParam> implements IOvnBatchLotParamService {


}