package com.lmrj.cim.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.lmrj.cim.modules.sys.exception.FileNameLengthLimitExceededException;
import com.lmrj.cim.modules.sys.exception.InvalidExtensionException;
import com.lmrj.cim.modules.sys.mapper.AttachmentMapper;
import com.lmrj.cim.modules.sys.service.IAttachmentService;
import com.lmrj.cim.utils.FileUploadUtil;
import com.lmrj.cim.utils.UserUtils;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.utils.IpUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.sys.entity.Attachment;
import com.lmrj.util.file.FileUtil;
import com.lmrj.util.lang.StringUtil;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package cn.gov.gzst.oss.service.impl
* @title: 附件管理服务实现
* @description: 附件管理服务实现
* @author: 张飞
* @date: 2018-04-25 14:25:54
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("attachmentService")
public class AttachmentServiceImpl extends CommonServiceImpl<AttachmentMapper, Attachment>
        implements IAttachmentService {
    public static final String DEFAULT_CONFIG_FILE = "upload.properties";
    protected String configname = DEFAULT_CONFIG_FILE;
    @Value("${upload.max.size}")
    private long maxSize = 0;
    private boolean needDatePathAndRandomName = true;
    @Value("#{'${upload.allowed.extension}'.split(',')}")
    private String[] allowedExtension;
    @Value("${upload.base.dir}")
    private String baseDir;

    @Override
    public Page<Attachment> selectPage(Page<Attachment> page, Wrapper<Attachment> wrapper) {
        Page<Attachment> pageInfo = new Page<Attachment>();
        wrapper.eq("1", "1");
        int total = baseMapper.selectCount(wrapper);
        List<Attachment> records = baseMapper.selectAttachmentPage(page, wrapper);
        pageInfo.setTotal(total);
        pageInfo.setRecords(records);
        return pageInfo;
    }

    @Override
    public List<Attachment> selectList(String bizId, String bizCategory ) {
        if(StringUtil.isBlank(bizId)){
            return Lists.newArrayList();
        }
        Wrapper wrapper = new EntityWrapper<Attachment>().eq("biz_id", bizId);
        if(StringUtil.isNotBlank(bizCategory)){
            wrapper.eq("biz_category", bizCategory);
        }
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Attachment upload(HttpServletRequest request, MultipartFile file) throws FileSizeLimitExceededException,
            InvalidExtensionException, FileNameLengthLimitExceededException, IOException {
        String url = FileUploadUtil.upload(request, baseDir, file, allowedExtension, maxSize,
                needDatePathAndRandomName);
        String filename = file.getOriginalFilename();
        long size = file.getSize();
        String realFileName = StringUtil.getFileNameNoEx(filename);
        String fileext = StringUtil.getExtensionName(filename);
        // 保存上传的信息
        Attachment attachment = new Attachment();
        attachment.setFileExt(fileext);
        attachment.setFileName(realFileName);
        attachment.setFilePath(url);
        attachment.setFileSize(size);
        attachment.setStatus("1");
        attachment.setUploadIp(IpUtils.getIpAddr(request));
        attachment.setUploadTime(new Date());
        attachment.setUserId(UserUtils.getUser().getId());
        insert(attachment);
        return attachment;
    }

    @Override
    public boolean deleteBatchIds(Collection<? extends Serializable> idList) {
        for (Object object : idList) {
            deleteById((Serializable) object);
        }
        return true;
    }

    @Override
    public boolean deleteById(Serializable id) {
        Attachment attachment = selectById(id);
        String basePath = ServletUtils.getRequest().getServletContext().getRealPath("/");
        String filePath = attachment.getFilePath();
        FileUtil.delFile(basePath + filePath);
        return super.deleteById(id);
    }

}
