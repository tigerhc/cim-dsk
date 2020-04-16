package com.lmrj.core.sys.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.sys.service.ISysConfigService;
import com.lmrj.core.sys.entity.SysConfig;
import com.lmrj.core.sys.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.sys.config.service.impl
* @title: sys_config服务实现
* @description: sys_config服务实现
* @author: zhangweijiang
* @date: 2019-07-14 03:03:35
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("sysConfigService")
public class SysConfigServiceImpl  extends CommonServiceImpl<SysConfigMapper,SysConfig> implements  ISysConfigService {

}
