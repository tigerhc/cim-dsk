package com.lmrj.rms.log.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rms.log.entity.RmsRecipeCheckLog;
import com.lmrj.rms.log.mapper.RmsRecipeCheckLogMapper;
import com.lmrj.rms.log.service.IRmsRecipeCheckLogService;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wdj
 * @date 2021-03-25 15:54
 */
@Transactional
@Service("rmsRecipeCheckLogService")
public class RmsRecipeCheckLogServiceImpl   extends CommonServiceImpl<RmsRecipeCheckLogMapper, RmsRecipeCheckLog> implements IRmsRecipeCheckLogService {
    @Override
    public void addLog(RmsRecipeCheckLog rmsCh) {
        baseMapper.insert(rmsCh);
        //校验后调用 一会来写
    }
}
