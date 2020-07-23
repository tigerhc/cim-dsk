package com.lmrj.rms.permit.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.rms.permit.entity.RmsRecipePermitConfig;
import com.lmrj.rms.permit.service.IRmsRecipePermitConfigService;
import com.lmrj.rms.permit.service.IRmsRecipePermitService;
import com.lmrj.rms.permit.entity.RmsRecipePermit;
import com.lmrj.rms.permit.mapper.RmsRecipePermitMapper;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


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

    @Autowired
    private IRmsRecipeService recipeService;
    @Autowired
    private IRmsRecipePermitConfigService recipePermitConfigService;

    @Override
    public void recipePermit(String approveStep, String roleName, String recipeId, String submitResult, String submitDesc) throws Exception{
        RmsRecipePermit recipePermit = new RmsRecipePermit();
        List<RmsRecipePermitConfig> permitConfigs = recipePermitConfigService.selectList(new EntityWrapper<RmsRecipePermitConfig>().eq("submitter_role_name", roleName));
        RmsRecipePermitConfig recipePermitConfig = new RmsRecipePermitConfig();
        if (permitConfigs.size() > 0){
            recipePermitConfig = permitConfigs.get(0);
        }
        RmsRecipe recipe = recipeService.selectById(recipeId);
        recipePermit.setRecipeId(recipeId);
        recipePermit.setRecipeCode(recipe.getRecipeCode());
        recipePermit.setWiRecipeId(recipeId);
        recipePermit.setSubmitterId(recipePermitConfig.getSubmitterRoleId());
        recipePermit.setSubmitterRole(roleName);
        recipePermit.setSubmitResult(submitResult);
        recipePermit.setSubmitDesc(submitDesc);
        recipePermit.setSubmitDate(new Date());
        baseMapper.insert(recipePermit);
        if ("1".equals(submitResult)){
            //审批通过，修改配方审批结果，并将审批状态加1,如果审批状态为3则将审批状态改为0
            if ("EQP".equals(recipe.getVersionType())){
                if ("2".equals(approveStep)){
                    recipe.setApproveStep("0");
                } else {
                    int i = Integer.parseInt(approveStep);
                    i++;
                    recipe.setApproveStep(i + "");
                }
            }
            if ("GOLD".equals(recipe.getVersionType())){
                if ("3".equals(approveStep)){
                    recipe.setApproveStep("0");
                } else {
                    int i = Integer.parseInt(approveStep);
                    i++;
                    recipe.setApproveStep(i + "");
                }
            }
            recipe.setApproveResult(submitResult);
            recipeService.updateById(recipe);
        }else{
            //审批不通过，修改配方审批结果
            recipe.setApproveResult(submitResult);
            recipeService.updateById(recipe);
        }
    }
}
