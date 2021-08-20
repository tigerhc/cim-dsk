package com.lmrj.fab.eqp.controller;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashMap;
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

    @Value("${dsk.lineNo}")
    String lineNo1;

    @GetMapping("chart")
    public void chart(HttpServletRequest request, @RequestParam String curProject) {//TODO 高协助首页分前后工程  curProject
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
    public void list(HttpServletRequest request, HttpServletResponse response,@RequestParam String curProject){//TODO 高协助首页分前后工程  curProject
        Map<String, Object> rs = new HashMap<>();
        List<FabEquipmentStatus> fabEquipmentStatusList=fabEquipmentStatusService.selectEqpStatus("", "", "",curProject);
        rs.put("eqpList",fabEquipmentStatusList);
        String alarmInfo = "警报发生：";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -2);
        List<Map> amsInfoLis = fabEquipmentStatusService.selectAlarmInfo(lineNo1,curProject);
        if(amsInfoLis.size()>0){
            for (Map amsInfoLi : amsInfoLis) {
                String eqpId = (String) amsInfoLi.get("eqpId");
                FabEquipmentStatus fabEquipmentStatus = fabEquipmentStatusService.findByEqpId(eqpId);
                if ("ALARM".equals(fabEquipmentStatus.getEqpStatus())) {
                    String alarmStr = eqpId + ":" + (String) amsInfoLi.get("alarmName");
                    alarmInfo = alarmInfo + "            " + alarmStr;
                }
            }
        }
        if (!"警报发生：".equals(alarmInfo)) {
            rs.put("displayText", alarmInfo);
            rs.put("displayStatus", "ALARM");
        } else{
            rs.put("displayText", "设备正常稼动中");
            rs.put("displayStatus", "RUN");
        }

//        if (CollectionUtils.isEmpty(fabEquipmentStatusList)) {
//            FastJsonUtils.print(fabEquipmentStatusList);
//        }
//        FastJsonUtils.print(fabEquipmentStatusList);
        FastJsonUtils.print(rs);
    }

    @GetMapping("listPdtStatus")
    public void listPdtStatus(@RequestParam String lineNo, @RequestParam String curProject, HttpServletRequest request, HttpServletResponse response){//TODO 高协助首页分前后工程  curProject
        List<Map> maps = fabEquipmentStatusService.selectYield(lineNo,curProject);
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
