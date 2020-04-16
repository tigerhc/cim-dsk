package com.lmrj.edc.param.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.google.common.collect.Lists;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Title: edc_param_record
 * @Description: edc_param_record
 * @author zhangweijiang
 * @date 2019-06-11 21:26:40
 * @version V1.0
 *
 */
@TableName("edc_param_record")
@Data
@SuppressWarnings("serial")
public class EdcParamRecord extends BaseDataEntity {

    /**设备号*/
    @TableField(value = "EQP_ID")
	private String eqpId;
    /**业务日期*/
    @TableField(value = "BIZ_DATE")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date bizDate;
    /**业务大类*/
    @TableField(value = "BIZ_TYPE")
	private String bizType;
    /**业务小类*/
    @TableField(value = "BIZ_SUB_TYPE")
	private String bizSubType;
    @TableField(value = "create_by")
    protected String createBy; // 创建者
    @TableField(exist = false)
    private List<EdcParamRecordDtl> edcParamRecordDtlList = Lists.newArrayList();

}
