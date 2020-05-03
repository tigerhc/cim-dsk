package com.lmrj.mes.track.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.entity
 * @title: mes_lot_track_log实体
 * @description: mes_lot_track_log实体
 * @author: 张伟江
 * @date: 2020-04-28 14:03:29
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("mes_lot_track_log")
@SuppressWarnings("serial")
@Data
public class MesLotTrackLog extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.UUID)
    protected String id;

    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**作业指示书批量*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**作业指示书机种*/
    @TableField(value = "production_no")
    private String productionNo;
    /**作业指示书序列号*/
    @TableField(value = "order_no")
    private String orderNo;
    /**事件 CODE*/
    @TableField(value = "event_code")
    private String eventCode;
    /**批次产量*/
    @TableField(value = "lot_yield")
    private Integer lotYield;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**结束时间*/
    @TableField(value = "end_time")
    private Date endTime;
    @TableField(value = "remarks")
    protected String remarks; // 备注
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy; // 创建者
    @TableField(exist = false)
    protected String createByName; // 创建者姓名
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date createDate; // 创建日期

}
