package com.lmrj.mes.measure.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("measure_sim_record")
@SuppressWarnings("serial")
@Data
public class MeasureSim {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**批号*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**机种*/
    @TableField(value = "production_no")
    private String productionNo;
    /**线别*/
    @TableField(value = "line_no")
    private String lineNo;
    /**串行计数器*/
    @TableField(value = "serial_counter")
    private String serialCounter;
    /**综合判定*/
    @TableField(value = "measure_judgment")
    private String measureJudgment;
    /**名称*/
    @TableField(value = "measure_name")
    private String measureName;
    /**测量次数*/
    @TableField(value = "measure_counter")
    private String measureCounter;
    @TableField(value = "measure_type")
    private String measureType;

    @TableField(value = "a1")
    private Double a1;
    @TableField(value = "b1")
    private Double b1;
    @TableField(value = "c1")
    private Double c1;
    @TableField(value = "c2")
    private Double c2;
    @TableField(value = "c3")
    private Double c3;
    @TableField(value = "c4")
    private Double c4;
    @TableField(value = "c5")
    private Double c5;
    @TableField(value = "c6")
    private Double c6;
    @TableField(value = "c7")
    private Double c7;
    @TableField(value = "c8")
    private Double c8;
    @TableField(value = "c9")
    private Double c9;
    @TableField(value = "c10")
    private Double c10;
    @TableField(value = "c11")
    private Double c11;
    @TableField(value = "c12")
    private Double c12;
    @TableField(value = "c13")
    private Double c13;
    @TableField(value = "c14")
    private Double c14;
    @TableField(value = "c15")
    private Double c15;
    @TableField(value = "c16")
    private Double c16;
    @TableField(value = "c17")
    private Double c17;
    @TableField(value = "c19")
    private Double c19;
    @TableField(value = "c20")
    private Double c20;
    @TableField(value = "c21")
    private Double c21;
    @TableField(value = "c23")
    private Double c23;
    @TableField(value = "c24")
    private Double c24;
    @TableField(value = "c26")
    private Double c26;
    @TableField(value = "c28")
    private Double c28;
    @TableField(value = "c30")
    private Double c30;
    @TableField(value = "c31")
    private Double c31;
    @TableField(value = "c35")
    private Double c35;
    @TableField(value = "c37")
    private Double c37;
    @TableField(value = "c40")
    private Double c40;

    /**发生时刻*/
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "mea_date")
//    @TableField(value = "measure_time")
    private Date meaDate;
}
