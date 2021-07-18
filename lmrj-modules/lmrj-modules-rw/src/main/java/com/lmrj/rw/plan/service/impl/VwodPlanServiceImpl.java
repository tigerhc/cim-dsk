package com.lmrj.rw.plan.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rw.plan.entity.VwodPlan;
import com.lmrj.rw.plan.mapper.VwodPlanMapper;
import com.lmrj.rw.plan.service.IVwodPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("vwodplanservice")
public class VwodPlanServiceImpl extends CommonServiceImpl<VwodPlanMapper, VwodPlan> implements IVwodPlanService {
}
