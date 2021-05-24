package com.lmrj.iot.config.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author wdj
 * @date 2021-05-13 15:42
 */
@TableName("iot_collect_data_his")
@SuppressWarnings("serial")
@Data
public class IotCollectDataHis extends BaseDataEntity {
    /**主键*/
    @TableField(value = "Id")
    private String id;
    /**设备编号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**采集方式*/
    @TableField(value = "collect_type")
    private String collectType;
    /**采集时间*/
    @TableField(value = "collect_date")
    private String collectDate;
    /**上次示数*/
    @TableField(value = "last_num")
    private String lastNum;
    /**本次示数*/
    @TableField(value = "this_num")
    private String thisNum;

    /**计数比*/
    @TableField(value = "num_multiple")
    private String numMultiple;
    /**示数类型*/
    @TableField(value = "num_type")
    private String numType;
    /**是否警报*/
    @TableField(value = "alarm_flag")
    private String alarmFlag;
    /**警报编码*/
    @TableField(value = "alarm_code")
    private String alarmCode;
    /**归档时间*/
    @TableField(value = "end_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date endDate;
}
