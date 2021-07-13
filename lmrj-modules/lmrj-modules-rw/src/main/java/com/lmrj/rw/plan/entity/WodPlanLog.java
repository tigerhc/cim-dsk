package com.lmrj.rw.plan.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author wdj
 * @date 2021-05-14 13:56
 */
@TableName("wod_plan_log")
@SuppressWarnings("serial")
@Data
public class WodPlanLog   extends BaseDataEntity {
    /**主键*/
    @TableField(value = "id")
    private String id;
    /**计划/报警 编号*/
    @TableField(value = "wod_id")
    private String wodId;
    /**设备编号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**采集时间*/
    @TableField(value = "assigned_time", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date assignedTime;
    /**处理方式*/
    @TableField(value = "deal_type")
    private String dealType;
    /**处理方式*/
    @TableField(value = "create_by")
    private String createBy;
    /**处理时间*/
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date createDate;
    /**计划状态*/
    @TableField(value = "plan_status")
    private String planStatus;
    /**计划类型*/
    @TableField(value = "plan_type")
    private String planType;
    /**备注信息*/
    @TableField(value = "remarks")
    private String remarks;
}
