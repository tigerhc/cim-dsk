package com.lmrj.cim.modules.oa.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.cim.modules.oa.entity.OaNotificationEntity;
import com.lmrj.cim.modules.oa.mapper.OaNotificationMapper;
import com.lmrj.cim.modules.oa.service.IOaNotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: 通知公告
 * @Description: 通知公告
 * @author lmrj
 * @date 2017-06-10 17:15:17
 * @version V1.0
 *
 */
@Transactional
@Service("oaNotificationService")
public class OaNotificationServiceImpl  extends CommonServiceImpl<OaNotificationMapper, OaNotificationEntity> implements IOaNotificationService {

}
