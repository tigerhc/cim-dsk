package com.lmrj.rms.log.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rms.log.entity.RmsRecipeCheckLog;
import com.lmrj.rms.recipe.entity.RmsRecipe;

/**
 * @author wdj
 * @date 2021-03-25 15:49
 */
public interface IRmsRecipeCheckLogService  extends ICommonService<RmsRecipeCheckLog> {
    void addLog(RmsRecipeCheckLog rmsCh);
}
