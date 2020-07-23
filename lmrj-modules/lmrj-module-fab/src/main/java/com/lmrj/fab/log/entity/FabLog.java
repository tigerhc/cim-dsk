package com.lmrj.fab.log.entity;

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
 * @package com.lmrj.fab.log.entity
 * @title: fab_log实体
 * @description: fab_log实体
 * @author: 张伟江
 * @date: 2020-07-07 16:09:16
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("fab_log")
@SuppressWarnings("serial")
@Data
public class FabLog extends AbstractEntity {

    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**事件id*/
    @TableField(value = "event_id")
    private String eventId;
    /**事件名*/
    @Excel(name = "事件", orderNum = "2", width = 20)
    @TableField(value = "event_name")
    private String eventName;
    /**设备号*/
    @Excel(name = "设备号", orderNum = "1", width = 20)
    @TableField(value = "eqp_id")
    private String eqpId;
    /**设备名称*/
    @TableField(value = "eqp_name")
    private String eqpName;
    /**lot_id*/
    @TableField(value = "lot_no")
    @Excel(name = "批次", orderNum = "3", width = 20)
    private String lotNo;
    /**程序名称*/
    @TableField(value = "recipe_code")
    @Excel(name = "配方CODE", orderNum = "4", width = 20)
    private String recipeCode;
    /**y 成功 n 失败*/
    @TableField(value = "event_status")
    @Excel(name = "事件状态", orderNum = "5", width = 20)
    private String eventStatus;
    /**事件描述*/
    @TableField(value = "event_desc")
    @Excel(name = "时间描述", orderNum = "6", width = 20)
    private String eventDesc;
    /**存储发起mq传输的业务主表id，以便日志连续统一*/
    @TableField(value = "origin_id")
    private String originId;
    @TableField(value = "ip")
    @Excel(name = "ip", orderNum = "7", width = 20)
    private String ip;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @Excel(name = "创建者", orderNum = "8", width = 20)
    protected String createBy; // 创建者
    @TableField(exist = false)
    protected String createByName; // 创建者姓名
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @Excel(name = "创建时间", orderNum = "9", width = 20)
    protected Date createDate; // 创建日期

}
