package com.lmrj.edc.amsrpt.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.amsrpt.entity
 * @title: edc_ams_rpt_record_dtl实体
 * @description: edc_ams_rpt_record_dtl实体
 * @author: zhangweijiang
 * @date: 2020-02-15 02:49:43
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_ams_rpt_record_dtl")
@SuppressWarnings("serial")
@Data
public class EdcAmsRptRecordDtl extends AbstractEntity {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**REPEAT ALARM ID*/
    @TableField(value = "rpt_alarm_id")
    private String rptAlarmId;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**报警记录ID*/
    @TableField(value = "alarm_record_id")
    private String alarmRecordId;
    /**警报code*/
    @TableField(value = "alarm_code")
    private String alarmCode;
    /**警报名称*/
    @TableField(value = "alarm_name")
    private String alarmName;
    /**报警详细*/
    @TableField(value = "alarm_detail")
    private String alarmDetail;
    /**警报开关 1:set 0:cleared*/
    @TableField(value = "alarm_switch")
    private String alarmSwitch;
    /**开始时间*/
    @TableField(value = "start_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date startDate;
    /**结束时间*/
    @TableField(value = "end_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date endDate;

}
