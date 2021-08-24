package com.lmrj.fab.eqp.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.1.entity
 * @title: fab_equipment_status实体
 * @description: fab_equipment_status实体
 * @author: zhangweijiang
 * @date: 2019-06-18 20:41:20
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("fab_equipment_status")
@SuppressWarnings("serial")
@Data
public class FabEquipmentStatus extends BaseDataEntity {

    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**控制状态*/
    @TableField(value = "control_state")
    private String controlState;
    /**品番(产品品号)*/
    @TableField(value = "production_no")
    private String productionNo;
    /**批次*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**设备状态*/
    @TableField(value = "eqp_status")
    private String eqpStatus;
    /**通信状态*/
    @TableField(value = "connection_status")
    private String connectionStatus;
    /**程序名称*/
    @TableField(value = "recipe_code")
    private String recipeCode;
    /**锁机标志*/
    @TableField(value = "lock_flag")
    private String lockFlag;
    /**日投入数*/
    @TableField(value = "day_yield")
    private Integer dayYield;
    /**批次产量*/
    @TableField(value = "lot_yield")
    private Integer lotYield;
    /**批次设备产量*/
    @TableField(value = "lot_yield_eqp")
    private Integer lotYieldEqp;
    /**设备报警内容*/
    @TableField(value = "alarm_name")
    private String alarmName;

}
