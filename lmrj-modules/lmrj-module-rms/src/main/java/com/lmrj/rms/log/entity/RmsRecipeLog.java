package com.lmrj.rms.log.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.common.mvc.entity.AbstractEntity;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

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
public class RmsRecipeLog extends AbstractEntity {

    /**主键*/
    @TableField(value = "id")
    private String id;
    /**事件*/
    @TableField(value = "event_code")
    @Excel(name = "事件", orderNum = "1", width = 20)
    private String eventCode;
    /**设备号*/
    @TableField(value = "eqp_id")
    @Excel(name = "设备号", orderNum = "2", width = 20)
    private String eqpId;
    /**设备类型*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备类型名称*/
    @TableField(value = "eqp_model_name")
    @Excel(name = "设备名称", orderNum = "3", width = 20)
    private String eqpModelName;
    /**配方CODE*/
    @TableField(value = "recipe_code")
    @Excel(name = "配方CODE", orderNum = "4", width = 20)
    private String recipeCode;
    /**配方名称(备用)*/
    @TableField(value = "recipe_name")
    @Excel(name = "配方名称", orderNum = "5", width = 20)
    private String recipeName;
    /**配方版本,DRAFT,EQP,GOLD*/
    @TableField(value = "version_type")
    @Excel(name = "设备版本", orderNum = "6", width = 20)
    private String versionType;
    /**配方版本号*/
    @TableField(value = "version_no")
    @Excel(name = "版本号", orderNum = "7", width = 20)
    private Double versionNo;
    /**批次id*/
    @TableField(value = "lot_id")
    @Excel(name = "批次", orderNum = "8", width = 20)
    private String lotId;
    /**参数信息*/
    @TableField(value = "param")
    @Excel(name = "参数信息", orderNum = "9", width = 20)
    private String param;
    /**创建者*/
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**创建时间*/
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @Excel(name = "创建时间", orderNum = "9", width = 20)
    private Date createDate;

}
