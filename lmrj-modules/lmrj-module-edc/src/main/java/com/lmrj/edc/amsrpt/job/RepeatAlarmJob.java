package com.lmrj.edc.amsrpt.job;

import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptRecordService;
import com.lmrj.fab.eqp.service.impl.EqpApiService;
import com.lmrj.fab.log.service.IFabLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zmq on 2017/4/25.
 */
@Slf4j
@Component
public class RepeatAlarmJob {

    @Autowired
    private IFabLogService  fabLogService;
    @Autowired
    private IEdcAmsRptRecordService edcAmsRptRecordService;
    @Autowired
    private IEdcAmsRecordService edcAmsRecordService;
    @Autowired
    private IEdcAmsRptDefineService edcAmsRptDefineService;
    @Autowired
    private IEmailSendService emailSendService; //发送邮件使用
    @Autowired
    private EqpApiService eqptService ; //设备操作OPI接口


    /*protected void run() {
        log.info("start 检查报警信息");
        Integer jobCycle = 5;
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -jobCycle);
        Date jobStartDate = calendar.getTime(); //repeat alarm 开始触发时间

        String eventId = StringUtil.randomTimeUUID("RPT");
        fabLogService.save(eventId, "1", "", "repeatAlarmJob", "开始进入定时任务检查报警信息", "");
        //// TODO: 2020/7/7 查询配置,添加条件
        List<EdcAmsRptDefine> amsRptDefineList = edcAmsRptDefineService.selectList(new EntityWrapper<>()); //

        for (EdcAmsRptDefine amsRptDefine : amsRptDefineList) {
            mdStationAlarm = mdStationAlarmService.get(mdStationAlarm.getId());

            Integer repeatAlarmCycle = mdStationAlarm.getCondCycleLength();
            Integer repeatAlarmAmt = mdStationAlarm.getCondAlarmAmt();


            calendar.add(Calendar.MINUTE, -repeatAlarmCycle);
            Date alarmStartDate = calendar.getTime(); //alarm 开始收集时间

            long repeatAlarmCycleMillisecond = repeatAlarmCycle * 60 * 1000;
            //查询基础alarm的定义
            MdAlarm mdAlarm = mdAlarmService.getAlarmByIdAndDtId(mdStationAlarm.getAlarmId(), mdStationAlarm.getDeviceTypeId());
            if (mdAlarm != null) {
                //查询所有符合条件的设备
                List<String> deviceCodeList = arAlarmRecordService.getDeviceCode(alarmStartDate, currentDate, mdAlarm.getAlarmAlid(), mdStationAlarm.getDeviceTypeId(), mdStationAlarm.getStationId());
                for (int i = 0; i < deviceCodeList.size(); i++) {
                    String deviceCode = deviceCodeList.get(i);
                    List<ArAlarmRecord> arAlarmRecordList = arAlarmRecordService.getArAlarmRecord(alarmStartDate, currentDate, mdAlarm.getAlarmAlid(), mdStationAlarm.getDeviceTypeId(), mdStationAlarm.getStationId(), deviceCode);
                    //所有符合条件的alarm没有超过repeat alarm设定次数,则直接退出
                    if (arAlarmRecordList.size() < repeatAlarmAmt) {
                        continue;
                    }
                    Date repeatJudgeDate = arAlarmRecordList.get(0).getAlarmDate();
                    int repeatJudgeIndex = 0;
                    for (int j = 0; j < arAlarmRecordList.size(); j++) {
                        ArAlarmRecord arAlarmRecord = arAlarmRecordList.get(j);
                        Date alarmDate = arAlarmRecord.getAlarmDate();
                        //在job周期 开始之前的,不用判断.
                        if (jobStartDate.compareTo(alarmDate) > 0) {
                            continue;
                        }
                        //已触发过的,不用判断.
                        if ("Y".equals(arAlarmRecord.getRepeatFlag())) {
                            continue;
                        }
                        while (repeatAlarmCycleMillisecond < (alarmDate.getTime() - repeatJudgeDate.getTime())) {
                            if (repeatJudgeIndex + 1 < arAlarmRecordList.size()) {
                                break;
                            }
                            repeatJudgeDate = arAlarmRecordList.get(repeatJudgeIndex++).getAlarmDate();
                        }
                        if (j - repeatJudgeIndex + 1 >= repeatAlarmAmt) {
                            //触发repeat alarm
                            fabLogService.save(eventId, "1", deviceCode, "repeatAlarmJob", deviceCode+":触发了repeatAlarm! 报警id:"+mdAlarm.getAlarmAlid(), "");
                            resolveRepeatAlarm(mdStationAlarm, arAlarmRecord, arAlarmRecordList, repeatJudgeIndex, j);
                            arAlarmRecordService.updateRepeatFlag(alarmStartDate, currentDate, deviceCode, mdAlarm.getAlarmAlid());
                            break;
                        }
                    }
                }
            }
        }

        fabLogService.save(eventId, "1", "", "repeatAlarmJob", "定时任务检查报警信息结束！", "");
        log.info("end 发送结束");
    }

    private void resolveRepeatAlarm(EdcAmsRptDefine amsRptDefine, EdcAmsRecord arAlarmRecord, List<EdcAmsRecord> arAlarmRecordList, int startIndex, int endIndex) {

        //保存repeatAlarm
        EdcAmsRptRecord  arAlarmRepeat = new EdcAmsRptRecord();
        // TODO: 2020/7/7 拼接对象
        arAlarmRepeat.setCreateDate(new Date());
        arAlarmRepeat.setRptAlarmId(amsRptDefine.getInactiveUserId());
        //arAlarmRepeat.setClientId(arAlarmRecord.getClientId());
        //arAlarmRepeat.setClientCode(arAlarmRecord.getClientCode());
        //arAlarmRepeat.setClientName(arAlarmRecord.getClientName());
        //arAlarmRepeat.setStationId(arAlarmRecord.getStationId());
        //arAlarmRepeat.setStationCode(arAlarmRecord.getStationCode());
        //arAlarmRepeat.setStationName(arAlarmRecord.getStationName());
        //arAlarmRepeat.setDeviceTypeId(arAlarmRecord.getDeviceTypeId());
        //arAlarmRepeat.setDeviceTypeCode(arAlarmRecord.getDeviceTypeCode());
        //arAlarmRepeat.setDeviceTypeName(arAlarmRecord.getDeviceTypeName());
        //arAlarmRepeat.setDeviceId(arAlarmRecord.getDeviceId());
        //arAlarmRepeat.setDeviceCode(arAlarmRecord.getDeviceCode());
        //arAlarmRepeat.setDevcieName(arAlarmRecord.getDevcieName());
        //arAlarmRepeat.setAlarmId(arAlarmRecord.getAlarmId()); //todo
        //arAlarmRepeat.setAlarmCode(arAlarmRecord.getAlarmCode());
        //arAlarmRepeat.setAlarmName(arAlarmRecord.getAlarmName());
        //arAlarmRepeat.setVerNo(0);
        //arAlarmRepeat.setBizStatus("N");

        //保存buyoff
        //List<MdStationAlarmBuyoff> mdStationAlarmBuyoffs = mdStationAlarm.getMdStationAlarmBuyoffList();
        //List<ArAlarmRepeatBuyoff> buyoffs = new ArrayList<>();
        //if (mdStationAlarmBuyoffs.size() > 0) {
        //    for (MdStationAlarmBuyoff buyoff : mdStationAlarmBuyoffs) {
        //        ArAlarmRepeatBuyoff repeatBuyoff = new ArAlarmRepeatBuyoff();
        //        repeatBuyoff.setBuyoffName(buyoff.getBuyoffName());
        //        repeatBuyoff.setId("");
        //        if ("Y".equals(buyoff.getValueFixedFlag())) {
        //            repeatBuyoff.setBuyoffVal(buyoff.getValPass());
        //        } else {
        //            repeatBuyoff.setBuyoffVal("[" + buyoff.getMinVal() + "," + buyoff.getMaxVal() + "]");
        //        }
        //        buyoffs.add(repeatBuyoff);
        //    }
        //    arAlarmRepeat.setArAlarmRepeatBuyoffList(buyoffs);
        //}

        // TODO: 2020/7/7
        //保存dtl
        List<EdcAmsRptRecordDtl> dtls = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            EdcAmsRecord alarmRecord = arAlarmRecordList.get(i);
            EdcAmsRptRecordDtl arAlarmRepeatDtl = new EdcAmsRptRecordDtl();
            arAlarmRepeatDtl.setAlarmRecordId(alarmRecord.getId());
            arAlarmRepeatDtl.setId("");
            arAlarmRepeatDtl.setVerNo(0);
            dtls.add(arAlarmRepeatDtl);
        }
        arAlarmRepeat.setArAlarmRepeatDtlList(dtls);
        arAlarmRepeatService.save(arAlarmRepeat);
        //处理方式
        List<MdStationAlarmDeal> mdStationAlarmDeals = mdStationAlarm.getMdStationAlarmDealList();
        if (mdStationAlarmDeals.size() > 0) {
            for (MdStationAlarmDeal deal : mdStationAlarmDeals) {
                //邮件
                if ("EMAIL".equals(deal.getActionCode())) {
                    List<MdStationAlarmMail> mdStationAlarmMails = mdStationAlarm.getMdStationAlarmMailList();
                    List<String> list = new ArrayList<>();
                    for (MdStationAlarmMail mails : mdStationAlarmMails) {
                        String mail = mails.getUserEmail();
                        list.add(mail);
                    }
                    MdEqpFunctionFlag mdEqpFunctionFlag = MdEqpFunctionService.checkSwitchFunction(arAlarmRepeat.getDeviceCode(), WebServiceFunction.WS_SEND_COMMON_MAIL);
                    if ("Y".equalsIgnoreCase(mdEqpFunctionFlag.getFlag())) {
                        //mailService.sendCommonMail(JsonMapper.toJsonString(list), "RepeatAlarm信息", "机台:" +
                        //        arAlarmRepeat.getDeviceCode() + ",触发RepeatAlarm,报警信息如下：" + "\n" + "报警id:" + arAlarmRepeat.getAlarmId() + ",报警描述:"
                        //        + arAlarmRepeat.getAlarmName() + "\n" + "请速联系相关操作人员消警！");

                        //emailSendService.send(emails, "PARAM_CHANGE",datas);
                    }
                }
                if ("HOLD".equals(deal.getActionCode())) {
                    //hold机
                    eqptService.lockEqptByType("1", arAlarmRepeat.getDeviceCode(), "ALARM_LOCK");
                }
            }
        }
        //查询是否有已存在未处理的repeat
//        List<ArAlarmRepeat> repeats = arAlarmRepeatService.searchRepeatAlarm(arAlarmRepeat.getDeviceTypeId(), arAlarmRepeat.getDeviceCode(), arAlarmRepeat.getAlarmId());
//        if (repeats.size() <= 0) {

    }*/
}
