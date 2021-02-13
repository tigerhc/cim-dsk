package com.lmrj.cim.modules.monitor.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.cim.modules.monitor.entity.LoginLog;
import com.lmrj.cim.modules.monitor.mapper.LoginLogMapper;
import com.lmrj.cim.modules.monitor.service.ILoginLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.web.modules.sys.service.impl
* @title: 登陆日志服务实现
* @description: 登陆日志服务实现
* @author: sys
* @date: 2018-09-28 11:31:36
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("loginlogService")
public class LoginLogServiceImpl  extends CommonServiceImpl<LoginLogMapper,LoginLog> implements ILoginLogService {

}
