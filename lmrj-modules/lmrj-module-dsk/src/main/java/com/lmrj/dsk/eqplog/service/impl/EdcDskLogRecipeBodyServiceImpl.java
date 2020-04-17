package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeBodyService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipeBody;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogRecipeBodyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service.impl
* @title: edc_dsk_log_recipe_body服务实现
* @description: edc_dsk_log_recipe_body服务实现
* @author: 张伟江
* @date: 2020-04-17 17:21:46
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcDskLogRecipeBodyService")
public class EdcDskLogRecipeBodyServiceImpl  extends CommonServiceImpl<EdcDskLogRecipeBodyMapper,EdcDskLogRecipeBody> implements  IEdcDskLogRecipeBodyService {

}