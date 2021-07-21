package com.lmrj.edc.param.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * @Title: edc_param_record_dtl
 * @Description: edc_param_record_dtl
 * @author zhangweijiang
 * @date 2019-06-11 21:26:40
 * @version V1.0
 *
 */
@TableName("edc_param_record_dtl")
@Data
@SuppressWarnings("serial")
public class EdcParamRecordDtl extends BaseDataEntity {

    /**SV取值ID*/
    //@TableField(value = "RECORD_ID",el="recordId.id")
    @TableField(value = "RECORD_ID")
	private String recordId;
    @TableField(value = "EQP_ID")
    private String eqpId;
    /**SV_ROW_ID*/
    @TableField(value = "PARAM_ROW_ID")
	private String paramRowId;
    /**SVID*/
    @TableField(value = "PARAM_ID")
	private String paramId;
    /**SV编码*/
    @TableField(value = "PARAM_CODE")
	private String paramCode;
    /**SV名称*/
    @TableField(value = "PARAM_NAME")
	private String paramName;
    /**SV简称*/
    @TableField(value = "PARAM_SHOT_NAME")
	private String paramShotName;
    /**当前值*/
    @TableField(value = "PARAM_VALUE")
	private String paramValue;
    /**计量单位*/
    @TableField(value = "PARAM_UNIT")
	private String paramUnit;
    /**通讯协议 格式:A,B*/
    @TableField(value = "PROTOCOL")
	private String protocol;
    /**子通讯协议 格式:SV EC*/
    @TableField(value = "PROTOCOL_SUB")
	private String protocolSub;
    /**SPEC类型*/
    @TableField(value = "VALUE_TYPE")
	private String valueType;
    /**设定值*/
    @TableField(value = "SET_VALUE")
	private String setValue;
    /**最小值*/
    @TableField(value = "MIN_VALUE")
	private String minValue;
    /**最大值*/
    @TableField(value = "MAX_VALUE")
	private String maxValue;
    @TableField(value = "create_by")
    protected String createBy; // 创建者

    /**业务日期*/
    @TableField(value = "create_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}
