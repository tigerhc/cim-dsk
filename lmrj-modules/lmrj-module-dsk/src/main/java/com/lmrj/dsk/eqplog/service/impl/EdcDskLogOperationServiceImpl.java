package com.lmrj.dsk.eqplog.service.impl;

import com.google.common.collect.Lists;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogOperationMapper;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.service.impl
 * @title: edc_dsk_log_operation服务实现
 * @description: edc_dsk_log_operation服务实现
 * @author: 张伟江
 * @date: 2020-04-14 10:10:16
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Slf4j
@Transactional
@Service("edcDskLogOperationService")
public class EdcDskLogOperationServiceImpl extends CommonServiceImpl<EdcDskLogOperationMapper, EdcDskLogOperation> implements IEdcDskLogOperationService {
    @Autowired
    IEdcDskLogProductionService iEdcDskLogProductionService;
    @Override
    public List<EdcDskLogOperation> findDataByTimeAndEqpId(String eqpId, Date startTime, Date endTime) {
        return baseMapper.findDataByTimeAndEqpId(eqpId, startTime, endTime);
    }
    @Override
    public List<String> findEqpId(Date startTime, Date endTime) {
        return baseMapper.findEqpId(startTime, endTime);
    }
    public void fixOperationCsvYield(File file,String filePath)throws Exception{
        List<String> lines = FileUtil.readLines(file, "UTF-8");
        List<String> newLines=new ArrayList<>();
        //标题
        newLines.add(FileUtil.csvBom +lines.get(0));
        String titlt=lines.get(0);
        lines.remove(0);
        Boolean flag=true;
        String repeatFileName=file.getName();
        String pattern = "yyyyMMddHHmm999";
        for (String line : lines) {
            String lineData[]=line.split(",",-1);
            EdcDskLogProduction edcDskLogProduction=iEdcDskLogProductionService.findLastYield(lineData[0],chanangeDate(lineData[6]));
            if(edcDskLogProduction==null){
                log.info("数据异常");
                return;
            }
            String newLine = fixline(line,String.valueOf(edcDskLogProduction.getDayYield()),",",4);
            newLine = fixline(newLine,String.valueOf(edcDskLogProduction.getLotYield()),",",5);
            newLine = fixline(newLine,String.valueOf(edcDskLogProduction.getLotNo()),",",13);
            newLines.add(newLine);
            if(flag){
                String filename[]=repeatFileName.split("_");
                repeatFileName=filename[0]+"_"+filename[1]+"_"+ DateUtil.formatDate(chanangeDate(lineData[6]), pattern)+"_"+filename[3];
                flag=false;
            }
        }
        String originalPath=filePath+"/"+"ORIGINAL";
        filePath = new String(filePath.getBytes("GBK"), "iso-8859-1");
        originalPath = new String(originalPath.getBytes("GBK"), "iso-8859-1");
        FileUtil.mkDir(filePath);
        FileUtil.move(filePath + "\\" + file.getName(), originalPath + "\\" + file.getName(), false);
        FileUtil.writeLines(new File(filePath + "\\" +repeatFileName),"UTF-8",newLines,true);
        log.info(titlt);
    }

    public String fixline(String str,String data,String regex,int targetIndex){
        String str1[]=str.split(regex,-1);
        Arrays.fill(str1, targetIndex, targetIndex+1, data);
        String str2 = "";
        for (int i = 0; i < str1.length; i++) {
            if (i == 0) {
                str2 = str1[i];
            } else {
                str2 = str2 + "," + str1[i];
            }
        }
        return str2;
    }
    public List<File> getFileList(String strPath,Date startTime) throws Exception {
        List<File> fileList = (List<File>) FileUtil.listFiles(new File(strPath), new String[]{"csv"}, false);
        List<File> newfileList = Lists.newArrayList();
        for (File file : fileList) {
            if (file.getName().contains("Operationlog")&& file.lastModified()>startTime.getTime()) {
                log.info("文件获取" + file.getName());
                newfileList.add(file);
            }
        }
        //文件排序,先处理旧文件
        Collections.sort(newfileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o2.lastModified() > o1.lastModified()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        log.info("{}文件排序结束", strPath);
        return newfileList;
    }
    public Date chanangeDate(String time){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*public void fixLine(File file, String title, List<String> lines) throws Exception {
        String data[] = lines.get(0).split(",");
        String eqpId = data[0];
        String startTime = data[4];
        MesLotTrack mesLotTrack = mesLotTrackService.findLotNo(startTime, eqpId);
        if (mesLotTrack == null) {
            log.info("edcDskLogProductionService.findLotNo(time,eqpid)为空");
            log.info("数据库查询无数据");
            //如果数据库中没有满足条件的数据则不执行
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String endDateString = formatter.format(mesLotTrack.getEndTime());
        MesLotTrack mesLotTrack1=mesLotTrackService.findNextStartTime(endDateString,eqpId);
        List<String> newlines = new ArrayList<>();
        newlines.add(FileUtil.csvBom+title);
        for (int j = 0; j < lines.size(); j++) {
            String data1[] = lines.get(j).split(",");
            Date endDate = DateUtil.parseDate(data1[5]);
            Date starDate = DateUtil.parseDate(data1[4]); //当前行开始时间
            //判断当前行工作时间段在不在数据库时间段内
            if (starDate.getTime() > mesLotTrack.getStartTime().getTime() && endDate.getTime() < mesLotTrack1.getStartTime().getTime()) {
                if (!data1[14].equals(mesLotTrack.getLotNo())) {
                    Arrays.fill(data1, 14, 15, mesLotTrack.getLotNo());
                    log.info("当前行解析完成");
                }
                String str = "";
                for (int l = 0; l < data1.length; l++) {
                    String str1 = data1[l];
                    if (l == 0) {
                        str = str + str1;
                    } else {
                        str = str + "," + str1;
                    }
                }
                newlines.add(fixone(str,newlines.size()));
            } else{
                //如果
                // TODO: 2020/7/22 写入新文件
                // TODO: 2020/7/22 文件名字需要重新修改,批次可能会变
                fancha(file.getName(),mesLotTrack.getLotNo(),newlines);
                // TODO: 2020/7/22 另起一个批次
                for (int o = 0; o < j ; o++) { //todo 注意
                    lines.remove(0);
                }
                fixLine(file, title, lines);
                return;
            }
        }
        //反查已读文件
        fancha(file.getName(),mesLotTrack.getLotNo(),newlines);
    }*/
}