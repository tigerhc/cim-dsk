package com.lmrj.core.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.sys.config.entity
 * @title: sys_config实体
 * @description: sys_config实体
 * @author: zhangweijiang
 * @date: 2019-07-14 03:03:35
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("sys_config")
@SuppressWarnings("serial")
@Data
public class SysConfig extends BaseDataEntity {

    /**名称*/
    @TableField(value = "config_name")
    private String configName;
    /**参数键*/
    @TableField(value = "config_key")
    private String configKey;
    /**参数值*/
    @TableField(value = "config_value")
    private String configValue;
    /**系统内置（1是 0否）*/
    @TableField(value = "sys_flag")
    private String sysFlag;

}
