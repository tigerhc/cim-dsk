package com.lmrj.dsk.eqplog.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.entity
 * @title: edc_dsk_log_recipe_body实体
 * @description: edc_dsk_log_recipe_body实体
 * @author: 张伟江
 * @date: 2020-04-17 17:21:46
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_dsk_log_recipe_body")
@SuppressWarnings("serial")
@Data
public class EdcDskLogRecipeBody extends AbstractEntity {

    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**配方日志ID*/
    @TableField(value = "recipe_log_id")
    private String recipeLogId;
    /**参数CODE*/
    @TableField(value = "para_code")
    private String paraCode;
    /**参数名称*/
    @TableField(value = "para_name")
    private String paraName;
    /**设定值*/
    @TableField(value = "set_value")
    private String setValue;
    /**设定旧值*/
    @TableField(value = "pre_value")
    private String preValue;
    /**规格最小值*/
    @TableField(value = "min_value")
    private String minValue;
    /**规格最大值*/
    @TableField(value = "max_value")
    private String maxValue;
    @TableField(value = "sort_no")
    private Integer sortNo;

}
