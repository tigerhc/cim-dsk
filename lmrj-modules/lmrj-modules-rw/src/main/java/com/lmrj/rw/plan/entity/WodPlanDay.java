package com.lmrj.rw.plan.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@TableName("wod_plan_day")
@SuppressWarnings("serial")
@Data
public class WodPlanDay extends BaseDataEntity {

    /**主键*/
    @TableField(value = "Id")
    private String id;
    /**设备编号*/
    @TableField(value = "eqp_id")
    private String eqpId;

    /**计划类型*/
    @TableField(value = "plan_type")
    private String planType;

    /**计划类型*/
    @TableField(value = "plan_cycle")
    private String planCycle;

    /**处理时间*/
    @TableField(value = "effect_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date effectDate;

    /**备注*/
    @TableField(value = "remarks")
    private String remarks;
    @TableField(exist = false)
    private List<RwPlan> rwPlanList;


    /**采集时间*/
    @TableField(exist = false, fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date assignedTime;
    /**指派人*/
    @TableField(exist = false)
    private String assignedUser;
    /**被指派人*/
    @TableField(exist = false)
    private String designee;

}
