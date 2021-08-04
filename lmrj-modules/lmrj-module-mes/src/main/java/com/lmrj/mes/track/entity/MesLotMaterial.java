package com.lmrj.mes.track.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

@TableName("mes_lot_material")
@SuppressWarnings("serial")
@Data
public class MesLotMaterial extends BaseDataEntity {
    @TableField(value = "id")
    private String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    @Excel(name = "设备id", orderNum = "2")
    private String eqpId;
    /**作业指示书批量*/
    @Excel(name = "批量", orderNum = "1")
    @TableField(value = "lot_no")
    private String lotNo;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy; // 创建者
    @TableField(exist = false)
    protected String createByName; // 创建者姓名
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date createDate; // 创建日期
    @TableField(value = "update_by")
    private String updateBy;
    /** 更新时间 */
    @TableField(value = "update_date")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    @TableField(exist = false)
    private List<MesLotMaterialInfo> mesLotMaterialInFoList = Lists.newArrayList();
}
