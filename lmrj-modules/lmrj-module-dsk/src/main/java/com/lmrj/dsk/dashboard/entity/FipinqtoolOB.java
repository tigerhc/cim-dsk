package com.lmrj.dsk.dashboard.entity;

/**
 * Created by cmj on 2017/11/18.
 */
public class FipinqtoolOB {

    private String tool_id_fk;
    private String tool_stat;
    private String toolg_id;
    private String bay_id;
    private String bay_name;
    private int time;//状态时间 //分钟
    private int second_time;//持续秒
    private Long timeToal;//总时间
    private int toolIds;//同个设备组的设备数量
    private int wholeToolIds;//所有设备的数量


    public String getBay_id() {
        return bay_id;
    }

    public void setBay_id(String bay_id) {
        this.bay_id = bay_id;
    }

    public int getToolIds() {
        return toolIds;
    }

    public void setToolIds(int toolIds) {
        this.toolIds = toolIds;
    }

    public String getToolg_id() {
        return toolg_id;
    }

    public void setToolg_id(String toolg_id) {
        this.toolg_id = toolg_id;
    }

    public String getTool_id_fk() {
        return tool_id_fk;
    }

    public void setTool_id_fk(String tool_id_fk) {
        this.tool_id_fk = tool_id_fk;
    }

    public String getTool_stat() {
        return tool_stat;
    }

    public void setTool_stat(String tool_stat) {
        this.tool_stat = tool_stat;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Long getTimeToal() {
        return timeToal;
    }

    public void setTimeToal(Long timeToal) {
        this.timeToal = timeToal;
    }

    public int getSecond_time() {
        return second_time;
    }

    public void setSecond_time(int second_time) {
        this.second_time = second_time;
    }

    public int getWholeToolIds() {
        return wholeToolIds;
    }

    public void setWholeToolIds(int wholeToolIds) {
        this.wholeToolIds = wholeToolIds;
    }

    public String getBay_name() {
        return bay_name;
    }

    public void setBay_name(String bay_name) {
        this.bay_name = bay_name;
    }
}
