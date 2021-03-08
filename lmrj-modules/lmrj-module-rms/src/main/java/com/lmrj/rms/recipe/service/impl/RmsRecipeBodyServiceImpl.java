package com.lmrj.rms.recipe.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.rms.config.entity.RmsRecipeDownloadConfig;
import com.lmrj.rms.config.service.IRmsRecipeDownloadConfigService;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.entity.TRXO;
import com.lmrj.rms.recipe.mapper.RmsRecipeMapper;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.rms.recipe.mapper.RmsRecipeBodyMapper;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import com.lmrj.rms.template.entity.RmsRecipeTemplate;
import com.lmrj.rms.template.service.IRmsRecipeTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.recipe.service.impl
* @title: rms_recipe_body服务实现
* @description: rms_recipe_body服务实现
* @author: zhangweijiang
* @date: 2019-06-15 01:58:21
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/

@Slf4j
@Transactional
@Service("rmsRecipeBodyService")
public class RmsRecipeBodyServiceImpl  extends CommonServiceImpl<RmsRecipeBodyMapper,RmsRecipeBody> implements  IRmsRecipeBodyService {

    @Autowired
    private IRmsRecipeService recipeService;
    @Autowired
    private IFabEquipmentService fabEquipmentService;
    @Autowired
    private IRmsRecipeDownloadConfigService rmsRecipeDownloadConfigService;
    @Autowired
    private IRmsRecipeTemplateService rmsRecipeTemplateService;

    @Override
    public int copyParaFromExist(String recipeIdNew, String recipeIdOld) {
        return baseMapper.copy(recipeIdNew, recipeIdOld);
    }

    @Override
    public int copyMinValue(String recipeIdNew, String recipeIdOld) {
        return baseMapper.copyMinValue(recipeIdNew, recipeIdOld);
    }

    @Override
    public int copyMaxValue(String recipeIdNew, String recipeIdOld) {
        return baseMapper.copyMaxValue(recipeIdNew, recipeIdOld);
    }

    @Override
    public TRXO checkRecipeBody(String eqpId, String recipeCode, String recipeBody, String recipeBodySize) {
        TRXO reply = new TRXO();
        reply.setTrxId("TXR01", 5);
        reply.setTypeId("O", 1);
        reply.setResult("Y", 1);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        List<RmsRecipeDownloadConfig> downloadConfigs = rmsRecipeDownloadConfigService.selectList(new EntityWrapper<RmsRecipeDownloadConfig>().eq("eqp_model_id", fabEquipment.getModelId()));
        RmsRecipeDownloadConfig downloadConfig = null;
        String versionType = null;
        List<RmsRecipe> rmsRecipes = new ArrayList<>();
        if (downloadConfigs != null && downloadConfigs.size() > 0) {
            downloadConfig = downloadConfigs.get(0);
            versionType = downloadConfig.getLevel1();
            rmsRecipes = recipeService.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_code", recipeCode).eq("status", "Y").eq("del_flag", "0").eq("version_type", versionType));
            if (rmsRecipes.size() == 0) {
                versionType = downloadConfig.getLevel2();
                rmsRecipes = recipeService.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_code", recipeCode).eq("status", "Y").eq("del_flag", "0").eq("version_type", versionType));
                if (rmsRecipes.size() == 0) {
                    versionType = downloadConfig.getLevel3();
                    rmsRecipes = recipeService.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_code", recipeCode).eq("status", "Y").eq("del_flag", "0").eq("version_type", versionType));
                    if (rmsRecipes.size() == 0) {
                        rmsRecipes = recipeService.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_code", recipeCode).eq("status", "Y").eq("del_flag", "0").eq("version_type", "DRAFT"));
                    }
                }
            }
        } else {
            rmsRecipes = recipeService.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_code", recipeCode).eq("status", "Y").eq("del_flag", "0").eq("version_type", "DRAFT"));
        }
        if (rmsRecipes.size() == 0) {
            log.info("未找到[" + recipeCode + "]对应的配方");
            reply.setResult("N", 1);
            reply.setMsg("未找到[" + recipeCode + "]对应的配方", 100);
            return reply;
        }
        List<RmsRecipeBody> recipeBodies = baseMapper.queryRecipeBody(rmsRecipes.get(0).getId());
        int size = Integer.parseInt(recipeBodySize);
        if (size > recipeBodies.size()) {
            log.info("需校验参数个数超出维护参数个数");
            reply.setResult("N", 1);
            reply.setMsg("需校验参数个数超出维护参数个数", 100);
            return reply;
        }

        //将需要校验的recipeBodyList转换成map,
        Map<String, RmsRecipeBody> map = new HashMap<>();
        for (RmsRecipeBody rmsRecipeBody : recipeBodies) {
            List<RmsRecipeTemplate> recipeTemplates = new ArrayList<>();
            recipeTemplates = rmsRecipeTemplateService.selectList(new EntityWrapper<RmsRecipeTemplate>().eq("para_code", rmsRecipeBody.getParaCode()).eq("para_name", rmsRecipeBody.getParaName()));
            if (recipeTemplates.size() > 0) {
                RmsRecipeTemplate recipeTemplate = recipeTemplates.get(0);
                if ("Y".equals(recipeTemplate.getMonitorFlag())) {
                    map.put(rmsRecipeBody.getParaName(), rmsRecipeBody);
                }
            }
        }

        for (int i = 0; i < size; i++) {
            String key;
            String value;
            if (i < size - 1) {
                String[] stirs = recipeBody.substring(i * 100, (i + 1) * 100).trim().split("=");
                key = stirs[0];
                value = stirs[1];
            } else {
                String[] strings = recipeBody.substring(i * 100).trim().split("=");
                key = strings[0];
                value = strings[1];
            }

            if (map.get(key) != null) {
                if (Integer.parseInt(value) < Integer.parseInt(map.get(key).getMinValue()) || Integer.parseInt(value) > Integer.parseInt(map.get(key).getMaxValue())) {
                    log.info("[" + recipeBodies.get(i).getParaName() + "]的参数值["+ value +"]校验不通过");
                    reply.setResult("N", 1);
                    reply.setMsg("[" + recipeBodies.get(i).getParaName() + "]的参数值["+ value +"]校验不通过", 100);
                }
            }
        }
        return reply;
    }
}
