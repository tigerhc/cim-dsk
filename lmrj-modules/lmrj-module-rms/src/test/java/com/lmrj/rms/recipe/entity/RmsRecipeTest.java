package com.lmrj.rms.recipe.entity;

import com.google.common.collect.Lists;
import com.lmrj.util.mapper.JsonUtil;
import com.lmrj.core.entity.MesResult;

import java.util.List;
import java.util.Map;

/**
 * Created by zwj on 2019/7/1.
 */
public class RmsRecipeTest {

    public static void main(String[] args) {
        String msg = "{\"flag\":\"N\",\"msg\":\"\",\"content\":{\"PTNO\":\"01\",\"SEGNO\":\"5\",\"STC\":\"2:TIME\",\"WT_SW1\":\"1:ON\",\"WZ_UP1\":\"1.5\",\"WZ_LO1\":\"1.5\",\"WT_TM1\":\"\",\"WT_SW2\":\"1:ON\",\"WZ_UP2\":\"1.5\",\"WZ_LO2\":\"20\",\"WT_TM2\":\"\",\"WT_SW3\":\"0:OFF\",\"WZ_UP3\":\"\",\"WZ_LO3\":\"\",\"WT_TM3\":\"\",\"WT_SW4\":\"0:OFF\",\"WZ_UP4\":\"\",\"WZ_LO4\":\"\",\"WT_TM4\":\"\",\"WT_SW5\":\"0:OFF\",\"WZ_UP5\":\"\",\"WZ_LO5\":\"\",\"WT_TM5\":\"\",\"SSP\":\"0.0\",\"TSP_S01\":\"150\",\"TIME_S01\":\"15\",\"JC_S01\":\"0:CONT\",\"PVTY1_S01\":\"0:Disable\",\"PVEV1_S01\":\"\",\"PVTY2_S01\":\"0:Disable\",\"PVEV2_S01\":\"\",\"TME1_S01\":\"0:OFF\",\"TON1_S01\":\"\",\"TOFF1_S01\":\"\",\"TME2_S01\":\"0:OFF\",\"TON2_S01\":\"\",\"TOFF2_S01\":\"\",\"TME3_S01\":\"0:OFF\",\"TON3_S01\":\"\",\"TOFF3_S01\":\"\",\"TME4_S01\":\"0:OFF\",\"TON4_S01\":\"\",\"TOFF4_S01\":\"\",\"TSP_S02\":\"150\",\"TIME_S02\":\"30\",\"JC_S02\":\"0:CONT\",\"PVTY1_S02\":\"0:Disable\",\"PVEV1_S02\":\"\",\"PVTY2_S02\":\"0:Disable\",\"PVEV2_S02\":\"\",\"TME1_S02\":\"0:OFF\",\"TON1_S02\":\"\",\"TOFF1_S02\":\"\",\"TME2_S02\":\"0:OFF\",\"TON2_S02\":\"\",\"TOFF2_S02\":\"\",\"TME3_S02\":\"0:OFF\",\"TON3_S02\":\"\",\"TOFF3_S02\":\"\",\"TME4_S02\":\"0:OFF\",\"TON4_S02\":\"\",\"TOFF4_S02\":\"\",\"TSP_S03\":\"200\",\"TIME_S03\":\"10\",\"JC_S03\":\"0:CONT\",\"PVTY1_S03\":\"0:Disable\",\"PVEV1_S03\":\"\",\"PVTY2_S03\":\"0:Disable\",\"PVEV2_S03\":\"\",\"TME1_S03\":\"0:OFF\",\"TON1_S03\":\"\",\"TOFF1_S03\":\"\",\"TME2_S03\":\"0:OFF\",\"TON2_S03\":\"\",\"TOFF2_S03\":\"\",\"TME3_S03\":\"0:OFF\",\"TON3_S03\":\"\",\"TOFF3_S03\":\"\",\"TME4_S03\":\"0:OFF\",\"TON4_S03\":\"\",\"TOFF4_S03\":\"\",\"TSP_S04\":\"200\",\"TIME_S04\":\"90\",\"JC_S04\":\"0:CONT\",\"PVTY1_S04\":\"0:Disable\",\"PVEV1_S04\":\"\",\"PVTY2_S04\":\"0:Disable\",\"PVEV2_S04\":\"\",\"TME1_S04\":\"0:OFF\",\"TON1_S04\":\"\",\"TOFF1_S04\":\"\",\"TME2_S04\":\"0:OFF\",\"TON2_S04\":\"\",\"TOFF2_S04\":\"\",\"TME3_S04\":\"0:OFF\",\"TON3_S04\":\"\",\"TOFF3_S04\":\"\",\"TME4_S04\":\"0:OFF\",\"TON4_S04\":\"\",\"TOFF4_S04\":\"\",\"TSP_S05\":\"70\",\"TIME_S05\":\"70\",\"JC_S05\":\"2:LOC\",\"PVTY1_S05\":\"0:Disable\",\"PVEV1_S05\":\"\",\"PVTY2_S05\":\"0:Disable\",\"PVEV2_S05\":\"\",\"TME1_S05\":\"1:ON\",\"TON1_S05\":\"\",\"TOFF1_S05\":\"\",\"TME2_S05\":\"0:OFF\",\"TON2_S05\":\"\",\"TOFF2_S05\":\"\",\"TME3_S05\":\"0:OFF\",\"TON3_S05\":\"\",\"TOFF3_S05\":\"\",\"TME4_S05\":\"0:OFF\",\"TON4_S05\":\"\",\"TOFF4_S05\":\"\",\"TSP_S06\":\"\",\"TIME_S06\":\"\",\"JC_S06\":\"0:CONT\",\"PVTY1_S06\":\"0:Disable\",\"PVEV1_S06\":\"\",\"PVTY2_S06\":\"0:Disable\",\"PVEV2_S06\":\"\",\"TME1_S06\":\"0:OFF\",\"TON1_S06\":\"\",\"TOFF1_S06\":\"\",\"TME2_S06\":\"0:OFF\",\"TON2_S06\":\"\",\"TOFF2_S06\":\"\",\"TME3_S06\":\"0:OFF\",\"TON3_S06\":\"\",\"TOFF3_S06\":\"\",\"TME4_S06\":\"0:OFF\",\"TON4_S06\":\"\",\"TOFF4_S06\":\"\",\"TSP_S07\":\"\",\"TIME_S07\":\"\",\"JC_S07\":\"0:CONT\",\"PVTY1_S07\":\"0:Disable\",\"PVEV1_S07\":\"\",\"PVTY2_S07\":\"0:Disable\",\"PVEV2_S07\":\"\",\"TME1_S07\":\"0:OFF\",\"TON1_S07\":\"\",\"TOFF1_S07\":\"\",\"TME2_S07\":\"0:OFF\",\"TON2_S07\":\"\",\"TOFF2_S07\":\"\",\"TME3_S07\":\"0:OFF\",\"TON3_S07\":\"\",\"TOFF3_S07\":\"\",\"TME4_S07\":\"0:OFF\",\"TON4_S07\":\"\",\"TOFF4_S07\":\"\",\"TSP_S08\":\"\",\"TIME_S08\":\"\",\"JC_S08\":\"0:CONT\",\"PVTY1_S08\":\"0:Disable\",\"PVEV1_S08\":\"\",\"PVTY2_S08\":\"0:Disable\",\"PVEV2_S08\":\"\",\"TME1_S08\":\"0:OFF\",\"TON1_S08\":\"\",\"TOFF1_S08\":\"\",\"TME2_S08\":\"0:OFF\",\"TON2_S08\":\"\",\"TOFF2_S08\":\"\",\"TME3_S08\":\"0:OFF\",\"TON3_S08\":\"\",\"TOFF3_S08\":\"\",\"TME4_S08\":\"0:OFF\",\"TON4_S08\":\"\",\"TOFF4_S08\":\"\",\"TSP_S09\":\"\",\"TIME_S09\":\"\",\"JC_S09\":\"0:CONT\",\"PVTY1_S09\":\"0:Disable\",\"PVEV1_S09\":\"\",\"PVTY2_S09\":\"0:Disable\",\"PVEV2_S09\":\"\",\"TME1_S09\":\"0:OFF\",\"TON1_S09\":\"\",\"TOFF1_S09\":\"\",\"TME2_S09\":\"0:OFF\",\"TON2_S09\":\"\",\"TOFF2_S09\":\"\",\"TME3_S09\":\"0:OFF\",\"TON3_S09\":\"\",\"TOFF3_S09\":\"\",\"TME4_S09\":\"0:OFF\",\"TON4_S09\":\"\",\"TOFF4_S09\":\"\",\"TSP_S10\":\"\",\"TIME_S10\":\"\",\"JC_S10\":\"0:CONT\",\"PVTY1_S10\":\"0:Disable\",\"PVEV1_S10\":\"\",\"PVTY2_S10\":\"0:Disable\",\"PVEV2_S10\":\"\",\"TME1_S10\":\"0:OFF\",\"TON1_S10\":\"\",\"TOFF1_S10\":\"\",\"TME2_S10\":\"0:OFF\",\"TON2_S10\":\"\",\"TOFF2_S10\":\"\",\"TME3_S10\":\"0:OFF\",\"TON3_S10\":\"\",\"TOFF3_S10\":\"\",\"TME4_S10\":\"0:OFF\",\"TON4_S10\":\"\",\"TOFF4_S10\":\"\"}}";
        MesResult mesResult = JsonUtil.from(msg, MesResult.class);
        Map<String, String> contentMap = (Map<String, String>) mesResult.getContent();
        //Map<String, String> contentMap = JsonMapper.from(content, HashMap.class);
        List<RmsRecipeBody> rmsRecipeBodyDtlList = Lists.newArrayList();
        for(String key: contentMap.keySet()){
            System.out.println("key= "+ key + " and value= " + contentMap.get(key));
            RmsRecipeBody rmsRecipeBody = new RmsRecipeBody();
            rmsRecipeBody.setParaCode(key);
            rmsRecipeBody.setSetValue(contentMap.get(key));
        }
        RmsRecipe rmsRecipe = new RmsRecipe();
        rmsRecipe.setRecipeCode("111");
        rmsRecipe.setRmsRecipeBodyDtlList(rmsRecipeBodyDtlList);
        rmsRecipe.setEqpId("2222");
        // TODO: 2019/7/1
        rmsRecipe.setEqpModelId("8a826f43c78a480bac4c192518afa47e");
        rmsRecipe.setEqpModelName("BlueM-UP55A");
    }

}
