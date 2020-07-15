package com.lmrj.rms.permit.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.permit.entity
 * @title: rms_recipe_permit_config实体
 * @description: rms_recipe_permit_config实体
 * @author: 张伟江
 * @date: 2020-07-15 23:08:59
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rms_recipe_permit_config")
@SuppressWarnings("serial")
@Data
public class RmsRecipePermitConfig extends BaseDataEntity {

    /**0:提交, 1,2,3*/
    @TableField(value = "submit_level")
    private Integer submitLevel;
    /**角色ID*/
    @TableField(value = "submitter_role_id")
    private String submitterRoleId;
    /**角色code*/
    @TableField(value = "submitter_role_code")
    private String submitterRoleCode;
    /**角色name*/
    @TableField(value = "submitter_role_name")
    private String submitterRoleName;
	
}