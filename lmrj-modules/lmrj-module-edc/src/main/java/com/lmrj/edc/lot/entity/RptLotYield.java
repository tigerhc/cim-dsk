package com.lmrj.edc.lot.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.lot.entity
 * @title: rpt_lot_yield实体
 * @description: rpt_lot_yield实体
 * @author: 张伟江
 * @date: 2020-05-17 21:10:40
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rpt_lot_yield")
@SuppressWarnings("serial")
@Data
public class RptLotYield extends BaseDataEntity {

    /**批次*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**品番*/
    @TableField(value = "production_no")
    private String productionNo;
    /**品名*/
    @TableField(value = "production_name")
    private String productionName;
    /**作业指示书序列号*/
    @TableField(value = "order_no")
    private String orderNo;
    /**批次产量*/
    @TableField(value = "lot_yield")
    private Integer lotYield;
    /**批次设备产量*/
    @TableField(value = "lot_yield_eqp")
    private Integer lotYieldEqp;
    /**批次投入*/
    @TableField(value = "lot_input")
    private Integer lotInput;
    /**工序ID*/
    @TableField(value = "step_id")
    private String stepId;
    /**工序CODE*/
    @TableField(value = "step_code")
    private String stepCode;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**结束时间*/
    @TableField(value = "end_time")
    private Date endTime;

}
