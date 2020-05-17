package com.lmrj.aps.plan.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;

import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.aps.plan.service
* @title: aps_plan_pdt_yield_detail服务接口
* @description: aps_plan_pdt_yield_detail服务接口
* @author: 张伟江
* @date: 2020-05-17 21:01:21
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IApsPlanPdtYieldDetailService extends ICommonService<ApsPlanPdtYieldDetail> {
    List<ApsPlanPdtYieldDetail> selectDayYield();

}
