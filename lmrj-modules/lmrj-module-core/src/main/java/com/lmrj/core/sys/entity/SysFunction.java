package com.lmrj.core.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.core.sys.entity
 * @title: sys_function实体
 * @description: sys_function实体
 * @author: 张伟江
 * @date: 2020-06-23 09:55:28
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("sys_function")
@SuppressWarnings("serial")
@Data
public class SysFunction extends BaseDataEntity {

    /**功能类型*/
    @TableField(value = "fct_type")
    private String fctType;
    /**功能代码*/
    @TableField(value = "fct_code")
    private String fctCode;
    /**功能描述*/
    @TableField(value = "fct_desc")
    private String fctDesc;
    /**默认值*/
    @TableField(value = "dafault_value")
    private String dafaultValue;
    /**有效标志*/
    @TableField(value = "active_flag")
    private String activeFlag;
	
}