package com.lmrj.rms.template.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.template.entity
 * @title: rms_recipe_template实体
 * @description: rms_recipe_template实体
 * @author: zhangweijiang
 * @date: 2019-06-15 01:58:43
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rms_recipe_template")
@SuppressWarnings("serial")
@Data
public class RmsRecipeTemplate extends BaseDataEntity {

    /**设备型号ID*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备型号*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**本字段存储模板中的id,作为参数系统中的唯一标识。在创建时设置与本表id同。*/
    @TableField(value = "para_code")
    @Excel(name = "参数代码", orderNum = "2", width = 20)
    private String paraCode;
    /**参数名*/
    @TableField(value = "para_name")
    @Excel(name = "参数名称", orderNum = "2", width = 20)
    private String paraName;
    /**简称*/
    @TableField(value = "para_short_name")
    @Excel(name = "参数简称", orderNum = "2", width = 20)
    private String paraShortName;
    /**单位*/
    @TableField(value = "para_unit")
    @Excel(name = "单位", orderNum = "2", width = 20)
    private String paraUnit;
    /**设定值*/
    @TableField(value = "set_value")
    @Excel(name = "设定值", orderNum = "2", width = 20)
    private String setValue;
    /**最小值*/
    @TableField(value = "limit_min")
    private Double limitMin;
    /**最大值*/
    @TableField(value = "limit_max")
    private Double limitMax;
    /**界限类型0、范围值（需转换成数值，并进行比较）1、固定值该类型控制保存时的值检查，避免空项提交。*/
    @TableField(value = "limit_type")
    private String limitType;
    /**显示标记 N、不在初次打开页面显示Y、在初次打开页面显示*/
    @TableField(value = "show_flag")
    @Excel(name = "是否首页显示", orderNum = "2", width = 20)
    private String showFlag;
    /**监控标记 N、不监控Y、监控*/
    @TableField(value = "monitor_flag")
    @Excel(name = "是否监控", orderNum = "2", width = 20)
    private String monitorFlag;
    /**参数等级1、提示2、警告3、停机4、停机+扣留*/
    @TableField(value = "para_level")
    private String paraLevel;
    /**参数数据类型0、FLOAT1、ASCII码2、BCD码*/
    @TableField(value = "para_data_type")
    private String paraDataType;
    /**计算系数*/
    @TableField(value = "count_rate")
    private String countRate;
    /**参数长度*/
    @TableField(value = "decode_length")
    private Double decodeLength;
    /**解析起始位*/
    @TableField(value = "decode_start")
    private Double decodeStart;
    /**解析结束位*/
    @TableField(value = "decode_end")
    private Double decodeEnd;
    /**GROUP_NAME*/
    @TableField(value = "group_name")
    private String groupName;
    /**DEVICE_VARIABLE_ID*/
    @TableField(value = "eqp_variable_id")
    private String eqpVariableId;
    /**1、SV2、EC3、RECIPE PARA*/
    @TableField(value = "eqp_variable_type")
    private String eqpVariableType;
    /**描述*/
    @TableField(value = "para_desc")
    private String paraDesc;
    /**排序号*/
    @TableField(value = "sort_no")
    @Excel(name = "排序号", orderNum = "2", width = 20)
    private Double sortNo;
    /**更新次数*/
    @TableField(value = "update_cnt")
    private Double updateCnt;

}
