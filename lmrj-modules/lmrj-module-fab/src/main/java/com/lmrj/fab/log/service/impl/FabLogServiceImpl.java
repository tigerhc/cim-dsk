package com.lmrj.fab.log.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.log.entity.FabLog;
import com.lmrj.fab.log.mapper.FabLogMapper;
import com.lmrj.fab.log.service.IFabLogService;
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
public class FabLogServiceImpl  extends CommonServiceImpl<FabLogMapper,FabLog> implements  IFabLogService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    //公共打日志方法
    @Override
    public void save(String eventId, String userId, String deviceCode, String eventName, String eventDesc, String lotId){
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("eventId", eventId);
        msgMap.put("msgName", "LogSave");
        msgMap.put("userId",userId);
        msgMap.put("deviceCode", deviceCode);
        msgMap.put("eventName",eventName);
        msgMap.put("eventDesc",eventDesc);
        msgMap.put("lotId",lotId);
        try {
            InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址
            msgMap.put("IP",address.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String msg = JsonUtil.toJsonString(msgMap);
        rabbitTemplate.convertAndSend("C2S.Q.FAB_LOG_D", msg);
    }
}
