package com.lmrj.fab.bc.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

@TableName("fab_org_structure")
@SuppressWarnings("serial")
@Data
public class FabOrgStructure extends BaseDataEntity {
    /**组织CODE*/
    @TableField(value = "org_code")
    @Excel(name = "组织CODE", orderNum = "1", width = 20)
    private String orgCode;
    /**组织NAME*/
    @TableField(value = "org_name")
    @Excel(name = "组织NAME", orderNum = "2", width = 40)
    private String orgName;
    /**组织类型*/
    @TableField(value = "org_type")
    @Excel(name = "组织类型", orderNum = "3", width = 20)
    private String orgType;
    /**中心ID*/
    @TableField(value = "center_id")
    @Excel(name = "中心ID", orderNum = "4", width = 20)
    private String centerId;
    /**中心CODE*/
    @TableField(value = "center_code")
    @Excel(name = "中心CODE", orderNum = "5", width = 20)
    private String centerCode;
    @TableField(value = "center_name")
    private String centerName;
    @TableField(value = "company_id")
    private String companyId;
    @TableField(value = "company_code")
    private String companyCode;
    @TableField(value = "company_name")
    private String companyName;
    @TableField(value = "factory_id")
    private String factoryId;
    @TableField(value = "factory_code")
    private String factoryCode;
    @TableField(value = "factory_name")
    private String factoryName;
    @TableField(value = "department_id")
    private String departmentId;
    @TableField(value = "department_code")
    private String departmentCode;
    @TableField(value = "department_name")
    private String departmentName;
    @TableField(value = "line_id")
    private String lineId;
    @TableField(value = "line_code")
    private String lineCode;
    @TableField(value = "line_name")
    private String lineName;
    @TableField(value = "station_id")
    private String stationId;
    @TableField(value = "station_code")
    private String stationCode;
    @TableField(value = "station_name")
    private String stationName;
    @TableField(value = "process_id")
    private String processId;
    @TableField(value = "process_code")
    private String processCode;
    @TableField(value = "process_name")
    private String processName;
    @TableField(value = "remarks")
    private String remarks;
    @TableField(value = "ver_no")
    private Integer verNo;
    @TableField(value = "name")
    private String name;
    @TableField(value = "email")
    private String email;
}
