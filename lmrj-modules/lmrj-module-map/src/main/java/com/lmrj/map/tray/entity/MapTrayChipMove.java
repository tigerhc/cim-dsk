package com.lmrj.map.tray.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.entity
 * @title: map_tray_chip_move实体
 * @description: map_tray_chip_move实体
 * @author: stev
 * @date: 2020-08-02 15:31:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@TableName("map_tray_chip_move")
@Data
public class MapTrayChipMove {

    private Long id;
    /** 芯片ID */
    @TableField(value = "chip_id")
    private String chipId;
    /** 设备号 */
    @TableField(value = "eqp_id")
    private String eqpId;
    /** 品番号 */
    @TableField(value = "production_no")
    private String productionNo;
    /** 批次号 */
    @TableField(value = "lot_no")
    private String lotNo;
    /** 起始托盘ID */
    @TableField(value = "from_tray_id")
    private String fromTrayId;
    /** 起始托盘X */
    @TableField(value = "from_x")
    private Short fromX;
    /** 起始托盘Y */
    @TableField(value = "from_y")
    private Short fromY;
    /** 目标托盘ID */
    @TableField(value = "to_tray_id")
    private String toTrayId;
    /** 目标托盘X */
    @TableField(value = "to_x")
    private Short toX;
    /** 目标托盘Y */
    @TableField(value = "to_y")
    private Short toY;
    /** 目标托盘Z */
    @TableField(value = "to_z")
    private String toZ;
    /** 判定结果 */
    @TableField(value = "judge_result")
    private String judgeResult;
    /** 开始时间 */
    @TableField(value = "start_time")
    private Date startTime;
    /** 是否同批次 */
    @TableField(value = "map_lot")
    private String mapLot;

    @TableField(exist = false)
    private Integer eqpType;

    @TableField(value = "map_flag")
    private Integer mapFlag;

    @TableField(exist = false)
    private Integer searchFlag;

    @TableField(exist = false)
    private String lmtTime;
}
