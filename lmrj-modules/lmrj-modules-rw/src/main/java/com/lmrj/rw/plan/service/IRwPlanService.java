package com.lmrj.rw.plan.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rw.plan.entity.RwPlan;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-14 13:57
 */
public interface IRwPlanService extends ICommonService<RwPlan> {
    void createPlan(RwPlan plan);
    List<RwPlan> queryCurrectPlan(String id, String eqpId, String assignedTime, String assignedendTime, String dealTime, String dealendTime, String planStatus, String planType);

    /**
     * 2指派后--->已指派（指派人）
     * 3接单后--->已接单（被指派人）
     * 4处理后--->处理结束并回复（被指派人）
     * 5结单--->归档并结束（指派人）
     * @param plan
     * @return
     */
    public boolean updatePlan(RwPlan plan);

    public String checkOnlineWod(RwPlan plan);
}
