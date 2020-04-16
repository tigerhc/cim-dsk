package com.lmrj.edc.param.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.param.service.IEdcParamDefineModelService;
import com.lmrj.edc.param.entity.EdcParamDefineModel;
import com.lmrj.edc.param.mapper.EdcParamDefineModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.param.service.impl
* @title: edc_param_define_model服务实现
* @description: edc_param_define_model服务实现
* @author: zhangweijiang
* @date: 2019-06-14 23:14:33
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcParamDefineModelService")
public class EdcParamDefineModelServiceImpl  extends CommonServiceImpl<EdcParamDefineModelMapper,EdcParamDefineModel> implements  IEdcParamDefineModelService {

}