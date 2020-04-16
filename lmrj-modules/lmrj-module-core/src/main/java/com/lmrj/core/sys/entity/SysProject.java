package com.lmrj.core.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.bbs.entity
 * @title: sys_project实体
 * @description: sys_project实体
 * @author: zwj
 * @date: 2019-05-28 07:13:47
 * @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
 */

@TableName("sys_project")
@Data
@SuppressWarnings("serial")
public class SysProject extends BaseDataEntity {

	@TableField(value = "project_id")
	private String projectId;
    /**PROJECT_NAME*/
    @TableField(value = "project_name")
	private String projectName;
    /**PROJECT_DETAIL*/
    @TableField(value = "project_detail")
	private String projectDetail;
	@TableField(value = "project_url")
    private String projectUrl;
	@TableField(value = "project_photo_url")
	private String projectPhotoUrl;
	@TableField(value = "status")
    private Integer status;
	@TableField(value = "sort_code")
	private Integer sortCode;

}
