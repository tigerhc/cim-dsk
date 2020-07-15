package com.lmrj.rms.permit.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rms.permit.service.IRmsRecipePermitService;
import com.lmrj.rms.permit.entity.RmsRecipePermit;
import com.lmrj.rms.permit.mapper.RmsRecipePermitMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.permit.service.impl
* @title: rms_recipe_permit服务实现
* @description: rms_recipe_permit服务实现
* @author: 张伟江
* @date: 2020-07-15 23:08:38
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rmsRecipePermitService")
public class RmsRecipePermitServiceImpl  extends CommonServiceImpl<RmsRecipePermitMapper,RmsRecipePermit> implements  IRmsRecipePermitService {

}