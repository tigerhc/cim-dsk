package com.lmrj.fab.eqp.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.entity
 * @title: fab_equipment_model实体
 * @description: fab_equipment_model实体
 * @author: kang
 * @date: 2019-06-07 22:18:19
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@TableName("fab_equipment_model")
@SuppressWarnings("serial")
@Data
public class FabEquipmentModel extends BaseDataEntity {

    /**ID*/
	private String id;
    /**设备厂家*/
    @TableField(value = "manufacturer_name")
	private String manufacturerName;
    /**设备类型*/
    @TableField(value = "class_code")
	private String classCode;
    @TableField(exist = false)
    private String modelName;
    /**SML文件路径*/
    @TableField(value = "sml_path")
	private String smlPath;
    /**设备对应的JAVA类*/
    @TableField(value = "host_java_class")
	private String hostJavaClass;
    /**有效标志*/
    @TableField(value = "active_flag")
	private String activeFlag;
    /**图片路径*/
    @TableField(value = "icon_path")
	private String iconPath;


}
