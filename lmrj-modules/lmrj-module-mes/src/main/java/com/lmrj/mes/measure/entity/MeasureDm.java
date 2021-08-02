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

@TableName("measure_sx_record")
@SuppressWarnings("serial")
@Data
public class MeasureDm {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**制品码*/
    @TableField(value = "chip_id")
    private String chipId;
    /**批号*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**机种*/
    @TableField(value = "production_no")
    private String productionNo;
    /**线别*/
    @TableField(value = "line_no")
    private String lineNo;
    /**综合判定*/
    @TableField(value = "measure_judgment")
    private String measureJudgment;
    /**操作员名称*/
    @TableField(value = "measure_name")
    private String measureName;
    /**测量次数*/
    @TableField(value = "measure_counter")
    private String measureCounter;
    /**测量类型*/
    @TableField(value = "measure_type")
    private String measureType;

    @TableField(value = "f0_1")
    private Double f0_1;
    @TableField(value = "f0_2")
    private Double f0_2;
    @TableField(value = "f0_3")
    private Double f0_3;
    @TableField(value = "f1_1")
    private Double f1_1;
    @TableField(value = "f1_2")
    private Double f1_2;
    @TableField(value = "f1_3")
    private Double f1_3;
    @TableField(value = "f3_1")
    private Double f3_1;
    @TableField(value = "f3_2")
    private Double f3_2;
    @TableField(value = "f3_3")
    private Double f3_3;
    @TableField(value = "f3_4")
    private Double f3_4;
    @TableField(value = "f3_5")
    private Double f3_5;
    @TableField(value = "f3_6")
    private Double f3_6;
    @TableField(value = "f3_7")
    private Double f3_7;
    @TableField(value = "f3_8")
    private Double f3_8;
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
