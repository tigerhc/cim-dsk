package com.lmrj.core.email.mapper;

import com.lmrj.core.email.entity.EmailTemplate;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.web.modules.email.mapper
* @title: 邮件模板数据库控制层接口
* @description: 邮件模板数据库控制层接口
* @author: 张飞
* @date: 2018-09-12 10:59:18
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Mapper
public interface EmailTemplateMapper extends BaseMapper<EmailTemplate> {

}
