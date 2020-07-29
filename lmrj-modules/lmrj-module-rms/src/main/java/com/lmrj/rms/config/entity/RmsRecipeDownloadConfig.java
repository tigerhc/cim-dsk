package com.lmrj.rms.config.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.config.entity
 * @title: rms_recipe_download_config实体
 * @description: rms_recipe_download_config实体
 * @author: 何思国
 * @date: 2020-07-29 16:21:03
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rms_recipe_download_config")
@SuppressWarnings("serial")
@Data
public class RmsRecipeDownloadConfig extends BaseDataEntity {

    /**设备型号ID*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备型号*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**等级1（DRAFT,EQP，GOLD）*/
    @TableField(value = "level1")
    private String level1;
    /**等级2（DRAFT,EQP，GOLD）*/
    @TableField(value = "level2")
    private String level2;
    /**等级3（DRAFT,EQP，GOLD）*/
    @TableField(value = "level3")
    private String level3;
	
}