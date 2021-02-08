package com.lmrj.mes.track.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.entity
 * @title: mes_lot_track实体
 * @description: mes_lot_track实体
 * @author: 张伟江
 * @date: 2020-04-28 14:03:17
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("mes_lot_track")
@SuppressWarnings("serial")
@Data
public class MesLotTrack extends BaseDataEntity {

    /**设备号*/
    @TableField(value = "eqp_id")
    @Excel(name = "设备id", orderNum = "2")
    private String eqpId;
    /**作业指示书批量*/
    @Excel(name = "批量", orderNum = "1")
    @TableField(value = "lot_no")
    private String lotNo;
    /**作业指示书机种*/
    @Excel(name = "品名", orderNum = "3")
    @TableField(value = "production_name")
    private String productionName;
    @Excel(name = "品番", orderNum = "3")
    @TableField(value = "production_no")
    private String productionNo;
    /**作业指示书序列号*/
    @TableField(value = "order_no")
    @Excel(name = "订单", orderNum = "4")
    private String orderNo;
    /**批次产量*/
    @Excel(name = "产量", orderNum = "5")
    @TableField(value = "lot_yield")
    private Integer lotYield;
    @Excel(name = "设备产量", orderNum = "5")
    @TableField(value = "lot_yield_eqp")
    private Integer lotYieldEqp;
    /**批次投入*/
    @Excel(name = "产量", orderNum = "5")
    @TableField(value = "lot_input")
    private Integer lotInput;
    /**开始时间*/
    @Excel(name = "开始时间", orderNum = "6",format="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "start_time")
    private Date startTime;
    /**结束时间*/
    @Excel(name = "结束时间", orderNum = "7",format="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "end_time")
    private Date endTime;

}
