package com.lmrj.ms.webSocket.entity;

public class WebSocketInfo {

    private String msg_action;
    private String msg_info;

    public WebSocketInfo() {
    }

    public WebSocketInfo(String msg_action, String msg_info) {
        this.msg_action = msg_action;
        this.msg_info = msg_info;
    }

    public String getMsg_action() {
        return msg_action;
    }

    public void setMsg_action(String msg_action) {
        this.msg_action = msg_action;
    }

    public String getMsg_info() {
        return msg_info;
    }

    public void setMsg_info(String msg_info) {
        this.msg_info = msg_info;
    }
}
