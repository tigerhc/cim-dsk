package com.lmrj.rms.permit.service.impl;

import com.google.common.collect.Maps;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.rms.log.service.IRmsRecipeLogService;
import com.lmrj.rms.permit.entity.RmsRecipePermitConfig;
import com.lmrj.rms.permit.service.IRmsRecipePermitConfigService;
import com.lmrj.rms.permit.service.IRmsRecipePermitService;
import com.lmrj.rms.permit.entity.RmsRecipePermit;
import com.lmrj.rms.permit.mapper.RmsRecipePermitMapper;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import com.lmrj.util.file.FtpUtil;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private IRmsRecipeLogService rmsRecipeLogService;
    @Autowired
    private IEmailSendService emailSendService;
    @Autowired
    private IRmsRecipePermitConfigService rmsRecipePermitConfigService;
    @Autowired
    private IFabEquipmentService fabEquipmentService;

//    public static String[] FTP94 = new String[]{"106.12.76.94", "21", "cim", "Pp123!@#"};
    public static String[] FTP94 = new String[]{"127.0.0.1", "21", "FTP", "FTP"};
    private static String rootPath = "/RECIPE/";

    //老版遗弃
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

    //新版再用
    @Override
    public void permit(String roleName, String recipeId, String submitResult, String submitDesc) {
        List<RmsRecipePermit> rmsRecipePermits = baseMapper.selectList(new EntityWrapper<RmsRecipePermit>().eq("recipe_id", recipeId).isNull("submit_result"));
        RmsRecipePermit recipePermit = baseMapper.selectList(new EntityWrapper<RmsRecipePermit>().eq("recipe_id", recipeId).isNull("submit_result").eq("submitter_role", roleName)).get(0);
        RmsRecipe recipe = recipeService.selectById(recipeId);
        rmsRecipeLogService.addLog(recipe,"permit",recipe.getEqpId());
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(recipe.getEqpId());
        recipe.setApproveResult(submitResult);
        int approveStep = Integer.parseInt(recipe.getApproveStep());
        approveStep++;
        if ("1".equals(submitResult)){
            //审批通过,修改审批信息，修改配方审批等级
            recipePermit.setSubmitResult(submitResult);
            recipePermit.setSubmitDesc(submitDesc);
            recipePermit.setSubmitDate(new Date());
            baseMapper.updateById(recipePermit);
            if (rmsRecipePermits.size() > 1) {
                //大于1说明不是最后一级审批
                recipe.setApproveStep(approveStep + "");
                //发送邮件
                String[] email = rmsRecipePermitConfigService.getEmail(recipe.getApproveStep());
                Map<String, Object> datas = Maps.newHashMap();
                datas.put("RECIPE_CODE", recipe.getRecipeCode());
                datas.put("EQP_ID", recipe.getEqpId());
                datas.put("PERMIT_LEVEL", recipe.getApproveStep());
                datas.put("VERSION_TYPE", recipe.getVersionType());
                emailSendService.send(email,"RECIPE_PERMIT",datas);
                recipeService.updateById(recipe);
            }else if(rmsRecipePermits.size() == 1){
                //修改文件路径,复制文件
                String DRAFTPath = rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + recipe.getEqpModelName() + "/DRAFT/" + recipe.getEqpId() + "/" + recipe.getRecipeName();
                String filePath = null;
                if ("EQP".equals(recipe.getVersionType())){
                    //获取备份文件目标路径
                    filePath = rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + recipe.getEqpModelName() + "/EQP/" + recipe.getEqpId() + "/" + recipe.getRecipeName();
                    //找到之前激活的EQP版本修改为停用
                    RmsRecipe eqp = recipeService.findLastByRecipeCode(recipe, "EQP");
                    if (eqp != null) {
                        eqp.setStatus("N");
                        recipeService.updateById(eqp);
                    }
                } else if ("GOLD".equals(recipe.getVersionType())){
                    //获取备份文件目标路径
                    filePath = rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + recipe.getEqpModelName() + "/GOLD/" + recipe.getRecipeName();
                    //找到之前激活的GOLD版本修改为停用
                    RmsRecipe gold = recipeService.findLastByRecipeCode(recipe, "GOLD");
                    if (gold != null ) {
                        gold.setStatus("N");
                        recipeService.updateById(gold);
                    }
                }
                String recipeFilePath = recipe.getRecipeFilePath();
                if (recipeFilePath != null && !"".equals(recipeFilePath)) {
                    String[] strings = recipeFilePath.split("/");
                    String fileName = strings[strings.length - 1];
                    //复制recipe到his文件夹
                    FtpUtil.copyFile(FTP94,DRAFTPath,fileName,filePath + "/HIS",fileName);
                    //删除原来不带版本号的recipe
                    FtpUtil.deleteFile(FTP94,filePath + "/" + recipe.getRecipeName());
                    //复制为最新版本
                    FtpUtil.copyFile(FTP94,DRAFTPath,fileName,filePath,filePath + "/" + recipe.getRecipeName());
                    //删除草稿版
                    FtpUtil.deleteFile(FTP94,recipeFilePath);
                    recipe.setRecipeFilePath(filePath + "/HIS/" + fileName);
                }
                recipe.setApproveStep("0");
                recipe.setStatus("Y");
                recipeService.updateById(recipe);
            }
        }else{
            //审批不通过，修改审批记录，修改配方状态，删除后续审批记录
            recipePermit.setSubmitResult(submitResult);
            recipePermit.setSubmitDesc(submitDesc);
            recipePermit.setSubmitDate(new Date());
            baseMapper.updateById(recipePermit);
            recipe.setApproveStep("1");
            recipe.setStatus("2");
            recipeService.updateById(recipe);
            List<String> ids = new ArrayList<>();
            for (RmsRecipePermit rmsRecipePermit:rmsRecipePermits) {
                if (!rmsRecipePermit.getId().equals(recipePermit.getId())){
                    ids.add(rmsRecipePermit.getId());
                }
            }
            if (ids.size() > 0){
                baseMapper.deleteBatchIds(ids);
            }
        }
    }


    @Override
    public void addPermitList(String recipeId, String versionType) {
        RmsRecipe recipe = recipeService.selectById(recipeId);
        Integer approveStep = Integer.parseInt(recipe.getApproveStep());
        if ("EQP".equals(versionType)){
            if (approveStep > 2) {
                approveStep = 2;
                recipe.setApproveStep("2");
                recipeService.updateById(recipe);
            }
            for (int i = approveStep; i<=2; i++){
                RmsRecipePermit recipePermit = new RmsRecipePermit();
                RmsRecipePermitConfig recipePermitConfig = recipePermitConfigService.selectList(new EntityWrapper<RmsRecipePermitConfig>().eq("submit_level", i + "")).get(0);
                recipePermit.setRecipeId(recipeId);
                recipePermit.setRecipeCode(recipe.getRecipeCode());
                recipePermit.setWiRecipeId(recipeId);
                recipePermit.setSubmitterId(recipePermitConfig.getSubmitterRoleId());
                recipePermit.setSubmitterRole(recipePermitConfig.getSubmitterRoleName());
                baseMapper.insert(recipePermit);
            }
        }
        if ("GOLD".equals(versionType)){
            if (approveStep > 3) {
                approveStep = 3;
                recipe.setApproveStep("3");
                recipeService.updateById(recipe);
            }
            for (int i = approveStep; i<=3; i++){
                RmsRecipePermit recipePermit = new RmsRecipePermit();
                RmsRecipePermitConfig recipePermitConfig = recipePermitConfigService.selectList(new EntityWrapper<RmsRecipePermitConfig>().eq("submit_level", i + "")).get(0);
                recipePermit.setRecipeId(recipeId);
                recipePermit.setRecipeCode(recipe.getRecipeCode());
                recipePermit.setWiRecipeId(recipeId);
                recipePermit.setSubmitterId(recipePermitConfig.getSubmitterRoleId());
                recipePermit.setSubmitterRole(recipePermitConfig.getSubmitterRoleName());
                baseMapper.insert(recipePermit);
            }
        }
    }
}
