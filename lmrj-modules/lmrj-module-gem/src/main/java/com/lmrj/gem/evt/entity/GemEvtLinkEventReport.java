package com.lmrj.gem.evt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 事件 event和report绑定
 * </p>
 *
 * @author zwj
 * @since 2019-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gem_evt_link_event_report")
public class GemEvtLinkEventReport extends BaseDataEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 事件ID
     */
    @TableField("EVENT_ID")
    private String eventId;

    /**
     * REPORT ID
     */
    @TableField("REPORT_ID")
    private String reportId;

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

    /** 排序 */
    @TableField(value = "SORT")
    private Integer sort;

}
