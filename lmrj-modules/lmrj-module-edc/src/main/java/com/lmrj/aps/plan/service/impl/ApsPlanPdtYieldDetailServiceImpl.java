package com.lmrj.aps.plan.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldDetailService;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import com.lmrj.aps.plan.mapper.ApsPlanPdtYieldDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.aps.plan.service.impl
* @title: aps_plan_pdt_yield_detail服务实现
* @description: aps_plan_pdt_yield_detail服务实现
* @author: 张伟江
* @date: 2020-05-17 21:01:21
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("apsPlanPdtYieldDetailService")
public class ApsPlanPdtYieldDetailServiceImpl  extends CommonServiceImpl<ApsPlanPdtYieldDetailMapper,ApsPlanPdtYieldDetail> implements  IApsPlanPdtYieldDetailService {

    @Override
    public List<ApsPlanPdtYieldDetail> selectDayYield() {
        return baseMapper.selectDayYield();
    }
}
