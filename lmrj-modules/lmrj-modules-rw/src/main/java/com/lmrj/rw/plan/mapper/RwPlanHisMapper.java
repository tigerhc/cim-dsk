package com.lmrj.rw.plan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.rw.plan.entity.RwPlanHis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author wdj
 * @date 2021-05-14 14:02
 */
@Mapper
public interface RwPlanHisMapper extends BaseMapper<RwPlanHis> {


    List<RwPlanHis> rwplanhislist(@Param("id") String id, @Param("eqpId") String eqpId, @Param("officeId") String officeId,
                                  @Param("startAssign") Date startAssign, @Param("endAssign") Date endAssign,
                                  @Param("startDeal") Date startDeal, @Param("endDeal") Date endDeal,
                                  @Param("planStatus") String planStatus, @Param("planType") String planType
            ,                       @Param("flag") String flag);

}
