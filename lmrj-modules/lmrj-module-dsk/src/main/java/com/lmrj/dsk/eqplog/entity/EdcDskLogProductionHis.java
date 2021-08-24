package com.lmrj.dsk.eqplog.entity;

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
 * @title: edc_dsk_log_production_his实体
 * @description: edc_dsk_log_production_his实体
 * @author: 张伟江
 * @date: 2020-06-08 13:56:08
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_dsk_log_production_his")
@SuppressWarnings("serial")
@Data
public class EdcDskLogProductionHis extends AbstractEntity {

    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**设备类型*/
    @TableField(value = "eqp_model_id")
    private String eqpModelId;
    /**设备类型名称*/
    @TableField(value = "eqp_model_name")
    private String eqpModelName;
    /**设备名*/
    @TableField(value = "eqp_no")
    private String eqpNo;
    /**配方CODE*/
    @TableField(value = "recipe_code")
    private String recipeCode;
    /**日产量*/
    @TableField(value = "day_yield")
    private Integer dayYield;
    /**批次产量*/
    @TableField(value = "lot_yield")
    private Integer lotYield;
    /**日投入数*/
    @TableField(value = "day_input")
    private Integer dayInput;
    /**批量投入数*/
    @TableField(value = "lot_input")
    private Integer lotInput;
    /**制品的批号*/
    @TableField(value = "material_lot_no")
    private String materialLotNo;
    /**制品的型号*/
    @TableField(value = "material_model")
    private String materialModel;
    /**制品的序列号*/
    @TableField(value = "material_no")
    private String materialNo;
    /**制品序列号2*/
    @TableField(value = "material_no2")
    private String materialNo2;
    /**作业指示书批量*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**作业指示书机种*/
    @TableField(value = "production_no")
    private String productionNo;
    /**作业指示书序列号*/
    @TableField(value = "order_no")
    private String orderNo;
    /**判定结果*/
    @TableField(value = "judge_result")
    private String judgeResult;
    /**开始时间*/
    @TableField(value = "start_time")
    private Date startTime;
    /**结束时间*/
    @TableField(value = "end_time")
    private Date endTime;
    /**消耗时间*/
    @TableField(value = "duration")
    private Double duration;
    /**文件名称*/
    @TableField(value = "csv_log_name")
    private String csvLogName;

    public EdcDskLogProductionHis() {
    }

    public EdcDskLogProductionHis(EdcDskLogProduction edcDskLogProduction) {
        this.id = edcDskLogProduction.getId();
        this.eqpId = edcDskLogProduction.getEqpId();
        this.eqpModelId = edcDskLogProduction.getEqpModelId();
        this.eqpModelName = edcDskLogProduction.getEqpModelName();
        this.eqpNo = edcDskLogProduction.getEqpNo();
        this.recipeCode = edcDskLogProduction.getRecipeCode();
        this.dayYield = edcDskLogProduction.getDayYield();
        this.lotYield = edcDskLogProduction.getLotYield();
        this.materialLotNo = edcDskLogProduction.getMaterialLotNo();
        this.materialModel = edcDskLogProduction.getMaterialModel();
        this.materialNo = edcDskLogProduction.getMaterialNo();
        this.materialNo2 = edcDskLogProduction.getMaterialNo2();
        this.lotNo = edcDskLogProduction.getLotNo();
        this.productionNo = edcDskLogProduction.getProductionNo();
        this.orderNo = edcDskLogProduction.getOrderNo();
        this.judgeResult = edcDskLogProduction.getJudgeResult();
        this.startTime = edcDskLogProduction.getStartTime();
        this.endTime = edcDskLogProduction.getEndTime();
        this.duration = edcDskLogProduction.getDuration();
    }
}
