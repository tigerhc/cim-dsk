package com.lmrj.edc.evt.entity;

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
 * @package com.lmrj.edc.evt.entity
 * @title: edc_evt_record实体
 * @description: edc_evt_record实体
 * @author: 张伟江
 * @date: 2019-06-14 16:09:50
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_evt_record")
@SuppressWarnings("serial")
@Data
public class EdcEvtRecord extends AbstractEntity {

    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @Excel(name = "设备id", orderNum = "1")
    @TableField(value = "eqp_id")
    private String eqpId;
    /**事件ID*/
    @Excel(name = "事件id", orderNum = "2")
    @TableField(value = "event_id")
    private String eventId;
    @TableField(value = "event_desc")
    private String eventDesc;
    @TableField(value = "event_params")
    private String eventParams;
    /**开始日期*/
    @Excel(name = "开始日期", orderNum = "3")
    @TableField(value = "start_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date startDate;

    @Excel(name = "创建日期", orderNum = "4")
    @TableField(value = "create_date", fill = FieldFill.INSERT, update = "now()")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date createDate; // 创建日期

}
