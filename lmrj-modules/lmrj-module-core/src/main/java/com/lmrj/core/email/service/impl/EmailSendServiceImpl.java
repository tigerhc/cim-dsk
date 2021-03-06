package com.lmrj.core.email.service.impl;

import com.lmrj.core.email.entity.EmailSendLog;
import com.lmrj.core.email.entity.EmailTemplate;
import com.lmrj.core.email.service.IEmailSendLogService;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.core.email.service.IEmailTemplateService;
import com.lmrj.common.email.disruptor.EmailHelper;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.modules.email.service.impl
 * @title:
 * @description: 邮件发送
 * @author: 张飞
 * @date: 2018/9/12 11:15
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
@Service("emailSendService")
public class EmailSendServiceImpl implements IEmailSendService {

    @Autowired
    private IEmailTemplateService emailTemplateService;
    @Autowired
    private IEmailSendLogService emailSendLogService;
    @Autowired
    private EmailHelper emailHelper; //自动注入的Bean

    @Value("${spring.mail.sender}")
    private String sender; //昵称

    @Override
    public void send(String to, String code, Map<String, Object> datas) {
        String[] tos = {to};
        if(to.indexOf(",")>0){
            tos = to.split(",");
        }
        send(tos, code, datas);
    }

    @Override
    public void send(String[] emails, String code, Map<String, Object> datas) {
        EmailTemplate template = emailTemplateService.selectOne(new EntityWrapper<EmailTemplate>().eq("code", code));
        if (datas == null) {
            datas = new HashMap<>();
        }
        if (template == null) {
            return;
        }
        String content = parseContent(StringEscapeUtils.unescapeHtml4(template.getTemplateContent()), datas);
        String subject = parseContent(StringEscapeUtils.unescapeHtml4(template.getTemplateSubject()), datas);
        // List<EmailSendLog> emailSendLogList = new ArrayList<EmailSendLog>();
        EmailSendLog emailSendLog = new EmailSendLog();
        String email = StringUtil.join(emails, ",");
        emailSendLog.setEmail(email);
        emailSendLog.setSubject(subject);
        emailSendLog.setContent(content);
        emailSendLog.setMsg("发送成功");
        emailSendLog.setSendCode(code);
        emailSendLog.setResponseDate(new Date());
        emailSendLog.setSendData(JsonUtil.toJsonString(datas));
        emailSendLog.setStatus(EmailSendLog.EMAIL_SEND_STATUS_IN);
        emailSendLog.setTryNum(0);
        emailSendLog.setDelFlag("0");
        // emailSendLogList.add(emailSendLog);
        emailSendLogService.insert(emailSendLog);
        String id = emailSendLog.getId();
        // 发送邮件
        this.doSend(id, emails, subject, content);
    }

    @Override
    public void send(String eventId, String email, String code, Map<String, Object> datas) {
        String[] emails = {email};
        send(eventId, emails, code, datas);
    }

    @Override
    public void send(String eventId, String[] emails, String code, Map<String, Object> datas) {
        EmailTemplate template = emailTemplateService.selectOne(new EntityWrapper<EmailTemplate>().eq("code", code));
        if (datas == null) {
            datas = new HashMap<>();
        }
        if (template == null) {
            return;
        }
        String content = parseContent(StringEscapeUtils.unescapeHtml4(template.getTemplateContent()), datas);
        String subject = parseContent(StringEscapeUtils.unescapeHtml4(template.getTemplateSubject()), datas);
        doSend(eventId, emails, subject, content);
    }


    @Override
    public void doSend(String eventId, String to, String subject, String text) {
        String[] tos = {to};
        if(to.indexOf(",")>0){
            tos = to.split(",");
        }
        doSend(eventId, tos, subject, text);
    }

    @Override
    public void doSend(String eventId, String[] tos, String subject, String text) {
        try {
            MimeMessage message = emailHelper.createMimeMessage(null);//创建一个MINE消息
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(tos);
            helper.setSubject(subject);
            helper.setText(text, true);
            emailHelper.sendAsync(eventId, message, null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void sendOneByOne(String[] emails, String code, Map<String, Object> datas) {
        EmailTemplate template = emailTemplateService.selectOne(new EntityWrapper<EmailTemplate>().eq("code", code));
        if (datas == null) {
            datas = new HashMap<>();
        }
        if (template == null) {
            return;
        }
        String content = parseContent(StringEscapeUtils.unescapeHtml4(template.getTemplateContent()), datas);
        String subject = parseContent(StringEscapeUtils.unescapeHtml4(template.getTemplateSubject()), datas);
        for (String to : emails) {
            EmailSendLog emailSendLog = new EmailSendLog();
            emailSendLog.setEmail(to);
            emailSendLog.setSubject(subject);
            emailSendLog.setContent(content);
            emailSendLog.setMsg("发送成功");
            emailSendLog.setSendCode(code);
            emailSendLog.setResponseDate(new Date());
            emailSendLog.setSendData(JsonUtil.toJsonString(datas));
            emailSendLog.setStatus(EmailSendLog.EMAIL_SEND_STATUS_IN);
            emailSendLog.setTryNum(0);
            emailSendLog.setDelFlag("0");
            emailSendLogService.insert(emailSendLog);
            // 发送邮件
            doSend(emailSendLog.getId(), to, subject, content);
        }
    }

    private String parseContent(String content, Map<String, Object> dataMap) {
        try {
            String tempname = StringUtil.hashKeyForDisk(content);
            Configuration configuration = new Configuration();
            configuration.setNumberFormat("#");
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate(tempname, content);
            freemarker.template.Template template = new freemarker.template.Template(tempname, new StringReader(content));
            StringWriter stringWriter = new StringWriter();
            template.process(dataMap, stringWriter);
            configuration.setTemplateLoader(stringLoader);
            content = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("模板解析失败");
        }
        return content;
    }
}
