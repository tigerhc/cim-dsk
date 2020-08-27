package com.lmrj.map.tray.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;

import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.entity
 * @title: map_tray_chip_log实体
 * @description: map_tray_chip_log实体
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("map_tray_chip_log")
@SuppressWarnings("serial")
@Data
public class MapTrayChipLog extends BaseDataEntity {

    /**执行开始时间*/
    @TableField(value = "begin_time")
    private Date beginTime;
    /**执行结束时间*/
    @TableField(value = "end_time")
    private Date endTime;
    /**执行开始ID*/
    @TableField(value = "begin_no")
    private Long beginNo;
    /**执行结束ID*/
    @TableField(value = "end_no")
    private Long endNo;
    /**总数量*/
    @TableField(value = "proc_total")
    private Long procTotal;
    /**成功数量*/
    @TableField(value = "proc_suc")
    private Long procSuc;
    /**警告数量*/
    @TableField(value = "proc_warn")
    private Long procWarn;
    /**执行结果代码*/
    @TableField(value = "res_code")
    private String resCode;
    /**执行错误信息*/
    @TableField(value = "res_message")
    private String resMessage;

}
