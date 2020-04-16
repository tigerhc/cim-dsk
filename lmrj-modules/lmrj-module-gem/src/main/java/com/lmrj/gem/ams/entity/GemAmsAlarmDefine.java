package com.lmrj.gem.ams.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报警定义
 * </p>
 *
 * @author zwj
 * @since 2019-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gem_ams_alarm_define")
public class GemAmsAlarmDefine extends BaseDataEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 警报ID
     */
    @TableField("ALARM_ID")
    private String alarmId;

    /**
     * 警报 CODE
     */
    @TableField("ALARM_CODE")
    private String alarmCode;

    /**
     * 警报名称
     */
    @TableField("ALARM_NAME")
    private String alarmName;

    /**
     * 警报种类
     */
    @TableField("ALARM_CATEGORY")
    private Integer alarmCategory;

    /**
     * 警报描述
     */
    @TableField("ALARM_DESC")
    private String alarmDesc;

    /**
     * 警报类型
     */
    @TableField("ALARM_TYPE")
    private String alarmType;

    /**
     * 监控标记 N、不监控Y、监控
     */
    @TableField("MONITOR_FLAG")
    private String monitorFlag;

    /**
     * 子设备号
     */
    @TableField("SUB_EQP_ID")
    private String subEqpId;

    /**
     * 设备型号
     */
    @TableField("EQP_MODEL_ID")
    private String eqpModelId;

    /**
     * 设备型号名称
     */
    @TableField("EQP_MODEL_NAME")
    private String eqpModelName;

}
