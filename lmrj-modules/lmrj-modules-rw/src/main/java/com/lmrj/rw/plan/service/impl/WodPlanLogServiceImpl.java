package com.lmrj.rw.plan.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rw.plan.entity.RwPlan;
import com.lmrj.rw.plan.entity.WodPlanLog;
import com.lmrj.rw.plan.mapper.WodPlanLogMapper;
import com.lmrj.rw.plan.service.IWodPlanLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * @author wdj
 * @date 2021-05-14 14:12
 */
@Transactional
@Service("wodPlanLogService")
public class WodPlanLogServiceImpl  extends CommonServiceImpl<WodPlanLogMapper, WodPlanLog> implements IWodPlanLogService {
    @Override
    public void addLog(RwPlan plan, String remarks, String createBy, Date createDate) {
        WodPlanLog log = new WodPlanLog();
        log.setEqpId(plan.getEqpId());
        log.setPlanStatus(plan.getPlanStatus());
        log.setPlanType(plan.getPlanType());
        log.setDealType(plan.getDealType());
        log.setWodId(plan.getPlanId());
        log.setRemarks(remarks);
        log.setCreateBy(createBy);
        log.setCreateDate(createDate);
        this.baseMapper.insert(log);
    }
}
