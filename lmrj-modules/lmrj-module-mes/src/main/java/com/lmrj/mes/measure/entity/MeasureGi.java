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
//5Gi分离数据
@TableName("measure_gi_record")
@SuppressWarnings("serial")
@Data
public class MeasureGi {
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

    @TableField(value = "burr_f")
    private Double burrf;
    @TableField(value = "pin_f1")
    private Double pinf1;
    @TableField(value = "pin_f2")
    private Double pinf2;
    @TableField(value = "pin_f3")
    private Double pinf3;
    @TableField(value = "pin_f4")
    private Double pinf4;
    @TableField(value = "pin_f5")
    private Double pinf5;
    @TableField(value = "pin_f6")
    private Double pinf6;
    @TableField(value = "pin_f1_f2")
    private Double pinf1f2;
    @TableField(value = "pin_f2_f3")
    private Double pinf2f3;
    @TableField(value = "pin_f3_f4")
    private Double pinf3f4;
    @TableField(value = "pin_f4_f5")
    private Double pinf4f5;
    @TableField(value = "pin_f5_f6")
    private Double pinf5f6;
    @TableField(value = "pin_s1")
    private Double pins1;
    @TableField(value = "pin_s2")
    private Double pins2;
    @TableField(value = "pin_s3")
    private Double pins3;
    @TableField(value = "pin_s4")
    private Double pins4;
    @TableField(value = "pin_s5")
    private Double pins5;
    @TableField(value = "pin_s6")
    private Double pins6;


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
