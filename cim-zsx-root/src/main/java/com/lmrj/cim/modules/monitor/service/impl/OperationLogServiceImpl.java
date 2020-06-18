package com.lmrj.cim.modules.monitor.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.cim.modules.monitor.entity.OperationLog;
import com.lmrj.cim.modules.monitor.mapper.OperationLogMapper;
import com.lmrj.cim.modules.monitor.service.IOperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.web.modules.sys.service.impl
* @title: 操作日志服务实现
* @description: 操作日志服务实现
* @author: 张飞
* @date: 2018-09-30 15:53:25
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("operationlogService")
public class OperationLogServiceImpl  extends CommonServiceImpl<OperationLogMapper,OperationLog> implements IOperationLogService {

}
