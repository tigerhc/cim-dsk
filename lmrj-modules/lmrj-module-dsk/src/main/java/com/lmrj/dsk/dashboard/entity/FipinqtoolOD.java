package com.lmrj.dsk.dashboard.entity;

import java.util.Map;

/**
 * Created by cmj on 2017/11/18.
 */
public class FipinqtoolOD {

    private String date;//日期
    private Double crop_mobility;//稼动率
    private Map<String,Integer> toolStatusRate;//状态比率

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getCrop_mobility() {
        return crop_mobility;
    }

    public void setCrop_mobility(Double crop_mobility) {
        this.crop_mobility = crop_mobility;
    }

    public Map<String, Integer> getToolStatusRate() {
        return toolStatusRate;
    }

    public void setToolStatusRate(Map<String, Integer> toolStatusRate) {
        this.toolStatusRate = toolStatusRate;
    }
}
