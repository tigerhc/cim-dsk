package com.lmrj.oven.batchlot.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.entity
 * @title: ovn_batch_lot_param实体
 * @description: ovn_batch_lot_param实体
 * @author: zhangweijiang
 * @date: 2019-06-09 08:55:13
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@TableName("ovn_batch_lot_param")
@SuppressWarnings("serial")
@Data
public class OvnBatchLotParam extends BaseDataEntity {

    //@TableField(value = "batch_id",el="ovnBatchLot.id")
    //private OvnBatchLot ovnBatchLot;
    /**编号*/
    @TableField(value = "id")
    private String id;
    @TableField(value = "batch_id")
	private String batchId;
    /**温度PV*/
    @TableField(value = "temp_pv")
	private String tempPv;
    /**温度SP*/
    @TableField(value = "temp_sp")
    private String tempSp;
    /**温度范围最小值*/
    @TableField(value = "temp_min")
    private String tempMin;
    /**温度范围最大值*/
    @TableField(value = "temp_max")
    private String tempMax;

    /**段位*/
    @TableField(value = "step")
	private Short step;

    //@TableField(value = "temps_title")
    //private String tempsTitle;
    //@TableField(value = "temps_value")
    //private String tempsValue;

    @TableField(value = "other_temps_value")
    private String otherTempsValue;



}
