package com.lmrj.rms.recipe.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.sys.entity.User;
import com.lmrj.core.sys.entity.UserRole;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
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
import com.lmrj.util.file.FtpUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import com.lmrj.core.entity.MesResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
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

    @Override
    public RmsRecipe selectById(Serializable id){
        RmsRecipe rmsRecipe = super.selectById(id);
        List<RmsRecipeBody> rmsRecipeBodyList = rmsRecipeBodyService.selectList(new EntityWrapper<RmsRecipeBody>(RmsRecipeBody.class).eq("recipe_id",id));
        rmsRecipe.setRmsRecipeBodyDtlList(rmsRecipeBodyList);
        return rmsRecipe;
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

    @Override
    public List<RmsRecipe> recipePermitList() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String id = ShiroExt.getPrincipalProperty(principal, "id");
        User user = UserUtil.getUser(id);
        List<UserRole> userRoles = UserUtil.getUserRoleListByUser(user);
        List<String> roleIdList = new ArrayList<>();
        for (UserRole userRole:userRoles) {
            roleIdList.add(userRole.getRoleId());
        }
        return baseMapper.recipePermitList(roleIdList);
    }

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
        rmsRecipeBodyService.insertBatch(rmsRecipeBodyList);
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
    public boolean uploadRecipe(String eqpId, String recipeName) throws Exception{
//        return uploadOvenRecipe(eqpId, recipeName);
        return analysisFile(eqpId, recipeName);
    }

    /**
     * 上传recipe
     * @param eqpId
     * @param recipeName
     * @return
     */
    public boolean uploadOvenRecipe(String eqpId, String recipeName) throws Exception{
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "UPLOAD_RECIPE");
        map.put("RECIPE_NAME", recipeName);
        map.put("EQP_ID", eqpId);
        map.put("USER_ID", "admin");
        String msgg = JsonUtil.toJsonString(map);
        System.out.println(msgg);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment == null){
            throw new Exception("该设备不存在");
        }
        String bc = fabEquipment.getBcCode();
        log.info("发送至 S2C.T.CURE.COMMAND({});", bc);
        Object test = rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, msgg);
        byte[] message = (byte[]) test;
        String msg = null;
        try {
            msg = new String(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.info("接收 S2C.T.CURE.COMMAND 数据失败");
            log.error("Exception:", e);
        }
        MesResult mesResult = JsonUtil.from(msg, MesResult.class);
        //判断返回值flag是否正确
        if ("Y".equals(mesResult.getFlag())) {
            //String content = mesResult.getContent().toString();
            Map<String, String> contentMap = (Map<String, String>) mesResult.getContent();
            List<RmsRecipeBody> rmsRecipeBodyDtlList = Lists.newArrayList();
            for (String key : contentMap.keySet()) {
                log.debug("key= " + key + " and value= " + contentMap.get(key));
                RmsRecipeBody rmsRecipeBody = new RmsRecipeBody();
                rmsRecipeBody.setParaCode(key);
                rmsRecipeBody.setSetValue(contentMap.get(key));
                rmsRecipeBodyDtlList.add(rmsRecipeBody);
            }
            RmsRecipe rmsRecipe = new RmsRecipe();
            rmsRecipe.setRecipeCode(recipeName);
            rmsRecipe.setRmsRecipeBodyDtlList(rmsRecipeBodyDtlList);
            rmsRecipe.setEqpId(eqpId);
            rmsRecipe.setEqpModelId(fabEquipment.getModelId());
            rmsRecipe.setEqpModelName(fabEquipment.getModelName());
            this.insert(rmsRecipe);
            //提前备份文件
            String[] FTP94 = {"10.11.100.40", "21", "cim", "Pp123!@#"};
            boolean copyFlag = FtpUtil.copyFile(FTP94, "/recipe/shanghai/cure/UP55A/DRAFT/", rmsRecipe.getRecipeName(), "/recipe/shanghai/cure/UP55A/DRAFT/HIS", rmsRecipe.getRecipeName());
            log.info("迁移文件结果:{};", copyFlag);
        }
        return true;
    }

    public boolean analysisFile(String eqpId, String recipeName) throws Exception {
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment == null){
            throw new Exception("该设备不存在");
        }
        String fileName = "SIM6812-DI2";
        String filePath = "C:/Users/daoda/Desktop/"+ fileName +".csv";
//        InputStream inputStream = new FileInputStream("C:/Users/daoda/Desktop/"+ fileName +".csv");
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        List<String[]> excelData = ExcelUtil.getExcelData(workbook);
//        Map<String, String> contentMap = new HashMap<>();
//        for (int i = 0; i < excelData.size(); i++) {
//            String[] strings = excelData.get(i);
//            if (strings[0] != null && strings[1] != null){
//                contentMap.put(strings[0] + strings[1] , strings[4]);
//            }
//        }
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
        RmsRecipe rmsRecipe = new RmsRecipe();
        rmsRecipe.setRecipeCode(recipeName);
        rmsRecipe.setRmsRecipeBodyDtlList(rmsRecipeBodyDtlList);
        rmsRecipe.setEqpId(eqpId);
        rmsRecipe.setEqpModelId(fabEquipment.getModelId());
        rmsRecipe.setEqpModelName(fabEquipment.getModelName());
        this.insert(rmsRecipe);
        return true;
    }

    @Override
    public boolean downloadRecipe(String eqpId, String recipeName) throws Exception{
        return download(eqpId, recipeName);
    }

    /**
     * 下载recipe
     * @param eqpId
     * @param recipeName
     * @return
     */
    public boolean download(String eqpId, String recipeName) throws Exception{
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "DOWN LOAD_RECIPE");
        map.put("RECIPE_NAME", recipeName);
        map.put("EQP_ID", eqpId);
        map.put("USER_ID", "admin");
        String msgg = JsonUtil.toJsonString(map);
        System.out.println(msgg);
        FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
        if (fabEquipment == null){
            throw new Exception("该设备不存在");
        }
        String bc = fabEquipment.getBcCode();
        log.info("发送至 S2C.T.CURE.COMMAND({});", bc);
        Object test = rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, msgg);
        String msg = (String) test;
//        try {
//            msg = new String(message, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            log.info("接收 S2C.T.CURE.COMMAND 数据失败");
//            log.error("Exception:", e);
//        }
//        MesResult mesResult = JsonUtil.from(msg, MesResult.class);
        //判断返回值flag是否正确
//        if ("Y".equals(mesResult.getFlag())) {
//            //String content = mesResult.getContent().toString();
//            Map<String, String> contentMap = (Map<String, String>) mesResult.getContent();
//            List<RmsRecipeBody> rmsRecipeBodyDtlList = Lists.newArrayList();
//            for (String key : contentMap.keySet()) {
//                log.debug("key= " + key + " and value= " + contentMap.get(key));
//                RmsRecipeBody rmsRecipeBody = new RmsRecipeBody();
//                rmsRecipeBody.setParaCode(key);
//                rmsRecipeBody.setSetValue(contentMap.get(key));
//                rmsRecipeBodyDtlList.add(rmsRecipeBody);
//            }
//            RmsRecipe rmsRecipe = new RmsRecipe();
//            rmsRecipe.setRecipeCode(recipeName);
//            rmsRecipe.setRmsRecipeBodyDtlList(rmsRecipeBodyDtlList);
//            rmsRecipe.setEqpId(eqpId);
//            rmsRecipe.setEqpModelId(fabEquipment.getModelId());
//            rmsRecipe.setEqpModelName(fabEquipment.getModelName());
//            this.insert(rmsRecipe);
//            //提前备份文件
//            String[] FTP94 = {"10.11.100.40", "21", "cim", "Pp123!@#"};
//            boolean copyFlag = FtpUtil.copyFile(FTP94, "/recipe/shanghai/cure/UP55A/DRAFT/", rmsRecipe.getRecipeName(), "/recipe/shanghai/cure/UP55A/DRAFT/HIS", rmsRecipe.getRecipeName());
//            log.info("迁移文件结果:{};", copyFlag);
//        }
        rmsRecipeLogService.downloadLog(baseMapper.selectList(new EntityWrapper<RmsRecipe>().eq("recipe_name",recipeName).eq("VERSION_TYPE", "GOLD")).get(0), "download", eqpId);
        return true;
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
        return findLastByRecipeCode(rmsRecipe);
    }

    /**
     * 获取recipe的上一个版本
     * 用于recipe升级时,和当前版本进行比较
     * @param rmsRecipe
     * @return
     */
    @Override
    public RmsRecipe findLastByRecipeCode(RmsRecipe rmsRecipe){
        RmsRecipe rmsRecipeLast =  baseMapper.findLastByRecipeCode(rmsRecipe.getId(), rmsRecipe.getRecipeCode(), rmsRecipe.getEqpModelId());
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

}
