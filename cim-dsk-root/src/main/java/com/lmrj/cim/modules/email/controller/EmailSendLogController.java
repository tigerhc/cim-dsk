package com.lmrj.cim.modules.email.controller;

import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.cim.modules.email.entity.EmailSendLog;
import com.lmrj.cim.modules.email.service.IEmailSendLogService;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.util.lang.StringUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.utils.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.modules.email.controller
 * @title: 邮件发送日志控制器
 * @description: 邮件发送日志控制器
 * @author: 张飞
 * @date: 2018-09-12 10:58:46
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/email/sendlog")
@RequiresPathPermission("email:sendlog")
@ViewPrefix("modules/email/sendlog")
@LogAspectj(title = "邮件发送日志")
public class EmailSendLogController extends BaseBeanController<EmailSendLog> {

    @Autowired
    private IEmailSendLogService emailSendLogService;


    @GetMapping(value = "list")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void list( HttpServletRequest request) throws IOException {
        //加入条件
        EntityWrapper<EmailSendLog> entityWrapper = new EntityWrapper<>(EmailSendLog.class);
        entityWrapper.orderBy("responseDate",false);
        String subject= request.getParameter("subject");
        if (!StringUtil.isEmpty(subject)){
            entityWrapper.like("subject",subject);
        }
        String email= request.getParameter("email");
        if (!StringUtil.isEmpty(email)){
            entityWrapper.eq("email",email);
        }
        String status=request.getParameter("status");
        if (!StringUtil.isEmpty(status)){
            entityWrapper.eq("status",status);
        }
        // 预处理
        Page pageBean = emailSendLogService.selectPage(PageRequest.getPage(),entityWrapper);
        FastJsonUtils.print(pageBean,EmailSendLog.class,"id,email,subject,content,sendData,sendCode,responseDate,tryNum,msg,status,delFlag,emarks");
    }

    @PostMapping("{id}/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response delete(@PathVariable("id") String id) {
        emailSendLogService.deleteById(id);
        return Response.ok("删除成功");
    }

    @PostMapping("batch/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response batchDelete(@RequestParam("ids") String[] ids) {
        List<String> idList = java.util.Arrays.asList(ids);
        emailSendLogService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    @PostMapping(value = "retrySend")
    public Response retrySend(@RequestParam(value = "ids", required = false) String[] ids) {
        try {
            List<String> idList = java.util.Arrays.asList(ids);
            emailSendLogService.retrySend(idList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("重发队列添加失败");
        }
        return Response.ok("重发队列添加成功");
    }
}
