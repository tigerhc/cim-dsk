package com.lmrj.rw.plan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.rw.plan.entity.RwPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author wdj
 * @date 2021-05-14 13:57
 */
@Mapper
public interface RwPlanMapper extends BaseMapper<RwPlan> {

    List<RwPlan> rwplanlist(@Param("id") String id, @Param("userId") String userId,@Param("eqpList") List<String> eqpList,
                                  @Param("startAssign") Date startAssign, @Param("endAssign") Date endAssign,
                                  @Param("startDeal") Date startDeal, @Param("endDeal") Date endDeal,
                                  @Param("planStatus") String planStatus, @Param("planType") String planType);
}
