package com.lmrj.fab.log.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.log.entity.FabLog;
import com.lmrj.fab.log.mapper.FabLogMapper;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.log.service.impl
 * @title: fab_log服务实现
 * @description: fab_log服务实现
 * @author: 张伟江
 * @date: 2020-07-07 16:09:16
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Transactional
@Service("fabLogService")
public class FabLogServiceImpl extends CommonServiceImpl<FabLogMapper, FabLog> implements IFabLogService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void log(String eqpId, String eventId, String eventName, String eventDesc, String lotNo, String userId) {
        FabLog fabLog = new FabLog();
        fabLog.setEqpId(eqpId);
        if (StringUtil.isBlank(eventId)) {
            eventId = StringUtil.randomTimeUUID("LOG");
        } else if (eventId.length() < 5) {
            eventId = StringUtil.randomTimeUUID(eventId);
        }
        fabLog.setEventId(eventId);
        fabLog.setEqpId(eqpId);
        fabLog.setEventName(eventName);
        fabLog.setEventDesc(eventDesc);
        fabLog.setLotNo(lotNo);
        fabLog.setCreateBy(userId);
        String msg = JsonUtil.toJsonString(fabLog);
        rabbitTemplate.convertAndSend("C2S.Q.FAB_LOG_D", msg);
    }

    //InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址
    //        msgMap.put("IP",address.getHostAddress());
}
