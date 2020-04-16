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
 * @title: edc_ams_rpt_define_act实体
 * @description: edc_ams_rpt_define_act实体
 * @author: zhangweijiang
 * @date: 2020-02-15 02:46:34
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_ams_rpt_define_act")
@SuppressWarnings("serial")
@Data
public class EdcAmsRptDefineAct extends BaseDataEntity {

    /**REPEAT ALARM ID*/
    @TableField(value = "rpt_alarm_id")
    private String rptAlarmId;
    /**动作CODE*/
    @TableField(value = "action_code")
    private String actionCode;
    /**有效标记*/
    @TableField(value = "active_flag")
    private String activeFlag;

}
