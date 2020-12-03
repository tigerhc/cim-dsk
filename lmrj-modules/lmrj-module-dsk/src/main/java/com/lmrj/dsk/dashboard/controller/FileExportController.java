package com.lmrj.dsk.dashboard.controller;

import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.dsk.dashboard.entity.ToolGroupInfo;
import com.lmrj.dsk.dashboard.service.IDashboardService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.calendar.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @author: zhangweijiang
 * @date: 2020-02-15 02:39:16
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("fileexport")
public class FileExportController {
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    @Autowired
    IDashboardService dashboardServiceImpl;
    @Autowired
    IEdcDskLogProductionService iEdcDskLogProductionService;
    @Autowired
    IEdcDskLogOperationService iEdcDskLogOperationService;
    @Autowired
    IEdcDskLogRecipeService iEdcDskLogRecipeService;
    @RequestMapping("/q1-bak")
    public void getToolGroupInfo(String fab_id_fk ,HttpServletRequest request, HttpServletResponse response) {
        ToolGroupInfo toolGroupInfo = dashboardServiceImpl.findOrgGroupInfo(fab_id_fk);
        ServletUtils.printJson(response, toolGroupInfo);
    }
    @RequestMapping("/fileexport")
    public void fileExport(String eqpId, String date,HttpServletRequest request, HttpServletResponse response) {
        Date time = DateUtil.parseDate(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        Date startTime=cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR,1);
        Date endTime=cal.getTime();
        //首先查询当天批次
        List<MesLotTrack> lotList = iMesLotTrackService.selectList(new EntityWrapper<MesLotTrack>().eq("eqp_id", eqpId).between("start_time",startTime,endTime).orderBy("start_time"));
        //打印产量日志
        iEdcDskLogProductionService.exportTrmProductionFile(lotList,"PRODUCTION");
        //打印时间日志
        iEdcDskLogOperationService.exportTrmOperationFile(eqpId,startTime,endTime);
        //打印配方日志
        iEdcDskLogRecipeService.exportRecipeFile(eqpId,startTime,endTime);
    }
}
