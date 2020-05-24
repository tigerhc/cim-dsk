package com.lmrj.core.email.service;

import java.util.Map;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.bbs.modules.sys.service
* @title: 系统消息服务接口
* @description: 系统消息服务接口
* @author: 张飞
* @date: 2018-09-03 15:10:32
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
public interface IEmailSendService {
    void send(String email, String code, Map<String, Object> datas);

    void send(String[] emails, String code, Map<String, Object> datas);

    void send(String eventId, String email, String code, Map<String, Object> datas);

    void send(String eventId, String[] emails, String code, Map<String, Object> datas);

    void sendEmail(String eventId,String to,String subject,String text);
}
