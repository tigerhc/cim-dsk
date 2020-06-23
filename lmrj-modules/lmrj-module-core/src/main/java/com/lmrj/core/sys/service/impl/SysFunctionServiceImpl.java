package com.lmrj.core.sys.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.sys.service.ISysFunctionService;
import com.lmrj.core.sys.entity.SysFunction;
import com.lmrj.core.sys.mapper.SysFunctionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.core.sys.service.impl
* @title: sys_function服务实现
* @description: sys_function服务实现
* @author: 张伟江
* @date: 2020-06-23 09:55:28
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("sysFunctionService")
public class SysFunctionServiceImpl  extends CommonServiceImpl<SysFunctionMapper,SysFunction> implements  ISysFunctionService {

}