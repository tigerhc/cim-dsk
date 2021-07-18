package com.lmrj.rw.plan.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rw.plan.entity.WodPlanDay;

public interface IWodPlanDayService extends ICommonService<WodPlanDay> {

    String checkTime(WodPlanDay wodPlanDay);
}
