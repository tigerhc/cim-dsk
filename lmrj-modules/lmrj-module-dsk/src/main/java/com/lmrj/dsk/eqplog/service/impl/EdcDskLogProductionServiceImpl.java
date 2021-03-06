package com.lmrj.dsk.eqplog.service.impl;

import com.google.common.collect.Maps;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogProductionMapper;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.edc.config.service.impl.EdcConfigFileCsvServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.impl.FabEquipmentServiceImpl;
import com.lmrj.fab.log.service.IFabLogService;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.file.FileUtil;
import com.lmrj.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
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
@Slf4j
public class EdcDskLogProductionServiceImpl extends CommonServiceImpl<EdcDskLogProductionMapper, EdcDskLogProduction> implements IEdcDskLogProductionService {
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
    public EdcDskLogProduction findLastYield(String eqpId,Date startTime) {
        EdcDskLogProduction edcDskLogProduction = baseMapper.findLastYield(eqpId,startTime);
        return edcDskLogProduction;

    }

    @Override
    public Integer findNewYieldByLot(String eqpId, String productionNo, String lotNo) {
        return baseMapper.findNewYieldByLot(eqpId, productionNo, lotNo);
    }

    @Override
    public List<EdcDskLogProduction> findDataBylotNo(String lotNo, String eqpId, String productionNo) {
        return baseMapper.findDataBylotNo(lotNo, eqpId, productionNo);
    }

    @Override
    public List<EdcDskLogProduction> findProByTime(Date startTime, Date endTime, String eqpId) {
        return baseMapper.findProByTime(startTime, endTime, eqpId);
    }

    @Override
    public Boolean exportTrmProductionFile(List<MesLotTrack> lotList,String fileType) {
        if(fileType.equals("PRODUCTION")){
            List<EdcDskLogProduction> proList = new ArrayList<>();
            for (MesLotTrack mesLotTrack : lotList) {
                //打印产量日志
                proList = baseMapper.findDataBylotNo(mesLotTrack.getLotNo(),mesLotTrack.getEqpId(),mesLotTrack.getProductionNo());
                try {
                    this.printProlog(proList,fileType);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

        }
        return true;
    }

    @Override
    public Boolean exportTrmTempHlogFile(Date startTime , Date endTime , String eqpId){
        List<EdcDskLogProduction> proList = baseMapper.findProByTime(startTime,endTime,eqpId);
        try {
            this.printTemplog(proList);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param eqpId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<EdcDskLogProductionHis> findBackUpYield(String eqpId, Date startTime, Date endTime) {
        List<EdcDskLogProductionHis> hisList = new LinkedList<>();
        List<String> deleteList = new LinkedList<>();
        List<EdcDskLogProduction> yields = baseMapper.findYields(eqpId, startTime, endTime);
        Map<String, Boolean> map = Maps.newHashMap(); // 存在两条数据,查看是否出现过
        for (int i = 0; i < yields.size(); i++) {
            if (yields.get(i).getDayYield() == 1) {
                String key = yields.get(i).getEqpId() + yields.get(i).getProductionNo() + yields.get(i).getLotNo();
                if (map.get(key + "day") == null) {
                    map.put(key + "day", true);
                } else {
                    //已经有一条数据日产量为1,则删除一条
                    deleteList.add(yields.get(i).getId());
                    EdcDskLogProductionHis edcDskLogProductionHis = new EdcDskLogProductionHis(yields.get(i));
                    hisList.add(edcDskLogProductionHis);
                }
                continue;
            } else if (yields.get(i).getLotYield() == 1) {
                String key = yields.get(i).getEqpId() + yields.get(i).getProductionNo() + yields.get(i).getLotNo();
                if (map.get(key + "lot") == null) {
                    map.put(key + "lot", true);
                } else {
                    deleteList.add(yields.get(i).getId());
                    EdcDskLogProductionHis edcDskLogProductionHis = new EdcDskLogProductionHis(yields.get(i));
                    hisList.add(edcDskLogProductionHis);
                }
                continue;
            } else {
                if (i == yields.size() - 1) {
                    break;
                } else {
                    if (yields.get(i + 1).getDayYield() == 1 || yields.get(i + 1).getLotYield() == 1) {
                        continue;
                    } else {
                        deleteList.add(yields.get(i).getId());
                        EdcDskLogProductionHis edcDskLogProductionHis = new EdcDskLogProductionHis(yields.get(i));
                        hisList.add(edcDskLogProductionHis);
                    }
                }
            }
        }
        if (deleteList.size() != 0) {
            super.deleteBatchIds(deleteList);
        }
        return hisList;
    }

    //修改品番和批次
    public List<MesLotTrack> updateProductionData(Date startTime, Date endTime) {
        List<MesLotTrack> mesLotTrackList = iMesLotTrackService.findCorrectData(startTime, endTime);
        List<EdcDskLogProduction> wrongDataList = new ArrayList<>();
        List<MesLotTrack> wrongLotList = new ArrayList<>();
        //循环MesLotTrack批次
        for (int i = 0; i < mesLotTrackList.size(); i++) {
            List<EdcDskLogProduction> lotNoList =new ArrayList<>();
            MesLotTrack mesLotTrack=mesLotTrackList.get(i);
            boolean wrongLotFlag = false;
            String eqpNo=iFabEquipmentService.findeqpNoInfab(mesLotTrack.getEqpId());
            MesLotTrack nextLotTrack=null;
            if(i<mesLotTrackList.size()-1){
                if(mesLotTrackList.get(i+1).getEqpId().equals(mesLotTrack.getEqpId())){
                    nextLotTrack=mesLotTrackList.get(i+1);
                }
            }
            if(nextLotTrack!=null){
                lotNoList = baseMapper.findProByTime(mesLotTrack.getStartTime(), nextLotTrack.getStartTime(), mesLotTrack.getEqpId());
            }else{
                lotNoList = baseMapper.findProByTime(mesLotTrack.getStartTime(), mesLotTrack.getEndTime(), mesLotTrack.getEqpId());
            }
            for (EdcDskLogProduction edcDskLogProduction : lotNoList) {
                if (!edcDskLogProduction.getLotNo().equals(mesLotTrack.getLotNo())) {
                    edcDskLogProduction.setProductionNo(mesLotTrack.getProductionNo());
                    edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
                    edcDskLogProduction.setEqpNo(eqpNo);
                    wrongDataList.add(edcDskLogProduction);
                    wrongLotFlag = true;
                }
            }
            if (wrongLotFlag) {
                //存入修改的批次信息 返回
                wrongLotList.add(mesLotTrack);
            }
        }
        for (MesLotTrack mesLotTrack : mesLotTrackList) {
            MesLotTrack nextLot=iMesLotTrackService.selectEndTime(mesLotTrack.getEqpId(),mesLotTrack.getLotNo());
            List<EdcDskLogProduction> lotNoList =new ArrayList<>();
            boolean wrongLotFlag = false;
            //查询批次时间内的production数据
            String eqpNo=iFabEquipmentService.findeqpNoInfab(mesLotTrack.getEqpId());
            if(nextLot!=null){
                lotNoList = baseMapper.findProByTime(mesLotTrack.getStartTime(), nextLot.getStartTime(), mesLotTrack.getEqpId());
            }else{
                lotNoList = baseMapper.findProByTime(mesLotTrack.getStartTime(), mesLotTrack.getEndTime(), mesLotTrack.getEqpId());
            }
            //循环比较每个批次时间内数据是否正确
            for (EdcDskLogProduction edcDskLogProduction : lotNoList) {
                if (!edcDskLogProduction.getLotNo().equals(mesLotTrack.getLotNo())) {
                    edcDskLogProduction.setProductionNo(mesLotTrack.getProductionNo());
                    edcDskLogProduction.setLotNo(mesLotTrack.getLotNo());
                    edcDskLogProduction.setEqpNo(eqpNo);
                    wrongDataList.add(edcDskLogProduction);
                    wrongLotFlag = true;
                }
            }
            if (wrongLotFlag) {
                //存入修改的批次信息 返回
                wrongLotList.add(mesLotTrack);
            }
        }
        if (!wrongDataList.isEmpty()) {
            this.updateBatchById(wrongDataList,100);
            String eventId = StringUtil.randomTimeUUID("RPT");
            fabLogService.info("", eventId, "updateProductionData", "修复品番和批次数据数量：" + wrongDataList.size(), "", "");
        } else {
            log.info("数据品番和批次正确");
        }
        return wrongLotList;
    }

    //修改批量内连番    和track表设备产量
    public void updateProductionLotYieId(List<EdcDskLogProduction> edcDskLogProductionList) {
        //循环数据   重新计算批量内连番
        List<EdcDskLogProduction> wrongDataList = new ArrayList<>();
        int j=1;
        if(edcDskLogProductionList.get(0).getEqpId().contains("TRM") ||edcDskLogProductionList.get(0).getEqpId().contains("REFLOW")){
            j=12;
            for (EdcDskLogProduction edcDskLogProduction : edcDskLogProductionList) {
                if (edcDskLogProduction.getLotYield() != j) {
                    edcDskLogProduction.setLotYield(j);
                    wrongDataList.add(edcDskLogProduction);
                }
                j=j+12;
            }
        }else{
            for (EdcDskLogProduction edcDskLogProduction : edcDskLogProductionList) {
                if (edcDskLogProduction.getLotYield() != j) {
                    edcDskLogProduction.setLotYield(j);
                    wrongDataList.add(edcDskLogProduction);
                }
                j++;
            }
        }
        if (!wrongDataList.isEmpty()) {
            //将设备产量数据放入mesLotTrack对象，修改track表设备产量
            MesLotTrack mesLotTrack = new MesLotTrack();
            mesLotTrack.setEqpId(edcDskLogProductionList.get(0).getEqpId());
            mesLotTrack.setLotNo(edcDskLogProductionList.get(0).getLotNo());
            if(edcDskLogProductionList.get(0).getEqpId()=="SIM-REFLOW1"){
                mesLotTrack.setLotYieldEqp(edcDskLogProductionList.size()*12);
            }else{
                mesLotTrack.setLotYieldEqp(edcDskLogProductionList.size());
            }
            iMesLotTrackService.updateTrackLotYeildEqp(mesLotTrack.getEqpId(),mesLotTrack.getLotNo(),edcDskLogProductionList.size());
            this.updateBatchById(wrongDataList,100);
            String eventId = StringUtil.randomTimeUUID("RPT");
            fabLogService.info("", eventId, "updateProductionLotYieId", "修正批量内连番数据条数：" + wrongDataList.size(), "", "");
        } else {
            log.info("数据批量内连番正确");
        }
    }
    /**
     * 导出production csv文件
     *
     * @param wrongList
     * @returns
     */
    //打印批次或品番错误的数据的production文件
    public void printProductionCsv(List<MesLotTrack> wrongList) {
        for (MesLotTrack lot : wrongList) {
            //根据批次、品番和设备号查找所有记录
            List<EdcDskLogProduction> prolist = baseMapper.findDataBylotNo(lot.getLotNo(), lot.getEqpId(), lot.getProductionNo());
            //修改批次内连番
            updateProductionLotYieId(prolist);
            prolist = baseMapper.findDataBylotNo(lot.getLotNo(), lot.getEqpId(), lot.getProductionNo());
            try {
                //导出文件 一个批次生成一个文件
                String fileType = "PRODUCTION";
                this.printProlog(prolist,fileType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*public void printProductionlog(List<EdcDskLogProduction> prolist) throws Exception{
        List<String> lines = new ArrayList<>();
        String filename=null;
        EdcDskLogProduction pro;
        String pattern1 = "yyyyMMddHHmmssSSS";
        String pattern2 = "yyyy-MM-dd HH:mm:ss SSS";
        lines.add(FileUtil.csvBom+edcConfigFileCsvService.findTitle(prolist.get(0).getEqpId(),fileType));
        for (int i = 0; i < prolist.size(); i++) {
            pro=prolist.get(i);
            if(i==0){
                String createTimeString = DateUtil.formatDate(pro.getCreateDate(), pattern1);
                filename="DSK_"+pro.getEqpId()+"_"+pro.getLotNo()+"_"+ createTimeString +"_Productionlog.csv";
            }
            String startTimeString = DateUtil.formatDate(pro.getStartTime(), pattern2);
            String endTimeString=DateUtil.formatDate(pro.getEndTime(), pattern2);
            String line=pro.getEqpId()+","+pro.getEqpModelName()+","+startTimeString+","+endTimeString+","+pro.getDayYield()+
                    ","+pro.getLotYield()+","+pro.getLotNo()+","+pro.getDuration();
            if(i>0&&!pro.getLotNo().equals(prolist.get(i-1).getLotNo())){
                File newFile = new File(filePath + "\\" + filename);
                FileUtil.writeLines(newFile, "UTF-8", lines);
                for (int j = 0; j < i ; j++) {
                    prolist.remove(0);
                }
                printProductionlog(prolist);
                return;
            }
            lines.add(line);
        }
        File newFile = new File(filePath + "\\" + filename);
        FileUtil.writeLines(newFile, "UTF-8", lines);
    }*/

    public void printProlog(List<EdcDskLogProduction> prolist,String fileType) throws Exception {
        String eqpNo = "";
        List<String> lines = new ArrayList<>();
        String filename = null;
        EdcDskLogProduction pro;
        String pattern1 = "yyyyMMddHHmm999";
        String pattern2 = "yyyy-MM-dd HH:mm:ss SSS";
        String filePath = null;
        String fileBackUpPath = null;
        //获取表格title添加到lines中
        lines.add(FileUtil.csvBom + edcConfigFileCsvService.findTitle(prolist.get(0).getEqpId(), fileType)+",模腔1计数,模腔2计数,模腔3计数,模腔1 LF预热器温度L,模腔2 LF预热器温度L,模腔3 LF预热器温度L,模腔1 LF预热器温度R,模腔2 LF预热器温度R,模腔3 LF预热器温度R,模具1温度上型,模具2温度上型,模具3温度上型,模具1温度下型,模具2温度下型,模具3温度下型,模腔1夹持位置,模腔2夹持位置,模腔3夹持位置,模腔1夹持压力,模腔2夹持压力,模腔3夹持压力,模腔1合模时间,模腔2合模时间,模腔3合模时间");
        for (int i = 0; i < prolist.size(); i++) {
            eqpNo = iFabEquipmentService.findeqpNoInfab(prolist.get(i).getEqpId());
            pro = prolist.get(i);
            //拼写文件存储路径及备份路径
            if (i == 0) {
                String createTimeString = DateUtil.formatDate(pro.getCreateDate(), pattern1);
                filename = "DSK_" + pro.getEqpId() + "_" + pro.getLotNo() + "_" + createTimeString + "_Productionlog.csv";
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(pro.getEqpId());
                filePath = "E:/FTP/EQUIPMENT/SIM/" + DateUtil.getYear() + "/" + fabEquipment.getStepCode() + "/" + pro.getEqpId() + "/" + DateUtil.getMonth();
                fileBackUpPath = "E:/FTP/EQUIPMENT/SIM/" + DateUtil.getYear() + "/" + fabEquipment.getStepCode() + "/" + pro.getEqpId() + "/" + DateUtil.getMonth() + "/ORIGINAL";
                filePath = new String(filePath.getBytes("GBK"), "iso-8859-1");
                fileBackUpPath = new String(fileBackUpPath.getBytes("GBK"), "iso-8859-1");
            }
            String startTimeString = DateUtil.formatDate(pro.getStartTime(), pattern2);
            String endTimeString = DateUtil.formatDate(pro.getEndTime(), pattern2);
            //拼写当前行字符串
            String line = pro.getEqpId() + "," + pro.getEqpModelName() + "," + eqpNo + "," + pro.getRecipeCode() + "," + startTimeString + "," + endTimeString + "," + pro.getDayYield() + "," + pro.getLotYield() + "," +
                    pro.getDuration() + "," + "," + "," + "," + "," + pro.getOrderNo() + "," + pro.getLotNo() + "," + pro.getProductionNo() + "," + pro.getParamValue();
            lines.add(line.replace("null",""));
        }
        //创建文件路径
        FileUtil.mkDir(fileBackUpPath);
        File newFile = new File(filePath + "\\" + filename);
        FileUtil.writeLines(newFile, "UTF-8", lines);
        String eventId = StringUtil.randomTimeUUID("RPT");
        fabLogService.info(filename.split("_")[1], eventId, "printProlog", "生成Production文件", filename.split("_")[2], "");
        //获取目录下所有文件判断是否有同名文件存在，若存在将文件备份
        List<File> fileList = (List<File>) FileUtil.listFiles(new File(filePath), new String[]{"csv"}, false);
        for (File file : fileList) {
            if (file.getName().contains("Productionlog.csv")) {
                //eqpId lotNo
                if (file.getName().split("_")[1].equals(filename.split("_")[1]) &&
                        file.getName().split("_")[2].equals(filename.split("_")[2]) &&
                        !file.getName().split("_")[3].equals(filename.split("_")[3])) {
                    FileUtil.move(filePath + "\\" + file.getName(), fileBackUpPath + "\\" + file.getName(), false);
                }
            }
        }
    }
    public void printTemplog(List<EdcDskLogProduction> prolist)  throws Exception{
        String eqpNo = "";
        List<String> lines = new ArrayList<>();
        String filename = null;
        EdcDskLogProduction pro;
        String pattern1 = "yyyyMMddHHmm999";
        String pattern2 = "yyyy-MM-dd HH:mm:ss SSS";
        String filePath = null;
        String fileBackUpPath = null;
        //获取表格title添加到lines中
        lines.add(FileUtil.csvBom + "事件发生时刻,模腔1计数,模腔2计数,模腔3计数,模腔1预热器L温度,模腔2预热器L温度,模腔3预热器L温度,模腔1预热器R温度,模腔2预热器R温度,模腔3预热器R温度,模具1温度上型,模具2温度上型,模具3温度上型,模具1温度下型,模具2温度下型,模具3温度下型");
        for (int i = 0; i < prolist.size(); i++) {
            pro = prolist.get(i);
            //拼写文件存储路径及备份路径
            if (i == 0) {
                String createTimeString = DateUtil.formatDate(pro.getCreateDate(), pattern1);
                filename = "DSK_" + pro.getEqpId() + "_" + createTimeString + "_TempHlog.csv";
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(pro.getEqpId());
                filePath = "E:/FTP/EQUIPMENT/SIM/" + DateUtil.getYear() + "/" + fabEquipment.getStepCode() + "/" + pro.getEqpId() + "/" + DateUtil.getMonth();
                fileBackUpPath = "E:/FTP/EQUIPMENT/SIM/" + DateUtil.getYear() + "/" + fabEquipment.getStepCode() + "/" + pro.getEqpId() + "/" + DateUtil.getMonth() + "/ORIGINAL";
                filePath = new String(filePath.getBytes("GBK"), "iso-8859-1");
                fileBackUpPath = new String(fileBackUpPath.getBytes("GBK"), "iso-8859-1");
            }
            String startTimeString = DateUtil.formatDate(pro.getStartTime(), pattern2);
            //拼写当前行字符串
            if(null!=pro.getParamValue() && !"".equals(pro.getParamValue())){
                String tempStrs[] = pro.getParamValue().split(",");
                String temp=StringUtil.join(tempStrs,",",0,15);
                String line = startTimeString+","+temp;
                lines.add(line.replace("null",""));
            }
        }
        //创建文件路径
        if(fileBackUpPath!=null && filePath!=null){
            FileUtil.mkDir(fileBackUpPath);
            File newFile = new File(filePath + "\\" + filename);
            FileUtil.writeLines(newFile, "UTF-8", lines);
            String eventId = StringUtil.randomTimeUUID("RPT");
            fabLogService.info(filename.split("_")[1], eventId, "printTemplog", "生成TRM温度文件", filename.split("_")[2], "");
        }

        //获取目录下所有文件判断是否有同名文件存在，若存在将文件备份
        /*List<File> fileList = (List<File>) FileUtil.listFiles(new File(filePath), new String[]{"csv"}, false);
        for (File file : fileList) {
            if (file.getName().contains("TempHlog.csv")) {
                //DSK_SIM-REFLOW1_202011301245258_TempHlog
                if (file.getName().split("_")[1].equals(filename.split("_")[1]) &&
                        file.getName().split("_")[2].equals(filename.split("_")[2])) {
                    FileUtil.move(filePath + "\\" + file.getName(), fileBackUpPath + "\\" + file.getName(), false);
                }
            }
        }*/
    }
}
