package com.lmrj.fab.eqp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * equipment web service
 * time:2016-8-5
 */
@Slf4j
@Service
public class EqpApiService implements Serializable {
    private static final long serialVersionUID = -3450064362986273896L;

    public String lockEqpt(String userId, String eqptId, String type) {
//        log.info("=====lockEqptByType=====" + "用户:" + userId + ";设备号:" + eqptId + ";类型" + type);
//
//        String eventId = CIMIdGen.nextId("41");
//        ImMesCallService imMesCallService = SpringBeanFactoryUtil.getBean(ImMesCallService.class);
//        ImMesCall imMesCall = new ImMesCall();
//        imMesCall.setEventId(eventId);
//        imMesCall.setDeviceCode(eqptId);
//        imMesCall.setEventName("lockEqptByType");
//        imMesCall.setEventDesc("用户：" + userId + "; 设备号：" + eqptId + "; 类型：" + type + " 接收到请求，开始处理");
//        imMesCall.setLotId(null);
//        imMesCall.setEventStatus("Y");
//        User user = new User();
//        user.setId(userId);
//        imMesCall.setCreateBy(user);
//        imMesCall.setUpdateBy(user);
//        imMesCallService.save(imMesCall);
//        MdEqpFunctionFlag mdEqpFunctionFlag = MdEqpFunctionService.checkSwitchFunction(eqptId, WebServiceFunction.WS_LOCK_EQPT_BY_TYPE);
//        if ("N".equalsIgnoreCase(mdEqpFunctionFlag.getFlag())) {
//
//            return mdEqpFunctionFlag.getDefaultValue();
//        }
////        log.info("=====方法lockEqptByType=====userId:"+ userId + ";eqptId:"+eqptId+ ";type:"+type);
//
//        String messages = checkEqptParam(userId, eqptId, type);
//        if (!"PASS".equals(messages)) {
//            return messages;
//        }
//
//        //changeEqptStatus_lockFlag(eqptId, "Y");
//
//        if ("SPC_LOCK".equals(type)) {
//            mdEqpFunctionFlag = MdEqpFunctionService.checkSwitchFunction(eqptId, WebServiceFunction.WS_LOCK_EQPT_BY_TYPE + "_" + type);
//            if ("N".equalsIgnoreCase(mdEqpFunctionFlag.getFlag())) {
//                imMesCall.setEventDesc("处理结束，处理结果为：" + "PASS" + " ,无需锁机");
//                imMesCall.setId(null);
//                imMesCallService.save(imMesCall);
//                return "PASS";
//            }
//        }
//
//        if ("MES_PM_LOCK".equals(type) || "MES_TOOLING_LOCK".equals(type) || "MES_MATERIAL_LOCK".equals(type)) {
//            if ("SPC_LOCK".equals(type)) {
//                imMesCall.setEventDesc("处理结束，处理结果为：" + "PASS" + " ,CIM与SPC相互交互，SPC报警自动停机，未Buyoff完成，机台不能继续作业");
//            } else {
//                imMesCall.setEventDesc("处理结束，处理结果为：PASS");
//            }
//
//            imMesCall.setId(null);
//            imMesCallService.save(imMesCall);
//            return "PASS";
//        }
//        MdDeviceInfoLockService mdDeviceInfoLockService = SpringBeanFactoryUtil.getBean(MdDeviceInfoLockService.class);
//        String message = mdDeviceInfoLockService.lockEqpt(eqptId, type);
//        imMesCall.setEventDesc("处理结束，处理结果为：" + message);
//        imMesCall.setId(null);
//        imMesCallService.save(imMesCall);
        //锁机必成功
        return "PASS";
    }

    /**
     * mes 开机
     *
     * @param key
     * @param userId
     * @param eqptId
     * @param type
     * @return
     */
    public String startEqp(String key, String userId, String eqptId, String type) {
        //log.info("=====startEqpt=====令牌：" + key + "; 用户：" + userId + "; 设备号：" + eqptId + "; 类型：" + type);
        ////日志保存
        //String eventId = CIMIdGen.nextId("41");
        //ImMesCallService imMesCallService = SpringBeanFactoryUtil.getBean(ImMesCallService.class);
        //imMesCallService.save(eventId, userId, eqptId, "startEqp", "令牌:" + key + ";用户:" + userId + ";设备号:" + eqptId + ";类型" + type + " 接收到请求，开始处理", "1");
        //
        ////令牌校验
        //EncryptFlag encryptFlag = BaseEncrypt.CheckCorrectKey(eqptId, key, "startEqpt");
        //if ("N".equalsIgnoreCase(encryptFlag.getFlag())) {
        //    return "令牌校验错误!";
        //}
        //
        ////功能开关
        //MdEqpFunctionFlag mdEqpFunctionFlag = MdEqpFunctionService.checkSwitchFunction(eqptId, WebServiceFunction.WS_START_EQPT);
        //if (!"Y".equalsIgnoreCase(mdEqpFunctionFlag.getFlag())) {
        //    return mdEqpFunctionFlag.getDefaultValue();
        //}
        //
        //checkEqptParam(userId, eqptId, type);
        //
        ////普通烤箱开机指令
        //MdDeviceAreaEqpService mdDeviceAreaEqpService = SpringBeanFactoryUtil.getBean(MdDeviceAreaEqpService.class);
        //MdDeviceAreaEqp deviceAreaEqp = new MdDeviceAreaEqp();
        //deviceAreaEqp.setEqpCode(eqptId);
        //deviceAreaEqp.setEqpType("1");
        //List<MdDeviceAreaEqp> eqptLists = mdDeviceAreaEqpService.findList(deviceAreaEqp);
        //if (eqptLists != null && eqptLists.size() > 0) {
        //    //电镀烤箱测试MQ方式，添加一厂后线,IC后线,二厂后线,二厂测试,一厂测试
        //    //烤箱MQ方式，一厂前线,IC老厂，新厂前线,二厂前线
        //    deviceAreaEqp = eqptLists.get(0);
        //    Map<String, String> resultMap;
        //    if("11888c6332c24a75a0a79a19492ed31a".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "3a413db5b36e4e8599493aaa43bc495d".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "3a413db5b36e4e8599493aaa43bc496d".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "c445f86feee8431d9779b9ab7eb1a102".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "61fdad81eeec4d4b88f327705f7e669a".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "c6aace8318af4d80bad8d97ae8c79f0b".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "8a3b8f69405d4d5c87147c04fcadcc3e".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "d99f6ddf7c234b2a8e6ecce87212d70b".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "7bf4315d990143f2a629831dc0972c7c".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "a4fd61d93d73480681120d245d14bb1e".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "d18d3e329bb5487396dc93d0fdcbffae".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "b7ce5e7311ae498685f199604b17d742".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "8f627fe36c1b4e9ba46d9b40a6181377".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "1aff788239ab418caa2918133e484d3e".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "847544220d19445fbbb029d4962ac9f41".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "727a83907a7842299c25d27079cbe963".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "847544220d19445fbbb029d4962ac9f4".equals(deviceAreaEqp.getAreaId().getId()) ||
        //            "52e9ae03f2ec4f9583d15a39ad11839a".equals(deviceAreaEqp.getAreaId().getId())||
        //            "4e3cde65b0a047b5a02b7302e77552c1".equals(deviceAreaEqp.getAreaId().getId())||
        //            "da85b4e2bdd040b58823b105d155ba25".equals(deviceAreaEqp.getAreaId().getId())||
        //            "4d2ad1e72f4f48b28cc09c23340505c7".equals(deviceAreaEqp.getAreaId().getId())||
        //            "a4fd61d93d73480681120d245d14cc1e".equals(deviceAreaEqp.getAreaId().getId())){
        //        resultMap = mdDeviceAreaEqpService.startEQP4CureByMQ(eqptId, userId);
        //    }else{
        //        resultMap = mdDeviceAreaEqpService.startEqp(eqptId, userId);
        //    }
        //    if ("Y".equals(resultMap.get("flag"))) {
        //        return "PASS";
        //    } else {
        //        return resultMap.get("msg");
        //    }
        //}
        //
        //MdDeviceInfoLockService mdDeviceInfoLockService = SpringBeanFactoryUtil.getBean(MdDeviceInfoLockService.class);
        //String message = mdDeviceInfoLockService.startEqpt(eqptId, type);
        //imMesCallService.save(eventId, userId, eqptId, "startEqp", "处理结束，处理结果为:" + message, "1");
        //return message;
        return "";
    }

    public String releaseEqpt(String userId, String eqptId, String type) {
//        log.info("=====releaseEqptByType=====" + "用户:" + userId + ";设备号:" + eqptId + ";类型" + type);
//        String eventId = CIMIdGen.nextId("41");
//        ImMesCallService imMesCallService = SpringBeanFactoryUtil.getBean(ImMesCallService.class);
//        ImMesCall imMesCall = new ImMesCall();
//        imMesCall.setEventId(eventId);
//        imMesCall.setDeviceCode(eqptId);
//        imMesCall.setEventName("releaseEqptByType");
//        imMesCall.setEventDesc("用户：" + userId + "; 设备号：" + eqptId + "; 类型：" + type + ",接收到请求,开始处理");
//        imMesCall.setLotId(null);
//        imMesCall.setEventStatus("Y");
//        User user = new User();
//        user.setId(userId);
//        imMesCall.setCreateBy(user);
//        imMesCall.setUpdateBy(user);
//        imMesCallService.save(imMesCall);
//        MdEqpFunctionFlag mdEqpFunctionFlag = MdEqpFunctionService.checkSwitchFunction(eqptId, WebServiceFunction.WS_RELEASE_EQPT_BY_TYPE);
//        if ("N".equalsIgnoreCase(mdEqpFunctionFlag.getFlag())) {
//            return mdEqpFunctionFlag.getDefaultValue();
//        }
////        log.info("=====方法releaseEqptByType=====userId:"+ userId + ";eqptId:"+eqptId+ ";type:"+type);
//
//        String message = checkEqptParam(userId, eqptId, type);
//        if (!"PASS".equals(message)) {
//            imMesCall.setEventDesc("处理结束，处理结果为: " + message);
//            imMesCall.setId(null);
//            imMesCallService.save(imMesCall);
//            return message;
//        }
//        if ("MES_PM_LOCK".equals(type) || "MES_TOOLING_LOCK".equals(type) || "MES_MATERIAL_LOCK".equals(type)) {
//            imMesCall.setEventDesc("处理结束，处理结果为: PASS");
//            imMesCall.setId(null);
//            imMesCallService.save(imMesCall);
//            return "PASS";
//        }
//        MdDeviceInfoLockService mdDeviceInfoLockService = SpringBeanFactoryUtil.getBean(MdDeviceInfoLockService.class);
//        message = mdDeviceInfoLockService.releaseEqpt(eqptId, type);
//        imMesCall.setEventDesc("处理结束，处理结果为：" + "PASS,真实值:" + message);
//        imMesCall.setId(null);
//        imMesCallService.save(imMesCall);
        //解锁必成功
        return "PASS";
    }

    private String checkEqpParam(String userId, String eqptId) {
        log.info("=====checkEqptParam=====用户:" + userId + ";设备号:" + eqptId);
        if (userId == null || userId.isEmpty()) {
            return "userId不能为空";
        }
        if (eqptId == null || eqptId.isEmpty()) {
            return "eqpId不能为空";
        }
        return "PASS";
    }

    private String checkEqptParam(String userId, String eqptId, String type) {
        log.info("=====checkEqptParam=====用户:" + userId + ";设备号:" + eqptId + ";类型:" + type);
        if (userId == null || userId.isEmpty()) {
            return "userId不能为空";
        }
        if (eqptId == null || eqptId.isEmpty()) {
            return "eqpId不能为空";
        }
        if (type == null || type.isEmpty()) {
            return "锁机类型不能为空";
        }
        return "PASS";
    }

    public String findEqptStatus(String userId, String eqptId) {
        log.info("=====findEqptStatus=====" + "用户:" + userId + ";设备号:" + eqptId);
        String message = "PASS";
//        try {
//            MdDeviceInfoExtDao mdDeviceInfoExtDao = SpringBeanFactoryUtil.getBean(MdDeviceInfoExtDao.class);
//            MdDeviceInfoExt mdDeviceInfoExt = new MdDeviceInfoExt();
//            mdDeviceInfoExt.setDeviceRowId(eqptId);
//            List<MdDeviceInfoExt> mdDeviceInfoExts = mdDeviceInfoExtDao.findList(mdDeviceInfoExt);
//            String result;
//            if (mdDeviceInfoExts == null || mdDeviceInfoExts.size() == 0) {
//                //ImMesCallService imMesCallService = SpringBeanFactoryUtil.getBean(ImMesCallService.class);
//                //imMesCallService.save(eventId, userId, eqptId, "CheckPmTime", "mdDeviceInfoExt没有记录", "");
//                log.info("=====findEqptStatus=====mdDeviceInfoExt没有记录;设备号:" + eqptId);
//                return eqptId + "mdDeviceInfoExt没有记录";
//            }
//            mdDeviceInfoExt = mdDeviceInfoExts.get(0);
//            if ("Y".equals(mdDeviceInfoExt.getLockFlag())) {
//                MdDeviceInfoLock mdDeviceInfoLock = new MdDeviceInfoLock();
//                mdDeviceInfoLock.setDeviceRowid(mdDeviceInfoExt.getDeviceRowId());
//                MdDeviceInfoLockDao mdDeviceInfoLockDao = SpringBeanFactoryUtil.getBean(MdDeviceInfoLockDao.class);
//                List<MdDeviceInfoLock> mdDeviceInfoLocks = mdDeviceInfoLockDao.findLockOthers(mdDeviceInfoLock);
//                String lockTypes = "";
//                for (int i = 0; i < mdDeviceInfoLocks.size(); i++) {
//                    lockTypes += mdDeviceInfoLocks.get(i).getLockType() + ";";
//                }
//                mdDeviceInfoExt.setRemarks(lockTypes);
//            }
//            //如果设备未锁机,则判断idle time时间
////            if (!"Y".equals(mdDeviceInfoExt.getLockFlag())) {
////                Map<String, String> idleTimeResult = edcTransferChecklistService.checkIdleTime(userId, eqptId);
////                //默认值Y:不需要锁机，　N:需要校验idle时间
////                if ("Y".equals(idleTimeResult.get("flag"))) {
////                    mdDeviceInfoExt.setLockFlag("N");
////                } else {
////                    Map<String, String> elargolTimeoutResult = checkDBElargolTimeout(eqptId);
////                    if ("Y".equals(elargolTimeoutResult.get("flag"))) {
////                        mdDeviceInfoExt.setLockFlag("Y");
////                        mdDeviceInfoExt.setRemarks("IDLE_TIME_CHECK_LOCK");
////                    }
////                }
////            }
//
//            //如果设备未锁机,则判断PM有效期
//            //if ("Y".equals(mdDeviceInfoExt.getLockFlag())) {
//            //    ImMesCallService imMesCallService = SpringBeanFactoryUtil.getBean(ImMesCallService.class);
//            //    imMesCallService.save(eventId, userId, eqptId, "CheckPMExpire", "已锁机，不进行PMCHECK", "");
//            //}
////            else {
////                //判断PM有效期
////                //imMesCallService.save(eventId, userId, eqptId, "CheckPMExpire", "开始检查PM时间是否到期", "");
////                String pmResult = this.checkPmTime(eqptId);
////                JSONObject obj = new JSONObject(pmResult);
////                String pnFlag = obj.getString("FLAG");
////                if ("Y".equals(pnFlag)) {
////                    //imMesCallService.save(eventId, userId, eqptId, "CheckPMExpire", "检查结束:" + pnFlag, "");
////                } else {
////                    //this.lockEqptByType(userId,eqptId,"PMCHECK");
////                    mdDeviceInfoExt.setLockFlag("Y");
////                    mdDeviceInfoExt.setRemarks("PM_EXPIRE_LOCK");
////                    String pmType = obj.getString("PMTYPE");
////                    String nextPmTime = obj.getString("NEXTPMTIME");
////                    imMesCallService.save(eventId, userId, eqptId, "CheckPMExpire", "检查结束:" + pnFlag + ",pmType:" + pmType + "nextPmTime:" + nextPmTime, "");
////                }
////            }
//            MesCurrentDeviceStatusDao mesCurrentDeviceStatusDao = SpringBeanFactoryUtil.getBean(MesCurrentDeviceStatusDao.class);
//            MesCurrentDeviceStatus mesCurrentDeviceStatuse = mesCurrentDeviceStatusDao.findStatusBydeviceCode(eqptId);
//            if (mesCurrentDeviceStatuse == null){
//                mdDeviceInfoExt.setDeviceStatus("IDLE");
//            }else {
//                mdDeviceInfoExt.setDeviceStatus(mesCurrentDeviceStatuse.getMesStatus());
//            }
//            result = JsonMapper.toJsonString(mdDeviceInfoExt);
//            log.info("findEqptStatus{}:{}",eqptId,result);
//            //imMesCallService.save(eventId, userId, eqptId, "findEqptStatus", result, mdDeviceInfoExt.getDeviceStatus());
//            return result;
//        } catch (Exception e) {
//            log.info("findEqptStatus{}:异常",eqptId, e);
//            //e.printStackTrace();
//            return "服务端异常";
//        }
        return "服务端异常";

    }

    /**
     * 改变机台锁机状态
     *
     * @param eqptId
     * @param lockFlag
     * @return
     */
    protected int changeEqptStatus_lockFlag(String eqptId, String lockFlag) {
        //MdDeviceInfoExtDao mdDeviceInfoExtDao = SpringBeanFactoryUtil.getBean(MdDeviceInfoExtDao.class);
        //MdDeviceInfoExt mdDeviceInfoExt = new MdDeviceInfoExt();
        //mdDeviceInfoExt.setLockFlag(lockFlag);
        //mdDeviceInfoExt.setDeviceRowId(eqptId);
        //return mdDeviceInfoExtDao.updatePart(mdDeviceInfoExt);
        return 1;
    }

    //public String checkMatTime(String key, String eqptId, String matType)
    //
    //public String startCheck(String key, String eqptId, String matType, String pressNos)
    //
    //public String findEqptByClient(String clientCode)
    //
    //public String findMesAoLot(String deviceCode, String stripId, String funcType)
    //public String reportEqptStatus(String deviceCode ,String eqptStatus)

}
