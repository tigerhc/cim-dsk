package com.lmrj.fab.eqp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;

/**fab_model_template_body
 * @author wdj
 * @date 2021-06-01 8:41
 */
@TableName("fab_model_template_body")
@SuppressWarnings("serial")
@Data
public class FabModelTemplateBody extends AbstractEntity {
    /**ID*/
    @TableField(value = "id")
    private String id;
    /**模板ID*/
    @TableField(value = "template_id")
    private String templateId;
    /**模板name*/
    @TableField(value = "template_name")
    private String templateName;
    /**设备厂家*/
    @TableField(value = "manufacturer_name")
    private String manufacturerName;
    /**设备大类*/
    @TableField(value = "parent_type")
    private String parentType;
    /**设备子类*/
    @TableField(value = "type")
    private String type;
    /**设备类型*/
    @TableField(value = "sub_class_code")
    private String subClassCode;
    /**部门ID*/
    @TableField(value = "office_id")
    private String officeId;
    /**有效标志*/
    @TableField(value = "active_flag")
    private String activeFlag;
    /**备注*/
    @TableField(value = "remarks")
    private String remarks;
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
    private String num;
    @TableField(value = "del_flag")
    private String delFlag;
}
