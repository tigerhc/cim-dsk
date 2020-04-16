package com.lmrj.core.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.lmrj.common.mvc.entity.AbstractEntity;

@TableName("sys_user_role")
public class UserRole extends AbstractEntity {

	/** 编号 */
	@TableId(value = "id", type = IdType.UUID)
	private String id;
	/** 用户编号 */
	private String userId;
	/** 角色编号 */
	private String roleId;

	/**
	 * 获取 id
	 *
	 * @return: String 编号
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置 id
	 *
	 * @param: id
	 *             编号
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
