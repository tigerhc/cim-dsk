package com.lmrj.dsk.dashboard.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.core.sys.service.IOrganizationService;
import com.lmrj.dsk.dashboard.entity.FbpbistolO;
import com.lmrj.dsk.dashboard.entity.FbpbistolOA;
import com.lmrj.dsk.dashboard.entity.FipinqtoolO;
import com.lmrj.dsk.dashboard.entity.FipinqtoolOA;
import com.lmrj.dsk.dashboard.entity.FipinqtoolOC;
import com.lmrj.dsk.dashboard.entity.FipinqtoolOD;
import com.lmrj.dsk.dashboard.entity.FipinqtoolOE;
import com.lmrj.dsk.dashboard.entity.ToolGroupDetail;
import com.lmrj.dsk.dashboard.entity.ToolGroupInfo;
import com.lmrj.dsk.dashboard.service.IDashboardService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.state.service.IRptEqpStateDayService;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.util.calendar.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service.impl
* @title: edc_dsk_log_operation服务实现
* @description: edc_dsk_log_operation服务实现
* @author: 张伟江
* @date: 2020-04-14 10:10:16
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("dashboardService")
public class DashboardServiceImpl  implements IDashboardService {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IFabEquipmentStatusService fabEquipmentStatusService;

    @Autowired
    private IEdcAmsRecordService edcAmsRecordService;

    @Autowired
    private IRptEqpStateDayService rptEqpStateDayService;

    @Override
    public ToolGroupInfo findOrgGroupInfo(String fab) {
        List<Organization> organizationList = organizationService.findChildren(fab);

        ToolGroupInfo toolGroupInfo = new ToolGroupInfo();
            toolGroupInfo.setRtn_code("0000000");
            toolGroupInfo.setRtn_mesg("success");
            toolGroupInfo.setTbl_cnt("13");
            toolGroupInfo.setTelephone("");
            toolGroupInfo.setTrx_id("FBPBISDAT");
            toolGroupInfo.setType_id("O");

        organizationList.forEach(organization -> {
            ToolGroupDetail toolGroupDetail = new ToolGroupDetail();
            toolGroupDetail.setData_cate("TOLG");
            toolGroupDetail.setData_desc(organization.getName());
            toolGroupDetail.setData_ext(organization.getName());
            toolGroupDetail.setData_id("1");
            toolGroupDetail.setData_item(fab);
            toolGroupDetail.setData_seq_id(organization.getId());
            toolGroupDetail.setExt_1(organization.getName());
            toolGroupInfo.getOary().add(toolGroupDetail);

        });
        return toolGroupInfo;
    }

    @Override
    public FbpbistolO findEqpStatusByFabId(String fab) {
        List<FabEquipmentStatus> fabEquipmentStatusList = fabEquipmentStatusService.selectEqpStatus("", "", fab);
        FbpbistolO fbpbistolO = new FbpbistolO();
        fbpbistolO.setDown_rate(0);
        fbpbistolO.setRtn_code("0000000");
        fbpbistolO.setRtn_mesg("SUCCESS");
        fbpbistolO.setTbl_cnt(0);
        fbpbistolO.setTbl_cnt_B(0);
        fbpbistolO.setTotal_tools(fabEquipmentStatusList.size());
        fbpbistolO.setTotal_tools_down(0);
        fbpbistolO.setTrx_id("FBPBISTOL");
        fbpbistolO.setType_id("O");
        List<FbpbistolOA> oary =Lists.newArrayList();
        fbpbistolO.setOary(oary);
        fabEquipmentStatusList.forEach(fabEquipmentStatus -> {
            FbpbistolOA fbpbistolOA = new FbpbistolOA();
            fbpbistolOA.setTool_id(fabEquipmentStatus.getEqpId());
            fbpbistolOA.setTool_stat(fabEquipmentStatus.getEqpStatus());
            fbpbistolOA.setTool_dsc(fabEquipmentStatus.getEqpId());
            oary.add(fbpbistolOA);
        });
        return fbpbistolO;
    }


    @Override
    public FipinqtoolO findAlarmByFab(String fab) {
        List<EdcAmsRecord> edcAmsRecordList = edcAmsRecordService.selectAmsRecord("", "", fab, "");
        FipinqtoolO fipinqtoolO = new FipinqtoolO();
        fipinqtoolO.setRtn_code("0000000");
        fipinqtoolO.setRtn_mesg("SUCCESS");
        fipinqtoolO.setTrx_id("FBPBISTOL");
        fipinqtoolO.setType_id("O");
        List<FipinqtoolOA> oary =Lists.newArrayList();
        fipinqtoolO.setOary(oary);
        //"tool_dsc":"102A-9#10#排版机",
        //        "tool_id":"102A-ABB01",
        //"tool_stat":"PM"
        edcAmsRecordList.forEach(edcAmsRecord -> {
            FipinqtoolOA fipinqtoolOA = new FipinqtoolOA();
            fipinqtoolOA.setAlarm_code("故障");
            String time = DateUtil.formatDate(edcAmsRecord.getStartDate(), "yyyy-MM-dd HH:mm:ss");
            fipinqtoolOA.setEvt_timestamp(time);
            fipinqtoolOA.setStart_timestamp(time);
            fipinqtoolOA.setFab_id_fk("DSK");
            fipinqtoolOA.setTool_dsc(edcAmsRecord.getEqpId());
            oary.add(fipinqtoolOA);
        });
        return fipinqtoolO;
    }

    @Override
    public FipinqtoolO findEqpStateByFab(String fab) {
        return findEqpStateByFab("", "", fab);
    }

    @Override
    public FipinqtoolO findEqpStateByStep(String step) {
        return findEqpStateByFab(step, "", "");
    }

    public FipinqtoolO findEqpStateByFab(String officeId, String lineNo, String fab) {
        List<Map> list = rptEqpStateDayService.selectNowGroupState(officeId, lineNo, fab, "step_code");
        FipinqtoolO fipinqtoolO = new FipinqtoolO();
        fipinqtoolO.setRtn_code("0000000");
        fipinqtoolO.setRtn_mesg("SUCCESS");
        fipinqtoolO.setTrx_id("FBPBISTOL");
        fipinqtoolO.setType_id("O");
        List<FipinqtoolOE> oaryE =Lists.newArrayList();
        fipinqtoolO.setOaryE(oaryE);
        list.forEach(map -> {
            int runTime = ((BigDecimal) map.get("runTime")).intValue();
            int idleTime = ((BigDecimal) map.get("idleTime")).intValue();
            int downTime = ((BigDecimal) map.get("downTime")).intValue();
            int pmTime = ((BigDecimal) map.get("pmTime")).intValue();
            int otherTime = ((BigDecimal) map.get("otherTime")).intValue();
            FipinqtoolOE fipinqtoolOE = new FipinqtoolOE();
            fipinqtoolOE.setToolg_id(map.get("groupName").toString());
            fipinqtoolOE.setRun_time(runTime);
            fipinqtoolOE.setTotal_time(idleTime+downTime+pmTime+otherTime);
            oaryE.add(fipinqtoolOE);
        });


        List<FipinqtoolOC> oaryC = Lists.newArrayList();
        fipinqtoolO.setOaryC(oaryC);

        int runTimeTotal = 0;
        int idleTimeTotal = 0;
        int downTimeTotal = 0;
        int pmTimeTotal = 0;
        int otherTimeTotal = 0;
        for (Map map : list) {

             runTimeTotal  += ((BigDecimal) map.get("runTime")).intValue();
             idleTimeTotal += ((BigDecimal) map.get("idleTime")).intValue();
            downTimeTotal += ((BigDecimal) map.get("downTime")).intValue();
            pmTimeTotal += ((BigDecimal) map.get("pmTime")).intValue();
            otherTimeTotal += ((BigDecimal) map.get("otherTime")).intValue();
        }
        int total = runTimeTotal+idleTimeTotal+downTimeTotal+pmTimeTotal+otherTimeTotal;

        FipinqtoolOC fipinqtoolOCRun = new FipinqtoolOC();
        fipinqtoolOCRun.setTime(runTimeTotal);
        fipinqtoolOCRun.setTool_stat("RUN");
        fipinqtoolOCRun.setTotalTime(total+0L);
        oaryC.add(fipinqtoolOCRun);

        FipinqtoolOC fipinqtoolOCDOWN = new FipinqtoolOC();
        fipinqtoolOCDOWN.setTime(downTimeTotal);
        fipinqtoolOCDOWN.setTool_stat("DOWN");
        fipinqtoolOCDOWN.setTotalTime(total+0L);
        oaryC.add(fipinqtoolOCDOWN);

        FipinqtoolOC fipinqtoolOCIDLE = new FipinqtoolOC();
        fipinqtoolOCIDLE.setTime(idleTimeTotal);
        fipinqtoolOCIDLE.setTool_stat("IDLE");
        fipinqtoolOCIDLE.setTotalTime(total+0L);
        oaryC.add(fipinqtoolOCIDLE);

        FipinqtoolOC fipinqtoolOCPM = new FipinqtoolOC();
        fipinqtoolOCPM.setTime(pmTimeTotal);
        fipinqtoolOCPM.setTool_stat("PM");
        fipinqtoolOCPM.setTotalTime(total+0L);
        oaryC.add(fipinqtoolOCPM);

        FipinqtoolOC fipinqtoolOCother = new FipinqtoolOC();
        fipinqtoolOCother.setTime(otherTimeTotal);
        fipinqtoolOCother.setTool_stat("OTHER");
        fipinqtoolOCother.setTotalTime(total+0L);
        oaryC.add(fipinqtoolOCother);
        return fipinqtoolO;
    }



    @Override
    public FipinqtoolO findEqpStateByPeriod(String fab) {
        List<Map> list = rptEqpStateDayService.selectEqpStateByPeriod("", "", fab);
        FipinqtoolO fipinqtoolO = new FipinqtoolO();
        fipinqtoolO.setRtn_code("0000000");
        fipinqtoolO.setRtn_mesg("SUCCESS");
        fipinqtoolO.setTrx_id("FIPINQTOL");
        fipinqtoolO.setType_id("O");
        List<FipinqtoolOD> oaryD =Lists.newArrayList();
        fipinqtoolO.setOaryD(oaryD);
        list.forEach(map -> {
            Map<String,Integer> toolStatusRate = Maps.newHashMap();
            int runTime = ((BigDecimal) map.get("runTime")).intValue();
            int idleTime = ((BigDecimal) map.get("idleTime")).intValue();
            int downTime = ((BigDecimal) map.get("downTime")).intValue();
            int pmTime = ((BigDecimal) map.get("pmTime")).intValue();
            int otherTime = ((BigDecimal) map.get("otherTime")).intValue();

            toolStatusRate.put("OTHER",otherTime);
            toolStatusRate.put("DOWN",downTime);
            toolStatusRate.put("IDLE",idleTime);
            toolStatusRate.put("RUN",runTime);
            toolStatusRate.put("PM",pmTime);
            FipinqtoolOD fipinqtoolOD = new FipinqtoolOD();
            fipinqtoolOD.setToolStatusRate(toolStatusRate);
            fipinqtoolOD.setCrop_mobility(runTime/1.0);
            fipinqtoolOD.setDate(map.get("periodDate").toString());
            oaryD.add(fipinqtoolOD);
        });
        return fipinqtoolO;
    }
}
