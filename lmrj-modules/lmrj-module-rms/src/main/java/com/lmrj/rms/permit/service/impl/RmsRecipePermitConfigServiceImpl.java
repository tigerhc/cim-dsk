package com.lmrj.rms.permit.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rms.permit.service.IRmsRecipePermitConfigService;
import com.lmrj.rms.permit.entity.RmsRecipePermitConfig;
import com.lmrj.rms.permit.mapper.RmsRecipePermitConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.permit.service.impl
* @title: rms_recipe_permit_config服务实现
* @description: rms_recipe_permit_config服务实现
* @author: 张伟江
* @date: 2020-07-15 23:08:59
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rmsRecipePermitConfigService")
public class RmsRecipePermitConfigServiceImpl  extends CommonServiceImpl<RmsRecipePermitConfigMapper,RmsRecipePermitConfig> implements  IRmsRecipePermitConfigService {

}