package com.lmrj.edc.ams.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.google.common.collect.Lists;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.ams.entity
 * @title: edc_ams_define实体
 * @description: edc_ams_define实体
 * @author: zhangweijiang
 * @date: 2020-02-15 02:39:17
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_ams_define")
@SuppressWarnings("serial")
@Data
public class EdcAmsDefine extends BaseDataEntity {

    /**警报CODE*/
    @TableField(value = "alarm_code")
    private String alarmCode;
    /**警报名称*/
    @TableField(value = "alarm_name")
    private String alarmName;
    /**警报种类*/
    @TableField(value = "alarm_category")
    private Integer alarmCategory;
    /**警报描述*/
    @TableField(value = "alarm_desc")
    private String alarmDesc;
    /**警报类型*/
    @TableField(value = "alarm_type")
    private String alarmType;
    /**监控标记 0:不监控 1:监控*/
    @TableField(value = "monitor_flag")
    private String monitorFlag;
    /**子设备号*/
    @TableField(value = "sub_eqp_id")
    private String subEqpId;
    /**设备型号*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备型号名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    @TableField(exist = false)
    private List<EdcAmsDefineI18n> edcAmsDefineI18nList= Lists.newArrayList();

}
