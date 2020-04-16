package com.lmrj.gem.param.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;

/**
 * @Title: 设备参数定义
 * @Description: 设备参数统一定义
 * @author zhangweijiang
 * @date 2018-05-22 15:28:42
 * @version V1.0
 *
 */
@TableName("eqp_param_code_define")
@SuppressWarnings("serial")
public class EqpParamCodeDefine extends BaseDataEntity {

    /**工艺类型ID*/
    @TableField(value = "PROCESS_TYPE_ID")
	private String processTypeId;
    /**工艺类型CODE*/
    @TableField(value = "PROCESS_TYPE_CODE")
	private String processTypeCode;
    /**参数CODE,  厂商+model+00000000 ~ 99999999,各型号不可重复 共10位*/
    @TableField(value = "PARAM_CODE")
	private String paramCode;
    /**参数名称*/
    @TableField(value = "PARAM_NAME")
	private String paramName;
    /**参数描述*/
    @TableField(value = "PARAM_DESC")
	private String paramDesc;
    /**参数简称*/
    @TableField(value = "PARAM_SHORTNAME")
	private String paramShortname;
    /**默认计量单位*/
    @TableField(value = "PARAM_UNIT")
	private String paramUnit;
    /**参数类别ID*/
    @TableField(value = "PARAM_CATEGORY_ID")
	private String paramCategoryId;
    /**参数类别CODE*/
    @TableField(value = "PARAM_CATEGORY_CODE")
	private String paramCategoryCode;
    /**有效标记*/
    @TableField(value = "ACTIVE_FLAG")
	private String activeFlag;

    /**
	 * 获取  processTypeId
	 *@return: String  工艺类型ID
	 */
	public String getProcessTypeId(){
		return this.processTypeId;
	}

	/**
	 * 设置  processTypeId
	 *@param: processTypeId  工艺类型ID
	 */
	public void setProcessTypeId(String processTypeId){
		this.processTypeId = processTypeId;
	}
    /**
	 * 获取  processTypeCode
	 *@return: String  工艺类型CODE
	 */
	public String getProcessTypeCode(){
		return this.processTypeCode;
	}

	/**
	 * 设置  processTypeCode
	 *@param: processTypeCode  工艺类型CODE
	 */
	public void setProcessTypeCode(String processTypeCode){
		this.processTypeCode = processTypeCode;
	}
    /**
	 * 获取  paramCode
	 *@return: String  参数CODE,  厂商+model+00000000 ~ 99999999,各型号不可重复 共10位
	 */
	public String getParamCode(){
		return this.paramCode;
	}

	/**
	 * 设置  paramCode
	 *@param: paramCode  参数CODE,  厂商+model+00000000 ~ 99999999,各型号不可重复 共10位
	 */
	public void setParamCode(String paramCode){
		this.paramCode = paramCode;
	}
    /**
	 * 获取  paramName
	 *@return: String  参数名称
	 */
	public String getParamName(){
		return this.paramName;
	}

	/**
	 * 设置  paramName
	 *@param: paramName  参数名称
	 */
	public void setParamName(String paramName){
		this.paramName = paramName;
	}
    /**
	 * 获取  paramDesc
	 *@return: String  参数描述
	 */
	public String getParamDesc(){
		return this.paramDesc;
	}

	/**
	 * 设置  paramDesc
	 *@param: paramDesc  参数描述
	 */
	public void setParamDesc(String paramDesc){
		this.paramDesc = paramDesc;
	}
    /**
	 * 获取  paramShortname
	 *@return: String  参数简称
	 */
	public String getParamShortname(){
		return this.paramShortname;
	}

	/**
	 * 设置  paramShortname
	 *@param: paramShortname  参数简称
	 */
	public void setParamShortname(String paramShortname){
		this.paramShortname = paramShortname;
	}
    /**
	 * 获取  paramUnit
	 *@return: String  默认计量单位
	 */
	public String getParamUnit(){
		return this.paramUnit;
	}

	/**
	 * 设置  paramUnit
	 *@param: paramUnit  默认计量单位
	 */
	public void setParamUnit(String paramUnit){
		this.paramUnit = paramUnit;
	}
    /**
	 * 获取  paramCategoryId
	 *@return: String  参数类别ID
	 */
	public String getParamCategoryId(){
		return this.paramCategoryId;
	}

	/**
	 * 设置  paramCategoryId
	 *@param: paramCategoryId  参数类别ID
	 */
	public void setParamCategoryId(String paramCategoryId){
		this.paramCategoryId = paramCategoryId;
	}
    /**
	 * 获取  paramCategoryCode
	 *@return: String  参数类别CODE
	 */
	public String getParamCategoryCode(){
		return this.paramCategoryCode;
	}

	/**
	 * 设置  paramCategoryCode
	 *@param: paramCategoryCode  参数类别CODE
	 */
	public void setParamCategoryCode(String paramCategoryCode){
		this.paramCategoryCode = paramCategoryCode;
	}
    /**
	 * 获取  activeFlag
	 *@return: String  有效标记
	 */
	public String getActiveFlag(){
		return this.activeFlag;
	}

	/**
	 * 设置  activeFlag
	 *@param: activeFlag  有效标记
	 */
	public void setActiveFlag(String activeFlag){
		this.activeFlag = activeFlag;
	}

}
