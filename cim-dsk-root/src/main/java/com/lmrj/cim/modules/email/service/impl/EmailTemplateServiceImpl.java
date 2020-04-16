package com.lmrj.cim.modules.email.service.impl;

import com.lmrj.cim.modules.email.entity.EmailTemplate;
import com.lmrj.cim.modules.email.mapper.EmailTemplateMapper;
import com.lmrj.cim.modules.email.service.IEmailTemplateService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.web.modules.email.service.impl
* @title: 邮件模板服务实现
* @description: 邮件模板服务实现
* @author: 张飞
* @date: 2018-09-12 10:59:18
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("emailtemplateService")
public class EmailTemplateServiceImpl  extends CommonServiceImpl<EmailTemplateMapper,EmailTemplate> implements  IEmailTemplateService {

}
