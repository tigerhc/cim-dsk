package com.lmrj.aps.plan.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

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
    private String productionNo;
    /**品名(产品名称)*/
    @TableField(value = "production_name")
    private String productionName;
    /**投入日期*/
    @TableField(value = "plan_date")
    private String planDate;
    /**批次数量*/
    @TableField(value = "lot_qty")
    private Integer lotQty;
    /**投入数*/
    @TableField(value = "plan_qty")
    private Integer planQty;

}
