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

@TableName("measure_sma_record")
@SuppressWarnings("serial")
@Data
public class MeasureSma {
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
    /**操作员名称*/
    @TableField(value = "measure_name")
    private String measureName;
    /**测量次数*/
    @TableField(value = "measure_counter")
    private String measureCounter;
    @TableField(value = "measure_type")
    private String measureType;

    @TableField(value = "d1_h")
    private Double d1h;
    @TableField(value = "d1_l")
    private Double d1l;
    @TableField(value = "d2_h")
    private Double d2h;
    @TableField(value = "d2_l")
    private Double d2l;
    @TableField(value = "a3_1")
    private Double a31;
    @TableField(value = "a3_2")
    private Double a32;
    @TableField(value = "b3_1")
    private Double b31;
    @TableField(value = "b3_2")
    private Double b32;
    @TableField(value = "c3_1")
    private Double c31;
    @TableField(value = "r3_1")
    private Double r31;
    @TableField(value = "r3_2")
    private Double r32;
    @TableField(value = "a4_1")
    private Double a41;
    @TableField(value = "a4_2")
    private Double a42;
    @TableField(value = "b4_1")
    private Double b41;
    @TableField(value = "b4_2")
    private Double b42;
    @TableField(value = "c4_1")
    private Double c41;
    @TableField(value = "r4_1")
    private Double r41;
    @TableField(value = "r4_2")
    private Double r42;

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
