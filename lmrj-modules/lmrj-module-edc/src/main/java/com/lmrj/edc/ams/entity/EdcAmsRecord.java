package com.lmrj.edc.ams.entity;

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
 * @package com.lmrj.edc.ams.entity
 * @title: edc_ams_record实体
 * @description: edc_ams_record实体
 * @author: 张伟江
 * @date: 2019-06-14 15:51:23
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_ams_record")
@SuppressWarnings("serial")
@Data
public class EdcAmsRecord extends AbstractEntity {

    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @Excel(name = "设备id", orderNum = "1")
    @TableField(value = "eqp_id")
    private String eqpId;
    /**警报ID*/
    @Excel(name = "警报CODE", orderNum = "2")
    @TableField(value = "alarm_code")
    private String alarmCode;
    /**警报名称*/
    @Excel(name = "警报名称", orderNum = "3")
    @TableField(value = "alarm_name")
    private String alarmName;
    @Excel(name = "报警详细", orderNum = "3")
    @TableField(value = "alarm_detail")
    private String alarmDetail;
    /**警报开关 1:set 0:cleared*/
    @Excel(name = "警报开关", orderNum = "4")
    @TableField(value = "alarm_switch")
    private String alarmSwitch;
    /**开始日期*/
    @Excel(name = "开始时间", orderNum = "5")
    @TableField(value = "start_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date startDate;
    /**结束时间*/
    @Excel(name = "结束时间", orderNum = "6")
    @TableField(value = "end_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date endDate;

    /**警报开关 1:set 0:cleared*/
    @Excel(name = "批次", orderNum = "8")
    @TableField(value = "lot_no")
    private String lotNo;

    /**警报开关 1:set 0:cleared*/
    @Excel(name = "批次产量", orderNum = "9")
    @TableField(value = "lot_yield")
    private Integer lotYield;

    @Excel(name = "创建时间", orderNum = "7")
    @TableField(value = "create_date", fill = FieldFill.INSERT, update = "now()")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date createDate; // 创建日期

}
