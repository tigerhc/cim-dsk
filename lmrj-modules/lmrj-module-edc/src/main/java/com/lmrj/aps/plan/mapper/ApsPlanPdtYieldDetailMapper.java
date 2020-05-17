package com.lmrj.aps.plan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.aps.plan.mapper
 * @title: aps_plan_pdt_yield_detail数据库控制层接口
 * @description: aps_plan_pdt_yield_detail数据库控制层接口
 * @author: 张伟江
 * @date: 2020-05-17 21:01:21
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface ApsPlanPdtYieldDetailMapper extends BaseMapper<ApsPlanPdtYieldDetail> {
    List<ApsPlanPdtYieldDetail> selectDayYield();
}
