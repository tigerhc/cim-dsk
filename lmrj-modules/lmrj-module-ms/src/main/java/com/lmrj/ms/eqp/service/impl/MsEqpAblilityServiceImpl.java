package com.lmrj.ms.eqp.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.ms.eqp.service.IMsEqpAblilityService;
import com.lmrj.ms.eqp.entity.MsEqpAblility;
import com.lmrj.ms.eqp.mapper.MsEqpAblilityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.ms.eqp.service.impl
* @title: ms_eqp_ablility服务实现
* @description: ms_eqp_ablility服务实现
* @author: 张伟江
* @date: 2020-06-06 18:20:21
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("msEqpAblilityService")
public class MsEqpAblilityServiceImpl  extends CommonServiceImpl<MsEqpAblilityMapper,MsEqpAblility> implements  IMsEqpAblilityService {

}