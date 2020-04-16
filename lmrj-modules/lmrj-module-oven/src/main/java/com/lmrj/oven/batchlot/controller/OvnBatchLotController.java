package com.lmrj.oven.batchlot.controller;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.oven.batchlot.entity.FabEquipmentOvenStatus;
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.cim.utils.OfficeUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    private AmqpTemplate rabbitTemplate;

    @GetMapping("testMsg")
    public void sendMsg(String msg){
        rabbitTemplate.convertAndSend("S2C.T.CURE.COMMAND","测试发送");
    }

    /**
     * 在异步获取数据之后
     *
     * @param pagejson
     */
    @Override
    public void afterAjaxList(PageResponse<OvnBatchLot> pagejson) {
        List<OvnBatchLot> list = pagejson.getResults();
        for(OvnBatchLot ovnBatchLot: list){
            if(ovnBatchLot.getOfficeId() != null){
                Organization office = OfficeUtils.getOffice(ovnBatchLot.getOfficeId());
                if(office != null){
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
        if(office != null){
            entity.setOfficeName(office.getName());
        }
        return entity;
    }

    @GetMapping("listEqp")
    public void list(HttpServletRequest request){
        List<FabEquipmentOvenStatus> fabEquipmentOvenStatusList=ovnBatchLotService.selectFabStatus("21100019");
        if (CollectionUtils.isEmpty(fabEquipmentOvenStatusList)) {
            FastJsonUtils.print(fabEquipmentOvenStatusList);
        }
        List<Map> fabStatusParams=ovnBatchLotService.selectFabStatusParam(fabEquipmentOvenStatusList);
        fabEquipmentOvenStatusList.forEach(fabEquipmentOvenStatus -> {
            for(Map map:fabStatusParams){
                if(fabEquipmentOvenStatus.getEqpId().equals(String.valueOf(map.get("EQPID")))){
                    if("ptNo".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))){
                        fabEquipmentOvenStatus.setPtNo(String.valueOf(map.get("PARAMVALUE")));
                    }
                    if("segNo".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))){
                        fabEquipmentOvenStatus.setSegNo(String.valueOf(map.get("PARAMVALUE")));
                    }
                    if("rtime".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))){
                        fabEquipmentOvenStatus.setRtime(String.valueOf(map.get("PARAMVALUE")));
                    }
                    if("temp".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))){
                        fabEquipmentOvenStatus.setTemp(String.valueOf(map.get("PARAMVALUE")));
                    }
                }
            }
        });
        FastJsonUtils.print(fabEquipmentOvenStatusList);
    }
    @GetMapping("chart")
    public void chart(HttpServletRequest request) {
        //方案模版
        List<Map> list=ovnBatchLotService.selectChart("21100019");
        //排序
        list.forEach(map -> {
            if("DOWN".equals(map.get("EQP_STATUS"))){
                map.put("SORT_CODE", 1);
            }else if("RUN".equals(map.get("EQP_STATUS"))){
                map.put("SORT_CODE", 2);
            }else if("ALARM".equals(map.get("EQP_STATUS"))){
                map.put("SORT_CODE", 3);
            }else if("IDLE".equals(map.get("EQP_STATUS"))){
                map.put("SORT_CODE", 4);
            }else{
                map.put("SORT_CODE", 5);
            }
        });
        list.sort((o1, o2) -> {
            return (Integer) o1.get("SORT_CODE")- (Integer)o2.get("SORT_CODE");
        });
        FastJsonUtils.print(list);
    }

    @GetMapping("resolveAllTempFile")
    public String resolveAllTempFile(@RequestParam String eqpId, HttpServletRequest request) {
        //方案模版
        ovnBatchLotService.resolveAllTempFile(eqpId);
        return "resolve All OK";
    }

}
