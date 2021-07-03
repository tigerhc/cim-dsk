package com.lmrj.rw.plan.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rw.plan.entity.RwPlanHis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-14 14:05
 */
public interface IRwPlanHisService extends ICommonService<RwPlanHis> {

    Integer enddata(RwPlanHis planHis);

    List<RwPlanHis> rwplanhislist(String id, String eqpId, String officeId, String assignedTime, String assignedendTime, String dealTime, String dealendTime, String planStatus, String planType,String flag);

}
