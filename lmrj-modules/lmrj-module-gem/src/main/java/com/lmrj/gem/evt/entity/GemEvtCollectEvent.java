package com.lmrj.gem.evt.entity;

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
@TableName("gem_evt_collect_event")
public class GemEvtCollectEvent extends BaseDataEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 设备号
     */
    @TableField("EQP_ID")
    private String eqpId;

    /**
     * 事件ID
     */
    @TableField("EVENT_ID")
    private String eventId;

    /**
     * 执行结果标记
     */
    @TableField("ACKC")
    private String ackc;

    /**
     * 事件描述
     */
    @TableField("EVENT_DESC")
    private String eventDesc;

}
