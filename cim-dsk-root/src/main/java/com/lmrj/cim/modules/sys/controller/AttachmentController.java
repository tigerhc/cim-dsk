package com.lmrj.cim.modules.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.modules.sys.exception.FileNameLengthLimitExceededException;
import com.lmrj.cim.modules.sys.exception.InvalidExtensionException;
import com.lmrj.cim.modules.sys.service.IAttachmentService;
import com.lmrj.cim.utils.PageRequest;
import com.lmrj.common.http.Response;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.common.utils.MessageUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.core.sys.entity.Attachment;
import com.lmrj.util.codec.EncodeUtils;
import com.lmrj.util.lang.StringUtil;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package cn.gov.gzst.oss.controller
 * @title: 附件管理控制器
 * @description: 附件管理控制器
 * @author: 张飞
 * @date: 2018-04-25 14:25:55
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Controller
@RequestMapping("attach")
public class AttachmentController extends BaseCRUDController<Attachment> {

    @Autowired
    private IAttachmentService attachmentService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @LogAspectj(logType = LogType.SELECT)
    //@RequiresMethodPermissions("list")
    public void list(HttpServletRequest request) throws IOException {
        //加入条件
        EntityWrapper<Attachment> entityWrapper = new EntityWrapper<>(Attachment.class);
        entityWrapper.orderBy("uploadTime", false);
        String fileName = request.getParameter("fileName");
        if (!StringUtil.isEmpty(fileName)) {
            entityWrapper.like("fileName", fileName);
        }
        // 预处理
        Page pageBean = attachmentService.selectPage(PageRequest.getPage(), entityWrapper);
        FastJsonUtils.print(pageBean);
    }

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @RequestMapping(value = "find")
    @LogAspectj(logType = LogType.SELECT)
    public void findByBiz(@RequestParam("biz") String biz, @RequestParam(value = "bizc", required = false) String bizCategory, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Attachment> list = attachmentService.selectList(biz, bizCategory,"");
        ServletUtils.printJson(response, list);
    }

    @RequestMapping(value = "findfile")
    @LogAspectj(logType = LogType.SELECT)
    public void findFileByBiz(@RequestParam("biz") String biz, @RequestParam(value = "bizc", required = false) String bizCategory, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Attachment> list = attachmentService.selectList(biz, bizCategory, "file");
        ServletUtils.printJson(response, list);
    }

    @RequestMapping(value = "findimg")
    @LogAspectj(logType = LogType.SELECT)
    public void findImgByBiz(@RequestParam("biz") String biz, @RequestParam(value = "bizc", required = false) String bizCategory, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Attachment> list = attachmentService.selectList(biz, bizCategory, "img");
        ServletUtils.printJson(response, list);
    }

    @PostMapping("{id}/delete")
    @LogAspectj(logType = LogType.DELETE)
    //@RequiresMethodPermissions("delete")
    public Response delete(@PathVariable("id") String id) {
        attachmentService.deleteById(id);
        return Response.ok();
    }

    @PostMapping("batch/delete")
    @LogAspectj(logType = LogType.DELETE)
    //@RequiresMethodPermissions("delete")
    public Response batchDelete(@RequestParam(value = "ids", required = false) String[] ids) {
        List<String> idList = java.util.Arrays.asList(ids);
        attachmentService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    /**
     * @param request
     * @param response
     * @return
     * @title: ajaxUpload
     * @description: 图片上传
     * @return: AjaxUploadResponse
     */
    @RequestMapping(value = "uploadimg", method = RequestMethod.POST)
    @ResponseBody
    public Response uploadImg(@RequestParam("file") MultipartFile[] files, @RequestParam(value ="biz" , required = false) String biz, @RequestParam(value ="bizc" , required = false) String bizc,
                              HttpServletRequest request, HttpServletResponse response) {
        List<Attachment> attachmentList = new ArrayList<Attachment>();
        for (MultipartFile file : files) {
            try {
                Attachment attachment = attachmentService.uploadImg(request, file, biz,bizc);
                attachmentList.add(attachment);
                continue;
            } catch (IOException e) {
                Response.error(MessageUtils.getMessage("upload.server.error"));
                continue;
            } catch (InvalidExtensionException e) {
                Response.error(MessageUtils.getMessage("upload.server.error"));
                continue;
            } catch (FileUploadBase.FileSizeLimitExceededException e) {
                Response.error(MessageUtils.getMessage("upload.server.error"));
                continue;
            } catch (FileNameLengthLimitExceededException e) {
                Response.error(MessageUtils.getMessage("upload.server.error"));
                continue;
            }
        }
        Response res = Response.ok();
        res.put("attachs", attachmentList);
        return res;
    }

    /**
     * @param request
     * @param response
     * @return
     * @title: ajaxUpload
     * @description: 文件上传
     * @return: AjaxUploadResponse
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile[] files, @RequestParam(value ="biz" , required = false) String biz, @RequestParam(value ="bizc" , required = false) String bizc, HttpServletRequest request, HttpServletResponse response) {
        List<Attachment> attachmentList = new ArrayList<Attachment>();
        for (MultipartFile file : files) {
            try {
                Attachment attachment = attachmentService.upload(request, file, biz, bizc);
                attachmentList.add(attachment);
                continue;
            } catch (IOException e) {
                Response.error(MessageUtils.getMessage("upload.server.error"));
                continue;
            } catch (InvalidExtensionException e) {
                Response.error(MessageUtils.getMessage("upload.server.error"));
                continue;
            } catch (FileUploadBase.FileSizeLimitExceededException e) {
                Response.error(MessageUtils.getMessage("upload.server.error"));
                continue;
            } catch (FileNameLengthLimitExceededException e) {
                Response.error(MessageUtils.getMessage("upload.server.error"));
                continue;
            }
        }
        Response res = Response.ok();
        res.put("attachs", attachmentList);
        return res;
    }


    @GetMapping("{attachId}/download")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public void downlad(@PathVariable("attachId") String attachId,   HttpServletRequest request, HttpServletResponse response) {
        Response response2 = Response.ok("导出成功");
        try {
            Attachment attachment = attachmentService.selectById(attachId);
            //FtpUtil.findInput()
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            //response.setHeader("Content-Disposition", "attachment;fileName=" + "111");
            response.setHeader("Content-Disposition", "attachment; filename="+ EncodeUtils.encodeUrl(attachment.getFileName()+"."+attachment.getFileExt()));

            String realAttachPath = attachment.getFilePath(); //"C:/11";
            InputStream inputStream = new FileInputStream(new File(realAttachPath));
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.close();

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
