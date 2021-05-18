package com.lmrj.dsk.eqplog.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.entity
 * @title: edc_dsk_log_production实体
 * @description: edc_dsk_log_production实体
 * @author: 张伟江
 * @date: 2020-04-14 10:10:00
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_dsk_log_lingouparam")
@SuppressWarnings("serial")
@Data
public class EdcDskLogLingouParam extends AbstractEntity {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**部门ID*/
    @TableField(value = "office_id")
    private String officeId;
    @TableField(exist = false)
    private String officeName;
    /**工序ID*/
    @TableField(value = "step_id")
    private String stepId;
    /**工序CODE*/
    @TableField(value = "step_code")
    private String stepCode;
    /**配方CODE*/
    @TableField(value = "recipe_code")
    private String recipeCode;
    /**程序版本号*/
    @TableField(value = "recipe_version_no")
    private String recipeVersionNo;
    /**作业开始时间*/
    @TableField(value = "start_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**作业结束时间*/
    @TableField(value = "end_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**作业记录文件名*/
    @TableField(value = "filename")
    private String filename;
    @TableField(value = "params_title")
    private String paramsTitle;
    /**批次号*/
    @TableField(value = "lot_id")
    private String lotId;
    /**批次号*/
    @TableField(value = "chip_id")
    private String chipId;
    /**批次号*/
    @TableField(value = "params")
    private String params;
    /**校验标记*/
    @TableField(value = "check_flag")
    private String checkFlag;
    /**校验结果*/
    @TableField(value = "check_result")
    private String checkResult;

}
