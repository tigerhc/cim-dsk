package com.lmrj.iot.config.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

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
	
}