package com.lmrj.edc.param.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.param.entity
 * @title: edc_param_define实体
 * @description: edc_param_define实体
 * @author: 张伟江
 * @date: 2019-06-14 23:05:34
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_param_define")
@SuppressWarnings("serial")
@Data
public class EdcParamDefine extends BaseDataEntity {

    /**工艺类型ID*/
    @TableField(value = "process_type_id")
    private String processTypeId;
    /**工艺类型CODE*/
    @TableField(value = "process_type_code")
    private String processTypeCode;
    /**参数CODE,  厂商+model+00000000 ~ 99999999,各型号不可重复 共10位*/
    @TableField(value = "param_code")
    private String paramCode;
    /**参数名称*/
    @TableField(value = "param_name")
    private String paramName;
    /**参数描述*/
    @TableField(value = "param_desc")
    private String paramDesc;
    /**参数简称*/
    @TableField(value = "param_shortname")
    private String paramShortname;
    /**默认计量单位*/
    @TableField(value = "param_unit")
    private String paramUnit;
    /**参数类别ID*/
    @TableField(value = "param_category_id")
    private String paramCategoryId;
    /**参数类别CODE*/
    @TableField(value = "param_category_code")
    private String paramCategoryCode;
    /**有效标记*/
    @TableField(value = "active_flag")
    private String activeFlag;

}
