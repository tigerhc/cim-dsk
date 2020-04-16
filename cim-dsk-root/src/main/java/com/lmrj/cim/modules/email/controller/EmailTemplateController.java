package com.lmrj.cim.modules.email.controller;

import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.cim.modules.email.entity.EmailTemplate;
import com.lmrj.cim.modules.email.service.IEmailTemplateService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.modules.email.controller
 * @title: 邮件模板控制器
 * @description: 邮件模板控制器
 * @author: 张飞
 * @date: 2018-09-12 10:59:18
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/email/template")
@RequiresPathPermission("email:template")
@ViewPrefix("modules/email/template")
@LogAspectj(title = "邮件模板")
public class EmailTemplateController extends BaseBeanController<EmailTemplate> {

    @Autowired
    private IEmailTemplateService emailTemplateService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @LogAspectj(logType = LogType.SELECT)
    @RequiresMethodPermissions("list")
    public void list( HttpServletRequest request) throws IOException {
        //加入条件
        EntityWrapper<EmailTemplate> entityWrapper = new EntityWrapper<>(EmailTemplate.class);
        entityWrapper.orderBy("createDate",false);
        String code= request.getParameter("code");
        if (!StringUtil.isEmpty(code)){
            entityWrapper.like("code",code);
        }
        String name=request.getParameter("name");
        if (!StringUtil.isEmpty(name)){
            entityWrapper.like("name",name);
        }
        // 预处理
        Page pageBean = emailTemplateService.selectPage(PageRequest.getPage(),entityWrapper);
        FastJsonUtils.print(pageBean,EmailTemplate.class,"id,name,code,templateSubject,templateContent");
    }

    @PostMapping("add")
    @LogAspectj(logType = LogType.INSERT)
    @RequiresMethodPermissions("add")
    public Response add(EmailTemplate entity, BindingResult result,
                        HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity,result);
        String templateCode = StringUtil.randomString(10);
        entity.setCode(templateCode);
        emailTemplateService.insert(entity);
        return Response.ok("添加成功");
    }


    @PostMapping("{id}/update")
    @RequiresMethodPermissions("add")
    @LogAspectj(logType = LogType.UPDATE)
    public Response update(EmailTemplate entity, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity,result);
        emailTemplateService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @PostMapping("{id}/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response delete(@PathVariable("id") String id) {
        emailTemplateService.deleteById(id);
        return Response.ok("删除成功");
    }

    @PostMapping("batch/delete")
    @LogAspectj(logType = LogType.DELETE)
    @RequiresMethodPermissions("delete")
    public Response batchDelete(@RequestParam("ids") String[] ids) {
        List<String> idList = java.util.Arrays.asList(ids);
        emailTemplateService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
