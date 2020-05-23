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
 * @title: edc_ams_rpt_record实体
 * @description: edc_ams_rpt_record实体
 * @author: zhangweijiang
 * @date: 2020-02-15 02:47:52
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_ams_rpt_record")
@SuppressWarnings("serial")
@Data
public class EdcAmsRptRecord extends BaseDataEntity {

    /**REPEAT ALARM ID*/
    @TableField(value = "rpt_alarm_id")
    private String rptAlarmId;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**报警ID*/
    @TableField(value = "alarm_id")
    private String alarmId;
    /**报警CODE*/
    @TableField(value = "alarm_code")
    private String alarmCode;
    /**报警NAME*/
    @TableField(value = "alarm_name")
    private String alarmName;
    /**设备型号*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备型号名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**作业指示书机种*/
    @TableField(value = "production_no")
    private String productionNo;
    /**作业指示书批量*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**处理结果*/
    @TableField(value = "deal_flag")
    private String dealFlag;
    /**处理时间*/
    @TableField(value = "deal_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date dealDate;
    /**处理人ID*/
    @TableField(value = "deal_user_id")
    private String dealUserId;
    /**处理人NAME*/
    @TableField(value = "deal_user_name")
    private String dealUserName;
    /**处理说明*/
    @TableField(value = "deal_desc")
    private String dealDesc;
    @TableField(exist = false)
    private List<EdcAmsRptRecordDtl> edcAmsRptRecordDtlList;

}
