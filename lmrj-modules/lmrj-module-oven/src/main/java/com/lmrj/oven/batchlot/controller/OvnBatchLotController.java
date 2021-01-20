package com.lmrj.oven.batchlot.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.lmrj.cim.utils.OfficeUtils;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.oven.batchlot.entity.FabEquipmentOvenStatus;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.controller
 * @title: ovn_batch_lot控制器
 * @description: ovn_batch_lot控制器
 * @author: zhangweijiang
 * @date: 2019-06-09 08:49:15
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("oven/ovnbatchlot")
@ViewPrefix("modules/OvnBatchLot")
@RequiresPathPermission("OvnBatchLot")
@LogAspectj(title = "ovn_batch_lot")
public class OvnBatchLotController extends BaseCRUDController<OvnBatchLot> {

    @Autowired
    private IOvnBatchLotService ovnBatchLotService;

    @Autowired
    private IFabEquipmentService eqpService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @GetMapping("testMsg")
    public void sendMsg(String msg) {
        //rabbitTemplate.convertAndSend("S2C.T.CURE.COMMAND","测试发送");
        OvnBatchLot ovnBatchLot = new OvnBatchLot();
        ovnBatchLot.setId("11111");
        FastJsonUtils.print(ovnBatchLot);
    }

    /**
     * 在异步获取数据之后
     *
     * @param pagejson
     */
    @Override
    public void afterAjaxList(PageResponse<OvnBatchLot> pagejson) {
        List<OvnBatchLot> list = pagejson.getResults();
        for (OvnBatchLot ovnBatchLot : list) {
            if (ovnBatchLot.getOfficeId() != null) {
                Organization office = OfficeUtils.getOffice(ovnBatchLot.getOfficeId());
                if (office != null) {
                    ovnBatchLot.setOfficeName(office.getName());
                }
            }
        }
    }

    /**
     * 在异步获取数据之前
     *
     * @param entity
     */
    public OvnBatchLot afterGet(OvnBatchLot entity) {
        Organization office = OfficeUtils.getOffice(entity.getOfficeId());
        if (office != null) {
            entity.setOfficeName(office.getName());
        }
        if ((entity.getOtherTempsTitle().length() - 1) == entity.getOtherTempsTitle().lastIndexOf(",")) {
            String other = entity.getOtherTempsTitle().substring(0, entity.getOtherTempsTitle().length() - 1);
            entity.setOtherTempsTitle(other);
        }
        if ((entity.getOtherTempsTitle().length() - 1) == entity.getOtherTempsTitle().lastIndexOf(",")) {
            String other = entity.getOtherTempsTitle().substring(0, entity.getOtherTempsTitle().length() - 1);
            entity.setOtherTempsTitle(other);
        }
        String otherTempsTitle = entity.getOtherTempsTitle().replaceAll("当前值", "PV");
        otherTempsTitle = otherTempsTitle.replaceAll("现在值", "PV");
        otherTempsTitle = otherTempsTitle.replaceAll("温区", "");
        entity.setOtherTempsTitle(otherTempsTitle);
        return entity;
    }

    @GetMapping("listEqp")
    public void list(HttpServletRequest request, HttpServletResponse response) {
        List<FabEquipmentOvenStatus> fabEquipmentOvenStatusList = ovnBatchLotService.selectFabStatus("");
        if (CollectionUtils.isEmpty(fabEquipmentOvenStatusList)) {
            FastJsonUtils.print(fabEquipmentOvenStatusList);
        }
        List<Map> fabStatusParams = ovnBatchLotService.selectFabStatusParam(fabEquipmentOvenStatusList);
        fabEquipmentOvenStatusList.forEach(fabEquipmentOvenStatus -> {
            for (Map map : fabStatusParams) {
                if (fabEquipmentOvenStatus.getEqpId().equals(String.valueOf(map.get("EQPID")))) {
                    if ("ptNo".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))) {
                        fabEquipmentOvenStatus.setPtNo(String.valueOf(map.get("PARAMVALUE")));
                    }
                    if ("segNo".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))) {
                        fabEquipmentOvenStatus.setSegNo(String.valueOf(map.get("PARAMVALUE")));
                    }
                    if ("rtime".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))) {
                        fabEquipmentOvenStatus.setRtime(String.valueOf(map.get("PARAMVALUE")));
                    }
                    if ("temp".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))) {
                        fabEquipmentOvenStatus.setTemp(String.valueOf(map.get("PARAMVALUE")));
                    }
                }
            }
        });
        String content = JSON.toJSONString(fabEquipmentOvenStatusList);
        ServletUtils.printJson(response, content);
        //FastJsonUtils.print(fabEquipmentOvenStatusList);
    }

    @RequestMapping("tempEqpList")
    public Response tempEqpList(){
        Response rs = Response.ok();
        rs.put("eqpId", eqpService.getTempEqpList());
        return rs;
    }

    @GetMapping("chart")
    public void chart(HttpServletRequest request) {
        //方案模版
        List<Map> list = ovnBatchLotService.selectChart("");
        //排序
        list.forEach(map -> {
            if ("DOWN".equals(map.get("EQP_STATUS"))) {
                map.put("SORT_CODE", 1);
            } else if ("RUN".equals(map.get("EQP_STATUS"))) {
                map.put("SORT_CODE", 2);
            } else if ("ALARM".equals(map.get("EQP_STATUS"))) {
                map.put("SORT_CODE", 3);
            } else if ("IDLE".equals(map.get("EQP_STATUS"))) {
                map.put("SORT_CODE", 4);
            } else {
                map.put("SORT_CODE", 5);
            }
        });
        list.sort((o1, o2) -> {
            return (Integer) o1.get("SORT_CODE") - (Integer) o2.get("SORT_CODE");
        });
        FastJsonUtils.print(list);
    }

    @GetMapping("resolveAllTempFile")
    public String resolveAllTempFile(@RequestParam String eqpId, HttpServletRequest request) {
        //方案模版
        ovnBatchLotService.resolveAllTempFile(eqpId);
        return "resolve All OK";
    }

    @RequestMapping(value = "/tempbytime/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public void rptMsRecordByTime(@PathVariable String eqpId, @RequestParam String beginTime, @RequestParam String endTime,
                                  HttpServletRequest request, HttpServletResponse response) {
        List<Map> maps = ovnBatchLotService.findDetailBytime(beginTime, endTime, eqpId);
        String content = "";
        if (maps == null) {
            content = JSON.toJSONString(DateResponse.error("请缩短时间范围"));
        } else {
            OvnBatchLot ovnBatchLot = ovnBatchLotService.selectOne(new EntityWrapper<OvnBatchLot>().eq("eqp_id", eqpId));
            Response res = DateResponse.ok(maps);
            res.put("title", ovnBatchLot.getOtherTempsTitle());
            content = JSON.toJSONStringWithDateFormat(res, JSON.DEFFAULT_DATE_FORMAT);
        }
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/tempbytimeOther", method = {RequestMethod.GET, RequestMethod.POST})
    public void rptMsRecordByTimeOther(@RequestParam String lotNo,
                                       HttpServletRequest request, HttpServletResponse response) {
        List<Map> maps = ovnBatchLotService.findDetailBytimeOther("SIM-YGAZO1",lotNo);
        String content = "";
        if (maps == null) {
            content = JSON.toJSONString(DateResponse.error("请缩短时间范围"));
        } else {
            OvnBatchLot ovnBatchLot = ovnBatchLotService.selectOne(new EntityWrapper<OvnBatchLot>().eq("eqp_id", "SIM-YGAZO1"));
            Response res = DateResponse.ok(maps);
            res.put("title", ovnBatchLot.getOtherTempsTitle());
            content = JSON.toJSONStringWithDateFormat(res, JSON.DEFFAULT_DATE_FORMAT);
        }
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/tempCharbytime/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public Map tempMsRecordByTime(@PathVariable String eqpId, @RequestParam String beginTime, @RequestParam String endTime,
                                  HttpServletRequest request, HttpServletResponse response) {
        List<Map> os = ovnBatchLotService.findDetailBytime(beginTime, endTime, eqpId);
        OvnBatchLot ovnBatchLot = ovnBatchLotService.selectOne(new EntityWrapper<OvnBatchLot>().eq("eqp_id", eqpId));
        Map maps = new HashMap();
        String aaa[] = ovnBatchLot.getOtherTempsTitle().split(",");
        List list = new ArrayList();
        List list1 = new ArrayList();
        for (int j = 0; j < aaa.length / 4; j++) {
            for (int i = 0; i < os.size(); i++) {
                String f = aaa[j * 4];
                String str = (String) os.get(i).get("other_temps_value");
                String h[] = str.split(",");
                String str1 = String.valueOf(os.get(i).get("temp_pv"));
                String str2 = String.valueOf(os.get(i).get("temp_sp"));
                String str3 = String.valueOf(os.get(i).get("temp_min"));
                String str4 = String.valueOf(os.get(i).get("temp_max"));
                String sss[] = {str1, str2, str3, str4};
                String fff = f.replace("现在值", "");
                list1.add(sss);
                String b = h[j * 4];
                String c = h[j * 4 + 1];
                String d = h[j * 4 + 2];
                String e = h[j * 4 + 3];
                String s[] = {b, c, d, e};
                list.add(s);
                String ff = f.replace("当前值", "");
                maps.put(ff, list);
                maps.put("第一温区", list1);
            }

        }
        return maps;
    }
}








