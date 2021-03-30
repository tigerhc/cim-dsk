package com.lmrj.edc.param.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.param.entity
 * @title: edc_eqp_log_param实体
 * @description: edc_eqp_log_param实体
 * @author: 高雪君
 * @date: 2021-03-30 09:05:34
 * @copyright: 2020 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_eqp_log_param")
@SuppressWarnings("serial")
@Data
public class EdcEqpLogParam extends BaseDataEntity {

    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @Excel(name = "设备id", orderNum = "1", width = 20)
    @TableField(value = "eqp_id")
    private String eqpId;
    /**事件ID*/
    @Excel(name = "设备型号", orderNum = "2", width = 10)
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    @Excel(name = "参数key值", orderNum = "3", width = 30)
    @TableField(value = "param_key")
    private String paramKey;
    @Excel(name = "参数名", orderNum = "4", width = 30)
    @TableField(value = "param_value")
    private String paramValue;
    @Excel(name = "参数类型", orderNum = "5", width = 30)
    @TableField(value = "param_type")
    private String paramType;
    @Excel(name = "备注", orderNum = "6", width = 30)
    @TableField(value = "remarks")
    private String remarks;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy; // 创建者
    @Excel(name = "创建日期", orderNum = "6", width = 30,format="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_date", fill = FieldFill.INSERT, update = "now()")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date createDate; // 创建日期

}
