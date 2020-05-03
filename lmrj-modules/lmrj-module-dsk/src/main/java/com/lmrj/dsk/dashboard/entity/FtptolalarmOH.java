package com.lmrj.dsk.dashboard.entity;

public class FtptolalarmOH {
    private String ins_task_stat;
    private int stat_task_num;
    private double stat_rate;
    private int total_task;
    private String fab_id_fk;

    public String getFab_id_fk() {
        return fab_id_fk;
    }

    public void setFab_id_fk(String fab_id_fk) {
        this.fab_id_fk = fab_id_fk;
    }

    public String getIns_task_stat() {
        return ins_task_stat;
    }

    public void setIns_task_stat(String ins_task_stat) {
        this.ins_task_stat = ins_task_stat;
    }

    public int getStat_task_num() {
        return stat_task_num;
    }

    public void setStat_task_num(int stat_task_num) {
        this.stat_task_num = stat_task_num;
    }

    public double getStat_rate() {
        return stat_rate;
    }

    public void setStat_rate(double stat_rate) {
        this.stat_rate = stat_rate;
    }

    public int getTotal_task() {
        return total_task;
    }

    public void setTotal_task(int total_task) {
        this.total_task = total_task;
    }
}
