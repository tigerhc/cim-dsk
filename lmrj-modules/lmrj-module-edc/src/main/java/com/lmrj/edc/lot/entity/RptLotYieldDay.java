package com.lmrj.edc.lot.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.lot.entity
 * @title: rpt_lot_yield_day实体
 * @description: rpt_lot_yield_day实体
 * @author: 张伟江
 * @date: 2020-05-17 21:10:56
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rpt_lot_yield_day")
@SuppressWarnings("serial")
@Data
public class RptLotYieldDay extends BaseDataEntity {

    /**统计日期*/
    @TableField(value = "period_date")
    private String periodDate;
    /**批次*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**品番*/
    @TableField(value = "production_no")
    private String productionNo;
    /**工序ID*/
    @TableField(value = "step_id")
    private String stepId;
    /**工序CODE*/
    @TableField(value = "step_code")
    private String stepCode;
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

}
