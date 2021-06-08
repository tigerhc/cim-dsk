package com.lmrj.fab.userRole.entity;

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
 * @date 2021-05-19 8:52
 */
@TableName("iot_role")
@SuppressWarnings("serial")
@Data
public class IotRole  extends BaseDataEntity {
    /**主键*/
    @TableField(value = "id")
    private String id;
    /**角色名称*/
    @TableField(value = "name")
    private String name;
    /**英文名称*/
    @TableField(value = "code")
    private String code;
    /**项目名称*/
    @TableField(value = "project_id")
    private String projectId;
    /**是否系统数据*/
    @TableField(value = "is_sys")
    private String isSys;
    /**是否可用*/
    @TableField(value = "usable")
    private String usable;
    /**创建人ID*/
    @TableField(value = "create_by")
    private String createBy;
    /**创建时间*/
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date createDate;
    /**修改人ID*/
    @TableField(value = "update_by")
    private String updateBy;
    /**更新时间*/
    @TableField(value = "update_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    private Date updateDate;
    /**备注信息*/
    @TableField(value = "remarks")
    private String remarks;
    /**删除标记*/
    @TableField(value = "del_flag")
    private String delFlag;
}
