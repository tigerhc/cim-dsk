package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogRecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service.impl
* @title: edc_dsk_log_recipe服务实现
* @description: edc_dsk_log_recipe服务实现
* @author: 张伟江
* @date: 2020-04-17 17:21:17
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcDskLogRecipeService")
public class EdcDskLogRecipeServiceImpl  extends CommonServiceImpl<EdcDskLogRecipeMapper,EdcDskLogRecipe> implements  IEdcDskLogRecipeService {

}