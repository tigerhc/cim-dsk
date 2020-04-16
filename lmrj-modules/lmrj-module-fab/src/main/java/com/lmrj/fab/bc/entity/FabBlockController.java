package com.lmrj.fab.bc.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.bc.entity
 * @title: fab_block_controller实体
 * @description: fab_block_controller实体
 * @author: zhangweijiang
 * @date: 2019-07-15 01:03:01
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("fab_block_controller")
@SuppressWarnings("serial")
@Data
public class FabBlockController extends BaseDataEntity {

    /**BC号*/
    @TableField(value = "bc_code")
    private String bcCode;
    /**BC名称*/
    @TableField(value = "bc_name")
    private String bcName;
    /**描述*/
    @TableField(value = "bc_desc")
    private String bcDesc;
    /**IP地址*/
    @TableField(value = "bc_ip")
    private String bcIp;
    /**端口号*/
    @TableField(value = "bc_port")
    private String bcPort;
    /**部门ID*/
    @TableField(value = "office_id")
    private String officeId;
    /**有效标志*/
    @TableField(value = "active_flag")
    private String activeFlag;
    /**位置号*/
    @TableField(value = "location")
    private String location;

}
