package com.lmrj.fab.eqp.entity;

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
 * @author wdj
 * @date 2021-06-01 16:40
 */
@TableName("fab_equipment_bind")
@SuppressWarnings("serial")
@Data
public class FabEquipmentBind extends BaseDataEntity {
    /**
     * ID
     */
    private String id;
    /**
     * 设备号
     */
    @TableField(value = "eqp_id")
    @Excel(name = "设备号", orderNum = "1")
    private String eqpId;
    @TableField(value = "parent_eqp_id")
    @Excel(name = "'父级设备号", orderNum = "1")
    private String parentEqpId;
    @TableField(value = "template_id")
    @Excel(name = "模板号", orderNum = "1")
    private String templateId;
    @TableField(exist = false)
    @Excel(name = "模板名称", orderNum = "1")
    private String templateName;

    @TableField(exist = false)
    @Excel(name = "子设备类型", orderNum = "1")
    private String subClassCode;

    @TableField(exist = false)
    private String eqpName;
    @TableField(value = "template_body_id")
    @Excel(name = "设备模板主体ID", orderNum = "1")
    private String templateBodyId;
    @TableField(value = "office_id")
    @Excel(name = "部门ID", orderNum = "1")
    private String officeId;
    @TableField(value = "remarks")
    @Excel(name = "备注", orderNum = "1")
    private String remarks;
    @TableField(value = "create_by")
    @Excel(name = "创建人ID", orderNum = "1")
    private String createBy;



    @TableField(value = "update_by")
    @Excel(name = "修改人ID", orderNum = "1")
    private String updateBy;

    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date createDate;

    @TableField(value = "update_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date updateDate;

}
