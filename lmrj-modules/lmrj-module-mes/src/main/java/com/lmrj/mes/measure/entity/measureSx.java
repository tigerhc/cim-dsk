package com.lmrj.mes.measure.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("measure_sx_record")
@SuppressWarnings("serial")
@Data
public class measureSx {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**批号*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**机种*/
    @TableField(value = "production_no")
    private String productionNo;
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
    @TableField(value = "d1")
    private Double d1;
    @TableField(value = "a2")
    private Double a2;
    @TableField(value = "b2")
    private Double b2;
    @TableField(value = "c2")
    private Double c2;
    @TableField(value = "d2")
    private Double d2;
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
