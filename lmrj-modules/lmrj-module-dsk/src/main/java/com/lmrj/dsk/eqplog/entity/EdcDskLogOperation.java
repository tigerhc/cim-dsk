package com.lmrj.dsk.eqplog.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "设备号", orderNum = "1", width = 20)
    private String eqpId;
    /**设备类型*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备类型名称*/
    @TableField(value = "eqp_model_name")
    @Excel(name = "设备型号", orderNum = "2", width = 20)
    private String eqpModelName;
    /**设备名*/
    @TableField(value = "eqp_no")
    private String eqpNo;
    /**配方CODE*/
    @TableField(value = "recipe_code")
    @Excel(name = "配方名", orderNum = "7", width = 20)
    private String recipeCode;
    /**日投入数*/
    @TableField(value = "day_yield")
    @Excel(name = "日产量", orderNum = "8", width = 10)
    private Integer dayYield;
    /**批量投入数*/
    @TableField(value = "lot_yield")
    @Excel(name = "批次产量", orderNum = "9", width = 10)
    private Integer lotYield;
    @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "start_time")
    @Excel(name = "发生时间", orderNum = "10", width = 30, format="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**事件ID 0:停止中　, 1:自动运转中 2: 制品等待/材料等待 3~:警报代码*/
    @TableField(value = "event_id")
    @Excel(name = "事件ID", orderNum = "3", width = 10)
    private String eventId;
    @TableField(value = "alarm_code")
    @Excel(name = "报警ID", orderNum = "4", width = 15)
    private String alarmCode;
    /**事件的内容*/
    @TableField(value = "event_name")
    @Excel(name = "事件内容", orderNum = "5", width = 20)
    private String eventName;
    /**事件详细信息*/
    @TableField(value = "event_detail")
    @Excel(name = "事件详细", orderNum = "6", width = 50)
    private String eventDetail;
    /**事件参数*/
    @Excel(name = "事件参数", orderNum = "11", width = 50)
    @TableField(value = "event_params")
    private String eventParams;
    /**作业指示书订单*/
    @Excel(name = "订单号", orderNum = "20", width = 20)
    @TableField(value = "order_no")
    private String orderNo;
    /**作业指示书批量*/
    @Excel(name = "批号", orderNum = "21", width = 20)
    @TableField(value = "lot_no")
    private String lotNo;
    /**作业指示书品番*/
    @Excel(name = "品番", orderNum = "22", width = 20)
    @TableField(value = "production_no")
    private String productionNo;
    /**持续时间*/
    @TableField(value = "duration")
    private Double duration;


    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy; // 创建者
    /**发生时刻*/
    @Excel(name = "创建日期", orderNum = "99", width = 30,format="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

}
