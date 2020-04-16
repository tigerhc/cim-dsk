package com.lmrj.oven.batchlot.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.google.common.collect.Lists;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.entity
 * @title: ovn_batch_lot实体
 * @description: ovn_batch_lot实体
 * @author: zhangweijiang
 * @date: 2019-06-09 08:49:15
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@TableName("ovn_batch_lot")
@SuppressWarnings("serial")
@Data
public class OvnBatchLot extends BaseDataEntity {

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
    /**批次号*/
    @TableField(value = "lot_id")
	private String lotId;
    /**校验标记*/
    @TableField(value = "check_flag")
	private String checkFlag;
    /**校验结果*/
    @TableField(value = "check_result")
	private String checkResult;
    @TableField(exist = false)
    private List<OvnBatchLotParam> ovnBatchLotParamList = Lists.newArrayList();


}
