package com.lmrj.ms.webSocket.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmrj.ms.webSocket.entity.WebSocketInfo;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws/{user}")
@Component("webSocket")
public class MyWebSocket {

    private Session session;

    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //连接打开时执行
    @OnOpen
    public void onOpen(@PathParam("user") String user, Session session) {
        this.session =session;
        if(!webSocketSet.contains(this)){
            webSocketSet.add(this); // 加入set中
        }
        JSONObject jsonObject = new JSONObject();
        WebSocketInfo webSocketInfo = new WebSocketInfo("0", jsonObject.toString());
        String webStocketStr = JSON.toJSONString(webSocketInfo);
        try {
            session.getBasicRemote().sendText(webStocketStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //收到消息时执行
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    public void sendMessage(String message) {
        for (MyWebSocket javaWebSocket : webSocketSet) {
            synchronized (javaWebSocket.session){
                try {
                    javaWebSocket.session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

        }

    }

    //连接关闭时执行
    @OnClose
    public void onClose(Session session, CloseReason closeReason) throws IOException {
        webSocketSet.remove(this); // 从set中删除
    }

    //连接错误时执行
    @OnError
    public void onError(Throwable t) {

    }

}
