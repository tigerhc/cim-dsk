package com.lmrj.ms.record.entity;

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
 * @package com.lmrj.ms.record.entity
 * @title: ms_measure_record实体
 * @description: ms_measure_record实体
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("ms_measure_record")
@SuppressWarnings("serial")
@Data
public class MsMeasureRecord extends BaseDataEntity {
    /**批号*/
    @TableField(value = "record_id")
    private String recordId;

    /**批号*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**机种*/
    @TableField(value = "production_no")
    private String productionNo;
    /**晶圆ID*/
    @TableField(value = "wafer_id")
    private String waferId;
    /**量测项目,用逗号隔开*/
    @TableField(value = "ms_item")
    private String msItem;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**时机*/
    @TableField(value = "timing")
    private String timing;
    /**采样数*/
    @TableField(value = "sample_count")
    private Integer sampleCount;
    /**状态 0:创建1: 已提交 N:作废*/
    @TableField(value = "status")
    private String status;
    /**判定结果,若超出范围,则N*/
    @TableField(value = "approve_result")
    private String approveResult;
    /**采样文件*/
    @TableField(value = "file_flag")
    private String fileFlag;
    /**采样图片*/
    @TableField(value = "img_flag")
    private String imgFlag;
    @TableField(exist = false)
    private List<MsMeasureRecordDetail> detail = Lists.newArrayList();

}
