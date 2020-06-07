package com.lmrj.ms.eqp.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.eqp.entity
 * @title: ms_eqp_ablility实体
 * @description: ms_eqp_ablility实体
 * @author: 张伟江
 * @date: 2020-06-06 18:20:21
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("ms_eqp_ablility")
@SuppressWarnings("serial")
@Data
public class MsEqpAblility extends BaseDataEntity {

    /**设备型号*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备型号名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**参数code*/
    @TableField(value = "para_code")
    private String paraCode;
    /**参数名*/
    @TableField(value = "para_name")
    private String paraName;
    /**x*/
    @TableField(value = "location_x")
    private Integer locationX;
    /**y*/
    @TableField(value = "location_y")
    private Integer locationY;
    /**h*/
    @TableField(value = "location_h")
    private Integer locationH;
    /**w*/
    @TableField(value = "location_w")
    private Integer locationW;
    /**转换规则*/
    @TableField(value = "converter")
    private String converter;
    /**附带配置*/
    @TableField(value = "config")
    private String config;

}
