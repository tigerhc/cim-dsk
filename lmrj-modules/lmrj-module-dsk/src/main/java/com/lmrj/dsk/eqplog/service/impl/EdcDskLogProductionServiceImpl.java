package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogProductionMapper;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service.impl
* @title: edc_dsk_log_production服务实现
* @description: edc_dsk_log_production服务实现
* @author: 张伟江
* @date: 2020-04-14 10:10:00
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcDskLogProductionService")
public class EdcDskLogProductionServiceImpl  extends CommonServiceImpl<EdcDskLogProductionMapper,EdcDskLogProduction> implements  IEdcDskLogProductionService {

    //@Override
    //public boolean insert(EdcDskLogProduction edcDskLogProduction) {
    //    // 保存主表
    //    super.insert(edcDskLogProduction);
    //    // 保存细表
    //    List<EdcDskLogRecipeBody> edcDskLogRecipeBodyList = edcDskLogProduction.get();
    //    for (EdcDskLogRecipeBody edcDskLogRecipeBody : edcDskLogRecipeBodyList) {
    //        edcDskLogRecipeBody.setRecipeLogId(edcDskLogRecipe.getId());
    //    }
    //    edcDskLogRecipeBodyService.insertBatch(edcDskLogRecipeBodyList);
    //    return true;
    //}
}
