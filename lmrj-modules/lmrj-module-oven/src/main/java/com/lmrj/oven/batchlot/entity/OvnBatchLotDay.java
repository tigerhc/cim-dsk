package com.lmrj.oven.batchlot.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

@TableName("ovn_batch_lot_day")
@Data
public class OvnBatchLotDay extends BaseDataEntity {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

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

    @TableField(value = "title_order")
    private int titleOrder;

    @TableField(value = "create_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
}
