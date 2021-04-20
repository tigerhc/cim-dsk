package com.lmrj.edc.evt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

@TableName("edc_err_log")
@Data
public class EdcErrLogBean extends BaseDataEntity {
    private String id;
    @TableField(value = "err_type")
    private String errType;
    @TableField(value = "err_position")
    private String errPosition;
    @TableField(value = "err_remark")
    private String errRemark;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(exist = false)
    private String createTimeStr;
}
