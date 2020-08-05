package com.lmrj.mes.lot.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.lot.entity
 * @title: mes_lot_wip实体
 * @description: mes_lot_wip实体
 * @author: 张伟江
 * @date: 2020-08-05 10:32:55
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("mes_lot_wip")
@SuppressWarnings("serial")
@Data
public class MesLotWip extends BaseDataEntity {

    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**作业指示书批量*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**品名*/
    @TableField(value = "production_name")
    private String productionName;
    /**作业指示书机种*/
    @TableField(value = "production_no")
    private String productionNo;
    /**作业指示书序列号*/
    @TableField(value = "order_no")
    private String orderNo;
    /**批次产量*/
    @TableField(value = "lot_yield")
    private Integer lotYield;
    /**批次设备产量*/
    @TableField(value = "lot_yield_eqp")
    private Integer lotYieldEqp;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**结束时间*/
    @TableField(value = "end_time")
    private Date endTime;
    /**站点ID*/
    @TableField(value = "station_id")
    private String stationId;
    /**站点CODE*/
    @TableField(value = "station_code")
    private String stationCode;
    /**工序ID*/
    @TableField(value = "step_id")
    private String stepId;
    /**工序CODE*/
    @TableField(value = "step_code")
    private String stepCode;

}
