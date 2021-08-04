package com.lmrj.core.email.service.impl;

import com.lmrj.core.email.entity.EmailSendLog;
import com.lmrj.core.email.mapper.EmailSendLogMapper;
import com.lmrj.core.email.service.IEmailSendLogService;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


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
public class EmailSendLogServiceImpl  extends CommonServiceImpl<EmailSendLogMapper,EmailSendLog> implements IEmailSendLogService {
    @Autowired
    private IEmailSendService emailSendService;
    @Override
    public boolean retrySend(List<? extends Serializable> idList) {
        //List<EmailSendLog>  smsSendLogList=new ArrayList<>();
        for (Serializable id: idList) {
            EmailSendLog sendLog=selectById(id);
            sendLog.setTryNum(0);
            if(EmailSendLog.EMAIL_SEND_STATUS_SUCCESS.equals(sendLog.getStatus()) ){
                sendLog.setId("");
                sendLog.setStatus(EmailSendLog.EMAIL_SEND_STATUS_IN);
            }else{
                sendLog.setStatus(EmailSendLog.EMAIL_SEND_STATUS_FAIL);
            }
            this.insertOrUpdate(sendLog);
            //smsSendLogList.add(sendLog);
            emailSendService.doSend(sendLog.getId(),sendLog.getEmail(),sendLog.getSubject(),sendLog.getContent());
        }
        //insertOrUpdateBatch(smsSendLogList);
        return false;
    }

    @Override
    public EmailSendLog selectEmailLog(String data){
        return baseMapper.selectEmailLog(data);
    }

    @Override
    public List<EmailSendLog> emailExport(Map<String, Object> param) {
        return baseMapper.emailExport(param);
    }

}
