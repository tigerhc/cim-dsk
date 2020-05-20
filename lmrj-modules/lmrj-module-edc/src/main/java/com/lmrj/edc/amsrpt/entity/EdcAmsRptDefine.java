package com.lmrj.edc.amsrpt.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.amsrpt.entity
 * @title: edc_ams_rpt_define实体
 * @description: edc_ams_rpt_define实体
 * @author: zhangweijiang
 * @date: 2020-02-15 02:46:43
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_ams_rpt_define")
@SuppressWarnings("serial")
@Data
public class EdcAmsRptDefine extends BaseDataEntity {

    /**警报ID*/
    @TableField(value = "alarm_id")
    private String alarmId;
    /**设备型号*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备型号名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**REPEAT ALARM描述*/
    @TableField(value = "repeat_alarm_desc")
    private String repeatAlarmDesc;
    /**重复次数*/
    @TableField(value = "repeat_num")
    private Integer repeatNum;
    /**周期(时间间隔)*/
    @TableField(value = "repeat_cycle")
    private Integer repeatCycle;
    /**动作CODE*/
    @TableField(value = "action_code")
    private String actionCode;
    /**站点ID*/
    @TableField(value = "station_id")
    private String stationId;
    /**站点CODE*/
    @TableField(value = "station_code")
    private String stationCode;
    /**激活状态*/
    @TableField(value = "active_flag")
    private String activeFlag;
    /**启用时间*/
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "active_date")
    private Date activeDate;
    /**启动人ID*/
    @TableField(value = "active_user_id")
    private String activeUserId;
    /**停用时间*/
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "inactive_date")
    private Date inactiveDate;
    /**停用人ID*/
    @TableField(value = "inactive_user_id")
    private String inactiveUserId;

    @TableField(exist = false)
    private List<EdcAmsRptDefineAct> edcAmsRptDefineAct;

    @TableField(exist = false)
    private List<EdcAmsRptDefineActEmail> edcAmsRptDefineActEmailList;
}
