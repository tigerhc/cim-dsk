package com.lmrj.fab.eqp.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.entity
 * @title: fab_equipment实体
 * @description: fab_equipment实体
 * @author: 张伟江
 * @date: 2019-06-04 16:03:56
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@TableName("fab_equipment")
@SuppressWarnings("serial")
@Data
public class FabEquipment extends BaseDataEntity {

	/**ID*/
	private String id;
	/**设备号*/
	@TableField(value = "eqp_id")
	@Excel(name = "设备id", orderNum = "1")
	private String eqpId;
	@TableField(value = "line_yield_flag")
	@Excel(name = "线别产量标记", orderNum = "1")
	private String lineYieldFlag;
	@TableField(value = "eqp_name")
	@Excel(name = "设备Name", orderNum = "1")
	private String eqpName;
	@Excel(name = "设备no", orderNum = "1")
	@TableField(value = "eqp_no")
	private String eqpNo;
	/**部门ID*/
	@TableField(value = "office_id")
	private String officeId;
	@TableField(exist = false)
	@Excel(name = "部门名称", orderNum = "3")
	private String officeName;
	@TableField(exist = false)
	private String officeIds;
	/**工序ID*/
	@TableField(value = "step_id")
	@Excel(name = "工序id", orderNum = "4")
	private String stepId;
	/**工序CODE*/
	@Excel(name = "工序编码", orderNum = "5")
	@TableField(value = "step_code")
	private String stepCode;
	@TableField(value = "step_yield_flag")
	private String stepYieldFlag;
	/**有效标志*/
	@Excel(name = "有效标记", orderNum = "6")
	@TableField(value = "active_flag")
	private String activeFlag;
	/**BC号*/
	@Excel(name = "BC号", orderNum = "7")
	@TableField(value = "bc_code")
	private String bcCode;
	/**机台IP地址*/
	@Excel(name = "ip", orderNum = "8")
	@TableField(value = "ip")
	private String ip;
	/**端口号*/
	@Excel(name = "端口号", orderNum = "9")
	@TableField(value = "port")
	private String port;
	/**DEVICE ID*/
	@Excel(name = "设备id", orderNum = "10")
	@TableField(value = "device_id")
	private String deviceId;
	/**设备型号*/
	@TableField(value = "model_id")
	private String modelId;
	 /**设备型号名称*/
	@Excel(name = "设备型号名称", orderNum = "12")
	@TableField(value = "model_name")
	private String modelName;
	/**协议名称*/
	@Excel(name = "协议名称", orderNum = "13")
	@TableField(value = "protocol_name")
	private String protocolName;
	/**厂别*/
	@Excel(name = "厂别", orderNum = "14")
	@TableField(value = "fab")
	private String fab;
	/**线别*/
	@Excel(name = "线别", orderNum = "15")
	@TableField(value = "line_no")
	private String lineNo;
	@Excel(name = "参数", orderNum = "15")
	@TableField(value = "eqp_param")
	private String eqpParam;
	/**位置号*/
	@Excel(name = "位置号", orderNum = "16")
	@TableField(value = "location")
	private String location;
	/**节拍 毫秒*/
	@Excel(name = "节拍", orderNum = "17")
	@TableField(value = "take_time")
	private int takeTime;
	/**客户端*/
	@Excel(name = "客户端", orderNum = "18")
	@TableField(value = "client_flag")
	private String clientFlag;

	@TableField(value = "sort_code")
	private Integer sortCode;


}
