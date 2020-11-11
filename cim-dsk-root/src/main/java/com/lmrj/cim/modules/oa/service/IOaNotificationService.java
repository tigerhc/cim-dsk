package com.lmrj.cim.modules.oa.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.cim.modules.oa.entity.OaNotification;

import java.util.List;
import java.util.Map;

/**
 * @Title: 通知公告
 * @Description: 通知公告
 * @author lmrj
 * @date 2017-06-10 17:15:17
 * @version V1.0
 *
 */
public interface IOaNotificationService extends ICommonService<OaNotification> {
    List<Map<String,Object>> findList();
}

