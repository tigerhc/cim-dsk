package com.lmrj.fab.eqp.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * @author wdj
 * @date 2021-05-31 22:11
 */
@TableName("fab_sensor")
@SuppressWarnings("serial")
@Data
public class FabSensor  extends BaseDataEntity {
    /**
     * ID
     */
    private String id;
    /**
     * 设备号
     */
    @TableField(value = "sor_id")
    @Excel(name = "传感器id", orderNum = "1")
    private String sorId;
    @TableField(value = "line_yield_flag")
    @Excel(name = "线别产量标记", orderNum = "1")
    private String lineYieldFlag;
    @TableField(value = "sor_name")
    @Excel(name = "传感器Name", orderNum = "1")
    private String sorName;
    @Excel(name = "传感器no", orderNum = "1")
    @TableField(value = "sor_no")
    private String sorNo;
    /**
     * 部门ID
     */
    @TableField(value = "office_id")
    private String officeId;
    @TableField(exist = false)
    @Excel(name = "部门名称", orderNum = "3")
    private String officeName;
    @TableField(exist = false)
    private String officeIds;
    /**
     * 工序ID
     */
    @TableField(value = "step_id")
    @Excel(name = "工序id", orderNum = "4")
    private String stepId;
    /**
     * 工序CODE
     */
    @Excel(name = "工序编码", orderNum = "5")
    @TableField(value = "step_code")
    private String stepCode;
    @TableField(value = "step_yield_flag")
    private String stepYieldFlag;
    /**
     * 有效标志
     */
    @Excel(name = "有效标记", orderNum = "6")
    @TableField(value = "active_flag")
    private String activeFlag;
    /**
     * BC号
     */
    @Excel(name = "BC号", orderNum = "7")
    @TableField(value = "bc_code")
    private String bcCode;
    /**
     * 机台IP地址
     */
    @Excel(name = "ip", orderNum = "8")
    @TableField(value = "ip")
    private String ip;
    /**
     * 端口号
     */
    @Excel(name = "端口号", orderNum = "9")
    @TableField(value = "port")
    private String port;
    /**
     * DEVICE ID
     */
    @Excel(name = "设备id", orderNum = "10")
    @TableField(value = "device_id")
    private String deviceId;
    /**
     * 设备型号
     */
    @TableField(value = "model_id")
    private String modelId;
    /**
     * 设备型号名称
     */
    @Excel(name = "设备型号名称", orderNum = "12")
    @TableField(value = "model_name")
    private String modelName;
    /**
     * 协议名称
     */
    @Excel(name = "协议名称", orderNum = "13")
    @TableField(value = "protocol_name")
    private String protocolName;
    /**
     * 厂别
     */
    @Excel(name = "厂别", orderNum = "14")
    @TableField(value = "fab")
    private String fab;
    /**
     * 线别
     */
    @Excel(name = "线别", orderNum = "15")
    @TableField(value = "line_no")
    private String lineNo;
    /**
     * 线别
     */
    @Excel(name = "子线别", orderNum = "15")
    @TableField(value = "sub_line_no")
    private String subLineNo;
    /**
     * 客户端
     */
    @Excel(name = "站别", orderNum = "19")
    @TableField(value = "station_code")
    private String stationCode;

    @Excel(name = "参数", orderNum = "15")
    @TableField(value = "eqp_param")
    private String eqpParam;
    /**
     * 位置号
     */
    @Excel(name = "位置号", orderNum = "16")
    @TableField(value = "location")
    private String location;
    /**
     * 节拍 毫秒
     */
    @Excel(name = "节拍", orderNum = "17")
    @TableField(value = "take_time")
    private int takeTime;
    /**
     * 客户端
     */
    @Excel(name = "客户端", orderNum = "18")
    @TableField(value = "client_flag")
    private String clientFlag;

    @TableField(value = "sort_no")
    private Integer sortNo;


}
