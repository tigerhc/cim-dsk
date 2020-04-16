package com.lmrj.edc.ams.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.ams.entity
 * @title: edc_ams_define_i18n实体
 * @description: edc_ams_define_i18n实体
 * @author: zhangweijiang
 * @date: 2020-02-15 02:42:19
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_ams_define_i18n")
@SuppressWarnings("serial")
@Data
public class EdcAmsDefineI18n extends BaseDataEntity {

    /**警报ID*/
    @TableField(value = "alarm_id")
    private String alarmId;
    /**警报CODE*/
    @TableField(value = "alarm_code")
    private String alarmCode;
    /**警报名称*/
    @TableField(value = "alarm_name")
    private String alarmName;
    /**语言*/
    @TableField(value = "alarm_language")
    private String alarmLanguage;

}
