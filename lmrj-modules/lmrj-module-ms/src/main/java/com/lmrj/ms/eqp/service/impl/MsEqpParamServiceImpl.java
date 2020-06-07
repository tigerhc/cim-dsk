package com.lmrj.ms.eqp.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.ms.eqp.service.IMsEqpParamService;
import com.lmrj.ms.eqp.entity.MsEqpParam;
import com.lmrj.ms.eqp.mapper.MsEqpParamMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.ms.eqp.service.impl
* @title: ms_eqp_param服务实现
* @description: ms_eqp_param服务实现
* @author: 张伟江
* @date: 2020-06-06 18:19:46
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("msEqpParamService")
public class MsEqpParamServiceImpl  extends CommonServiceImpl<MsEqpParamMapper,MsEqpParam> implements  IMsEqpParamService {

}