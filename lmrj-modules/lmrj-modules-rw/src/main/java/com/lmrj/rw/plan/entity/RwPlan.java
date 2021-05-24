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
 * @version V1.0
 * @package com.lmrj.rw.plan.entity
 * @title: rw_plan
 * @description: iot_collect_data实体
 * @author wdj
 * @date 2021-05-14 13:22
 */
@TableName("rw_plan")
@SuppressWarnings("serial")
@Data
public class RwPlan  extends BaseDataEntity {
    /**主键*/
    @TableField(value = "Id")
    private String id;
    /**计划/报警 编号*/
    @TableField(value = "plan_id")
    private String planId;
    /**设备编号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**采集时间*/
    @TableField(value = "assigned_time", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date assignedTime;
    /**指派人*/
    @TableField(value = "assigned_user")
    private String assignedUser;
    /**被指派人*/
    @TableField(value = "designee")
    private String designee;

    /**处理方式*/
    @TableField(value = "deal_type")
    private String dealType;
    /**处理时间*/
    @TableField(value = "deal_time", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date dealTime;
    /**计划状态*/
    @TableField(value = "plan_status")
    private String planStatus;
    /**计划类型*/
    @TableField(value = "plan_type")
    private String planType;

    /**处理意见*/
    @TableField(value = "deal_advice")
    private String dealAdvice;
    /**处理描述（回复）*/
    @TableField(value = "deal_des")
    private String dealDes;
    /**归档时间*/
    @TableField(value = "end_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date endDate;
}
