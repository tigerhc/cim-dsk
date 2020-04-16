package com.lmrj.core.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.lmrj.common.mybatis.mvc.entity.TreeEntity;
import lombok.Data;

import java.util.Date;

/**
 * @Title:
 * @Description:
 * @author 张飞
 * @date 2017-02-07 21:06:09
 * @version V1.0
 *
 */
@TableName("sys_organization")
@SuppressWarnings("serial")
@Data
public class Organization extends TreeEntity<Organization> {

	@TableField(value = "SORT_NO")
	private Double sortNo;
	/**归属区域*/
	@TableField(value = "AREA_ID")
	private String areaId;
	/**机构类型*/
	@TableField(value = "ORG_TYPE")
	private String orgType;
	/**机构等级*/
	@TableField(value = "ORG_GRADE")
	private String orgGrade;
	/**联系地址*/
	@TableField(value = "ADDRESS")
	private String address;
	/**邮政编码*/
	@TableField(value = "ZIP_CODE")
	private String zipCode;
	/**负责人*/
	@TableField(value = "MASTER")
	private String master;
	/**电话*/
	@TableField(value = "PHONE")
	private String phone;
	/**传真*/
	@TableField(value = "FAX")
	private String fax;
	/**邮箱*/
	@TableField(value = "EMAIL")
	private String email;
	/**有效标记*/
	@TableField(value = "ACTIVE_FLAG")
	private String activeFlag;
	/**主负责人*/
	@TableField(value = "PRIMARY_PERSON")
	private String primaryPerson;
	/**副负责人*/
	@TableField(value = "DEPUTY_PERSON")
	private String deputyPerson;
	/**工厂区域*/
	@TableField(value = "PLANT")
	private String plant;


	@TableField(value = "create_by", fill = FieldFill.INSERT)
	protected String createBy; // 创建者
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	protected Date createDate; // 创建日期
	@TableField(value = "update_by", fill = FieldFill.UPDATE)
	protected String updateBy; // 更新者
	@TableField(value = "update_date", fill = FieldFill.UPDATE)
	protected Date updateDate; // 更新日期
	@TableField(value = "del_flag", fill = FieldFill.INSERT)
	protected String delFlag = "0"; // 删除标记（0：正常；1：删除 ）
	@TableField(value = "remarks")
	private String remarks; ///备注



}
