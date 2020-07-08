package com.lmrj.core.sys.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.sys.entity.SysConfig;

import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.sys.config.service
* @title: sys_config服务接口
* @description: sys_config服务接口
* @author: zhangweijiang
* @date: 2019-07-14 03:03:35
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface ISysConfigService extends ICommonService<SysConfig> {

    List<SysConfig>  queryByConfigKey(String key);

}
