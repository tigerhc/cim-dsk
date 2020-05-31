package com.lmrj.aps.plan.service.impl;

import com.lmrj.aps.plan.entity.ApsPlanPdtYield;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import com.lmrj.aps.plan.mapper.ApsPlanPdtYieldMapper;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldDetailService;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


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
public class ApsPlanPdtYieldServiceImpl extends CommonServiceImpl<ApsPlanPdtYieldMapper, ApsPlanPdtYield> implements IApsPlanPdtYieldService {
    @Autowired
    private IApsPlanPdtYieldDetailService apsPlanPdtYieldDetailService;

    @Override
    public ApsPlanPdtYield selectById(Serializable id) {
        ApsPlanPdtYield apsPlanPdtYield = super.selectById(id);
        List<ApsPlanPdtYieldDetail> apsPlanPdtYieldDetailList = apsPlanPdtYieldDetailService.selectList(new EntityWrapper<ApsPlanPdtYieldDetail>(ApsPlanPdtYieldDetail.class)
                .eq("production_no", apsPlanPdtYield.getProductionNo())
                .like("plan_date", apsPlanPdtYield.getPlanDate())
                .orderBy("PLAN_DATE, lot_no"));
        apsPlanPdtYield.setApsPlanPdtYieldDetailList(apsPlanPdtYieldDetailList);
        return apsPlanPdtYield;
    }

    @Override
    public void deleteByPeriod(String period) {
        baseMapper.deleteByPeriod(period);
    }
}
