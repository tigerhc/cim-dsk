package com.lmrj.dsk.eqplog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.entity
 * @title: edc_dsk_log_production实体
 * @description: edc_dsk_log_production实体
 * @author: 张伟江
 * @date: 2020-04-14 10:10:00
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_dsk_log_production")
@SuppressWarnings("serial")
@Data
public class EdcDskLogProduction extends AbstractEntity {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**设备类型*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备类型名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**设备名*/
    @TableField(value = "eqp_no")
    private String eqpNo;
    /**产量*/
    @TableField(value = "yield")
    private Integer yield;
    /**批次产量*/
    @TableField(value = "yield_lot")
    private Integer yieldLot;
    /**制品的批号*/
    @TableField(value = "material_lot_no")
    private String materialLotNo;
    /**制品的型号*/
    @TableField(value = "material_model")
    private String materialModel;
    /**制品的序列号*/
    @TableField(value = "material_no")
    private String materialNo;
    /**制品序列号2*/
    @TableField(value = "material_no2")
    private String materialNo2;
    /**作业指示书批量*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**作业指示书机种*/
    @TableField(value = "lot_device")
    private String lotDevice;
    /**作业指示书序列号*/
    @TableField(value = "lot_no_no")
    private String lotNoNo;
    /**判定结果*/
    @TableField(value = "judge_result")
    private String judgeResult;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**结束时间*/
    @TableField(value = "end_time")
    private Date endTime;
    /**持续时间*/
    @TableField(value = "duration")
    private Double duration;

}
