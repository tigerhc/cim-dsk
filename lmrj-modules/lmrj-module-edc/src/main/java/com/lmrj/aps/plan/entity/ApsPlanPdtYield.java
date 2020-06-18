package com.lmrj.aps.plan.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.google.common.collect.Lists;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.aps.plan.entity
 * @title: aps_plan_pdt_yield实体
 * @description: aps_plan_pdt_yield实体
 * @author: 张伟江
 * @date: 2020-05-17 21:00:52
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("aps_plan_pdt_yield")
@SuppressWarnings("serial")
@Data
public class ApsPlanPdtYield extends BaseDataEntity {

    /**品番(产品品号)*/
    @TableField(value = "production_no")
    @Excel(name = "品番", orderNum = "1", width = 20)
    private String productionNo;
    /**品名(产品名称)*/
    @TableField(value = "production_name")
    @Excel(name = "品名", orderNum = "1", width = 40)
    private String productionName;
    /**投入日期*/
    @TableField(value = "plan_date")
    @Excel(name = "投入日期", orderNum = "1", width = 20)
    private String planDate;
    /**批次数量*/
    @TableField(value = "lot_qty")
    @Excel(name = "批次数量", orderNum = "1", width = 20)
    private Integer lotQty;
    /**投入数*/
    @TableField(value = "plan_qty")
    @Excel(name = "投入数", orderNum = "1", width = 20)
    private Integer planQty;
    @TableField(value = "sort_no")
    private Integer sortNo;
    @TableField(exist = false)
    private List<ApsPlanPdtYieldDetail> apsPlanPdtYieldDetailList = Lists.newArrayList();

}
