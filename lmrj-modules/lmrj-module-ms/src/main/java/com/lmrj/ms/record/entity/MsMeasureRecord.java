package com.lmrj.ms.record.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    /**流水号*/
    @TableField(value = "record_id")
    @Excel(name = "流水号", orderNum = "1", width = 40)
    private String recordId;

    /**批号*/
    @TableField(value = "lot_no")
    @Excel(name = "批号", orderNum = "4", width = 20)
    private String lotNo;
    /**机种*/
    @TableField(value = "production_no")
    @Excel(name = "产品", orderNum = "3", width = 40)
    private String productionNo;
    /**晶圆ID*/
    @TableField(value = "wafer_id")
    @Excel(name = "晶圆ID", orderNum = "5", width = 20)
    private String waferId;
    /**量测项目,用逗号隔开*/
    @TableField(value = "ms_item")
    private String msItem;
    /**设备号*/
    @TableField(value = "eqp_id")
    @Excel(name = "设备号", orderNum = "2", width = 20)
    private String eqpId;
    @Excel(name = "设备名", orderNum = "6")
    @TableField(exist = false)
    private String eqpName;
    /**时机*/
    @TableField(value = "timing")
    @Excel(name = "时机", orderNum = "6", width = 20)
    private String timing;
    /**采样数*/
    @TableField(value = "sample_count")
    @Excel(name = "采样数", orderNum = "7", width = 20)
    private Integer sampleCount;
    /**状态 0:创建1: 已提交 N:作废*/
    @TableField(value = "status")
    @Excel(name = "状态", orderNum = "8", width = 20)
    private String status;
    /**判定结果,若超出范围,则N*/
    @TableField(value = "approve_result")
    @Excel(name = "判定结果", orderNum = "9", width = 20)
    private String approveResult;
    /**采样文件*/
    @TableField(value = "file_flag")
    private String fileFlag;
    /**采样图片*/
    @TableField(value = "img_flag")
    private String imgFlag;
    @TableField(exist = false)
//    @Excel(name = "量测配置详细信息", orderNum = "10", width = 100)
    private List<MsMeasureRecordDetail> detail = Lists.newArrayList();

}
