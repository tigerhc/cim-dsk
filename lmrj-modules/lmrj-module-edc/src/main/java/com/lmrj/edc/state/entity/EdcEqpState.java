package com.lmrj.edc.state.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.state.entity
 * @title: edc_eqp_state实体
 * @description: edc_eqp_state实体
 * @author: 张伟江
 * @date: 2020-02-20 01:26:46
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_eqp_state")
@SuppressWarnings("serial")
@Data
public class EdcEqpState extends BaseDataEntity {

    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**状态*/
    @TableField(value = "state")
    private String state;
    /**上一个状态*/
    @TableField(value = "state_previous")
    private String statePrevious;
    /**E10状态*/
    @TableField(value = "state_10")
    private String state10;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**结束时间*/
    @TableField(value = "end_time")
    private Date endTime;
    /**持续时间*/
    @TableField(value = "state_times")
    private Double stateTimes;
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date updateDate; // 更新日期

}
