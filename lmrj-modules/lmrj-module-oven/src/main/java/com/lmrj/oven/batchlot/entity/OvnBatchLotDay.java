package com.lmrj.oven.batchlot.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("ovn_batch_lot_day")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OvnBatchLotDay {

    @TableField(value = "id")
    private int id;

    /*设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;

    /*最大值*/
    @TableField(value = "temp_max")
    private String tempMax;

    /*最小值*/
    @TableField(value = "temp_min")
    private String tempMin;

    /*开始值*/
    @TableField(value = "temp_start")
    private String tempStart;

    /*结束值*/
    @TableField(value = "temp_end")
    private String tempEnd;

    /*设备类型*/
    @TableField(value = "eqp_temp")
    private String eqpTemp;

    /*作业日期*/
    @TableField(value = "period_date")
    private String periodDate;



}
