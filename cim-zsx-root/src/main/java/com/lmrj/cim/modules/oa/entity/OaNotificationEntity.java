package com.lmrj.cim.modules.oa.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.lmrj.core.entity.DataEntity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @Title: 通知公告
 * @Description: 通知公告
 * @author lmrj
 * @date 2017-06-10 17:15:17
 * @version V1.0
 *
 */
@TableName("oa_notification")
@SuppressWarnings("serial")
@Data
public class OaNotificationEntity extends DataEntity {

	/** 字段主键 */
	@TableId(value = "id", type = IdType.UUID)
	private String id;
	/** 标题 */
	@TableField(value = "no_subject")
	@Excel(name = "标题", orderNum = "1", width = 20)
	private String subject;
	/** 内容 */
	@TableField(value = "content")
	@Excel(name = "内容", orderNum = "1", width = 40)
	private String content;
	/** 发布状态 */
	@TableField(value = "status")
	@Excel(name = "有效标识", orderNum = "1", width = 20)
	private String status;
	/** 创建时间 */
	@TableField(value = "create_date")
	@Excel(name = "创建时间", orderNum = "1", width = 40)
	private Date createDate;

}
