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
    @TableField(value = "c21")
    private Double c21;

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
