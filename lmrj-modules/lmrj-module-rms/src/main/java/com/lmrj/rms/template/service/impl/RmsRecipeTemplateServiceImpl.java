package com.lmrj.rms.template.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rms.template.service.IRmsRecipeTemplateService;
import com.lmrj.rms.template.entity.RmsRecipeTemplate;
import com.lmrj.rms.template.mapper.RmsRecipeTemplateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.template.service.impl
* @title: rms_recipe_template服务实现
* @description: rms_recipe_template服务实现
* @author: zhangweijiang
* @date: 2019-06-15 01:58:43
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rmsRecipeTemplateService")
public class RmsRecipeTemplateServiceImpl  extends CommonServiceImpl<RmsRecipeTemplateMapper,RmsRecipeTemplate> implements  IRmsRecipeTemplateService {

}