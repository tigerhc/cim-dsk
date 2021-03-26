package com.lmrj.rms.log.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.lmrj.core.entity.BaseDataEntity;
import com.lmrj.core.excel.annotation.ExcelField;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import lombok.Data;

import java.util.List;

/**
 * @author wdj
 * @date 2021-03-25 15:04
 * @title: rms_recipe_check_log实体
 * @description: 用于记录配方参数校验日志
 */
@TableName("rms_recipe_check_log")
@Data
public class RmsRecipeCheckLog  extends BaseDataEntity {
    /**配方CODE*/
    @ExcelField(title="配方CODE", type=0, align=1, sort=20)
    @TableField(value = "recipe_code")
    @Excel(name = "配方CODE", orderNum = "1", width = 20)
    private String recipeCode;
    /**配方名称(备用)*/
    @TableField(value = "recipe_name")
    @Excel(name = "配方名称", orderNum = "1", width = 20)
    private String recipeName;
    /**设备号*/
    @TableField(value = "eqp_id")
    @Excel(name = "设备号", orderNum = "1", width = 20)
    private String eqpId;
    @Excel(name = "程序等级", orderNum = "1", width = 20)
    @TableField(value = "version_type")
    private String versionType;
    /**配方版本号*/
    @TableField(value = "version_no")
    @Excel(name = "程序版本号", orderNum = "1", width = 20)
    private Integer versionNo;
    @TableField(value = "param_code")
    @Excel(name = "参数code", orderNum = "1",width = 20)
    private String paramCode;
    @TableField(value = "param_Name")
    @Excel(name = "参数名称", orderNum = "1",width = 20)
    private String paramName;
    @TableField(value = "check_result")
    @Excel(name = "校验结果", orderNum = "1",width = 20)
    private String checkResult;
    @TableField(value = "check_remarks")
    @Excel(name = "校验信息", orderNum = "1",width = 40)
    private String checkRemarks;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "check_date", fill = FieldFill.INSERT)
    @Excel(name = "校验时间", orderNum = "9", width = 20,format="yyyy-MM-dd HH:mm:ss")
    private String checkDate;
}
