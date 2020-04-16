package com.lmrj.gem.param.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.lmrj.core.entity.BaseDataEntity;
import com.lmrj.core.excel.annotation.ExcelField;

/**
 * @Title: eqp_param_setting
 * @Description: eqp_param_setting
 * @author zhangweijiang
 * @date 2018-04-27 20:43:55
 * @version V1.0
 *
 */
@TableName("eqp_param_setting")
@SuppressWarnings("serial")
public class EqpParamSetting extends BaseDataEntity {

    /**参数制程名称*/
    @TableField(value = "PARAM_DEFINE_ID")
	private String paramDefineId;
    /**参数CODE, 格式: 厂商缩写+model字母+1000001 ~ 1999999,各型号不可重复*/
    @TableField(value = "PARAM_CODE")
	private String paramCode;
    /**参数名称*/
    @TableField(value = "PARAM_NAME")
	private String paramName;
    /**参数简称*/
    @TableField(value = "PARAM_SHORTNAME")
	private String paramShortname;
    /**默认计量单位*/
    @TableField(value = "PARAM_UNIT")
	private String paramUnit;
    /**设备型号*/
    @TableField(value = "MODEL_ID")
	private String modelId;
    /**设备型号名称*/
    @TableField(value = "MODEL_NAME")
	private String modelName;
    /**通讯协议 格式:A,B*/
    @TableField(value = "PROTOCOL")
	private String protocol;
    /**子通讯协议 格式:SV EC*/
    @TableField(value = "PROTOCOL_SUB")
	private String protocolSub;
    /**通讯协议值*/
    @TableField(value = "PROTOCOL_VALUE")
	private String protocolValue;
    /**设定值*/
    @TableField(value = "SET_VALUE")
	private String setValue;
    /**默认值*/
    @TableField(value = "DEF_VALUE")
	private String defValue;
    /**最小值*/
    @TableField(value = "MIN_VALUE")
	private String minValue;
    /**最大值*/
    @TableField(value = "MAX_VALUE")
	private String maxValue;
    /**显示标记 N、不在初次打开页面显示Y、在初次打开页面显示*/
    @TableField(value = "SHOW_FLAG")
	private String showFlag;
    /**监控标记 N、不监控Y、监控*/
    @TableField(value = "MONITOR_FLAG")
	private String monitorFlag;
    /**值需转换标志*/
    @TableField(value = "VAL_MAPPING_FLAG")
	private String valMappingFlag;
	/**子设备号*/
	@TableField(value = "SUB_EQP_ID")
	private String subEqpId;

    /**
	 * 获取  paramDefineId
	 *@return: String  参数制程名称
	 */
	public String getParamDefineId(){
		return this.paramDefineId;
	}

	/**
	 * 设置  paramDefineId
	 *@param: paramDefineId  参数制程名称
	 */
	public void setParamDefineId(String paramDefineId){
		this.paramDefineId = paramDefineId;
	}
    /**
	 * 获取  paramCode
	 *@return: String  参数CODE, 格式: 厂商缩写+model字母+1000001 ~ 1999999,各型号不可重复
	 */
	public String getParamCode(){
		return this.paramCode;
	}

	/**
	 * 设置  paramCode
	 *@param: paramCode  参数CODE, 格式: 厂商缩写+model字母+1000001 ~ 1999999,各型号不可重复
	 */
	public void setParamCode(String paramCode){
		this.paramCode = paramCode;
	}
    /**
	 * 获取  paramName
	 *@return: String  参数名称
	 */
	@ExcelField(title="参数名称(请忽修改)", type=0, align=1, sort=10)
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
	 * 获取  paramShortname
	 *@return: String  参数简称
	 */
	@ExcelField(title="参数简称", type=0, align=1, sort=20)
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
	 * 获取  modelId
	 *@return: String  设备型号
	 */
	public String getModelId(){
		return this.modelId;
	}

	/**
	 * 设置  modelId
	 *@param: modelId  设备型号
	 */
	public void setModelId(String modelId){
		this.modelId = modelId;
	}
    /**
	 * 获取  modelName
	 *@return: String  设备型号名称
	 */
	@ExcelField(title="设备型号(请忽修改)", type=0, align=1, sort=2)
	public String getModelName(){
		return this.modelName;
	}

	/**
	 * 设置  modelName
	 *@param: modelName  设备型号名称
	 */
	public void setModelName(String modelName){
		this.modelName = modelName;
	}
    /**
	 * 获取  protocol
	 *@return: String  通讯协议 格式:A,B
	 */
	public String getProtocol(){
		return this.protocol;
	}

	/**
	 * 设置  protocol
	 *@param: protocol  通讯协议 格式:A,B
	 */
	public void setProtocol(String protocol){
		this.protocol = protocol;
	}
    /**
	 * 获取  protocolSub
	 *@return: String  子通讯协议 格式:SV EC
	 */
	public String getProtocolSub(){
		return this.protocolSub;
	}

	/**
	 * 设置  protocolSub
	 *@param: protocolSub  子通讯协议 格式:SV EC
	 */
	public void setProtocolSub(String protocolSub){
		this.protocolSub = protocolSub;
	}
    /**
	 * 获取  protocolValue
	 *@return: String  通讯协议值
	 */
	public String getProtocolValue(){
		return this.protocolValue;
	}

	/**
	 * 设置  protocolValue
	 *@param: protocolValue  通讯协议值
	 */
	public void setProtocolValue(String protocolValue){
		this.protocolValue = protocolValue;
	}
    /**
	 * 获取  setValue
	 *@return: String  设定值
	 */
	public String getSetValue(){
		return this.setValue;
	}

	/**
	 * 设置  setValue
	 *@param: setValue  设定值
	 */
	public void setSetValue(String setValue){
		this.setValue = setValue;
	}
    /**
	 * 获取  defValue
	 *@return: String  默认值
	 */
	public String getDefValue(){
		return this.defValue;
	}

	/**
	 * 设置  defValue
	 *@param: defValue  默认值
	 */
	public void setDefValue(String defValue){
		this.defValue = defValue;
	}
    /**
	 * 获取  minValue
	 *@return: String  最小值
	 */
	public String getMinValue(){
		return this.minValue;
	}

	/**
	 * 设置  minValue
	 *@param: minValue  最小值
	 */
	public void setMinValue(String minValue){
		this.minValue = minValue;
	}
    /**
	 * 获取  maxValue
	 *@return: String  最大值
	 */
	public String getMaxValue(){
		return this.maxValue;
	}

	/**
	 * 设置  maxValue
	 *@param: maxValue  最大值
	 */
	public void setMaxValue(String maxValue){
		this.maxValue = maxValue;
	}
    /**
	 * 获取  showFlag
	 *@return: String  显示标记 N、不在初次打开页面显示Y、在初次打开页面显示
	 */
	public String getShowFlag(){
		return this.showFlag;
	}

	/**
	 * 设置  showFlag
	 *@param: showFlag  显示标记 N、不在初次打开页面显示Y、在初次打开页面显示
	 */
	public void setShowFlag(String showFlag){
		this.showFlag = showFlag;
	}
    /**
	 * 获取  monitorFlag
	 *@return: String  监控标记 N、不监控Y、监控
	 */
	@ExcelField(title="监控标记", type=0, align=1, sort=50)
	public String getMonitorFlag(){
		return this.monitorFlag;
	}

	/**
	 * 设置  monitorFlag
	 *@param: monitorFlag  监控标记 N、不监控Y、监控
	 */
	public void setMonitorFlag(String monitorFlag){
		this.monitorFlag = monitorFlag;
	}
    /**
	 * 获取  valMappingFlag
	 *@return: String  值需转换标志
	 */
	public String getValMappingFlag(){
		return this.valMappingFlag;
	}

	/**
	 * 设置  valMappingFlag
	 *@param: valMappingFlag  值需转换标志
	 */
	public void setValMappingFlag(String valMappingFlag){
		this.valMappingFlag = valMappingFlag;
	}

	@ExcelField(title="子设备", type=0, align=1, sort=40)
    public String getSubEqpId() {
        return subEqpId;
    }

    public void setSubEqpId(String subEqpId) {
        this.subEqpId = subEqpId;
    }


	/**
	 * 为了excel导出,单独重写
	 */
	@TableId(value = "id", type = IdType.UUID)
	@ExcelField(title="ID(请忽修改,唯一标识)", type=0, align=1, sort=1)
	private String id;
	///**
	// * 获取  id
	// *@return: String  主键
	// */
	//@ExcelField(title="ID", type=0, align=1, sort=1)
	//@Override
	//public String getId(){
	//	return this.id;
	//}
}
