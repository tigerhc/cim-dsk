package com.lmrj.edc.param.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.param.entity.EdcEqpLogParam;
import com.lmrj.edc.param.mapper.EdcEqpLogParamMapper;
import com.lmrj.edc.param.service.IEdcEqpLogParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.param.service.impl
* @title: edc_eqp_log_param服务实现
* @description: edc_eqp_log_param服务实现
 * @author: 高雪君
 * @date: 2021-03-30 09:05:34
 * @copyright: 2020 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcEqpLogParamService")
public class EdcEqpLogParamServiceImpl extends CommonServiceImpl<EdcEqpLogParamMapper, EdcEqpLogParam> implements IEdcEqpLogParamService {

}