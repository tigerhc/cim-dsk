package com.lmrj.dsk.dashboard.entity;

/**
 * Created by cmj on 2017/11/18.
 */
public class FipinqtoolOE {

    private String toolg_id;//设备组代码
    private int  run_time;  //运行时长
    private int total_time;//总时长
    private String bay_id ;
    private String bay_name;
    private String bay_index;

    public String getToolg_id() {
        return toolg_id;
    }

    public void setToolg_id(String toolg_id) {
        this.toolg_id = toolg_id;
    }

    public int getRun_time() {
        return run_time;
    }

    public void setRun_time(int run_time) {
        this.run_time = run_time;
    }

    public int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public String getBay_id() {
        return bay_id;
    }

    public void setBay_id(String bay_id) {
        this.bay_id = bay_id;
    }

    public String getBay_name() {
        return bay_name;
    }

    public void setBay_name(String bay_name) {
        this.bay_name = bay_name;
    }

    public String getBay_index() {
        return bay_index;
    }

    public void setBay_index(String bay_index) {
        this.bay_index = bay_index;
    }
}
