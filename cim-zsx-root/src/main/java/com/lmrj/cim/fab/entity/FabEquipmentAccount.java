package com.lmrj.cim.fab.entity;

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

@TableName("fab_equipment_account")
@SuppressWarnings("serial")
@Data
public class FabEquipmentAccount extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.UUID)
    protected String id;

    /**
     * 设备号
     */
    @TableField(value = "eqp_id")
    @Excel(name = "设备id", orderNum = "1")
    private String eqpId;

    @TableField(value = "eqp_name")
    @Excel(name = "设备名称", orderNum = "2")
    private String eqpName;

    @TableField(value = "eqp_no")
    private String eqpNo;

    @Excel(name = "项目名称", orderNum = "6")
    @TableField(value = "project_name")
    private String projectName;

    @Excel(name = "测试参数", orderNum = "7")
    @TableField(value = "parameter")
    private String parameter;

    @Excel(name = "片数", orderNum = "8")
    @TableField(value = "number")
    private Integer number;

    @Excel(name = "批次号", orderNum = "9")
    @TableField(value = "lot_no")
    private String lotNo;

    @Excel(name = "日期", orderNum = "3")
    @TableField(value = "date", fill = FieldFill.UPDATE)
    @JSONField(format="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    protected Date date;

    @Excel(name = "开始时间", orderNum = "4")
    @TableField(value = "start_time", fill = FieldFill.UPDATE)
    @JSONField(format="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss")
    protected Date startTime;

    @Excel(name = "结束时间", orderNum = "5")
    @TableField(value = "end_time", fill = FieldFill.UPDATE)
    @JSONField(format="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss")
    protected Date endTime;

    @Excel(name = "操作人", orderNum = "10")
    @TableField(value = "operator", fill = FieldFill.UPDATE)
    protected String person;
}
