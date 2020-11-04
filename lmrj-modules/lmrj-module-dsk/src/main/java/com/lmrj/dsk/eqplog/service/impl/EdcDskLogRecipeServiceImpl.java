package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipeBody;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogRecipeMapper;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeBodyService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeService;
import com.lmrj.edc.config.service.impl.EdcConfigFileCsvServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.impl.FabEquipmentServiceImpl;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.file.FileUtil;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service.impl
* @title: edc_dsk_log_recipe服务实现
* @description: edc_dsk_log_recipe服务实现
* @author: 张伟江
* @date: 2020-04-17 17:21:17
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcDskLogRecipeService")
public class EdcDskLogRecipeServiceImpl  extends CommonServiceImpl<EdcDskLogRecipeMapper,EdcDskLogRecipe> implements  IEdcDskLogRecipeService {
    @Autowired
    private IFabLogService fabLogService;
    @Autowired
    EdcConfigFileCsvServiceImpl edcConfigFileCsvService;
    @Autowired
    FabEquipmentServiceImpl fabEquipmentService;
    @Autowired
    IMesLotTrackService iMesLotTrackService;
    @Autowired
    IFabEquipmentService iFabEquipmentService;
    @Autowired
    private IEdcDskLogRecipeBodyService edcDskLogRecipeBodyService;

    @Override
    public String findOldData(String eqpId, String startTime,String paramName){
        return baseMapper.findOldData(eqpId,startTime,paramName);
    }

    @Override
    public List<Map<String,String>> findData(String eqpId, Date startTime, Date endTime){
        return baseMapper.findData(eqpId,startTime,endTime);
    }
    @Override
    public EdcDskLogRecipe selectById(Serializable id){
        EdcDskLogRecipe edcDskLogRecipe = super.selectById(id);
        String oldRecipeId=baseMapper.findOldRecipeId(edcDskLogRecipe.getEqpId(),edcDskLogRecipe.getStartTime());
        List<EdcDskLogRecipeBody> rmsRecipeBodyList = edcDskLogRecipeBodyService.selectParamList(edcDskLogRecipe.getId());
        List<EdcDskLogRecipeBody> oldRmsRecipeBodyList = edcDskLogRecipeBodyService.selectParamList(oldRecipeId);
        if(oldRmsRecipeBodyList.size()==rmsRecipeBodyList.size()){
            for (int i = 0; i < rmsRecipeBodyList.size(); i++) {
                if(oldRmsRecipeBodyList.get(i).getSortNo()==rmsRecipeBodyList.get(i).getSortNo()){
                    rmsRecipeBodyList.get(i).setPreValue(oldRmsRecipeBodyList.get(i).getSetValue());
                }
            }
        }
        edcDskLogRecipe.setEdcDskLogRecipeBodyList(rmsRecipeBodyList);
        edcDskLogRecipe.setOldId(oldRecipeId);
        return edcDskLogRecipe;
    }

    @Override
    public boolean insert(EdcDskLogRecipe edcDskLogRecipe) {
        // 保存主表
        super.insert(edcDskLogRecipe);
        // 保存细表
        List<EdcDskLogRecipeBody> edcDskLogRecipeBodyList = edcDskLogRecipe.getEdcDskLogRecipeBodyList();
        for (EdcDskLogRecipeBody edcDskLogRecipeBody : edcDskLogRecipeBodyList) {
            edcDskLogRecipeBody.setRecipeLogId(edcDskLogRecipe.getId());
        }
        edcDskLogRecipeBodyService.insertBatch(edcDskLogRecipeBodyList,100);
        return true;
    }
    @Override
    public Boolean exportRecipeFile(String eqpId, Date startTime, Date endTime){
        List<Map<String,String>> recipeList = this.findData(eqpId,startTime,endTime);
        for (Map<String,String> map : recipeList) {
            String paramName  =  map.get("para_name");
            String starttime = map.get("start_time");
            String oldValue = this.findOldData(eqpId,starttime,paramName);
            map.put("oldValue",oldValue);
        }
        try {
            printRecipeLog(recipeList,"Recipelog");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void printRecipeLog(List<Map<String,String>> recipelist,String fileType) throws Exception {
        String eqpNo = "";
        List<String> lines = new ArrayList<>();
        String filename = null;
        String pattern1 = "yyyyMMddHHmm999";
        String pattern2 = "yyyy-MM-dd HH:mm:ss SSS";
        String filePath = null;
        String fileBackUpPath = null;
        //获取表格title添加到lines中
        lines.add(FileUtil.csvBom + edcConfigFileCsvService.findTitle(recipelist.get(0).get("eqp_id"), fileType));
        for (int i = 0; i < recipelist.size(); i++) {
            String eqpId=recipelist.get(0).get("eqp_id");
            FabEquipment fabEquipment = new FabEquipment();
            eqpNo = iFabEquipmentService.findeqpNoInfab(eqpId);
            Map<String,String> map = recipelist.get(i);
            //拼写文件存储路径及备份路径map.get("start_time")
            if (i == 0) {
                String createTimeString = map.get("start_time").replace("-","");
                filename = "DSK_" + eqpId + "_" + createTimeString + "_Recipelog.csv";
                fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
                filePath = "E:/FTP/EQUIPMENT/SIM/" + DateUtil.getYear() + "/" + fabEquipment.getStepCode() + "/" + eqpId + "/" + DateUtil.getMonth();
                fileBackUpPath = "E:/FTP/EQUIPMENT/SIM/" + DateUtil.getYear() + "/" + fabEquipment.getStepCode() + "/" + eqpId + "/" + DateUtil.getMonth() + "/ORIGINAL";
                filePath = new String(filePath.getBytes("GBK"), "iso-8859-1");
                fileBackUpPath = new String(fileBackUpPath.getBytes("GBK"), "iso-8859-1");
            }
            String startTimeString = map.get("start_time");
            //拼写当前行字符串
            String line = eqpId + "," + fabEquipment.getModelName() + "," + eqpNo + "," + map.get("recipe_code") + "," + startTimeString + "," + map.get("para_name") + "," + map.get("set_value")+","+map.get("oldValue");
            lines.add(line);
        }
        //创建文件路径
        FileUtil.mkDir(fileBackUpPath);
        File newFile = new File(filePath + "\\" + filename);
        FileUtil.writeLines(newFile, "UTF-8", lines);
        String eventId = StringUtil.randomTimeUUID("RPT");
        fabLogService.info(filename.split("_")[1], eventId, "printRecipelog", "生成Recipe文件", filename.split("_")[2], "");
        //获取目录下所有文件判断是否有同名文件存在，若存在将文件备份
        List<File> fileList = (List<File>) FileUtil.listFiles(new File(filePath), new String[]{"csv"}, false);
        for (File file : fileList) {
            if (file.getName().contains("Recipelog.csv")) {
                //eqpId lotNo
                if (file.getName().split("_")[1].equals(filename.split("_")[1]) &&
                        file.getName().split("_")[2].equals(filename.split("_")[2]) &&
                        !file.getName().split("_")[3].equals(filename.split("_")[3])) {
                    FileUtil.move(filePath + "\\" + file.getName(), fileBackUpPath + "\\" + file.getName(), false);
                }
            }
        }
    }


   public EdcDskLogRecipe findById(Serializable id){
      return super.selectById(id);
    }
}
