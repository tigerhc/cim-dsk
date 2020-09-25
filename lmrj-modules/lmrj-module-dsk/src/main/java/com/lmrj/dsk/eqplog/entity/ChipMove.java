package com.lmrj.dsk.eqplog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.entity
 * @title: edc_dsk_log_recipe_body实体
 * @description: edc_dsk_log_recipe_body实体
 * @author: 王栋
 * @date: 2020-04-17 17:21:46
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("map_tray_chip_move")
@SuppressWarnings("serial")
@Data
public class ChipMove extends AbstractEntity {
    @TableId(value = "id")
    protected String id;
    /**芯片ID*/
    @TableField(value = "chip_id")
    private String chipId;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**品番号*/
    @TableField(value = "production_no")
    private String productionNo;
    /**批次号*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**起始托盘ID*/
    @TableField(value = "from_tray_id")
    private String fromTrayId;
    /**起始托盘X*/
    @TableField(value = "from_x")
    private Integer fromX;
    /**起始托盘Y*/
    @TableField(value = "from_y")
    private Integer fromY;
    /**目标托盘ID*/
    @TableField(value = "to_tray_id")
    private String toTrayId;
    /**目标托盘X*/
    @TableField(value = "to_x")
    private Integer toX;
    /**目标托盘Y*/
    @TableField(value = "to_y")
    private Integer toY;
    /**目标托盘Z*/
    @TableField(value = "to_z")
    private String toZ;
    /**判定结果*/
    @TableField(value = "judge_result")
    private String judgeResult;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**创建日期*/
    @TableField(value = "create_date")
    private Date createDate;
}
