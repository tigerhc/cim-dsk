package com.lmrj.ms.config.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.google.common.collect.Lists;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.config.entity
 * @title: ms_measure_config实体
 * @description: ms_measure_config实体
 * @author: 张伟江
 * @date: 2020-06-06 18:32:57
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("ms_measure_config")
@SuppressWarnings("serial")
@Data
public class MsMeasureConfig extends BaseDataEntity {

    /**设备型号*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备型号名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**机种*/
    @TableField(value = "production_no")
    private String productionNo;
    /**时机,可为空*/
    @TableField(value = "timing")
    private String timing;

    /**状态 0:创建1: 已提交 N:作废*/
    @TableField(value = "status")
    private String status;
    ///**判定结果,若超出范围,则N*/
    //@TableField(value = "approve_result")
    //private String approveResult;
    /**采样文件*/
    @TableField(value = "file_flag")
    private String fileFlag;
    /**采样图片*/
    @TableField(value = "img_flag")
    private String imgFlag;

    @TableField(exist = false)
    private List<MsMeasureConfigDetail> detail = Lists.newArrayList();

}
