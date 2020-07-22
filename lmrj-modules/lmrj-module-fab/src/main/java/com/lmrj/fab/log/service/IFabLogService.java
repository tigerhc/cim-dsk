package com.lmrj.fab.log.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.log.entity.FabLog;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.fab.log.service
* @title: fab_log服务接口
* @description: fab_log服务接口
* @author: 张伟江
* @date: 2020-07-07 16:09:16
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IFabLogService extends ICommonService<FabLog> {

    void log(String eqpId, String eventId, String eventName, String eventDesc, String lotNo, String userId);
}
