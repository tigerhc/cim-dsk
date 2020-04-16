package com.lmrj.edc.amsrpt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
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
public class EdcAmsRptRecordDtl extends BaseDataEntity {

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
    private Date startDate;
    /**结束时间*/
    @TableField(value = "end_date")
    private Date endDate;

}
