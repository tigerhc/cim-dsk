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
 * @title: edc_dsk_log_production实体
 * @description: edc_dsk_log_production实体
 * @author: 张伟江
 * @date: 2020-04-14 10:10:00
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_dsk_log_inspection")
@SuppressWarnings("serial")
@Data
public class EdcDskLogInspection extends AbstractEntity {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**作业开始时间*/
    @TableField(value = "start_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**作业记录文件名*/
    @TableField(value = "filename")
    private String filename;
    /**批次号*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**批次号*/
    @TableField(value = "chip_id")
    private String chipId;
    /**批次号*/
    @TableField(value = "params")
    private String params;
    /**校验标记*/
    @TableField(value = "check_type")
    private String checkType;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy; // 创建者
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date createDate; // 创建日期
}
