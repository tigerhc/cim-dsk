package com.lmrj.map.tray.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;

import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.entity
 * @title: map_tray_chip_rebuilt实体
 * @description: map_tray_chip_rebuilt实体
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("map_tray_chip_rebuilt")
@SuppressWarnings("serial")
@Data
public class MapTrayChipRebuilt extends BaseDataEntity {

    /**重刷类型*/
    @TableField(value = "type")
    private Byte type;
    /**重刷批次号*/
    @TableField(value = "lot_nos")
    private String lotNos;
    /**重刷开始记录ID*/
    @TableField(value = "start_no")
    private Long startNo;
    /**执行结果*/
    @TableField(value = "status")
    private String status;
    /**备注*/
    @TableField(value = "remarks")
    private String remarks;

}
