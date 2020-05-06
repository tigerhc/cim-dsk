package com.lmrj.dsk.eqplog.entity;

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
 * @package com.lmrj.dsk.eqplog.entity
 * @title: edc_dsk_log_operation实体
 * @description: edc_dsk_log_operation实体
 * @author: 张伟江
 * @date: 2020-04-14 10:10:16
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_dsk_log_operation")
@SuppressWarnings("serial")
@Data
public class EdcDskLogOperation extends AbstractEntity {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**设备类型*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备类型名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**设备名*/
    @TableField(value = "eqp_no")
    private String eqpNo;
    /**配方CODE*/
    @TableField(value = "recipe_code")
    private String recipeCode;
    /**日投入数*/
    @TableField(value = "day_yield")
    private Integer dayYield;
    /**批量投入数*/
    @TableField(value = "lot_yield")
    private Integer lotYield;
    @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "start_time")
    private Date startTime;

    /**事件ID 0:停止中　, 1:自动运转中 2: 制品等待/材料等待 3~:警报代码*/
    @TableField(value = "event_id")
    private String eventId;
    @TableField(value = "alarm_code")
    private String alarmCode;
    /**事件的内容*/
    @TableField(value = "event_name")
    private String eventName;
    /**事件详细信息*/
    @TableField(value = "event_detail")
    private String eventDetail;
    /**事件参数*/
    @TableField(value = "event_params")
    private String eventParams;
    /**作业指示书订单*/
    @TableField(value = "order_no")
    private String orderNo;
    /**作业指示书批量*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**作业指示书品番*/
    @TableField(value = "production_no")
    private String productionNo;
    /**持续时间*/
    @TableField(value = "duration")
    private Double duration;


    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy; // 创建者
    /**发生时刻*/
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

}
