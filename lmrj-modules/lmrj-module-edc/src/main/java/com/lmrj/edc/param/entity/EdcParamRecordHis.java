package com.lmrj.edc.param.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.param.entity
 * @title: edc_param_record_his实体
 * @description: edc_param_record_his实体
 * @author: zhangweijiang
 * @date: 2019-06-14 23:31:46
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("edc_param_record_his")
@SuppressWarnings("serial")
@Data
public class EdcParamRecordHis extends BaseDataEntity {

    /**设备号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**业务日期*/
    @TableField(value = "biz_date")
    private Date bizDate;
    /**业务大类*/
    @TableField(value = "biz_type")
    private String bizType;
    /**业务小类*/
    @TableField(value = "biz_sub_type")
    private String bizSubType;

}
