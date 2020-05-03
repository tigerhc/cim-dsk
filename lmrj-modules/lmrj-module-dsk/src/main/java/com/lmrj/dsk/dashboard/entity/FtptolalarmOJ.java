package com.lmrj.dsk.dashboard.entity;

import java.util.List;

public class FtptolalarmOJ {
    private String date;
    private String fab_id_fk;
    private List<FtptolalarmOH> orayList;

    public String getFab_id_fk() {
        return fab_id_fk;
    }

    public void setFab_id_fk(String fab_id_fk) {
        this.fab_id_fk = fab_id_fk;
    }

    public List<FtptolalarmOH> getOrayList() {
        return orayList;
    }

    public void setOrayList(List<FtptolalarmOH> orayList) {
        this.orayList = orayList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
