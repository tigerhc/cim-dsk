package com.lmrj.rms.permit.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.permit.entity
 * @title: rms_recipe_permit实体
 * @description: rms_recipe_permit实体
 * @author: 张伟江
 * @date: 2020-07-15 23:08:38
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rms_recipe_permit")
@SuppressWarnings("serial")
@Data
public class RmsRecipePermit extends BaseDataEntity {

    /**配方id*/
    @TableField(value = "recipe_id")
    private String recipeId;
    /**配方CODE*/
    @TableField(value = "recipe_code")
    private String recipeCode;
    /**对比recipe_id*/
    @TableField(value = "wi_recipe_id")
    private String wiRecipeId;
    /**提交人*/
    @TableField(value = "submitter_id")
    private String submitterId;
    /**提交角色 0、工程师(提交人) 1、peleader 、pe经理 、qa经理审核*/
    @TableField(value = "submitter_role")
    private String submitterRole;
    /**审核结果*/
    @TableField(value = "submit_result")
    private String submitResult;
    /**意见*/
    @TableField(value = "submit_desc")
    private String submitDesc;
    /**时间*/
    @TableField(value = "submit_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date submitDate;

}
