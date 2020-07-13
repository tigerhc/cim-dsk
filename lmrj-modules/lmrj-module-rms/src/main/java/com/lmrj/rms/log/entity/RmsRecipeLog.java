package com.lmrj.rms.log.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.log.entity
 * @title: rms_recipe_log实体
 * @description: rms_recipe_log实体
 * @author: 张伟江
 * @date: 2020-07-07 16:10:43
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rms_recipe_log")
@SuppressWarnings("serial")
@Data
public class RmsRecipeLog extends BaseDataEntity {

    /**主键*/
    @TableField(value = "id")
    private String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**设备类型*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备类型名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**配方CODE*/
    @TableField(value = "recipe_code")
    private String recipeCode;
    /**配方名称(备用)*/
    @TableField(value = "recipe_name")
    private String recipeName;
    /**配方版本,DRAFT,EQP,GOLD*/
    @TableField(value = "version_type")
    private String versionType;
    /**配方版本号*/
    @TableField(value = "version_no")
    private Double versionNo;
    /**批次id*/
    @TableField(value = "lot_id")
    private String lotId;
    /**参数信息*/
    @TableField(value = "param")
    private String param;

}
