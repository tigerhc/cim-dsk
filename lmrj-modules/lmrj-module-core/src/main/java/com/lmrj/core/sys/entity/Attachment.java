package com.lmrj.core.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package cn.gov.gzst.oss.entity
 * @title: 附件实体
 * @description: 附件实体
 * @author: 张飞
 * @date: 2018-04-25 15:55:59
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@TableName("sys_attachment")
@SuppressWarnings("serial")
@Data
public class Attachment extends BaseDataEntity {

    /**id*/
    @TableId(value = "id", type = IdType.UUID)
	private String id;
	/**业务id*/
	@TableField(value = "biz_id")
	private String bizId;
	/**业务种类*/
	@TableField(value = "biz_category")
	private String bizCategory;
	/**文件名称*/
    @TableField(value = "file_name")
	private String fileName;
    /**用户ID*/
    @TableField(value = "user_id")
	private String userId;
    /**上传时间*/
    @TableField(value = "upload_time")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date uploadTime;
    /**上传的ID*/
    @TableField(value = "upload_ip")
	private String uploadIp;
    /**文件扩展名*/
    @TableField(value = "file_ext")
	private String fileExt;
    /**文件路径*/
    @TableField(value = "file_path")
	private String filePath;
    /**文件大小*/
    @TableField(value = "file_size")
	private Long fileSize;
    /**content_type*/
    @TableField(value = "content_type")
	private String contentType;
    /**状态*/
    @TableField(value = "status")
	private String status;
    ///**oss的根路径*/
    //@TableField(value = "base_path")
	//private String basePath;
}
