package com.lmrj.fab.eqp.controller;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.1.controller
 * @title: fab_equipment_status控制器
 * @description: fab_equipment_status控制器
 * @author: zhangweijiang
 * @date: 2019-06-18 20:41:20
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("fab/fabequipmentstatus")
@ViewPrefix("fab/fabequipmentstatus")
@RequiresPathPermission("fab:fabequipmentstatus")
@LogAspectj(title = "fab_equipment_status")
public class FabEquipmentStatusController extends BaseCRUDController<FabEquipmentStatus> {

    @Autowired
    private IFabEquipmentStatusService fabEquipmentStatusService;

    @Autowired
    private IFabEquipmentService fabEquipmentService;

    @GetMapping("chart")
    public void chart(HttpServletRequest request) {
        List<Map> map=fabEquipmentStatusService.selectEqpStatusChart();
        FastJsonUtils.print(map);
        //List<Map> map=fabEquipmentStatusService.selectEqpStatusChart();
        //return  Response.ok(map.toString());
    }

    @RequestMapping(value = "batch/initstatus", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Response initstatus(@RequestParam(value = "ids", required = false) String[] ids) {
        List idList = java.util.Arrays.asList(ids);
        fabEquipmentStatusService.initStatus(idList);
        return Response.ok("初始化成功");
    }

    @GetMapping("listEqpStatus")
    public void list(HttpServletRequest request, HttpServletResponse response){
        List<FabEquipmentStatus> fabEquipmentStatusList=fabEquipmentStatusService.selectEqpStatus("", "", "");
        if (CollectionUtils.isEmpty(fabEquipmentStatusList)) {
            FastJsonUtils.print(fabEquipmentStatusList);
        }
        FastJsonUtils.print(fabEquipmentStatusList);
    }

    @GetMapping("listPdtStatus")
    public void listPdtStatus(@RequestParam String lineNo, HttpServletRequest request, HttpServletResponse response){
        List<Map> maps = fabEquipmentStatusService.selectYield(lineNo);
        //List<FabEquipment> fabEquipmentList =  fabEquipmentService.selectList(new EntityWrapper<FabEquipment>().eq("line_no", lineNo).eq("step_yield_flag","1"));
        //List<String> eqpIds = Lists.newArrayList();
        //fabEquipmentList.forEach(fabEquipment -> {
        //    eqpIds.add(fabEquipment.getEqpId());
        //});
        //List<FabEquipmentStatus> fabEquipmentStatusList = fabEquipmentStatusService.selectList(new EntityWrapper<FabEquipmentStatus>().in("eqp_id", eqpIds));
        //List<>
        //if (CollectionUtils.isEmpty(fabEquipmentStatusList)) {
        //    FastJsonUtils.print(fabEquipmentStatusList);
        //}
        FastJsonUtils.print(maps);
    }

}
