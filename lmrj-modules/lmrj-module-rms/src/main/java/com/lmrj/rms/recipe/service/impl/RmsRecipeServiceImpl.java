package com.lmrj.rms.recipe.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.entity.MesResult;
import com.lmrj.core.sys.entity.User;
import com.lmrj.core.sys.entity.UserRole;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.rms.config.entity.RmsRecipeDownloadConfig;
import com.lmrj.rms.config.service.IRmsRecipeDownloadConfigService;
import com.lmrj.rms.log.service.IRmsRecipeLogService;
import com.lmrj.rms.permit.entity.RmsRecipePermit;
import com.lmrj.rms.permit.service.IRmsRecipePermitService;
import com.lmrj.rms.permit.utils.ShiroExt;
import com.lmrj.rms.recipe.entity.RmsRecipe;
import com.lmrj.rms.recipe.entity.RmsRecipeBody;
import com.lmrj.rms.recipe.mapper.RmsRecipeMapper;
import com.lmrj.rms.recipe.service.IRmsRecipeBodyService;
import com.lmrj.rms.recipe.service.IRmsRecipeService;
import com.lmrj.rms.recipe.utils.FileUtil;
import com.lmrj.rms.recipe.utils.FixedLength;
import com.lmrj.util.file.FtpUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.recipe.service.impl
 * @title: rms_recipe服务实现
 * @description: rms_recipe服务实现
 * @author: zhangweijiang
 * @date: 2019-06-15 01:58:00
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Transactional
@Service("rmsRecipeService")
@Slf4j
public class RmsRecipeServiceImpl  extends CommonServiceImpl<RmsRecipeMapper,RmsRecipe> implements  IRmsRecipeService {

    @Autowired
    private IRmsRecipeBodyService rmsRecipeBodyService;
    @Autowired
    private IFabEquipmentService fabEquipmentService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private IRmsRecipeLogService rmsRecipeLogService;
    @Autowired
    private IRmsRecipePermitService rmsRecipePermitService;
    @Autowired
    private IRmsRecipeDownloadConfigService rmsRecipeDownloadConfigService;


//    public static String[] FTP94 = new String[]{"106.12.76.94", "21", "cim", "Pp123!@#"};
    public static String[] FTP94 = new String[]{"118.195.191.202", "21", "yjftp", "Ivo123!@#"};
    private static String rootPath = "/RECIPE/";

    @Override
    public RmsRecipe selectById(Serializable id){
        RmsRecipe rmsRecipe = super.selectById(id);
        List<RmsRecipeBody> rmsRecipeBodyList = rmsRecipeBodyService.selectList(new EntityWrapper<RmsRecipeBody>(RmsRecipeBody.class).eq("recipe_id",id));
        rmsRecipe.setRmsRecipeBodyDtlList(rmsRecipeBodyList);
        return rmsRecipe;
    }




    @Override
    public RmsRecipe selectByTwoId(String id){
        String[] ids = id.split(",");
        RmsRecipe retRecipe = new RmsRecipe();
        RmsRecipe rmsRecipe1 = this.selectById(ids[0]);
        RmsRecipe rmsRecipe2 = this.selectById(ids[1]);
        if (rmsRecipe1.getRmsRecipeBodyDtlList() != null &&
                rmsRecipe2.getRmsRecipeBodyDtlList() != null &&
                rmsRecipe1.getRmsRecipeBodyDtlList().size() > 0 &&
                rmsRecipe2.getRmsRecipeBodyDtlList().size() > 0) {
            retRecipe.setRecipeName(rmsRecipe1.getRecipeName()+"----"+rmsRecipe2.getRecipeName());
            retRecipe.setId(rmsRecipe1.getId());
            retRecipe.setRmsRecipeBodyDtlList(contect(rmsRecipe1.getRmsRecipeBodyDtlList(),rmsRecipe2.getRmsRecipeBodyDtlList()));//测试数据是否可以这样赋值
            //retRecipe.getRmsRecipeBodyDtlList().addAll(contect(rmsRecipe1.getRmsRecipeBodyDtlList(),rmsRecipe2.getRmsRecipeBodyDtlList()));

        }
         return retRecipe;
    }

    public List<RmsRecipeBody> contect(List<RmsRecipeBody> one,List<RmsRecipeBody> two){
        int len = 0;
        List<RmsRecipeBody> retList = new ArrayList<RmsRecipeBody>();
        RmsRecipeBody ret = new RmsRecipeBody();
        if(one.size()>two.size()){
            //len  = two.size();
            for (int i=0;i<one.size();i++){
                ret = new RmsRecipeBody();
                if(i<two.size()) {
                    ret.setParaName(one.get(i).getParaName() + "----" + two.get(i).getParaName());
                    ret.setParaCode(one.get(i).getParaName() + "----" + two.get(i).getParaName());
                    ret.setSetValue(one.get(i).getSetValue());
                    ret.setMinValue(one.get(i).getMinValue());
                    ret.setMaxValue(one.get(i).getMaxValue());
                    ret.setSetValueOld(two.get(i).getSetValue());
                    ret.setMinValueOld(two.get(i).getMinValue());
                    ret.setMaxValueOld(two.get(i).getMaxValue());
                }else{
                    ret.setParaName(one.get(i).getParaName());
                    ret.setParaCode(one.get(i).getParaName());
                    ret.setSetValue(one.get(i).getSetValue());
                    ret.setMinValue(one.get(i).getMinValue());
                    ret.setMaxValue(one.get(i).getMaxValue());
                }
                retList.add(ret);
            }
        }else{
            //len  = one.size();
            for (int i=0;i<two.size();i++){
                ret = new RmsRecipeBody();
                if(i<one.size()) {
                    ret.setParaName(one.get(i).getParaName() + "----" + two.get(i).getParaName());
                    ret.setParaCode(one.get(i).getParaName() + "----" + two.get(i).getParaName());
                    ret.setSetValue(one.get(i).getSetValue());
                    ret.setMinValue(one.get(i).getMinValue());
                    ret.setMaxValue(one.get(i).getMaxValue());
                    ret.setSetValueOld(two.get(i).getSetValue());
                    ret.setMinValueOld(two.get(i).getMinValue());
                    ret.setMaxValueOld(two.get(i).getMaxValue());
                }else{
                    ret.setParaName(two.get(i).getParaName());
                    ret.setParaCode(two.get(i).getParaName());
                    ret.setSetValueOld(two.get(i).getSetValueOld());
                    ret.setMinValueOld(two.get(i).getMinValueOld());
                    ret.setMaxValueOld(two.get(i).getMaxValueOld());
                }
                retList.add(ret);
            }
        }

        return retList;

    }


    @Override
    public RmsRecipe selectByIdAndCompareParam(String id){
        //RmsRecipe rmsRecipe = super.selectById(id);
        //List<RmsRecipeBody> rmsRecipeBodyList = rmsRecipeBodyService.selectList(new EntityWrapper<RmsRecipeBody>(RmsRecipeBody.class).eq("recipe_id",id));
        //rmsRecipe.setRmsRecipeBodyDtlList(rmsRecipeBodyList);
        RmsRecipe rmsRecipe = this.selectById(id);
        RmsRecipe oldRecipe = this.findLastByRecipeCode(id);
        if(oldRecipe != null){
            rmsRecipe.setOldId(oldRecipe.getId());
            List<RmsRecipeBody> oldRmsRecipeBodyList = oldRecipe.getRmsRecipeBodyDtlList();
            Map<String,RmsRecipeBody> rmsRecipeBodyMap = Maps.newHashMap();
            for(RmsRecipeBody rmsRecipeBody : oldRmsRecipeBodyList){
                rmsRecipeBodyMap.put(rmsRecipeBody.getParaCode(), rmsRecipeBody);
            }
            for(RmsRecipeBody rmsRecipeBody: rmsRecipe.getRmsRecipeBodyDtlList()){
                RmsRecipeBody oldRmsRecipeBody = rmsRecipeBodyMap.get(rmsRecipeBody.getParaCode());
                rmsRecipeBody.setSetValueOld(oldRmsRecipeBody.getSetValue());
                rmsRecipeBody.setMinValueOld(oldRmsRecipeBody.getMinValue());
                rmsRecipeBody.setMaxValueOld(oldRmsRecipeBody.getMaxValue());
            }
        }
        return rmsRecipe;
    }

    @Override
    public Integer copyMinValue(String recipeIdNew, String recipeIdOld) {
        return rmsRecipeBodyService.copyMinValue(recipeIdNew, recipeIdOld);
    }

    @Override
    public Integer copyMaxValue(String recipeIdNew, String recipeIdOld) {
        return rmsRecipeBodyService.copyMaxValue(recipeIdNew, recipeIdOld);
    }

    @Override
    public List<String> recipeCodeList() {
        return baseMapper.recipeCodeList();
    }

    /**
     * 审批主页recipe查询
     * @param eqpId，recipeCode，startDate，endDate，versionType
     * @return
     */
    @Override
    public List<RmsRecipe> recipePermitList(String eqpId,String recipeCode,String startDate,String endDate,String versionType) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String id = ShiroExt.getPrincipalProperty(principal, "id");
        User user = UserUtil.getUser(id);
        List<UserRole> userRoles = UserUtil.getUserRoleListByUser(user);
        List<String> roleIdList = new ArrayList<>();
        for (UserRole userRole:userRoles) {
            roleIdList.add(userRole.getRoleId());
        }
        Date start = null;
        Date end = null;
        try {
            if (startDate !=null && !"".equals(startDate)) {
                start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate + " 00:00:00");
            }
            if (endDate != null && !"".equals(endDate)){
                end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate + " 23:59:59");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return baseMapper.recipePermitList(roleIdList, eqpId,recipeCode, start, end, versionType);
    }

    //废弃
    @Override
    public List<RmsRecipe> getRecipePermitList() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String id = ShiroExt.getPrincipalProperty(principal, "id");
        User user = UserUtil.getUser(id);
        List<UserRole> userRoles = UserUtil.getUserRoleListByUser(user);
        List<String> recipeIds = new ArrayList<>();
        for (UserRole userRole:userRoles) {
            List<RmsRecipePermit> list = rmsRecipePermitService.selectList(new EntityWrapper<RmsRecipePermit>().eq("submitter_id", userRole.getRoleId()));
            if (list.size() > 0){
                recipeIds.add(list.get(0).getRecipeId());
            }
        }
        return baseMapper.selectBatchIds(recipeIds);
    }

    @Override
    public boolean editStatus(String id, String status) {
        RmsRecipe rmsRecipe = baseMapper.selectById(id);
        rmsRecipe.setStatus(status);
        baseMapper.updateById(rmsRecipe);
        return true;
    }

    @Override
    public boolean insert(RmsRecipe rmsRecipe) {
        // 保存主表
        super.insert(rmsRecipe);
        // 保存细表
        List<RmsRecipeBody> rmsRecipeBodyList = rmsRecipe.getRmsRecipeBodyDtlList();
        for (RmsRecipeBody rmsRecipeBody : rmsRecipeBodyList) {
            rmsRecipeBody.setRecipeId(rmsRecipe.getId());
        }
        rmsRecipeBodyService.insertBatch(rmsRecipeBodyList,100);
        return true;
    }

    @Override
    public boolean insertOrUpdate(RmsRecipe rmsRecipe) {
        try {
            // 获得以前的数据
            List<RmsRecipeBody> oldRmsRecipeBodyList = rmsRecipeBodyService.selectList(new EntityWrapper<RmsRecipeBody>(RmsRecipeBody.class).eq("recipe_id",rmsRecipe.getId()));
            // 字段
            List<RmsRecipeBody> rmsRecipeBodyList = rmsRecipe.getRmsRecipeBodyDtlList();
            // 更新主表
            super.insertOrUpdate(rmsRecipe);
            List<String> newsRmsRecipeBodyIdList = new ArrayList<String>();
            // 保存或更新数据
            for (RmsRecipeBody rmsRecipeBody : rmsRecipeBodyList) {
                // 设置不变更的字段
                if (StringUtil.isEmpty(rmsRecipeBody.getId())) {
                    // 保存字段列表
                    rmsRecipeBody.setRecipeId(rmsRecipe.getId());
                    rmsRecipeBodyService.insert(rmsRecipeBody);
                } else {
                    rmsRecipeBodyService.insertOrUpdate(rmsRecipeBody);
                }
                newsRmsRecipeBodyIdList.add(rmsRecipeBody.getId());
            }

            // 删除老数据
            for (RmsRecipeBody rmsRecipeBody : oldRmsRecipeBodyList) {
                String rmsRecipeBodyId = rmsRecipeBody.getId();
                if (!newsRmsRecipeBodyIdList.contains(rmsRecipeBodyId)) {
                    rmsRecipeBodyService.deleteById(rmsRecipeBodyId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean uploadRecipe(String eqpId, List<String> recipeList) throws Exception{
        boolean flag = true;
        for (String recipeCode : recipeList) {
            flag = uploadOvenRecipe(eqpId, recipeCode);
        }
        return flag;
    }

    @Override
    public List<String> findRecipeList(String eqpId) throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "FIND_RECIPE_LIST");
        map.put("EQP_ID", eqpId);
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String userId = ShiroExt.getPrincipalProperty(principal, "id");
        map.put("USER_ID", userId);
        String msgg = JsonUtil.toJsonString(map);
        System.out.println(msgg);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment == null){
            throw new Exception("该设备不存在");
        }
        String bc = fabEquipment.getBcCode();
        log.info("发送至 S2C.T.RMS.COMMAND({});", bc);
        String msg = (String)rabbitTemplate.convertSendAndReceive("S2C.T.RMS.COMMAND", bc, msgg);
        MesResult mesResult = JsonUtil.from(msg, MesResult.class);
        Map<String, Object> content = Maps.newHashMap();
        List<String> recipeList = new ArrayList<>();
        //判断返回值flag是否正确
        if ("Y".equals(mesResult.getFlag())) {
            content = (Map<String, Object>) mesResult.getContent();
            recipeList = (List<String>)content.get("recipeList");
            if (recipeList.size() > 0) {
                String recipeName = content.get("recipeName").toString();
                int index = 0;
                if (recipeName != null && !"".equals(recipeName)) {
                    for (int i = 0; i < recipeList.size(); i++) {
                        if (recipeName.equals(recipeList.get(i))) {
                            index = i;
                            break;
                        }
                    }
                }
                Collections.swap(recipeList, index, 0);
            }
        }
        return recipeList;
    }

    /**
     * 上传recipe
     * @param eqpId
     * @param recipeCode
     * @return
     */
    public boolean uploadOvenRecipe(String eqpId, String recipeCode) throws Exception{
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "UPLOAD_RECIPE");
        map.put("RECIPE_CODE", recipeCode);
        map.put("EQP_ID", eqpId);
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String userId = ShiroExt.getPrincipalProperty(principal, "id");
        map.put("USER_ID", userId);
        String msgg = JsonUtil.toJsonString(map);
        System.out.println(msgg);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment == null){
            throw new Exception("该设备不存在");
        }
        String bc = fabEquipment.getBcCode();
        log.info("发送至 S2C.T.RMS.COMMAND({});", bc);
        String msg = (String)rabbitTemplate.convertSendAndReceive("S2C.T.RMS.COMMAND", bc, msgg);
//        byte[] message = (byte[]) test;
//        String msg = null;
//        try {
//            msg = new String(message, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            log.info("接收 S2C.T.RMS.COMMAND 数据失败");
//            log.error("Exception:", e);
//        }
        MesResult mesResult = JsonUtil.from(msg, MesResult.class);
        boolean flag = false;
        //判断返回值flag是否正确
        if ("Y".equals(mesResult.getFlag())) {
            //获取rmsRecipe参数
            //RmsRecipe rmsRecipe = (RmsRecipe)mesResult.getContent();
            String rmsRecipe1 = (String)mesResult.getContent();
            RmsRecipe rmsRecipe = JsonUtil.from(rmsRecipe1, RmsRecipe.class);
            flag = repeatUpload(rmsRecipe);
        }
        return flag;
    }

    /**
     * 是否重复上传
     * @param rmsRecipe
     * @return
     */
    public boolean repeatUpload(RmsRecipe rmsRecipe) throws Exception{
        String[] strings = rmsRecipe.getRecipeFilePath().split("/");
        String oldFileName = strings[strings.length - 1];
        String fileName = oldFileName.split("-")[0];
//        String fileSuffix = fileName.split("\\.")[1];
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(rmsRecipe.getEqpId());
        Integer versionNo = 1;
        //获取上一版本的配方
        List<RmsRecipe> rmsRecipes = baseMapper.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_code", rmsRecipe.getRecipeCode()).eq("eqp_id", rmsRecipe.getEqpId()).orderBy("version_no", false));
        if (rmsRecipes.size() > 0){
            RmsRecipe oldRecipe = rmsRecipes.get(0);
            versionNo = oldRecipe.getVersionNo();
            versionNo ++;
            //判断上一版本是否为DRAFT版本
            if ("DRAFT".equals(oldRecipe.getVersionType())){
                //判断上一版本是否已经开始审批了
                if ("0".equals(oldRecipe.getStatus())){
                    //改为禁用，文件备份到HIS文件夹
                    oldRecipe.setStatus("N");
                    String[] oldRecipePath = oldRecipe.getRecipeFilePath().split("/");
                    String oldRecipeFileName = oldRecipePath[oldRecipePath.length - 1];
                    String oldPath = rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + oldRecipe.getEqpModelName() + "/DRAFT/" + oldRecipe.getEqpId() + "/" + oldRecipe.getRecipeCode();
                    String hisPath = rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + oldRecipe.getEqpModelName() + "/DRAFT/" + oldRecipe.getEqpId() + "/" + oldRecipe.getRecipeCode() + "/HIS";
                    boolean copyFile = FtpUtil.copyFile(FTP94, oldPath, oldRecipeFileName, hisPath, oldRecipeFileName);
                    oldRecipe.setRecipeFilePath(hisPath + "/" + oldRecipeFileName);
                    baseMapper.updateById(oldRecipe);
                    FtpUtil.deleteFile(FTP94,oldRecipe.getRecipeFilePath());
                    if (!copyFile){
                        throw new Exception("备份文件失败");
                    }
                }
            }
        }
        //修改文件名
        String newFileName = fileName + "_V" + versionNo;
        boolean rename = FtpUtil.rename(FTP94, rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + fabEquipment.getModelName() + "/DRAFT/" + rmsRecipe.getEqpId() + "/" + rmsRecipe.getRecipeCode(), oldFileName, newFileName);
        rmsRecipe.setRecipeFilePath(rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + fabEquipment.getModelName() + "/DRAFT/" + rmsRecipe.getEqpId() + "/" + rmsRecipe.getRecipeCode() + "/" + newFileName);
        rmsRecipe.setVersionNo(versionNo);
        //判断是不是需要解析
        boolean flag;
        if (rmsRecipe.getRmsRecipeBodyDtlList().size() == 0){
            //去FTP下载文件并进行解析
            flag = downloadFromFTP(rmsRecipe,newFileName);
        } else {
            //解析过的对象直接添加到数据库
            rmsRecipe.setStatus("0");
            rmsRecipe.setVersionNo(versionNo);
            rmsRecipe.setRecipeName(rmsRecipe.getRecipeCode());
            rmsRecipe.setVersionType("DRAFT");
            rmsRecipe.setApproveStep("1");
            rmsRecipe.setEqpModelId(fabEquipment.getModelId());
            rmsRecipe.setEqpModelName(fabEquipment.getModelName());
            insert(rmsRecipe);
            //baseMapper.insert(rmsRecipe);
            //for (RmsRecipeBody recipeBody:rmsRecipe.getRmsRecipeBodyDtlList()) {
            //    rmsRecipeBodyService.insert(recipeBody);
            //}
            flag = true;
        }
        // todo 2021-07-11 01:14判断模板中是否有数据,没有数据则添加一条数据
        return flag;
    }

    /**
     * 从FTP服务器下载配方文件到本地
     * @param rmsRecipe, fileName
     * @return
     */
    public boolean downloadFromFTP(RmsRecipe rmsRecipe, String fileName) throws Exception {
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(rmsRecipe.getEqpId());
        String remotePath = rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + fabEquipment.getModelName() + "/DRAFT/" + rmsRecipe.getEqpId() + "/" + rmsRecipe.getRecipeCode();
        String localPath = "D:" + rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + fabEquipment.getModelName() + "/DRAFT/" + rmsRecipe.getEqpId() + "/" + rmsRecipe.getRecipeCode();
        boolean flag = false;
        try {
            //下载文件
            flag = FtpUtil.downloadFile(FTP94, remotePath, fileName, localPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (flag) {
            //如果flag为true说明文件下载成功，找到文件进行解析
            String filePath = localPath + "/" + fileName;
            flag = analysisFile(fabEquipment, rmsRecipe, filePath);
        }
        return flag;
    }


    /**
     * 解析文件
     * @param fabEquipment, recipeName, filePath
     * @return
     */
    public boolean analysisFile(FabEquipment fabEquipment, RmsRecipe rmsRecipe, String filePath) throws Exception {
        Map<String, String> contentMap = FileUtil.analysis(filePath);
        List<RmsRecipeBody> rmsRecipeBodyDtlList = Lists.newArrayList();
        for (String key : contentMap.keySet()) {
            log.debug("key= " + key + " and value= " + contentMap.get(key));
            RmsRecipeBody rmsRecipeBody = new RmsRecipeBody();
            rmsRecipeBody.setParaCode(key);
            String[] strings = contentMap.get(key).split("@@");
            rmsRecipeBody.setParaName(strings[0]);
            rmsRecipeBody.setSetValue(strings[1]);
            rmsRecipeBodyDtlList.add(rmsRecipeBody);
        }
        rmsRecipe.setRecipeName(rmsRecipe.getRecipeCode());
        rmsRecipe.setRmsRecipeBodyDtlList(rmsRecipeBodyDtlList);
        rmsRecipe.setStatus("0");
        rmsRecipe.setVersionType("DRAFT");
        rmsRecipe.setApproveStep("1");
        rmsRecipe.setEqpModelId(fabEquipment.getModelId());
        rmsRecipe.setEqpModelName(fabEquipment.getModelName());
        baseMapper.insert(rmsRecipe);
        return true;
    }

    @Override
    public boolean downloadRecipe(String eqpId, String recipeCode) throws Exception{
        return download(eqpId, recipeCode);
    }

    /**
     * 下载recipe
     * @param eqpId
     * @param recipeCode
     * @return
     */
    public boolean download(String eqpId, String recipeCode) throws Exception{
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment == null){
            throw new Exception("该设备不存在");
        }
        //去下载配置中找到优先下载的版本类型
        String versionType = getDownloadVersionType(eqpId, recipeCode);
        if (versionType == null) {
            throw new Exception("下载失败，未找到指定配方，请检查配置或先上传配方");
        }
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "DOWNLOAD_RECIPE");
        map.put("RECIPE_CODE", recipeCode);
        map.put("EQP_ID", eqpId);
        map.put("VERSION_TYPE", versionType);
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String userId = ShiroExt.getPrincipalProperty(principal, "id");
        map.put("USER_ID", userId);
        List<RmsRecipe> rmsRecipes = baseMapper.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_code", recipeCode).eq("eqp_id", eqpId).eq("version_type", versionType).orderBy("version_no", false));
        if (rmsRecipes != null & rmsRecipes.size() > 0) {
            map.put("RECIPE_FILE_PATH", rmsRecipes.get(0).getRecipeFilePath());
        }
        String msgg = JsonUtil.toJsonString(map);
        System.out.println(msgg);
        String bc = fabEquipment.getBcCode();
        log.info("发送至 S2C.T.RMS.COMMAND({});", bc);
        String msg = (String)rabbitTemplate.convertSendAndReceive("S2C.T.RMS.COMMAND", bc, msgg);
//        byte[] message = (byte[]) test;
//        String msg = null;
//        try {
//            msg = new String(message, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            log.info("接收 S2C.T.RMS.COMMAND 数据失败");
//            log.error("Exception:", e);
//        }
        MesResult mesResult = JsonUtil.from(msg, MesResult.class);
        //判断返回值flag是否正确
        if (!"Y".equals(mesResult.getFlag())){
            return false;
        }
        rmsRecipeLogService.addLog(baseMapper.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_name",recipeCode).eq("VERSION_TYPE", "GOLD")).get(0), "download", eqpId);
        return true;
    }

    /**
     * 获取下载的版本类型
     * @param eqpId
     * @param recipeName
     * @return
     */
    public String getDownloadVersionType(String eqpId, String recipeName) {
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        List<RmsRecipeDownloadConfig> downloadConfigs = rmsRecipeDownloadConfigService.selectList(new EntityWrapper<RmsRecipeDownloadConfig>().eq("eqp_model_id", fabEquipment.getModelId()));
        RmsRecipeDownloadConfig recipeDownloadConfig = null;
        if (downloadConfigs.size() > 0){
            recipeDownloadConfig = downloadConfigs.get(0);

        }
        if (recipeDownloadConfig == null){
            return null;
        }
        String level1 = recipeDownloadConfig.getLevel1();
        //一级一级进行判断
        if (level1 == null || "".equals(level1)) {
            return null;
        } else {
            //检查FTP上是否存在此文件
            boolean flag1 = checkFileExist(level1, fabEquipment, eqpId, recipeName);
            if (flag1){
                return level1;
            }
            String level2 = recipeDownloadConfig.getLevel2();
            if (level2 == null || "".equals(level2)){
                return null;
            } else {
                boolean flag2 = checkFileExist(level2, fabEquipment, eqpId, recipeName);
                if (flag2){
                    return level2;
                }
                String level3 = recipeDownloadConfig.getLevel3();
                if (level3 == null || "".equals(level3)){
                    return null;
                } else {
                    boolean flag3 = checkFileExist(level3, fabEquipment, eqpId, recipeName);
                    if (flag3){
                        return level3;
                    }
                    return null;
                }
            }
        }
    }

    /**
     * 看FTP上次配方文件是否存在
     * @param level, fabEquipment, eqpId, recipeName
     * @return
     */
    public boolean checkFileExist(String level, FabEquipment fabEquipment, String eqpId, String recipeName) {
        // TODO 2020/8/12 文件不存在时判断时间会很长
        List<RmsRecipe> rmsRecipes = baseMapper.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_name", recipeName));
        if (rmsRecipes.size() < 1){
            return false;
        }
        RmsRecipe rmsRecipe = rmsRecipes.get(0);
        String recipeFilePath = rmsRecipe.getRecipeFilePath();
        if (recipeFilePath == null || "".equals(recipeFilePath)) {
            return false;
        }
        String[] strings = recipeFilePath.split("/");
        String fileName = strings[strings.length - 1];
        String filePath = null;
        if ("GOLD".equals(level)){
            filePath = rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + fabEquipment.getModelName() + "/GOLD/" + recipeName;
            boolean flag = FtpUtil.checkFileExist(FtpUtil.connectFtp(FTP94), filePath, recipeName);
            return flag;
        } else {
            filePath = rootPath + fabEquipment.getFab() + "/" + fabEquipment.getStepCode() + "/" + fabEquipment.getModelName() + "/" + level + "/" + eqpId + "/" + recipeName;
            boolean flag = FtpUtil.checkFileExist(FtpUtil.connectFtp(FTP94), filePath, recipeName);
            return flag;
        }
    }

    /**
     * 获取recipe的上一个版本
     * 用于recipe升级时,和当前版本进行比较
     * @param id
     * @return
     */
    @Override
    public RmsRecipe findLastByRecipeCode(String id){
        RmsRecipe rmsRecipe = selectById(id);
        return findLastByRecipeCode(rmsRecipe, "GOLD");
    }

    /**
     * 获取recipe的上一个版本
     * 用于recipe升级时,和当前版本进行比较
     * @param rmsRecipe
     * @return
     */
    @Override
    public RmsRecipe findLastByRecipeCode(RmsRecipe rmsRecipe, String versionType){
        RmsRecipe rmsRecipeLast =  baseMapper.findLastByRecipeCode(rmsRecipe.getId(), rmsRecipe.getRecipeCode(), rmsRecipe.getEqpModelId(), versionType);
        if(rmsRecipeLast==null){
            return null;
        }
        List<RmsRecipeBody> rmsRecipeBodyList = rmsRecipeBodyService.selectList(new EntityWrapper<RmsRecipeBody>(RmsRecipeBody.class).eq("recipe_id",rmsRecipeLast.getId()));
        rmsRecipeLast.setRmsRecipeBodyDtlList(rmsRecipeBodyList);
        return rmsRecipeLast;
    }

    /**
     * 提交recipe升级
     * @param rmsRecipe
     * @return
     */
    @Override
    public boolean upgrade(RmsRecipe rmsRecipe) {
        // TODO: 2019/10/15 保存recipe信息
        rmsRecipe.setStatus("1");
        //更新指定字段数据,不使用insert

        baseMapper.insert(rmsRecipe);
        // TODO: 2019/10/15 创建recipe审批信息
        // TODO: 2019/10/15 邮件通知待审批人
        return true;
    }

    /**
     * 再启动
     *
     * @return
     */
    public RmsRecipe reEnable(RmsRecipe arRecipe) {
        //复制RmsRecipe
        RmsRecipe rmsRecipeNew = this.selectById(arRecipe.getId());
        rmsRecipeNew.setId("");
        rmsRecipeNew.setVersionType("Engineer");
        rmsRecipeNew.setStatus("0");
        rmsRecipeNew.setCreateDate(new Date());
        rmsRecipeNew.setRecipeDesc("生效");
        int versionNo = findVersionNo(arRecipe.getRecipeCode(), arRecipe.getEqpId(), null, arRecipe.getVersionType());
        rmsRecipeNew.setVersionNo(versionNo);
        rmsRecipeNew.setRmsRecipeBodyDtlList(Lists.newArrayList());
        this.insert(rmsRecipeNew);

        //复制recipe para
        rmsRecipeBodyService.copyParaFromExist(rmsRecipeNew.getId(), arRecipe.getId());

        //复制recipe ArAttach
        //ArAttach arAttach = new ArAttach();
        //arAttach.setRecipeRow(new ArRecipe(id));
        //List<ArAttach> arAttachList = arAttachDao.findList(arAttach);
        //for (ArAttach temp : arAttachList) {
        //    temp.setId(IdGen.uuid());
        //    temp.setRecipeRow(arRecipeNew);
        //    arAttachDao.insert(temp);
        //}
//        return "redirect:\" + Global.getAdminPath() + \"/recipe/arRecipe";
        return rmsRecipeNew;
    }

    /**
     * 获取版本号
     * @param recipeCode
     * @param eqpId
     * @param eqpModelId
     * @param versionType
     * @return
     */
    public int findVersionNo(String recipeCode, String eqpId, String eqpModelId, String versionType) {
        int versionNo = 0;
        String count = baseMapper.findMaxVersionNo(recipeCode, eqpId, eqpModelId, versionType);
        if (count != null) {
            versionNo = Integer.parseInt(count) + 1;
        }
        return versionNo;
    }

    @Override
    public boolean delete(String id) {
        List<RmsRecipeBody> oldRmsRecipeBodyList = rmsRecipeBodyService.selectList(new EntityWrapper<RmsRecipeBody>(RmsRecipeBody.class).eq("recipe_id",id));
        for(RmsRecipeBody oldRmsRecipeBody : oldRmsRecipeBodyList ) {

            String rmsRecipeBodyId = oldRmsRecipeBody.getRecipeId();
             rmsRecipeBodyService.deleteById(rmsRecipeBodyId);

        }
        baseMapper.deleteById(id);
        return true;
    }




}
