package com.lmrj.edc.particle.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

@TableName("edc_particle_record")
@Data
public class ParticleDataBean extends BaseDataEntity {
    @TableField(value = "eqp_id")
    private String eqpId;
    @TableField(value = "particle03_his")
    private int particle03His;//上一周期的值
    @TableField(value = "particle03_cur")
    private int particle03Cur;//累计值，当前（变化）值
    @TableField(value = "particle05_his")
    private int particle05His;
    @TableField(value = "particle05_cur")
    private int particle05Cur;
    @TableField(value = "particle1_his")
    private int particle1His;
    @TableField(value = "particle1_cur")
    private int particle1Cur;
    @TableField(value = "particle3_his")
    private int particle3His;
    @TableField(value = "particle3_cur")
    private int particle3Cur;
    @TableField(value = "particle5_his")
    private int particle5His;
    @TableField(value = "particle5_cur")
    private int particle5Cur;
    @TableField(value = "particle10_his")
    private int particle10His;
    @TableField(value = "particle10_cur")
    private int particle10Cur;
    @TableField(value = "temp")
    private double temp;//<!--温度（带1位小数，数值100表示10.0）-->
    @TableField(value = "humidity")
    private double humidity;//<!--湿度（带1位小数，数值100表示10.0）-->
    @TableField(value = "sampling_cnt")
    private int samplingCnt;
    @TableField(value = "sampling_flow")
    private double samplingFlow;//<!--当前值/100=当前流量-->
    @TableField(value = "set_period")
    private int period;
    @TableField(value = "wind_speed")
    private double windSpeed;//<!--风速（带2位小数，数值100表示1.00）（风速传感器适配：0-10V,0-2m/s）-->
    @TableField(value = "pressure_diff")
    private double pressureDiff;//<!--压差（带1位小数，数值100表示10.0）（风速传感器适配：0.25-4V SDP1000-L/SDP2000-L）-->
    @TableField(value = "start_time")
    private Date startTime;
    @TableField(exist = false)
    private String startTimeStr;

    @TableField(value = "particle03_alarm")
    private int particle03Alarm;//<!--0不报警，1报警-->
    @TableField(value = "particle05_alarm")
    private int particle05Alarm;//<!--0不报警，1报警-->
    @TableField(value = "particle1_alarm")
    private int particle1Alarm;//<!--0不报警，1报警-->
    @TableField(value = "particle3_alarm")
    private int particle3Alarm;//<!--0不报警，1报警-->
    @TableField(value = "particle5_alarm")
    private int particle5Alarm;//<!--0不报警，1报警-->
    @TableField(value = "particle10_alarm")
    private int particle10Alarm;//<!--0不报警，1报警-->
}
