package com.lmrj.edc.param.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.param.entity.EdcParamDefineModel;

import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.param.service
* @title: edc_param_define_model服务接口
* @description: edc_param_define_model服务接口
* @author: zhangweijiang
* @date: 2019-06-14 23:14:31
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcParamDefineModelService extends ICommonService<EdcParamDefineModel> {

    List<String> getParamValues(String eqpId,String numType);
}