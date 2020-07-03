package com.lmrj.rms.recipe.service.impl;

import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.service.DemoService;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@WebService(serviceName = "DemoService",//与前面接口一致
        targetNamespace = "http://sercive.DemoService.lmrj.com",  //与前面接口一致
        endpointInterface = "com.lmrj.rms.recipe.service.DemoService")  //接口地址
@Component
public class DemoServiceImpl implements DemoService {

    private IRmsRecipeService recipeService;

    @Override
    public String findRecipeName(String eqpId) {
//        RmsRecipe rmsRecipe = recipeService.selectById(eqpId);
        return "SIM-DM5#";
    }
}
