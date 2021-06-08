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
 * @date 2021-06-02 16:01
 */
@TableName("sensor_num_type")
@SuppressWarnings("serial")
@Data
public class SensorNumType  extends BaseDataEntity {
    /**
     * ID
     */
    private String id;
    /**
     * 设备号
     */
    @TableField(value = "class_code")
    @Excel(name = "传感器类型号", orderNum = "1")
    private String classCode;
    @TableField(value = "num_type")
    @Excel(name = "示数类型", orderNum = "1")
    private String numType;
    @TableField(value = "data_type")
    @Excel(name = "数据类型", orderNum = "1")
    private String dataType;
    @TableField(value = "collection_formula")
    @Excel(name = "采集公式", orderNum = "1")
    private String collectionFormula;
    @TableField(value = "param_unit")
    @Excel(name = "计量单位", orderNum = "1")
    private String paramUnit;
    @TableField(value = "remarks")
    @Excel(name = "备注", orderNum = "1")
    private String remarks;
    @TableField(value = "del_flag")
    @Excel(name = "记录删除标志", orderNum = "1")
    private String delFlag;
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
