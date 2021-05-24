package com.lmrj.rw.plan.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rw.plan.entity.RwPlanHis;
import com.lmrj.rw.plan.mapper.RwPlanHisMapper;
import com.lmrj.rw.plan.service.IRwPlanHisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wdj
 * @date 2021-05-14 14:13
 */
@Transactional
@Service("rwPlanHisService")
public class IRwPlanHisServiceImpl extends CommonServiceImpl<RwPlanHisMapper, RwPlanHis> implements IRwPlanHisService {

    /**
     * 工单数据归档
     * @param planHis
     */
    @Override
    public void enddata(RwPlanHis planHis) {
        baseMapper.insert(planHis);

    }

    @Override
    public List<RwPlanHis> rwplanhislist(String id, String eqpId, String officeId, String assignedTime, String assignedendTime, String dealTime, String dealendTime, String planStatus, String planType ,String flag) {

        Date startAssign = null;
        Date endAssign = null;
        Date startDeal = null;
        Date endDeal = null;
        List<RwPlanHis> retList = new ArrayList<RwPlanHis>();

        try {
            if (assignedTime !=null && !"".equals(assignedTime)) {
                startAssign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(assignedTime + " 00:00:00");
            }
            if (assignedendTime != null && !"".equals(assignedendTime)){
                endAssign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(assignedendTime + " 23:59:59");
            }

            if (dealTime !=null && !"".equals(dealTime)) {
                startDeal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dealTime + " 00:00:00");
            }
            if (dealendTime != null && !"".equals(dealendTime)){
                endDeal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dealendTime + " 23:59:59");
            }
            retList =  baseMapper.rwplanhislist(id,eqpId,officeId,startAssign,endAssign,startDeal,endDeal,planStatus,planType,flag);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retList;
    }
}
