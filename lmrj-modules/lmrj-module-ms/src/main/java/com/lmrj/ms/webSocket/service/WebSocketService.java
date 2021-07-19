package com.lmrj.ms.webSocket.service;

import com.alibaba.fastjson.JSON;
import com.lmrj.ms.webSocket.entity.FabEqptInfo;
import com.lmrj.ms.webSocket.entity.WebSocketInfo;
import com.lmrj.ms.webSocket.util.MyWebSocket;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("webSocketService")
public class WebSocketService {

    private static int i = 0;

    @Resource
    private MyWebSocket webScoket;

//    @Scheduled(cron = "*/1 * * * * *")
    public void eqptInfoChange() {
        FabEqptInfo fabEqptInfo = new FabEqptInfo();
        fabEqptInfo.setEqpId("1");
        if (i % 5 == 0) {
            fabEqptInfo.setStatus("RUN");
        }
        if (i % 5 == 1) {
            fabEqptInfo.setStatus("DOWN");
        }
        if (i % 5 == 2) {
            fabEqptInfo.setStatus("IDEL");
        }
        if (i % 5 == 3) {
            fabEqptInfo.setStatus("STOP");
        }
        if (i % 5 == 4) {
            fabEqptInfo.setStatus("SETUP");
        }
        i++;

        /* 消息推送 */
        String webStocketMsg = JSON.toJSONString(new WebSocketInfo("1", JSON.toJSONString(fabEqptInfo)));
        webScoket.sendMessage(webStocketMsg);
    }
}
