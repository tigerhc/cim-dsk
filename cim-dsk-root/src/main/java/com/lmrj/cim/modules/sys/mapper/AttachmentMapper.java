package com.lmrj.cim.modules.sys.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.core.sys.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package cn.gov.gzst.oss.mapper
* @title: 附件管理数据库控制层接口
* @description: 附件管理数据库控制层接口
* @author: 张飞
* @date: 2018-04-25 14:25:54
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Mapper
public interface AttachmentMapper extends BaseMapper<Attachment> {
    List<Attachment> selectAttachmentPage(Page<Attachment> page, @Param("ew") Wrapper<Attachment> wrapper);
}
