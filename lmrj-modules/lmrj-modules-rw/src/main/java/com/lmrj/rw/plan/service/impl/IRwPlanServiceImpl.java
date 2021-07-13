package com.lmrj.rw.plan.service.impl;

import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.sys.entity.User;
import com.lmrj.fab.userRole.service.IFabRoleEqpService;
import com.lmrj.fab.userRole.service.IFabUserRoleService;
import com.lmrj.rw.plan.entity.RwPlan;
import com.lmrj.rw.plan.entity.RwPlanHis;
import com.lmrj.rw.plan.mapper.RwPlanMapper;
import com.lmrj.rw.plan.service.IRwPlanHisService;
import com.lmrj.rw.plan.service.IRwPlanService;
import com.lmrj.rw.plan.service.IWodPlanLogService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wdj
 * @date 2021-05-14 14:12
 */
@Transactional
@Service("rwPlanService")
public class IRwPlanServiceImpl extends CommonServiceImpl<RwPlanMapper, RwPlan> implements IRwPlanService {
    @Autowired
    private IRwPlanHisService rwPlanHisService;
    @Autowired
    private IFabRoleEqpService iroleEqpService;
    @Autowired
    private IFabUserRoleService iUserRoleService;
    @Autowired
    private IWodPlanLogService wodPlanLogService;
    /**
     * 写入工单
     * @param plan
     */
    @Override
    public void createPlan(RwPlan plan) {
        baseMapper.insert(plan);
    }

    /**
     * 查询我的工单
     * @param id
     * @param eqpId
     * @param assignedTime
     * @param assignedendTime
     * @param dealTime
     * @param dealendTime
     * @param planStatus
     * @param planType
     * @return
     */
    @Override
    public List<RwPlan> queryCurrectPlan(String id, String eqpId, String assignedTime, String assignedendTime, String dealTime, String dealendTime, String planStatus, String planType) {
        List<RwPlan> retList = new ArrayList<>();
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String userId = getPrincipalProperty(principal, "id");
        User user = UserUtil.getUser(userId);
        List<String> roleList =  iUserRoleService.getRoleByUserId(user.getId());
        List<String> eqpList = iroleEqpService.getEqpByRoleList(roleList);
        Date startAssign = null;
        Date endAssign = null;
        Date startDeal = null;
        Date endDeal = null;
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

            retList = baseMapper.rwplanlist(id, user.getId(),eqpList,startAssign,endAssign,startDeal,endDeal,planStatus,planType);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retList;
    }


    /**
     * 按需查询在途工单
     */
    public List<RwPlan> getPlanList(RwPlan plan){
        Map<String,Object> map = new HashMap<String,Object>();
        if(isNotBlank(plan.getAssignedUser())){
            map.put("assigned_user",plan.getAssignedUser());
        }
        if(isNotBlank(plan.getAssignedTime())){
            map.put("assigned_time",plan.getAssignedTime());
        }
        if(isNotBlank(plan.getDesignee())){
            map.put("designee",plan.getDesignee());
        }
        if(isNotBlank(plan.getEqpId())){
            map.put("eqp_id",plan.getEqpId());
        }
        if(isNotBlank(plan.getPlanId())){
            map.put("plan_id",plan.getPlanId());
        }
        if(isNotBlank(plan.getPlanStatus())){
            map.put("plan_status",plan.getPlanStatus());
        }
        if(isNotBlank(plan.getPlanType())){
            map.put("plan_type",plan.getPlanType());
        }


        return  baseMapper.selectByMap(map);
    }

    /**
     * 2指派后--->已指派（指派人）
     * 3接单后--->已接单（被指派人）
     * 4处理后--->处理结束并回复（被指派人）
     * 5结单--->归档并结束（指派人）
     * @param plan
     * @return
     */
    @Override
    public boolean updatePlan(RwPlan plan){
        boolean flag = true;
        if(plan.getId()==null||"".equals(plan.getId())){
        //指定计划时不存在ID
        baseMapper.insert(plan);
        }
        switch(plan.getPlanStatus()){

            case "5":
                RwPlanHis planHis = new RwPlanHis();
                plan = baseMapper.selectOne(plan);
                plan.setEndDate(new Date());
                BeanUtils.copyProperties(plan,planHis);
             rwPlanHisService.enddata(planHis);
            case "2":
            case "3":
            case "4":
             Integer num =   baseMapper.updateById(plan);
             if(num!=1){
                 flag = false;
             }
                break;
        }

        return flag;


    }

    /**
     * 判断是否为空
     * @param str
     * @return
     */
    boolean isNotBlank(Object str){
        return (str==null||"".equals(str))?false:true;
    }

    /**
     * 获取人员id
     * @param principal
     * @param property
     * @return
     */
    public static String getPrincipalProperty(Object principal, String property) {
        String strValue = null;

        try {
            BeanInfo bi = Introspector.getBeanInfo(principal.getClass());

            // Loop through the properties to get the string value of the
            // specified property
            boolean foundProperty = false;
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(property)) {
                    Object value = pd.getReadMethod().invoke(principal,
                            (Object[]) null);
                    strValue = String.valueOf(value);
                    foundProperty = true;
                    break;
                }
            }

            if (!foundProperty) {
                final String message = "Property [" + property
                        + "] not found in principal of type ["
                        + principal.getClass().getName() + "]";

                throw new RuntimeException(message);
            }

        } catch (Exception e) {
            final String message = "Error reading property [" + property
                    + "] from principal of type ["
                    + principal.getClass().getName() + "]";

            throw new RuntimeException(message, e);
        }

        return strValue;
    }
}
