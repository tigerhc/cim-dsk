package com.lmrj.rms.template.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.rms.recipe.utils.FileUtil;
import com.lmrj.rms.template.service.IRmsRecipeTemplateService;
import com.lmrj.rms.template.entity.RmsRecipeTemplate;
import com.lmrj.rms.template.mapper.RmsRecipeTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


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

    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;

    @Override
    public boolean uploadRecipeTemplate(String eqpModelId, String fileName) throws Exception{
        Map<String, String> contentMap = FileUtil.analysisRecipeTemplate("D:\\RMS\\RecipeTemplate\\" + fileName);
        FabEquipmentModel model = fabEquipmentModelService.selectById(eqpModelId);
        for (String key : contentMap.keySet()) {
            if (!"recipeType".equals(key)) {
                RmsRecipeTemplate recipeTemplate = new RmsRecipeTemplate();
                recipeTemplate.setEqpModelId(eqpModelId);
                recipeTemplate.setEqpModelName(model.getManufacturerName());
                recipeTemplate.setParaCode(contentMap.get("recipeType") + "-" + key);
                recipeTemplate.setParaName(key);
                recipeTemplate.setShowFlag("Y");
                recipeTemplate.setMonitorFlag("Y");
                recipeTemplate.setSetValue(contentMap.get(key));
                baseMapper.insert(recipeTemplate);
            }
        }
        return true;
    }
}
