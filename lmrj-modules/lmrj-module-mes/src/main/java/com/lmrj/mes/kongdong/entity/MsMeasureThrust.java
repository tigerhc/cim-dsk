package com.lmrj.mes.kongdong.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.record.entity
 * @title: ms_measure_kongdong实体
 * @description: ms_measure_kongdong实体
 * @author: 高雪君
 * @date: 2020-11-23 10:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("ms_measure_thrust")
@SuppressWarnings("serial")
@Data
public class MsMeasureThrust extends BaseDataEntity {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**批号*/
    @TableField(value = "lot_no")
    @Excel(name = "批号", orderNum = "2", width = 20)
    private String lotNo;
    /**机种*/
    @TableField(value = "production_no")
    @Excel(name = "品番", orderNum = "3", width = 40)
    private String productionNo;
    /**机种*/
    @TableField(value = "production_name")
    @Excel(name = "品番名", orderNum = "4", width = 40)
    private String productionName;
    /**推力*/
    @TableField(value = "thrust")
    @Excel(name = "推力", orderNum = "5", width = 20)
    private String thrust;
    /**拉力*/
    @TableField(value = "pull")
    @Excel(name = "拉力", orderNum = "5", width = 20)
    private String pull;
    /**线别*/
    @TableField(value = "line_no")
    @Excel(name = "线别", orderNum = "6", width = 20)
    private String lineNo;
    @TableField(value = "eqp_id")
    @Excel(name = "设备名称", orderNum = "7", width = 20)
    private String eqpId;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy; // 创建者
    /**发生时刻*/
    @Excel(name = "创建日期", orderNum = "99", width = 30,format="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;
    @TableField(value = "create_by", fill = FieldFill.UPDATE)
    protected String updateBy; // 创建者
    /**修改时刻*/
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date updateDate; // 更新日期
}
