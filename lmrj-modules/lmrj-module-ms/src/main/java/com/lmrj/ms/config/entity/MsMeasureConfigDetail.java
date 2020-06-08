package com.lmrj.ms.config.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.config.entity
 * @title: ms_measure_config_detail实体
 * @description: ms_measure_config_detail实体
 * @author: 张伟江
 * @date: 2020-06-06 18:33:20
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("ms_measure_config_detail")
@SuppressWarnings("serial")
@Data
public class MsMeasureConfigDetail extends BaseDataEntity {

    /**项配置ID*/
    @TableField(value = "ms_config_id")
    private String msConfigId;
    /**量测项目编码*/
    @TableField(value = "item_code")
    private String itemCode;
    /**量测项目名称*/
    @TableField(value = "item_name")
    private String itemName;
    /**采样数*/
    @TableField(value = "sample_count")
    private Integer sampleCount;
    /**参数code*/
    @TableField(value = "para_code")
    private String paraCode;
    /**参数名*/
    @TableField(value = "para_name")
    private String paraName;
    /**最小值*/
    @TableField(value = "limit_min")
    private String limitMin;
    /**最大值*/
    @TableField(value = "limit_max")
    private String limitMax;
    /**界限类型0、范围值（需转换成数值，并进行比较）1、固定值该类型控制保存时的值检查，避免空项提交。*/
    @TableField(value = "limit_type")
    private String limitType;
    /**展现形式,input, grid*/
    @TableField(value = "show_type")
    private String showType;
    /**列数*/
    @TableField(value = "col_count")
    private Integer colCount;
    /**列名称,逗号隔开*/
    @TableField(value = "col_name")
    private Integer colName;
    /**行数*/
    @TableField(value = "row_count")
    private Integer rowCount;
    /**行名称,逗号隔开*/
    @TableField(value = "row_name")
    private Integer rowName;
    /**排序号*/
    @TableField(value = "sort_no")
    private Integer sortNo;
    /**有效标志*/
    @TableField(value = "active_flag")
    private String activeFlag;

}
