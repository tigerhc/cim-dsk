package com.lmrj.gem.ams.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author zwj
 * @since 2019-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gem_ams_alarm_spooling")
public class GemAmsAlarmSpooling extends BaseDataEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 设备号
     */
    @TableField("EQP_ID")
    private String eqpId;

    /**
     * 警报ID
     */
    @TableField("ALARM_ID")
    private String alarmId;

    /**
     * 警报名称
     */
    @TableField("ALARM_NAME")
    private String alarmName;

    /**
     * 警报开关 1:set 0:cleared
     */
    @TableField("ALARM_SWITCH")
    private String alarmSwitch;

}
