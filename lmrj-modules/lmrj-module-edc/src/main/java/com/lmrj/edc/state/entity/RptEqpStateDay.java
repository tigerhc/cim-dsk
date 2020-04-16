package com.lmrj.edc.state.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.state.entity
 * @title: rpt_eqp_state_day实体
 * @description: rpt_eqp_state_day实体
 * @author: 张伟江
 * @date: 2020-02-20 01:26:27
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rpt_eqp_state_day")
@SuppressWarnings("serial")
@Data
public class RptEqpStateDay extends BaseDataEntity {

    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**统计日期*/
    @TableField(value = "period_date")
    private String periodDate;
    /**RUN持续时间*/
    @TableField(value = "run_time")
    private Double runTime;
    /**IDLE持续时间*/
    @TableField(value = "idle_time")
    private Double idleTime;
    /**DOWN持续时间*/
    @TableField(value = "down_time")
    private Double downTime;
    /**PM持续时间*/
    @TableField(value = "pm_time")
    private Double pmTime;
    /**其他持续时间*/
    @TableField(value = "other_time")
    private Double otherTime;

}
