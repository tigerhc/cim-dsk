package com.lmrj.rw.plan.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.parse.QueryToWrapper;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.query.data.Pageable;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.query.utils.QueryableConvertUtils;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rw.plan.entity.RwPlan;
import com.lmrj.rw.plan.entity.RwPlanHis;
import com.lmrj.rw.plan.service.IRwPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 14:35
 */
@RestController
@RequestMapping("rw/rwplan")
@ViewPrefix("rw/rwplan")
@RequiresPathPermission("rw:rwplan")
@LogAspectj(title = "rw_rwplan")
public class RwPlanController  extends BaseCRUDController<RwPlan> {
    @Autowired
    private IRwPlanService iRwPlanService;

    /**
     * 我的工单查询
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/rwplanlist", method = { RequestMethod.GET, RequestMethod.POST })
    public void rwplanhislist(@RequestParam("id") String id,@RequestParam("eqpId") String eqpId, @RequestParam("assignedTime") String assignedTime,
                              @RequestParam("dealTime") String dealTime, @RequestParam("planStatus") String planStatus,
                              @RequestParam("assignedendTime") String assignedendTime, @RequestParam("dealendTime") String dealendTime,
                              @RequestParam("planType") String planType, HttpServletRequest request, HttpServletResponse response) {

        List<RwPlan> RwPlans = iRwPlanService.queryCurrectPlan(id, eqpId, assignedTime, assignedendTime, dealTime, dealendTime, planStatus, planType);
        String content = JSON.toJSONString(new DateResponse(RwPlans));
        ServletUtils.printJson(response, content);


    }

    /**
     * 指派/发起
     * @param id
     * @param assignedUser
     * @param assignedTime
     * @param designee
     * @param planStatus
     * @param dealAdvice
     * @param request
     * @return
     */
    @RequestMapping(value = "assign")
    public Response assign(@RequestParam String id, @RequestParam String assignedUser, @RequestParam String assignedTime
            , @RequestParam String designee, @RequestParam String planStatus, @RequestParam String dealAdvice,
                           HttpServletRequest request) {
        Response response = Response.ok("指派成功");
        boolean flag = false;
        RwPlan rwPlan = new RwPlan();
        rwPlan.setId(id);
        rwPlan.setAssignedUser(assignedUser);
        try {
            rwPlan.setAssignedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(assignedTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rwPlan.setDesignee(designee);
        rwPlan.setPlanStatus(planStatus);
        rwPlan.setDealAdvice(dealAdvice);
        try {
            flag = iRwPlanService.updatePlan(rwPlan);
            if (!flag){
                response = Response.error(999998, "指派失败");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = Response.error(999998,e.getMessage());
        }
        return response;
    }

    /**
     * 接单
     * @param id
     * @param designee
     * @param planStatus
     * @param request
     * @return
     */
    @RequestMapping(value = "receiveOrder")
    public Response receiveOrder(@RequestParam String id, @RequestParam String designee,
                                 @RequestParam String planStatus,HttpServletRequest request) {
        Response response = Response.ok("接单成功");
        boolean flag = false;
        RwPlan rwPlan = new RwPlan();
        rwPlan.setId(id);
        rwPlan.setDesignee(designee);
        rwPlan.setPlanStatus(planStatus);
        try {
            flag = iRwPlanService.updatePlan(rwPlan);
            if (!flag){
                response = Response.error(999998, "接单成功");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = Response.error(999998,e.getMessage());
        }
        return response;
    }

    /**
     * 上报
     * @param id
     * @param dealType
     * @param dealTime
     * @param dealDes
     * @param planStatus
     * @param request
     * @return
     */
    @RequestMapping(value = "Report")
    public Response Report(@RequestParam String id, @RequestParam String dealType, @RequestParam String dealTime,
                           @RequestParam String dealDes,@RequestParam String planStatus,HttpServletRequest request) {
        Response response = Response.ok("接单成功");
        boolean flag = false;
        RwPlan rwPlan = new RwPlan();
        rwPlan.setId(id);
        rwPlan.setDealType(dealType);
        rwPlan.setDealDes(dealDes);
        rwPlan.setPlanStatus(planStatus);
        try {
            rwPlan.setDealTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dealTime));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            response = Response.error(999998,e.getMessage());
        }
        try {
            flag = iRwPlanService.updatePlan(rwPlan);
            if (!flag){
                response = Response.error(999998, "接单失败");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = Response.error(999998,e.getMessage());
        }
        return response;
    }


    @RequestMapping(value = "Statement")
    public Response Statement(@RequestParam String id,HttpServletRequest request) {
        Response response = Response.ok("结单成功");
        boolean flag = false;
        RwPlan rwPlan = new RwPlan();
        rwPlan.setId(id);
        rwPlan.setPlanStatus("4");
        try {
            flag = iRwPlanService.updatePlan(rwPlan);
            if (!flag){
                response = Response.error(999998, "结单失败");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = Response.error(999998,e.getMessage());
        }
        return response;
    }



    @RequestMapping(
            value = {"page"},
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public void pageList(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) throws IOException {
        EntityWrapper<RwPlan> entityWrapper = new EntityWrapper(this.entityClass);
        this.prePage(queryable, entityWrapper, request, response);
        propertyPreFilterable.addQueryProperty(new String[]{"id"});
        QueryableConvertUtils.convertQueryValueToEntityValue(queryable, this.entityClass);
        SerializeFilter filter = propertyPreFilterable.constructFilter(this.entityClass);
        QueryToWrapper<RwPlan> queryToWrapper = new QueryToWrapper();
        queryToWrapper.parseCondition(entityWrapper, queryable);
        queryToWrapper.parseSort(entityWrapper, queryable);
        Pageable pageable = queryable.getPageable();
        com.baomidou.mybatisplus.plugins.Page<RwPlan> page = new com.baomidou.mybatisplus.plugins.Page(pageable.getPageNumber(), pageable.getPageSize());
        //com.baomidou.mybatisplus.plugins.Page<RwPlan> content = this.selectPage(page, wrapper);
        PageResponse<RwPlan> pagejson = new PageResponse(this.commonService.list(queryable, entityWrapper));
        this.afterPage(pagejson, request, response);
        String content = JSON.toJSONString(pagejson, filter, new SerializerFeature[0]);
        ServletUtils.printJson(response, content);
    }


}
