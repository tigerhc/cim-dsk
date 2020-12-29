package com.lmrj.map.tray.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

@TableName("map_tray_model")
@SuppressWarnings("serial")
@Data
public class MapTrayModel extends BaseDataEntity {
    /**治具的种类*/
    @TableField(value = "tray_model")
    private String trayModel;
    /**托盘行*/
    @TableField(value = "tray_row")
    private int trayRow;
    /**托盘列*/
    @TableField(value = "tray_col")
    private int trayCol;
    /**治具，托盘*/
    @TableField(value = "tray_type")
    private String trayType;
}
