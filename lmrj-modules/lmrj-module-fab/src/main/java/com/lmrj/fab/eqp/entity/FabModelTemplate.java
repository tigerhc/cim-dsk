package com.lmrj.fab.eqp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wdj
 * @date 2021-06-01 8:41
 */
@TableName("fab_model_template")
@SuppressWarnings("serial")
@Data
public class FabModelTemplate extends AbstractEntity {
    /**ID*/
    @TableField(value = "id")
    private String id;
    /**模板名称*/
    @TableField(value = "name")
    private String name;
    /**设备厂家*/
    @TableField(value = "manufacturer_name")
    private String manufacturerName;
    /**设备类型*/
    @TableField(value = "class_code")
    private String classCode;
    /**部门ID*/
    @TableField(value = "office_id")
    private String officeId;
    /**有效标志*/
    @TableField(value = "active_flag")
    private String activeFlag;
    /**备注*/
    @TableField(value = "remarks")
    private String remarks;
    /**备注*/
    @TableField(value = "del_flag")
    private String delFlag;
    /**创建人ID*/
    @TableField(value = "create_by")
    private String createBy;
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date createDate;
    /**修改人ID*/
    @TableField(value = "update_by")
    private String updateBy;
    @TableField(value = "update_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date updateDate;
    @TableField(exist = false)
    private List<FabModelTemplateBody> fabModelTemplateBodyList;

}
