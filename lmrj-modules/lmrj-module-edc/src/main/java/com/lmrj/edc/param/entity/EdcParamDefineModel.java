package com.lmrj.edc.param.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.param.entity
 * @title: edc_param_define_model实体
 * @description: edc_param_define_model实体
 * @author: zhangweijiang
 * @date: 2019-06-14 23:14:29
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_param_define_model")
@SuppressWarnings("serial")
@Data
public class EdcParamDefineModel extends BaseDataEntity {

    /**参数制程名称*/
    @TableField(value = "param_define_id")
    private String paramDefineId;
    /**参数CODE, 格式: 厂商缩写+model字母+1000001 ~ 1999999,各型号不可重复*/
    @TableField(value = "param_code")
    private String paramCode;
    /**参数名称*/
    @TableField(value = "param_name")
    private String paramName;
    /**参数简称*/
    @TableField(value = "param_shortname")
    private String paramShortname;
    /**计量单位*/
    @TableField(value = "param_unit")
    private String paramUnit;
    /**设备型号*/
    @TableField(value = "model_id")
    private String modelId;
    /**设备型号名称*/
    @TableField(value = "model_name")
    private String modelName;
    /**通讯协议 格式:A,B*/
    @TableField(value = "protocol")
    private String protocol;
    /**子通讯协议 格式:SV EC*/
    @TableField(value = "protocol_sub")
    private String protocolSub;
    /**通讯协议值*/
    @TableField(value = "protocol_value")
    private String protocolValue;
    /**设定值*/
    @TableField(value = "set_value")
    private String setValue;
    /**默认值*/
    @TableField(value = "def_value")
    private String defValue;
    /**最小值*/
    @TableField(value = "min_value")
    private String minValue;
    /**最大值*/
    @TableField(value = "max_value")
    private String maxValue;
    /**显示标记 N、不在初次打开页面显示Y、在初次打开页面显示*/
    @TableField(value = "show_flag")
    private String showFlag;
    /**监控标记 N、不监控Y、监控*/
    @TableField(value = "monitor_flag")
    private String monitorFlag;
    /**值需转换标志*/
    @TableField(value = "val_mapping_flag")
    private String valMappingFlag;
    /**子设备号*/
    @TableField(value = "sub_eqp_id")
    private String subEqpId;

}
