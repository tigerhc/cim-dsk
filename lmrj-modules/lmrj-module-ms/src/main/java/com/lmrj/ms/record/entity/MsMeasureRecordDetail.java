package com.lmrj.ms.record.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.record.entity
 * @title: ms_measure_record_detail实体
 * @description: ms_measure_record_detail实体
 * @author: 张伟江
 * @date: 2020-06-06 20:43:36
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("ms_measure_record_detail")
@SuppressWarnings("serial")
@Data
public class MsMeasureRecordDetail extends BaseDataEntity {

    /**量测记录ID*/
    @TableField(value = "ms_record_id")
    private String msRecordId;
    /**采样序号*/
    @TableField(value = "sample_no")
    @Excel(name = "采样序号", orderNum = "10", width = 20)
    private Integer sampleNo;
    /**量测项目编码*/
    @TableField(value = "item_code")
    private String itemCode;
    /**量测项目名称*/
    @TableField(value = "item_name")
    @Excel(name = "量测项目名称", orderNum = "10", width = 40)
    private String itemName;
    /**量测项目值*/
    @TableField(value = "item_value")
    @Excel(name = "量测项目值", orderNum = "10", width = 40)
    private String itemValue;
    /**判定结果,若超出范围,则N*/
    @TableField(value = "item_result")
    private String itemResult;
    /**最小值*/
    @TableField(value = "limit_min")
    private String limitMin;
    /**最大值*/
    @TableField(value = "limit_max")
    private String limitMax;
    /**界限类型0、范围值（需转换成数值，并进行比较）1、固定值该类型控制保存时的值检查，避免空项提交。*/
    @TableField(value = "limit_type")
    private String limitType;
    /**展现形式,input, grid*/
    @TableField(value = "show_type")
    private String showType;
    /**行名*/
    @TableField(value = "row_name")
    private String rowName;
    /**排序号*/
    @TableField(value = "sort_no")
    private Integer sortNo;
}
