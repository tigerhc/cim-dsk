package com.lmrj.map.tray.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.entity
 * @title: map_equipment_config实体
 * @description: map_equipment_config实体
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("map_equipment_config")
@SuppressWarnings("serial")
@Data
public class MapEquipmentConfig extends BaseDataEntity {
    /**设备ID*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**下游设备ID*/
    @TableField(value = "down_eqp_id")
    private String downEqpId;
    /**设备类型*/
    @TableField(value = "eqp_type")
    private String eqpType;
    /**子线别*/
    @TableField(value = "sub_line_no")
    private String subLineNo;
    /**子线别头或尾*/
    @TableField(value = "sub_line_type")
    private String subLineType;
    /**最小间隔时间*/
    @TableField(value = "interval_time_min")
    private long intervalTimeMin;
    /**最大间隔时间*/
    @TableField(value = "interval_time_max")
    private long intervalTimeMax;
    /**是否同批次*/
    @TableField(value = "same_lot_flag")
    private String sameLotFlag;
}
