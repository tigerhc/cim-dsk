package com.lmrj.aps.plan.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldService;
import com.lmrj.aps.plan.entity.ApsPlanPdtYield;
import com.lmrj.aps.plan.mapper.ApsPlanPdtYieldMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.aps.plan.service.impl
* @title: aps_plan_pdt_yield服务实现
* @description: aps_plan_pdt_yield服务实现
* @author: 张伟江
* @date: 2020-05-17 21:00:52
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("apsPlanPdtYieldService")
public class ApsPlanPdtYieldServiceImpl  extends CommonServiceImpl<ApsPlanPdtYieldMapper,ApsPlanPdtYield> implements  IApsPlanPdtYieldService {

}