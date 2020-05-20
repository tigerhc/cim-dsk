package com.lmrj.cim.modules.sys.service;

import com.lmrj.cim.modules.sys.exception.FileNameLengthLimitExceededException;
import com.lmrj.cim.modules.sys.exception.InvalidExtensionException;
import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.sys.entity.Attachment;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package cn.gov.gzst.oss.service
 * @title: 附件管理服务接口
 * @description: 附件管理服务接口
 * @author: 张飞
 * @date: 2018-04-25 14:25:55
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
public interface IAttachmentService extends ICommonService<Attachment> {
    public Attachment upload(HttpServletRequest request, MultipartFile file) throws FileUploadBase.FileSizeLimitExceededException,
            InvalidExtensionException, FileNameLengthLimitExceededException, IOException;

    List<Attachment> selectList(String bizId, String bizCategory);
}
