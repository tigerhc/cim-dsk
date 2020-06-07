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
 * @title: ms_eqp_param实体
 * @description: ms_eqp_param实体
 * @author: 张伟江
 * @date: 2020-06-06 18:19:46
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("ms_eqp_param")
@SuppressWarnings("serial")
@Data
public class MsEqpParam extends BaseDataEntity {

    /**设备型号*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备型号名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**OCR,EXCEL,TXT,SERIAL*/
    @TableField(value = "ms_get_type")
    private String msGetType;
    /**参数1*/
    @TableField(value = "param1")
    private String param1;
    /**参数2*/
    @TableField(value = "param2")
    private String param2;
    /**参数3*/
    @TableField(value = "param3")
    private String param3;
    /**参数4*/
    @TableField(value = "param4")
    private String param4;
    /**参数5*/
    @TableField(value = "param5")
    private String param5;

}
