package com.lmrj.dsk.eqplog.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.entity
 * @title: edc_dsk_log_recipe实体
 * @description: edc_dsk_log_recipe实体
 * @author: 张伟江
 * @date: 2020-04-17 17:21:17
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_dsk_log_recipe")
@SuppressWarnings("serial")
@Data
public class EdcDskLogRecipe extends AbstractEntity {

    @TableId(value = "id", type = IdType.UUID)
    protected String id;
    /**设备号*/
    @TableField(value = "eqp_id")
    @Excel(name = "设备号", orderNum = "1", width = 20)
    private String eqpId;
    /**设备类型*/
    @TableField(exist = false)
    private String eqpModelId;
    /**设备类型名称*/
    @TableField(exist = false)
    private String eqpModelName;
    /**设备序列*/
    @TableField(value = "eqp_no")
    private String eqpNo;
    /**配方CODE*/
    @TableField(value = "recipe_code")
    @Excel(name = "配方名", orderNum = "1", width = 30)
    private String recipeCode;
    /**发生时刻*/
    @TableField(value = "start_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    @Excel(name = "发生时间", orderNum = "1", width = 40, format="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**作业指示书订单*/
    @TableField(value = "order_no")
    private String orderNo;
    /**作业指示书批量*/
    @TableField(value = "lot_no")
    private String lotNo;
    /**作业指示书品番*/
    @TableField(value = "lot_num")
    private String lotNum;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy; // 创建者
    @TableField(exist = false)
    protected String createByName; // 创建者姓名
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
    protected Date createDate; // 创建日期

    @TableField(exist = false)
    List<EdcDskLogRecipeBody> edcDskLogRecipeBodyList;


}
