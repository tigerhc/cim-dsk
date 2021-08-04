package com.lmrj.core.email.service;

import com.lmrj.core.email.entity.EmailSendLog;
import com.lmrj.common.mybatis.mvc.service.ICommonService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.web.modules.email.service
* @title: 邮件发送日志服务接口
* @description: 邮件发送日志服务接口
* @author: 张飞
* @date: 2018-09-12 10:58:46
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
public interface IEmailSendLogService extends ICommonService<EmailSendLog> {

    /**
     * <p>
     *  邮件重发
     * </p>
     *
     * @param idList 主键ID列表
     * @return boolean
     */
    boolean retrySend(List<? extends Serializable> idList);

    EmailSendLog selectEmailLog(String data);

    List<EmailSendLog> emailExport(Map<String, Object> param);
}
