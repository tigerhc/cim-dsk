package com.lmrj.oven.batchlot.entity;

import com.baomidou.mybatisplus.annotations.TableField;
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

@SuppressWarnings("serial")
@Data
public class FabEquipmentOvenStatus extends BaseDataEntity {

    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**控制状态*/
    @TableField(value = "control_state")
    private String controlState;
    /**批次*/
    @TableField(value = "lot_id")
    private String lotId;
    /**设备状态*/
    @TableField(value = "eqp_status")
    private String eqpStatus;
    /**通信状态*/
    @TableField(value = "connection_status")
    private String connectionStatus;
    /**程序名称*/
    @TableField(value = "recipe_name")
    private String recipeName;
    /**锁机标志*/
    @TableField(value = "lock_flag")
    private String lockFlag;

    private String modelName;
    private String ptNo;
    private String segNo;
    private String rtime;
    private String temp;

}
