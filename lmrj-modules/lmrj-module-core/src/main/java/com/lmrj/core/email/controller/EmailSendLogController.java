package com.lmrj.core.email.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.email.service.IEmailSendLogService;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.core.email.entity.EmailSendLog;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresMethodPermissions;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.collection.MapUtil;
import com.lmrj.util.lang.StringUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.utils.PageRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


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

    @RequestMapping(value = "/emailExport", method = { RequestMethod.GET, RequestMethod.POST })
    public Response emailExport(@RequestParam String email, @RequestParam String subject, @RequestParam String status){
        try {
            String title = "邮件日志导出";
            Map<String, Object> param = new HashMap<>();
            param.put("email",email);
            param.put("subject", subject);
            param.put("status", status);
            Response res = Response.ok();
            List<ExcelExportEntity> keyList= new LinkedList<>();
            List<Map<String,Object>> dataList = new LinkedList<>();
            List<EmailSendLog> emailData = emailSendLogService.emailExport(param);
            ExcelExportEntity key = new ExcelExportEntity("Email","1");
            keyList.add(key);
            ExcelExportEntity key1 = new ExcelExportEntity("发送主题","2");
            keyList.add(key1);
            ExcelExportEntity key2 = new ExcelExportEntity("发送状态","3");
            keyList.add(key2);
            ExcelExportEntity key3 = new ExcelExportEntity("重试次数","4");
            keyList.add(key3);
            ExcelExportEntity key4 = new ExcelExportEntity("返回消息","5");
            keyList.add(key4);
            ExcelExportEntity key5 = new ExcelExportEntity("响应时间","6");
            keyList.add(key5);
            ExcelExportEntity key6 = new ExcelExportEntity("邮件内容","7");
            keyList.add(key6);

            emailData.forEach(emailSendLog -> {
                Map<String, Object> data = new HashMap<>() ;
                data.put("1",emailSendLog.getEmail());
                data.put("2",emailSendLog.getSubject());
                String dataStatus = emailSendLog.getStatus();
                if("1".equals(dataStatus)){
                    dataStatus = "发送成功";
                } else if ("-1".equals(dataStatus)){
                    dataStatus = "发送失败";
                } else if("0".equals(dataStatus)){
                    dataStatus = "待发送";
                }
                data.put("3",dataStatus);
                data.put("4",emailSendLog.getTryNum());
                data.put("5",emailSendLog.getMsg());
                data.put("6",emailSendLog.getResponseDateStr());
                data.put("7",emailSendLog.getContent());
                dataList.add(data);
            });

            Workbook book = ExcelExportUtil.exportExcel(new ExportParams("邮件日志导出","发送信息"),keyList,dataList);
            FileOutputStream fos = new FileOutputStream("D:/ExcelExportForMap.xls");
            book.write(fos);
            fos.close();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            book.write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtil.bytesToHexString2(bytes);
            title = title + "-" + DateUtil.getDateTime();
            res.put("bytes", bytesRes);
            res.put("title", title);
            return res;
        } catch (Exception var16) {
            var16.printStackTrace();
            return Response.error(999998, "导出失败");
        }
    }
}
