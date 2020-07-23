package com.lmrj.edc.config.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.config.entity
 * @title: edc_config_file_csv实体
 * @description: edc_config_file_csv实体
 * @author: 张伟江
 * @date: 2020-07-23 16:12:15
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_config_file_csv")
@SuppressWarnings("serial")
@Data
public class EdcConfigFileCsv extends BaseDataEntity {

    /**设备型号ID*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备型号*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**本字段存储模板中的id,作为参数系统中的唯一标识。在创建时设置与本表id同。*/
    @TableField(value = "col_code")
    private String colCode;
    /**参数名*/
    @TableField(value = "col_name")
    private String colName;
    /**简称*/
    @TableField(value = "col_short_name")
    private String colShortName;
    /**单位*/
    @TableField(value = "para_unit")
    private String paraUnit;
    /**设定值*/
    @TableField(value = "default_value")
    private String defaultValue;
    /**最小值*/
    @TableField(value = "limit_min")
    private Double limitMin;
    /**最大值*/
    @TableField(value = "limit_max")
    private Double limitMax;
    /**界限类型0、范围值（需转换成数值，并进行比较）1、固定值该类型控制保存时的值检查，避免空项提交。*/
    @TableField(value = "limit_type")
    private String limitType;
    /**监控标记 N、不监控Y、监控*/
    @TableField(value = "monitor_flag")
    private String monitorFlag;
    /**参数等级1、提示2、警告3、停机4、停机+扣留*/
    @TableField(value = "col_level")
    private String colLevel;
    /**参数数据类型0、FLOAT1、ASCII码2、BCD码*/
    @TableField(value = "col_data_type")
    private String colDataType;
    /**排序号*/
    @TableField(value = "sort_no")
    private Integer sortNo;
	
}