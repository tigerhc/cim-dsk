package com.lmrj.edc.amsrpt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.amsrpt.entity
 * @title: edc_ams_rpt_define_act_email实体
 * @description: edc_ams_rpt_define_act_email实体
 * @author: zhangweijiang
 * @date: 2020-02-15 02:47:06
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_ams_rpt_define_act_email")
@SuppressWarnings("serial")
@Data
public class EdcAmsRptDefineActEmail extends BaseDataEntity {

    /**REPEAT ALARM ID*/
    @TableField(value = "rpt_alarm_id")
    private String rptAlarmId;
    /**用户ID*/
    @TableField(value = "user_id")
    private String userId;
    /**用户名称*/
    @TableField(value = "user_name")
    private String userName;
    /**用户邮箱*/
    @TableField(value = "user_email")
    private String userEmail;

}
