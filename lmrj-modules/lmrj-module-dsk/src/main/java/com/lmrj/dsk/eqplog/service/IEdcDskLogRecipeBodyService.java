package com.lmrj.dsk.eqplog.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipeBody;

import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service
* @title: edc_dsk_log_recipe_body服务接口
* @description: edc_dsk_log_recipe_body服务接口
* @author: 张伟江
* @date: 2020-04-17 17:21:46
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcDskLogRecipeBodyService extends ICommonService<EdcDskLogRecipeBody> {
    List<EdcDskLogRecipeBody> selectParamList(String recipeLogId);
}