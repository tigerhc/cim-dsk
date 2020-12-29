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
 * @title: map_tray_config实体
 * @description: map_tray_config实体
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("map_tray_config")
@SuppressWarnings("serial")
@Data
public class MapTrayConfig extends BaseDataEntity {

    /**托盘ID*/
    @TableField(value = "tray_id")
    private String trayId;
    /**托盘行*/
    @TableField(value = "tray_row")
    private String trayRow;
    /**托盘列*/
    @TableField(value = "tray_col")
    private String trayCol;
    /**托盘描述*/
    @TableField(value = "tray_desc")
    private String trayDesc;
    /**有效标志, Y:生效  N:停用 F:冻结*/
    @TableField(value = "status")
    private String status;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**冻结时间,分钟*/
    @TableField(value = "freeze_time")
    private Integer freezeTime;
    /**治具，托盘*/
    @TableField(value = "tray_type")
    private String trayType;
    /**治具的种类*/
    @TableField(value = "tray_model")
    private String trayModel;
    /**排序号*/
    @TableField(value = "sort_no")
    private Integer sortNo;

}
