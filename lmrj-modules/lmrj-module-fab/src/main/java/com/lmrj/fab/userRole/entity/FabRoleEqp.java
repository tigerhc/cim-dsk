package com.lmrj.fab.userRole.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import com.lmrj.fab.eqp.entity.FabEquipment;
import lombok.Data;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 8:52
 */
@TableName("fab_role_eqp")
@SuppressWarnings("serial")
@Data
public class FabRoleEqp extends BaseDataEntity {
    /**主键*/
    @TableField(value = "id")
    private String id;
    /**设备编号*/
    @TableField(value = "eqp_id")
    private String eqpId;
    /**角色编号*/
    @TableField(value = "role_id")
    private String roleId;
}
