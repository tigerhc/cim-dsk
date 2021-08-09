package com.lmrj.oven.batchlot.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.controller
 * @title: ovn_batch_lot_param控制器
 * @description: ovn_batch_lot_param控制器
 * @author: zhangweijiang
 * @date: 2019-06-09 08:55:13
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("oven/ovnbatchlotparam")
@ViewPrefix("modules/OvnBatchLotParam")
@RequiresPathPermission("OvnBatchLotParam")
@LogAspectj(title = "ovn_batch_lot_param")
@Slf4j
public class OvnBatchLotParamController extends BaseCRUDController<OvnBatchLotParam> {
    @Autowired
    IFabLogService fabLogService;
    @Autowired
    IOvnBatchLotParamService ovnBatchLotParamService;

    @RequestMapping(value = "/findOvenTemp/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String findOvenTemp(Model model, @PathVariable String eqpId, @RequestParam String startTime,
                               @RequestParam String opId,
                               HttpServletRequest request, HttpServletResponse response) {
        log.info("findOvenTemp :  {}, {}", opId, eqpId);
        String eventDesc = "{\"eqpId\":\"" + eqpId + "\",\"opId\":\"" + opId + "\",\"startTime\":\"" + startTime + "\"}";//日志记录参数
        try {
            fabLogService.info(eqpId, "Param14", "MesLotTrackController.findOvenTemp", eventDesc, "", "wangdong");//日志记录参数
            //String eqpId ="SIM-DM1";
            if ("".equals(opId) || opId == null) {
                return "opId Cannot be empty";
            }
            if (eqpId.equals("DM-OVEN1")) {
                eqpId = "DM-OVEN1";
            } else if (eqpId.equals("DM-OVEN2")) {
                eqpId = "DM-OVEN2";
            } else if (eqpId.equals("SIM-OVEN1")) {
                eqpId = "SIM-OVEN1";
            } else if (eqpId.equals("SIM-OVEN2")) {
                eqpId = "SIM-OVEN2";
            } else if (eqpId.equals("SMA-OVEN1")) {
                eqpId = "SMA-OVEN1";
            } else {
                log.error("设备名称错误！   " + eqpId);
                return "eqpId error!:" + eqpId;
            }
            Date endTime = DateUtil.parseDate(startTime,"yyyy-MM-dd HH:mm:ss");
            OvnBatchLotParam ovnBatchLotParamStart =  ovnBatchLotParamService.selectDataBytemp(eqpId,endTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(ovnBatchLotParamStart.getCreateDate());
            cal.add(Calendar.HOUR_OF_DAY,-1);
            if(cal.getTimeInMillis()>new Date().getTime()){
                return "time param error!";
            }
            OvnBatchLotParam ovnBatchLotParam =  ovnBatchLotParamService.selectDataBytime(eqpId,cal.getTime());
            if(ovnBatchLotParam ==null || ovnBatchLotParam.getBatchId()==null){
                return "data not found";
            }
            String time = DateUtil.formatTime(ovnBatchLotParam.getCreateDate());
            String temp = "";
            if(eqpId.equals("DM-OVEN1")){
                temp = ovnBatchLotParam.getTempPv();
            }else if(eqpId.equals("DM-OVEN2")){
                temp = ovnBatchLotParam.getTempPv();
            }
            return time+","+temp;
        } catch (Exception e) {
            fabLogService.info(eqpId, "Error14", "MesLotTrackController.findOvenTemp", "有异常", eqpId, "wangdong");//日志记录
            return e.getMessage();
        }
    }

}
