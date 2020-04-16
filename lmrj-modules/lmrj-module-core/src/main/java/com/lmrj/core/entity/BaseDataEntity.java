package com.lmrj.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lmrj.common.mvc.entity.AbstractEntity;
import com.lmrj.common.query.constant.DataBaseConstant;

import java.util.Date;

/**
 * 数据Entity类
 *
 * @author 张飞
 * @version 2016-12-03
 *            主键类型
 */
public abstract class BaseDataEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	@TableId(value = "id", type = IdType.UUID)
	protected String id;
	@TableField(value = "remarks")
	protected String remarks; // 备注
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	protected String createBy; // 创建者
	@TableField(exist = false)
	protected String createByName; // 创建者姓名
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
	protected Date createDate; // 创建日期
	@TableField(value = "update_by", fill = FieldFill.UPDATE)
	protected String updateBy; // 更新者
	@TableField(exist = false)
	protected String updateByName; // 更新者姓名
	@TableField(value = "update_date", fill = FieldFill.UPDATE)
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS")
	protected Date updateDate; // 更新日期
	@TableField(value = "del_flag", fill = FieldFill.INSERT)
	@TableLogic //逻辑删除
	protected String delFlag = "0"; // 删除标记（0：正常；1：删除 ）

	public BaseDataEntity() {
		super();
		this.delFlag = DataBaseConstant.DEL_FLAG_NORMAL;
	}

	public String getId(){
		return this.id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}
}
