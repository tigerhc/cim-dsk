package com.lmrj.cim.config;

import com.lmrj.common.email.disruptor.EmailDao;
import com.lmrj.common.email.disruptor.EmailHelper;
import com.lmrj.core.email.dao.EmailDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package cn.dataact.lmrj.modules.config
 * @title:
 * @description: 邮件发送配置
 * @author: 张飞
 * @date: 2018/3/1 16:06
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
@Configuration
public class EmailConfig {

    @Bean
    public EmailDao emailDao(){
        EmailDao emailDao=  new EmailDaoImpl();
        return emailDao;
    }

    @Bean
    public EmailHelper emailHelper(EmailDao emailDao){
        EmailHelper emailHelper=  new EmailHelper();
        emailHelper.setHandlerCount(1);
        emailHelper.setBufferSize(1024);
        emailHelper.setEmailDao(emailDao);
        return emailHelper;
    }

}
