package com.lmrj.gem.evt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采集事件定义
 * </p>
 *
 * @author zwj
 * @since 2019-04-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gem_evt_collect_event_define")
public class GemEvtCollectEventDefine extends BaseDataEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 事件ID
     */
    @TableField("EVENT_ID")
    private String eventId;

    /**
     * 事件 CODE
     */
    @TableField("EVENT_CODE")
    private String eventCode;

    /**
     * 事件名称
     */
    @TableField("EVENT_NAME")
    private String eventName;

    /**
     * 事件种类
     */
    @TableField("EVENT_CATEGORY")
    private Integer eventCategory;

    /**
     * 事件描述
     */
    @TableField("EVENT_DESC")
    private String eventDesc;

    /**
     * 事件类型
     */
    @TableField("EVENT_TYPE")
    private String eventType;

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
