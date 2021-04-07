package com.lmrj.rms.recipe.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.rms.config.entity.RmsRecipeDownloadConfig;
import com.lmrj.rms.config.service.IRmsRecipeDownloadConfigService;
import com.lmrj.rms.log.entity.RmsRecipeCheckLog;
import com.lmrj.rms.log.service.IRmsRecipeCheckLogService;
import com.lmrj.rms.log.service.IRmsRecipeLogService;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.entity.TRXO;
import com.lmrj.rms.recipe.mapper.RmsRecipeMapper;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.rms.recipe.mapper.RmsRecipeBodyMapper;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import com.lmrj.rms.template.entity.RmsRecipeTemplate;
import com.lmrj.rms.template.service.IRmsRecipeTemplateService;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


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
    @Autowired
    private IRmsRecipeLogService recipeLogService;
    @Autowired
    private IRmsRecipeCheckLogService recipeCheckLogService;

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
        reply.setMsg("", 100);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        List<RmsRecipeDownloadConfig> downloadConfigs = rmsRecipeDownloadConfigService.selectList(new EntityWrapper<RmsRecipeDownloadConfig>().eq("eqp_model_id", fabEquipment.getModelId()));
        RmsRecipeDownloadConfig downloadConfig = null;
        String versionType = null;
        List<RmsRecipe> rmsRecipes = new ArrayList<>();
        recipeCode = recipeCode.replaceAll("\\\\", "-");
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
            log.info("not find [" + recipeCode + "] recipe");
            reply.setResult("N", 1);
            reply.setMsg("not find [" + recipeCode + "] recipe", 100);
            return reply;
        }
        List<RmsRecipeBody> recipeBodies = baseMapper.queryRecipeBody(rmsRecipes.get(0).getId());
        int size = Integer.parseInt(recipeBodySize.trim());
//        if (size > recipeBodies.size()) {
//            log.info("需校验参数个数超出维护参数个数");
//            reply.setResult("N", 1);
//            reply.setMsg("需校验参数个数超出维护参数个数", 100);
//            return reply;
//        }

        //将需要校验的recipeBodyList转换成map,
        Map<String, RmsRecipeTemplate> templateMap = new HashMap<>();
        List<RmsRecipeTemplate> rmsRecipeTemplates = rmsRecipeTemplateService.selectList(new EntityWrapper<RmsRecipeTemplate>().eq("eqp_model_id", fabEquipment.getModelId()));
        for (RmsRecipeTemplate recipeTemplate : rmsRecipeTemplates) {
            templateMap.put(recipeTemplate.getParaCode(), recipeTemplate);
        }
        Map<String, RmsRecipeBody> recipeBodyMap = new HashMap<>();
        if (rmsRecipeTemplates.size() == 0) {
            for (RmsRecipeBody rmsRecipeBody : recipeBodies) {
                recipeBodyMap.put(rmsRecipeBody.getParaName(), rmsRecipeBody);
            }
        } else {
            for (RmsRecipeBody rmsRecipeBody : recipeBodies) {
                RmsRecipeTemplate recipeTemplate = templateMap.get(rmsRecipeBody.getParaCode());
                if ("Y".equals(recipeTemplate.getMonitorFlag())) {
                    recipeBodyMap.put(rmsRecipeBody.getParaName(), rmsRecipeBody);
                }
            }
        }

        RmsRecipeCheckLog rmsChLog = new RmsRecipeCheckLog();
        for (int i = 0; i < size; i++) {
            String key = null;
            String value = null;
            if (i < size - 1) {
                String[] stirs = recipeBody.substring(i * 100, (i + 1) * 100).trim().split("=");
                if (stirs.length < 2) {
                    continue;
                }
                key = stirs[0];
                value = stirs[1];
            } else {
                String[] strings = recipeBody.substring(i * 100).trim().split("=");
                if (strings.length < 2) {
                    continue;
                }
                key = strings[0];
                value = strings[1];
            }

            if (recipeBodyMap.get(key) != null) {
                rmsChLog = new RmsRecipeCheckLog();
                rmsChLog.setEqpId(eqpId);
                rmsChLog.setVersionNo(rmsRecipes.get(0).getVersionNo());
                rmsChLog.setVersionType(rmsRecipes.get(0).getVersionType());
                rmsChLog.setRecipeCode(rmsRecipes.get(0).getRecipeCode());
                rmsChLog.setRecipeName(rmsRecipes.get(0).getRecipeName());
                rmsChLog.setParamCode(recipeBodies.get(i).getParaCode());
                rmsChLog.setParamName(recipeBodies.get(i).getParaName());
                rmsChLog.setCheckResult("成功");
                log.info("校验值：[" + value + "]     设定值：[" + recipeBodyMap.get(key).getSetValue() + "]     最小值：[" + recipeBodyMap.get(key).getMinValue() + "]     最大值：[" + recipeBodyMap.get(key).getMaxValue() + "]");
                if (!StringUtil.isEmpty(recipeBodyMap.get(key).getMinValue()) && !StringUtil.isEmpty(recipeBodyMap.get(key).getMaxValue())) {
                    if (Integer.parseInt(value) < Integer.parseInt(recipeBodyMap.get(key).getMinValue()) || Integer.parseInt(value) > Integer.parseInt(recipeBodyMap.get(key).getMaxValue())) {
                        log.info("参数:[" + key + "]-值:["+ value +"]不符合规范");
                        reply.setResult("N", 1);
                        reply.setMsg("param:[" + key + "]-value:["+ value +"] is error", 100);
                    }
                } else {
                    if (!StringUtil.isEmpty(recipeBodyMap.get(key).getSetValue())) {
                        if (!recipeBodyMap.get(key).getSetValue().equals(value)) {
                            log.info("参数:[" + key + "]-值:["+ value +"]不符合规范");
                            reply.setResult("N", 1);
                            reply.setMsg("param:[" + key + "]-value:["+ value +"] is error", 100);
                        }
                    }
                }
                Date date=new Date();
                DateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //创建一个格式化日期对象
                String punchTime = simpleDateFormat.format(date);
                rmsChLog.setCheckDate(punchTime);
                recipeCheckLogService.addLog(rmsChLog);//将校验信息插入日志表插入日志表
            }
        }
        if ("Y".equals(reply.getResult())){
            recipeLogService.addLog(rmsRecipes.get(0), "checkPass", eqpId);
        } else {
            recipeLogService.addLog(rmsRecipes.get(0), "checkFail", eqpId);
        }
        return reply;
    }

    @Override
    public boolean setLimitValue(String id, String maxValue, String minValue) {
        RmsRecipeBody recipeBody = baseMapper.selectById(id);
        recipeBody.setMaxValue(maxValue);
        recipeBody.setMinValue(minValue);
        Integer integer = baseMapper.updateById(recipeBody);
        if (integer == 1) {
            return true;
        }
        return false;
    }


    @Override
    public TRXO checkRecipeBodyInLog(String eqpId, String recipeCode, String recipeBody, String recipeBodySize) {
        TRXO reply = new TRXO();
        reply.setTrxId("TXR01", 5);
        reply.setTypeId("O", 1);
        reply.setResult("Y", 1);
        reply.setMsg("", 100);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        List<RmsRecipeDownloadConfig> downloadConfigs = rmsRecipeDownloadConfigService.selectList(new EntityWrapper<RmsRecipeDownloadConfig>().eq("eqp_model_id", fabEquipment.getModelId()));
        RmsRecipeDownloadConfig downloadConfig = null;
        String versionType = null;
        List<RmsRecipe> rmsRecipes = new ArrayList<>();
        recipeCode = recipeCode.replaceAll("\\\\", "-");
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
            log.info("not find [" + recipeCode + "] recipe");
            reply.setResult("N", 1);
            reply.setMsg("not find [" + recipeCode + "] recipe", 100);
            return reply;
        }
        List<RmsRecipeBody> recipeBodies = baseMapper.queryRecipeBody(rmsRecipes.get(0).getId());
        int size = Integer.parseInt(recipeBodySize.trim());
//        if (size > recipeBodies.size()) {
//            log.info("需校验参数个数超出维护参数个数");
//            reply.setResult("N", 1);
//            reply.setMsg("需校验参数个数超出维护参数个数", 100);
//            return reply;
//        }

        //将需要校验的recipeBodyList转换成map,
        Map<String, RmsRecipeBody> map = new HashMap<>();
        boolean flag = true;
        for (RmsRecipeBody rmsRecipeBody : recipeBodies) {
            List<RmsRecipeTemplate> recipeTemplates = new ArrayList<>();
            recipeTemplates = rmsRecipeTemplateService.selectList(new EntityWrapper<RmsRecipeTemplate>().eq("para_code", rmsRecipeBody.getParaCode()));
            if (recipeTemplates.size() > 0) {
                flag = false;
                RmsRecipeTemplate recipeTemplate = recipeTemplates.get(0);
                if ("Y".equals(recipeTemplate.getMonitorFlag())) {
                    map.put(rmsRecipeBody.getParaName(), rmsRecipeBody);
                }
            }
        }

        if (flag) {
            for (RmsRecipeBody rmsRecipeBody : recipeBodies) {
                map.put(rmsRecipeBody.getParaName(), rmsRecipeBody);
            }
        }
        RmsRecipeCheckLog rmsChLog = new RmsRecipeCheckLog();
        for (int i = 0; i < size; i++) {
            String key = null;
            String value = null;
            if (i < size - 1) {
                String[] stirs = recipeBody.substring(i * 100, (i + 1) * 100).trim().split("=");
                if (stirs.length < 2) {
                    continue;
                }
                key = stirs[0];
                value = stirs[1];
            } else {
                String[] strings = recipeBody.substring(i * 100).trim().split("=");
                if (strings.length < 2) {
                    continue;
                }
                key = strings[0];
                value = strings[1];
            }


            if (map.get(key) != null) {
                rmsChLog = new RmsRecipeCheckLog();
                rmsChLog.setEqpId(eqpId);
                rmsChLog.setVersionNo(rmsRecipes.get(0).getVersionNo());
                rmsChLog.setVersionType(rmsRecipes.get(0).getVersionType());
                rmsChLog.setRecipeCode(rmsRecipes.get(0).getRecipeCode());
                rmsChLog.setRecipeName(rmsRecipes.get(0).getRecipeName());
                rmsChLog.setParamCode(recipeBodies.get(i).getParaCode());
                rmsChLog.setParamName(recipeBodies.get(i).getParaName());
                rmsChLog.setCheckResult("成功");
                log.info("校验值：[" + value + "]     设定值：[" + map.get(key).getSetValue() + "]     最小值：[" + map.get(key).getMinValue() + "]     最大值：[" + map.get(key).getMaxValue() + "]");
                if (!StringUtil.isEmpty(map.get(key).getMinValue()) && !StringUtil.isEmpty(map.get(key).getMaxValue())) {
                    if (Integer.parseInt(value) < Integer.parseInt(map.get(key).getMinValue()) || Integer.parseInt(value) > Integer.parseInt(map.get(key).getMaxValue())) {
                        log.info("参数:[" + recipeBodies.get(i).getParaName() + "]-值:["+ value +"]不符合规范");
                        reply.setResult("N", 1);
                        reply.setMsg("param:[" + recipeBodies.get(i).getParaName() + "]-value:["+ value +"] is error", 100);
                        rmsChLog.setCheckRemarks("参数:[" + recipeBodies.get(i).getParaName() + "]-值:["+ value +"]不符合规范");
                        rmsChLog.setCheckResult("失败");

                    }
                } else {
                    if (!StringUtil.isEmpty(map.get(key).getSetValue())) {
                        if (!map.get(key).getSetValue().equals(value)) {
                            log.info("参数:[" + recipeBodies.get(i).getParaName() + "]-值:["+ value +"]不符合规范");
                            reply.setResult("N", 1);
                            reply.setMsg("param:[" + recipeBodies.get(i).getParaName() + "]-value:["+ value +"] is error", 100);
                            rmsChLog.setCheckRemarks("参数:[" + recipeBodies.get(i).getParaName() + "]-值:["+ value +"]不符合规范");
                            rmsChLog.setCheckResult("失败");
                        }
                    }
                }
                Date date=new Date();
                DateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //创建一个格式化日期对象
                String punchTime = simpleDateFormat.format(date);
                rmsChLog.setCheckDate(punchTime);
                recipeCheckLogService.addLog(rmsChLog);//将校验信息插入日志表插入日志表
            }
        }
        if ("Y".equals(reply.getResult())){
            recipeLogService.addLog(rmsRecipes.get(0), "checkPass", eqpId);
        } else {
            recipeLogService.addLog(rmsRecipes.get(0), "checkFail", eqpId);
        }
        return reply;
    }
}
