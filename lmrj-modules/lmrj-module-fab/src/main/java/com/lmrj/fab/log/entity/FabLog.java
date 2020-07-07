package com.lmrj.fab.log.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

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
public class FabLog extends BaseDataEntity {

    /**事件id*/
    @TableField(value = "event_id")
    private String eventId;
    /**事件名*/
    @TableField(value = "event_name")
    private String eventName;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**设备名称*/
    @TableField(value = "eqp_name")
    private String eqpName;
    /**lot_id*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**程序名称*/
    @TableField(value = "recipe_code")
    private String recipeCode;
    /**y 成功 n 失败*/
    @TableField(value = "event_status")
    private String eventStatus;
    /**事件描述*/
    @TableField(value = "event_desc")
    private String eventDesc;
    /**存储发起mq传输的业务主表id，以便日志连续统一*/
    @TableField(value = "origin_id")
    private String originId;
	
}