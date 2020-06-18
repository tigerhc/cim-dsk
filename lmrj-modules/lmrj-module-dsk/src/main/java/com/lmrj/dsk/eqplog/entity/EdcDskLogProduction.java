package com.lmrj.dsk.eqplog.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.entity
 * @title: edc_dsk_log_production实体
 * @description: edc_dsk_log_production实体
 * @author: 张伟江
 * @date: 2020-04-14 10:10:00
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_dsk_log_production")
@SuppressWarnings("serial")
@Data
public class EdcDskLogProduction extends AbstractEntity {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    @Excel(name = "设备号", orderNum = "1", width = 20)
    private String eqpId;
    /**设备类型*/
    @TableField(value = "eqp_model_id")
    @Excel(name = "设备型号", orderNum = "1", width = 40)
    private String eqpModelId;
    /**设备类型名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**设备名*/
    @TableField(value = "eqp_no")
    private String eqpNo;
    @TableField(value = "recipe_code")
    @Excel(name = "配方名", orderNum = "1", width = 30)
    private String recipeCode;
    /**日投入数*/
    @TableField(value = "day_yield")
    @Excel(name = "日产量", orderNum = "1", width = 20)
    private Integer dayYield;
    /**批量投入数*/
    @TableField(value = "lot_yield")
    @Excel(name = "批次产量", orderNum = "1", width = 20)
    private Integer lotYield;
    /**制品的批号*/
    @TableField(value = "material_lot_no")
    private String materialLotNo;
    /**制品的型号*/
    @TableField(value = "material_model")
    private String materialModel;
    /**制品的序列号*/
    @TableField(value = "material_no")
    private String materialNo;
    /**制品序列号2*/
    @TableField(value = "material_no2")
    private String materialNo2;
    /**作业指示书批量*/
    @TableField(value = "lot_no")
    @Excel(name = "批号", orderNum = "1", width = 20)
    private String lotNo;
    /**作业指示书机种*/
    @TableField(value = "production_no")
    private String productionNo;
    /**作业指示书序列号*/
    @TableField(value = "order_no")
    private String orderNo;
    /**判定结果*/
    @TableField(value = "judge_result")
    @Excel(name = "判定结果", orderNum = "1", width = 20)
    private String judgeResult;
    /**开始时间*/
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "start_time")
    @Excel(name = "发生时间", orderNum = "1", width = 40)
    private Date startTime;
    /**结束时间*/
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "end_time")
    private Date endTime;
    /**持续时间*/
    @TableField(value = "duration")
    private Double duration;
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date createDate; // 创建日期
}
