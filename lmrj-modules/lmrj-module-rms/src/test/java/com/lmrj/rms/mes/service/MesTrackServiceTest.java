package com.lmrj.rms.mes.service;

import com.google.common.collect.Maps;
import com.lmrj.util.mapper.JsonUtil;
import com.lmrj.core.entity.MesResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * Created by zwj on 2019/7/5.
 */
//@SpringBootTest(classes = MQApplication.class)
@RunWith(SpringRunner.class)
public class MesTrackServiceTest {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public static void main(String[] args) {
        String msg = "{\"flag\":\"Y\",\"msg\":\"银浆号不存在\",\"content\":null}";
        MesResult result = JsonUtil.from(msg, MesResult.class);
        if("Y".equals(result.flag)){
            Map<String, String> content = Maps.newHashMap();
            content.put("RECIPE_NAME", "111");
            result.setContent(content);
        }
        System.out.println(JsonUtil.toJsonString(result));
    }

    @Test
    public void sendMsg() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "AFTER_TRACK_IN");
        map.put("RECIPE_NAME", "601");
        map.put("LOT_ID", "LOT_ID1");
        map.put("EQP_ID", "OVEN-F-01");
        String bc = "BC1";
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
    }

    @Test
    public void sendMsg2() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "UPLOAD_RECIPE");
        map.put("RECIPE_NAME", "3");
        //map.put("LOT_ID", "LOT_ID1");
        //map.put("EQP_ID", "EQP_ID1");
        String bc = "BC1";
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
    }

    @Test
    public void sendMsg3() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "DOWNLOAD_RECIPE");
        map.put("RECIPE_NAME", "3");
        map.put("PATH", "/recipe/shanghai/cure/UP55A/DRAFT/");
        //map.put("LOT_ID", "LOT_ID1");
        //map.put("EQP_ID", "EQP_ID1");
        String bc = "BC1";
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
    }

    @Test
    // TODO: 2019/11/4 设备未连接,可以直接报错,当前报错: 写入失败
    public void sendMsg4() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "SELECT_RECIPE");
        map.put("RECIPE_NAME", "1");

        //map.put("LOT_ID", "LOT_ID1");
        map.put("EQP_ID", "OVEN-F-01");
        String bc = "BC1";
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
    }

    @Test
    // TODO: 2019/11/4 消息框标题: 远程消息提示:
    public void MSG_DISPLAY() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "MSG_DISPLAY");
        map.put("DISPLAY_MSG", "1111");
        //map.put("LOT_ID", "LOT_ID1");
        map.put("EQP_ID", "OVEN-F-02");
        String bc = "BC1";
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
    }

    @Test
    // TODO: 2019/11/4 消息框标题: 远程消息提示:
    public void PARAM_QUERY() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "PARAM_QUERY");
        map.put("DISPLAY_MSG", "1111");
        //map.put("LOT_ID", "LOT_ID1");
        map.put("EQP_ID", "OVEN-F-01");
        String bc = "BC1";
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
    }

    @Test
    // TODO: 2019/11/4 返回为空
    public void LIST_RECIPE() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "LIST_RECIPE");
        //map.put("DISPLAY_MSG", "1111");
        //map.put("LOT_ID", "LOT_ID1");
        //map.put("EQP_ID", "OVEN-F-01");
        String bc = "BC1";
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
    }

    @Test
    // TODO: 2019/11/4 返回为空
    public void start() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "START");
        //map.put("DISPLAY_MSG", "1111");
        map.put("LOT_ID", "LOT_ID1");
        map.put("EQP_ID", "OVEN-F-01");
        String bc = "BC1";
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
    }

    @Test
    // TODO: 2019/11/4 返回为空
    public void STOP() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "STOP");
        //map.put("DISPLAY_MSG", "1111");
        //map.put("LOT_ID", "LOT_ID1");
        map.put("EQP_ID", "OVEN-F-01");
        String bc = "BC1";
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
    }





}
