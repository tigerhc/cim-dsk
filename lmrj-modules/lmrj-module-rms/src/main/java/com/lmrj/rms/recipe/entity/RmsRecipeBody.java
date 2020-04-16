package com.lmrj.rms.recipe.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.recipe.entity
 * @title: rms_recipe_body实体
 * @description: rms_recipe_body实体
 * @author: zhangweijiang
 * @date: 2019-06-15 01:58:21
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rms_recipe_body")
@SuppressWarnings("serial")
@Data
public class RmsRecipeBody extends BaseDataEntity {

    /**配方ID*/
    @TableField(value = "recipe_id")
    private String recipeId;
    /**参数CODE*/
    @TableField(value = "para_code")
    private String paraCode;
    /**参数名称*/
    @TableField(value = "para_name")
    private String paraName;
    /**设定值*/
    @TableField(value = "set_value")
    private String setValue;
    /**规格最小值*/
    @TableField(value = "min_value")
    private String minValue;
    /**规格最大值*/
    @TableField(value = "max_value")
    private String maxValue;
    /**更新次数*/
    @TableField(value = "update_cnt")
    private Double updateCnt;

    @TableField(exist = false)
    private String setValueOld;
    /**规格最小值*/
    @TableField(exist = false)
    private String minValueOld;
    /**规格最大值*/
    @TableField(exist = false)
    private String maxValueOld;

}
