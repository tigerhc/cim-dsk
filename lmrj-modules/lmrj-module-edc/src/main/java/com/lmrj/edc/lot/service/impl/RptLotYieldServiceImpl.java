package com.lmrj.edc.lot.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.lot.service.IRptLotYieldService;
import com.lmrj.edc.lot.entity.RptLotYield;
import com.lmrj.edc.lot.mapper.RptLotYieldMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.lot.service.impl
* @title: rpt_lot_yield服务实现
* @description: rpt_lot_yield服务实现
* @author: 张伟江
* @date: 2020-05-17 21:10:40
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rptLotYieldService")
public class RptLotYieldServiceImpl  extends CommonServiceImpl<RptLotYieldMapper,RptLotYield> implements  IRptLotYieldService {

}