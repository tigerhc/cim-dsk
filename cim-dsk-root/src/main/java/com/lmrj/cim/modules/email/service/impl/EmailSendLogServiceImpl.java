package com.lmrj.cim.modules.email.service.impl;

import com.lmrj.cim.modules.email.mapper.EmailSendLogMapper;
import com.lmrj.cim.modules.email.entity.EmailSendLog;
import com.lmrj.cim.modules.email.service.IEmailSendLogService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.web.modules.email.service.impl
* @title: 邮件发送日志服务实现
* @description: 邮件发送日志服务实现
* @author: 张飞
* @date: 2018-09-12 10:58:46
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("emailsendlogService")
public class EmailSendLogServiceImpl  extends CommonServiceImpl<EmailSendLogMapper,EmailSendLog> implements  IEmailSendLogService {

    @Override
    public boolean retrySend(List<? extends Serializable> idList) {
        List<EmailSendLog>  smsSendLogList=new ArrayList<>();
        for (Serializable id: idList) {
            EmailSendLog sendLog=selectById(id);
            sendLog.setTryNum(0);
            sendLog.setStatus(EmailSendLog.EMAIL_SEND_STATUS_FAIL);
            smsSendLogList.add(sendLog);
        }
        insertOrUpdateBatch(smsSendLogList);
        return false;
    }

}
