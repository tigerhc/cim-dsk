package com.lmrj.dsk.eqplog.service.impl;

import com.google.common.collect.Maps;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogProductionMapper;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.config.service.impl.EdcConfigFileCsvServiceImpl;
import com.lmrj.util.file.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service.impl
* @title: edc_dsk_log_production服务实现
* @description: edc_dsk_log_production服务实现
* @author: 张伟江
* @date: 2020-04-14 10:10:00
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcDskLogProductionService")
public class EdcDskLogProductionServiceImpl  extends CommonServiceImpl<EdcDskLogProductionMapper,EdcDskLogProduction> implements  IEdcDskLogProductionService {
    public String fileType="PRODUCTION";
    @Autowired
    EdcConfigFileCsvServiceImpl edcConfigFileCsvService;
    //@Override
    //public boolean insert(EdcDskLogProduction edcDskLogProduction) {
    //    // 保存主表
    //    super.insert(edcDskLogProduction);
    //    // 保存细表
    //    List<EdcDskLogRecipeBody> edcDskLogRecipeBodyList = edcDskLogProduction.get();
    //    for (EdcDskLogRecipeBody edcDskLogRecipeBody : edcDskLogRecipeBodyList) {
    //        edcDskLogRecipeBody.setRecipeLogId(edcDskLogRecipe.getId());
    //    }
    //    edcDskLogRecipeBodyService.insertBatch(edcDskLogRecipeBodyList);
    //    return true;
    //}

    //production日志定时清除
    //清除批次中间运行数据,只保留开始和结束
    public boolean clearMiddleProductionLog() {
        //select lot_no,eqp_id  from edc_dsk_log_production where start_time between  '2020-05-05 09:51:44' and '2020-05-06 09:51:44'
        //group by lot_no
        //
        //select * from edc_dsk_log_production where lot_no = '0505B' and eqp_id='SIM-PRINTER1' order by start_time desc limit 1
        //
        //select * from edc_dsk_log_production where lot_no = '0505B' and eqp_id='SIM-PRINTER1' order by start_time asc limit 1
        //delete from edc_dsk_log_production where lot_no = '0505B1' and eqp_id='SIM-PRINTER1'  and (id != '11' or id != '2')
        return true;
    }
  //获取当前产量
    public boolean clearMiddleProductionLog2() {
        return true;
    }


    @Override
    public EdcDskLogProduction findNextYield(String eqpId, Date startTime) {
        EdcDskLogProduction edcDskLogProduction = baseMapper.findNextYield(eqpId, startTime);
        return edcDskLogProduction;

    }

    @Override
    public List<EdcDskLogProductionHis> findBackUpYield(String eqpId,Date startTime, Date endTime) {
        List<EdcDskLogProductionHis> hisList = new LinkedList<>();
        List<String> deleteList = new LinkedList<>();
        List<EdcDskLogProduction> yields = baseMapper.findYields(eqpId,startTime, endTime);
        Map<String, Boolean> map = Maps.newHashMap(); // 存在两条数据,查看是否出现过
        for (int i = 0;i < yields.size(); i++){
            if(yields.get(i).getDayYield() == 1 ){
                String key = yields.get(i).getEqpId()+yields.get(i).getProductionNo()+yields.get(i).getLotNo();
                if(map.get(key+"day") == null){
                    map.put(key+"day", true);
                }else{
                    //已经有一条数据日产量为1,则删除一条
                    deleteList.add(yields.get(i).getId());
                    EdcDskLogProductionHis edcDskLogProductionHis = new EdcDskLogProductionHis(yields.get(i));
                    hisList.add(edcDskLogProductionHis);
                }
                continue;
            }else if(yields.get(i).getLotYield() == 1){
                String key = yields.get(i).getEqpId()+yields.get(i).getProductionNo()+yields.get(i).getLotNo();
                if(map.get(key+"lot") == null ){
                    map.put(key+"lot", true);
                }else{
                    deleteList.add(yields.get(i).getId());
                    EdcDskLogProductionHis edcDskLogProductionHis = new EdcDskLogProductionHis(yields.get(i));
                    hisList.add(edcDskLogProductionHis);
                }
                continue;
            } else {
                if (i == yields.size()-1){
                    break;
                }else {
                    if (yields.get(i + 1).getDayYield() == 1 || yields.get(i + 1).getLotYield() == 1){
                        continue;
                    }else {
                        deleteList.add(yields.get(i).getId());
                        EdcDskLogProductionHis edcDskLogProductionHis = new EdcDskLogProductionHis(yields.get(i));
                        hisList.add(edcDskLogProductionHis);
                    }
                }
            }
        }
        if(deleteList.size() != 0 ){
            super.deleteBatchIds(deleteList);
        }
        return hisList;
    }

    @Override
    public Integer findNewYieldByLot(String eqpId, String productionNo, String lotNo) {
        return baseMapper.findNewYieldByLot(eqpId, productionNo,lotNo);
    }

    @Override
    public EdcDskLogProduction findLotNo(String startTime,String eqpId){
        return baseMapper.findLotNo(startTime,eqpId);
    }
    @Override
    public  String findLotNo1(String startTime,String endTime){
        return baseMapper.findLotNo1(startTime,endTime);
    }

    @Override
    public List<EdcDskLogProduction> findProductionlog(String startTime, String endTime) {
        return baseMapper.findProductionlog(startTime,endTime);
    }
    public void printProductionlog(List<EdcDskLogProduction> prolist,String filepath) throws Exception{
        List<String> lines = new ArrayList<>();
        String filename=null;
        EdcDskLogProduction pro;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        lines.add(FileUtil.csvBom+edcConfigFileCsvService.findTitle(prolist.get(0).getEqpId(),fileType));
        for (int i = 0; i < prolist.size(); i++) {
            pro=prolist.get(i);
            if(i==0){
                String CreateTimeString=formatter.format(pro.getCreateDate());
                filename="DSK_"+pro.getEqpId()+"_"+pro.getLotNo()+"_"+CreateTimeString+"_Productionlog.csv";
            }
            String startTimeString = formatter1.format(pro.getStartTime());
            String endTimeString=formatter1.format(pro.getEndTime());
            String CreateTimeString=formatter1.format(pro.getCreateDate());
            String line=pro.getEqpId()+","+pro.getEqpModelName()+","+startTimeString+","+endTimeString+","+pro.getDayYield()+
                    ","+pro.getLotYield()+","+pro.getLotNo()+","+pro.getDuration()+","+CreateTimeString;
            if(i>0&&!pro.getLotNo().equals(prolist.get(i-1).getLotNo())){
                File newFile = new File(filepath + "\\" + filename);
                FileUtil.writeLines(newFile, "UTF-8", lines);
                for (int j = 0; j < i ; j++) {
                    prolist.remove(0);
                }
                printProductionlog(prolist,filepath);
                return;
            }
            lines.add(line);
        }
        File newFile = new File(filepath + "\\" + filename);
        FileUtil.writeLines(newFile, "UTF-8", lines);
    }

}
