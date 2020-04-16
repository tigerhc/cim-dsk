package com.lmrj.edc.evt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.evt.entity
 * @title: edc_evt_define实体
 * @description: edc_evt_define实体
 * @author: 张伟江
 * @date: 2019-06-14 16:01:31
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_evt_define")
@SuppressWarnings("serial")
@Data
public class EdcEvtDefine extends BaseDataEntity {

    /**事件ID*/
    @TableField(value = "event_id")
    private String eventId;
    /**事件 CODE*/
    @TableField(value = "event_code")
    private String eventCode;
    /**事件名称*/
    @TableField(value = "event_name")
    private String eventName;
    /**事件种类*/
    @TableField(value = "event_category")
    private Integer eventCategory;
    /**事件描述*/
    @TableField(value = "event_desc")
    private String eventDesc;
    /**事件类型*/
    @TableField(value = "event_type")
    private String eventType;
    /**监控标记 N、不监控Y、监控*/
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

}
