package com.lmrj.rw.plan.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rw.plan.entity.RwPlan;
import com.lmrj.rw.plan.entity.WodPlanLog;

import java.util.Date;


/**
 * @author wdj
 * @date 2021-05-14 13:57
 */
public interface IWodPlanLogService  extends ICommonService<WodPlanLog> {
    //工单操作时写入
    void addLog(RwPlan plan, String remarks, String createBy, Date createDate);
}
