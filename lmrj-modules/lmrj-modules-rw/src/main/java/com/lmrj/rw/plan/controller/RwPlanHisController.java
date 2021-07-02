package com.lmrj.rw.plan.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.rw.plan.entity.RwPlanHis;
import com.lmrj.rw.plan.service.IRwPlanHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wdj
 * @date 2021-05-18 8:49
 */
@RestController
@RequestMapping("rw/rwplanhis")
@ViewPrefix("rw/rwplanhis")
@RequiresPathPermission("rw:rwplanhis")
@LogAspectj(title = "rw_rwplan_his")
public class RwPlanHisController extends BaseCRUDController<RwPlanHis> {
    @Autowired
    private IRwPlanHisService rwPlanHisService;


    /**
     * 工单数据归档
     * @param
     */
    @RequestMapping(value = "/enddata", method = { RequestMethod.GET, RequestMethod.POST })
    public void enddata(RwPlanHis planHis, HttpServletRequest request, HttpServletResponse response){
        Integer enddata = rwPlanHisService.enddata( planHis );
        String s = JSON.toJSONString( new DateResponse( enddata ) );
        System.out.println("hello");
        ServletUtils.printJson(response, s);
    }

    /**
     * 工单总览
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/rwplanhislist", method = { RequestMethod.GET, RequestMethod.POST })
    public void rwplanhislist(@RequestParam("id") String id,@RequestParam("officeId") String officeId,
                                 @RequestParam("eqpId") String eqpId, @RequestParam("assignedTime") String assignedTime,
                                 @RequestParam("dealTime") String dealTime, @RequestParam("planStatus") String planStatus,
                                @RequestParam("assignedendTime") String assignedendTime,@RequestParam("dealendTime") String dealendTime,
                              @RequestParam("planType") String planType,@RequestParam("flag") String flag,HttpServletRequest request, HttpServletResponse response) {

        List<RwPlanHis> RwPlanHiss = rwPlanHisService.rwplanhislist(id,officeId,eqpId,assignedTime,assignedendTime,dealTime,dealendTime,planStatus,planType,flag);
        String content = JSON.toJSONString(new DateResponse(RwPlanHiss));
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/test/{id}", method = { RequestMethod.GET, RequestMethod.POST })
    public String rwTest(@PathVariable String id , HttpServletRequest request, HttpServletResponse response){
        List<RwPlanHis> test = rwPlanHisService.test1( id );
        String s = JSON.toJSONString( new DateResponse( test ) );
        ServletUtils.printJson(response, s);
        System.out.println("啊啊啊啊");
        return "吴虎成";
    }
}
