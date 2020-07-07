package com.lmrj.rms.recipe.service.impl;

import com.google.common.collect.Maps;
import com.lmrj.core.entity.MesResult;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.service.DemoService;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@WebService(serviceName = "DemoService",//与前面接口一致
        targetNamespace = "http://sercive.DemoService.lmrj.com",  //与前面接口一致
        endpointInterface = "com.lmrj.rms.recipe.service.DemoService")  //接口地址
@Component
@Slf4j
public class DemoServiceImpl implements DemoService {

    @Autowired
    private IFabEquipmentService fabEquipmentService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public String findRecipeName(String eqpId) throws Exception{
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "FIND_RECIPE_NAME");
        map.put("EQP_ID", eqpId);
        String param = JsonUtil.toJsonString(map);
        System.out.println(param);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment == null){
            throw new Exception("该设备不存在");
        }
        String bc = fabEquipment.getBcCode();
        log.info("发送至 S2C.T.CURE.COMMAND({});", bc);
        Object test = rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, param);
        byte[] message = (byte[]) test;
        String msg = null;
        try {
            msg = new String(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.info("接收 S2C.T.CURE.COMMAND 数据失败");
            log.error("Exception:", e);
        }
        MesResult mesResult = JsonUtil.from(msg, MesResult.class);
        return "SIM-DM5#";
    }

}
