package com.lmrj.dsk.eqplog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@TableName("map_tray_chip_move")
@SuppressWarnings("serial")
@Data
public class ChipSingle {
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
    /**判定结果*/
    @TableField(value = "judge_result")
    private String judgeResult;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**创建日期*/
    @TableField(value = "create_date")
    private Date createDate;
    @TableField(value = "box_cnt")
    private Integer boxCnt;
}
