package com.lmrj.dsk.eqplog.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service
* @title: edc_dsk_log_recipe服务接口
* @description: edc_dsk_log_recipe服务接口
* @author: 张伟江
* @date: 2020-04-17 17:21:17
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcDskLogRecipeService extends ICommonService<EdcDskLogRecipe> {
    Boolean exportRecipeFile(String eqpId, Date startTime, Date endTime);

    String findOldData(String eqpId, String startTime,String paramName);

    List<Map<String,String>> findData(String eqpId, Date startTime, Date endTime);
}