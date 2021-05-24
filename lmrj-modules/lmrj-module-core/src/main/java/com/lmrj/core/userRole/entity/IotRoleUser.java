package com.lmrj.core.userRole.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * @author wdj
 * @date 2021-05-19 8:51
 */
@TableName("iot_user_role")
@SuppressWarnings("serial")
@Data
public class IotRoleUser extends BaseDataEntity {
    /**主键*/
    @TableField(value = "id")
    private String id;
    /**用户编号*/
    @TableField(value = "user_id")
    private String userId;
    /**角色编号*/
    @TableField(value = "role_id")
    private String roleId;
}
