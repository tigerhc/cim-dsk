package com.lmrj.cim.modules.monitor.mapper;

import com.lmrj.cim.modules.monitor.entity.OperationLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.web.modules.sys.mapper
* @title: 操作日志数据库控制层接口
* @description: 操作日志数据库控制层接口
* @author: 张飞
* @date: 2018-09-30 15:53:25
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

}
