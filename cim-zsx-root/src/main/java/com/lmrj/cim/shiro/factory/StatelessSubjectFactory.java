package com.lmrj.cim.shiro.factory;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package cn.dataact.lmrj.security.shiro.factory
 * @title:
 * @description: 无状态访问
 * @author: 张飞
 * @date: 2018/3/16 11:35
 * @copyright: 2017 www.gzst.gov.cn Inc. All rights reserved.
 */
public class StatelessSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context) {
        //不创建session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
