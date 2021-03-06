package com.lmrj.iot.config.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.iot.config.entity
 * @title: iot_collect_data实体
 * @description: iot_collect_data实体
 * @author: wdj
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("iot_collect_data")
@SuppressWarnings("serial")
@Data
public class IotCollectData extends BaseDataEntity {
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
    @TableField(value = "collect_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date collectDate;
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
	
}