package com.lmrj.rms.mes.service;

import com.google.common.collect.Maps;
import com.lmrj.util.mapper.JsonUtil;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.core.entity.MesResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.recipe.service.impl
* @title: rms_recipe服务实现
* @description: rms_recipe服务实现
* @author: zhangweijiang
* @date: 2019-06-15 01:58:00
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("mesTrackService")
public class MesTrackService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IRmsRecipeBodyService rmsRecipeBodyService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public String beforeTrackIn(String eqpId, String recipeName, String lotId, String userId, String params) {
        // TODO: 2019/6/23 打印日志
        // TODO: 2019/6/23 校验开关
        // TODO: 2019/6/23 检查机台状态,是否可以过账(断线或运行过程中无法过账)
        // TODO: 2019/6/23 检查recipe是否存在并审批
        // TODO: 2019/6/23 检查机台idle时间,是否需要保养或清洗
        // TODO: 2019/6/23 锡球更换检测(备选)
        // TODO: 2019/6/23 料饼更换检测(备选)

        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "BEFORE_TRACK_IN");
        if(recipeName.contains("-101-")){
            recipeName = "101";
        }else if(recipeName.contains("-202-")){
            recipeName = "202";
        }else if(recipeName.contains("-202-")){
            recipeName = "202";
        }else if(recipeName.contains("-601-")){
            recipeName = "601";
        }else if(recipeName.contains("-602-")){
            recipeName = "602";
        }else if(recipeName.contains("-401-")){
            recipeName = "401";
        }else if(recipeName.contains("-605-")){
            recipeName = "605";
        }else{
            MesResult mesResult = new MesResult();
            mesResult.setFlag("N");
            mesResult.setMsg(recipeName+"银浆号找不到对应的程序名");
            return JsonUtil.toJsonString(mesResult);
        }
        map.put("RECIPE_NAME", recipeName);
        map.put("LOT_ID", lotId);
        map.put("EQP_ID", eqpId);
        String bc = "SH_FOL_OV1";
        if(eqpId.indexOf("OVEN")>=0 || eqpId.indexOf("CM-EC-")>=0){
            bc = "SH_FOL_OV1";
        }else if(eqpId.indexOf("-PC")>=0){
            bc = "BC3";
        }
        logger.info("start");
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
        logger.info("end");
        byte[] message = (byte[]) test;
        String msg = null;
        try {
            msg = new String(message, "UTF-8");
            String msg2 = new String(message, "GBK");
            logger.info("encode UTF-8: {}", msg);
            logger.info("encode GBK: {}", msg2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // TODO: 2019/6/23 打印返回结果
        MesResult result = JsonUtil.from(msg, MesResult.class);
        if("Y".equals(result.flag)){
            //Map<String, String> content = Maps.newHashMap();
            //content.put("RECIPE_NAME", recipeCode);
            //result.setContent(content);
            //简单处理
            result.setContent(recipeName);
        }

        // TODO: 2019/6/23 判断结果,成功则更新设备状态表
        return JsonUtil.toJsonString(result);
    }

    public String afterTrackIn(String eqpId, String recipeName, String lotId, String userId, String params) {
        // TODO: 2019/6/23 打印日志
        // TODO: 2019/6/23 校验开关
        // TODO: 2019/6/23 检查机台状态,是否可以过账(断线或运行过程中无法过账)
        // TODO: 2019/6/23 检查recipe是否存在并审批
        // TODO: 2019/6/23 检查机台idle时间,是否需要保养或清洗
        // TODO: 2019/6/23 锡球更换检测(备选)
        // TODO: 2019/6/23 料饼更换检测(备选)

        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "AFTER_TRACK_IN");
        if(recipeName.contains("-101-")){
            recipeName = "101";
        }else if(recipeName.contains("-202-")){
            recipeName = "202";
        }else if(recipeName.contains("-202-")){
            recipeName = "202";
        }else if(recipeName.contains("-601-")){
            recipeName = "601";
        }else if(recipeName.contains("-602-")){
            recipeName = "602";
        }else if(recipeName.contains("-401-")){
            recipeName = "401";
        }else if(recipeName.contains("-605-")){
            recipeName = "605";
        }
        map.put("RECIPE_NAME", recipeName);
        map.put("LOT_ID", lotId);
        map.put("EQP_ID", eqpId);
        String bc = "SH_FOL_OV1";
        if(eqpId.indexOf("OVEN")>=0|| eqpId.indexOf("CM-EC-")>=0){
            bc = "SH_FOL_OV1";
        }else if(eqpId.indexOf("-PC")>=0){
            bc = "BC3";
        }
        logger.info("start");
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
        logger.info("end");
        byte[] message = (byte[]) test;
        String msg = null;
        try {
            msg = new String(message, "UTF-8");
            String msg2 = new String(message, "GBK");
            logger.info("encode UTF-8: {}", msg);
            logger.info("encode GBK: {}", msg2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // TODO: 2019/6/23 打印返回结果
        // TODO: 2019/6/23 判断结果,成功则更新设备状态表


        return msg;
    }

    public String downloadRecipe(String eqpId, String recipeName, String lotId, String userId, String params) {
        // TODO: 2019/6/23 打印日志
        // TODO: 2019/6/23 校验开关
        // TODO: 2019/6/23 检查recipe是否存在并审批
        // TODO: 2019/6/23 长号转短号

        //针对烘箱,下载recipe+开机一同完成
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "AFTER_TRACK_IN");
        map.put("RECIPE_NAME", recipeName);
        map.put("LOT_ID", lotId);
        map.put("EQP_ID", eqpId);
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", "SH_FOL_OV1", JsonUtil.toJsonString(map));
        byte[] message = (byte[]) test;
        String msg = null;
        try {
            msg = new String(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // TODO: 2019/6/23 打印返回结果
        // TODO: 2019/6/23 判断结果,成功则更新设备状态表

        System.out.println(msg);
        return msg;
    }

    public String stopEqp(String eqpId, String stepId, String userId, String params) {
        // TODO: 2019/6/23 打印日志
        // TODO: 2019/6/23 校验开关

        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "STOP");
        map.put("EQP_ID", eqpId);
        String bc = "BC1";
        if(eqpId.indexOf("OVEN")>=0){
            bc = "SH_FOL_OV1";
        }else if(eqpId.indexOf("-PC")>=0){
            bc = "BC3";
        }
        logger.info("start");
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
        logger.info("end");
        byte[] message = (byte[]) test;
        String msg = null;
        try {
            msg = new String(message, "UTF-8");
            logger.info("encode UTF-8: {}", msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // TODO: 2019/6/23 打印返回结果
        // TODO: 2019/6/23 判断结果,成功则更新设备状态表
        return msg;
    }

    public String getSVMsg(String eqpId,String vId){
        // TODO: 2019/6/23 打印日志
        // TODO: 2019/6/23 校验开关

        Map<String, String> map = Maps.newHashMap();
        map.put("EQP_ID", eqpId);
        map.put("VID", vId);
        String bc = "SH_FOL_OV1";
        if(eqpId.indexOf("OVEN")>=0|| eqpId.indexOf("CM-EC-")>=0){
            bc = "SH_FOL_OV1";
        }else if(eqpId.indexOf("-PC")>=0){
            bc = "BC3";
        }
        logger.info("start");
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
        logger.info("end");
        byte[] message = (byte[]) test;
        String msg = null;
        try {
            msg = new String(message, "UTF-8");
            logger.info("encode UTF-8: {}", msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // TODO: 2019/6/23 打印返回结果
        // TODO: 2019/6/23 判断结果,成功则更新设备状态表
        return msg;
    }
}
