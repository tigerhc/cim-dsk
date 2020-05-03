package com.lmrj.dsk.dashboard.entity;

import java.util.List;

/**
 * Created by cmj on 2017/11/18.
 */
public class FipinqtoolO {
    private String trx_id;
    private String type_id;
    private String rtn_code;
    private String rtn_mesg;

    private String fab_id_fk;
    private List<FipinqtoolOA> oary;
    private List<FipinqtoolOB> oaryB;
    private List<FipinqtoolOB> oaryB1;
    private List<FipinqtoolOC> oaryC;//当天的状态
    private List<FipinqtoolOD> oaryD;//最近5天的状态
    private List<FipinqtoolOE> oaryE;//最近5天的状态
    private List<FtptolalarmOA> oaryF;
    private List<FtptolalarmOH> oaryH; //当然点巡检任务完成率
    private List<FtptolalarmOJ> oaryj; //最近五天完成率
    private String records;
    private String total;
    private String page;
    private Double sum_time;

    public List<FtptolalarmOH> getOaryH() {
        return oaryH;
    }

    public void setOaryH(List<FtptolalarmOH> oaryH) {
        this.oaryH = oaryH;
    }

    public List<FtptolalarmOJ> getOaryj() {
        return oaryj;
    }

    public void setOaryj(List<FtptolalarmOJ> oaryj) {
        this.oaryj = oaryj;
    }

    public List<FtptolalarmOA> getOaryF() {
        return oaryF;
    }

    public void setOaryF(List<FtptolalarmOA> oaryF) {
        this.oaryF = oaryF;
    }

    public String getFab_id_fk() {
        return fab_id_fk;
    }

    public void setFab_id_fk(String fab_id_fk) {
        this.fab_id_fk = fab_id_fk;
    }

    public List<FipinqtoolOB> getOaryB() {
        return oaryB;
    }

    public void setOaryB(List<FipinqtoolOB> oaryB) {
        this.oaryB = oaryB;
    }

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getRtn_code() {
        return rtn_code;
    }

    public void setRtn_code(String rtn_code) {
        this.rtn_code = rtn_code;
    }

    public String getRtn_mesg() {
        return rtn_mesg;
    }

    public void setRtn_mesg(String rtn_mesg) {
        this.rtn_mesg = rtn_mesg;
    }

    public List<FipinqtoolOA> getOary() {
        return oary;
    }

    public void setOary(List<FipinqtoolOA> oary) {
        this.oary = oary;
    }

    public List<FipinqtoolOC> getOaryC() {
        return oaryC;
    }

    public void setOaryC(List<FipinqtoolOC> oaryC) {
        this.oaryC = oaryC;
    }

    public List<FipinqtoolOD> getOaryD() {
        return oaryD;
    }

    public void setOaryD(List<FipinqtoolOD> oaryD) {
        this.oaryD = oaryD;
    }

    public List<FipinqtoolOE> getOaryE() {
        return oaryE;
    }

    public void setOaryE(List<FipinqtoolOE> oaryE) {
        this.oaryE = oaryE;
    }

    public List<FipinqtoolOB> getOaryB1() {
        return oaryB1;
    }

    public void setOaryB1(List<FipinqtoolOB> oaryB1) {
        this.oaryB1 = oaryB1;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Double getSum_time() {
        return sum_time;
    }

    public void setSum_time(Double sum_time) {
        this.sum_time = sum_time;
    }
}
