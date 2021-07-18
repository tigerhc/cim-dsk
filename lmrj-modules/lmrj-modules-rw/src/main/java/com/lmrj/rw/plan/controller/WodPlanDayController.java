package com.lmrj.rw.plan.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rw.plan.entity.RwPlan;
import com.lmrj.rw.plan.entity.WodPlanDay;
import com.lmrj.rw.plan.service.IRwPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("rw/wodplanday")
@ViewPrefix("rw/wodplanday")
@RequiresPathPermission("rw:wodplanday")
@LogAspectj(title = "wod_plan_day")
public class WodPlanDayController  extends BaseCRUDController<WodPlanDay> {
    @Autowired
    private IRwPlanService rwPlanService;

    @Override
    @RequestMapping(
            value = {"{id}/update"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Response update(Model model, @Valid @ModelAttribute("data") WodPlanDay entity, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if(entity.getDesignee()!=null&&!"".equals(entity.getDesignee())){
            RwPlan rwPlan = new RwPlan();
            String ret = "";
            //进入制定计划页面
            //1.判断当前是否存在在途工单，
            rwPlan.setPlanId(entity.getId());
            ret = rwPlanService.checkOnlineWod(rwPlan);
            if(!"".equals(ret)){
              return  Response.error("保存失败!<br />原因:存在在途工单-编号：" + ret);
            }
            //2.判断发起时间是否符合规则  启始时间+周期+最后一次的创建时间
            rwPlan.setPlanStatus("2");
            rwPlan.setPlanId(entity.getId());
            rwPlan.setAssignedTime(entity.getAssignedTime());
            rwPlan.setDesignee(entity.getDesignee());
            rwPlan.setAssignedUser(entity.getAssignedUser());
            rwPlan.setPlanType(entity.getPlanType());
            rwPlan.setEqpId(entity.getEqpId());
            rwPlanService.insert(rwPlan);
            return null;
        }else{
            //进入工单计划页面
            return super.doSave(entity, request, response, result);
        }

    }
}
