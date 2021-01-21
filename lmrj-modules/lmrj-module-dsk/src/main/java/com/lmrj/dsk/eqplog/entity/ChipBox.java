package com.lmrj.dsk.eqplog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@TableName("map_tray_chip_box")
@SuppressWarnings("serial")
@Data
public class ChipBox {
    @TableId(value = "id")
    protected String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**品番号*/
    @TableField(value = "production_no")
    private String productionNo;
    /**批次号*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**目标托盘ID*/
    @TableField(value = "to_tray_id")
    private String toTrayId;
    /**判定结果*/
    @TableField(value = "judge_result")
    private String judgeResult;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**创建日期*/
    @TableField(value = "create_date")
    private Date createDate;
    @TableField(value = "map_flag")
    private Integer mapFlag;
}
