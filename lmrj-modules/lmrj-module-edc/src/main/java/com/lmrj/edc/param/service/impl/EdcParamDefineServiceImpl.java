package com.lmrj.edc.param.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.param.service.IEdcParamDefineService;
import com.lmrj.edc.param.entity.EdcParamDefine;
import com.lmrj.edc.param.mapper.EdcParamDefineMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.param.service.impl
* @title: edc_param_define服务实现
* @description: edc_param_define服务实现
* @author: 张伟江
* @date: 2019-06-14 23:05:34
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcParamDefineService")
public class EdcParamDefineServiceImpl  extends CommonServiceImpl<EdcParamDefineMapper,EdcParamDefine> implements  IEdcParamDefineService {

}