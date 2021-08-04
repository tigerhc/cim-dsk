package com.lmrj.mes.track.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
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

@TableName("mes_lot_material_info")
@SuppressWarnings("serial")
@Data
public class MesLotMaterialInfo extends BaseDataEntity {
    @TableField(value = "id")
    private String id;
    /**主表物料Id*/
    @TableField(value = "material_id")
    private String materialId;
    /**物料名称*/
    @TableField(value = "material_name")
    private String materialName;
    /**作业指示书批量*/
    @Excel(name = "批量", orderNum = "1")
    @TableField(value = "lot_no")
    private String lotNo;
    @Excel(name = "物料参数")
    @TableField(value = "params")
    private String params;
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
}
