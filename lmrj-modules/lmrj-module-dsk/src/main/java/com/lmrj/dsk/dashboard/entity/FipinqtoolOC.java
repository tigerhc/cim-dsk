package com.lmrj.dsk.dashboard.entity;

/**
 * Created by cmj on 2017/11/18.
 */
public class FipinqtoolOC {
    //一天的状态
    private String tool_stat;
    private int time;//状态时间
    private Long totalTime;//总时间


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

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }
}
