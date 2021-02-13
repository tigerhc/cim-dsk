package com.lmrj.cim.quartz;

import com.google.common.collect.Lists;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ProductionYieldReCalTask {

    @Autowired
    IMesLotTrackService mesLotTrackService;
    public String filePath = "E:\\FTP\\EQUIPMENT\\SIM\\2020\\PRINTER\\SIM-PRINTER1\\07";

    //@Scheduled(cron = "0 45 14 * * ?")
    public void fileAnalysistest() throws Exception {
        List<File> fileList = getFileList(filePath);
        for (File file : fileList) {
            log.info("开始解析 : {}", file.getName());
            this.fixfile(file);
        }
    }
    //获取文件

    public List<File> getFileList(String strPath) throws Exception {
        List<File> fileList = (List<File>) FileUtil.listFiles(new File(strPath), new String[]{"csv"}, false);
        List<File> filenewList = Lists.newArrayList();
        for (File file : fileList) {
            if (!file.getName().endsWith("-R.csv") && file.getName().contains("Productionlog")) {
                log.info("文件获取" + file.getName());
                filenewList.add(file);
            }
        }
        //文件排序,先处理旧文件
        Collections.sort(filenewList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o2.lastModified() < o1.lastModified()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        log.info("{}文件排序结束", strPath);
        return filenewList;
    }

    //日期格式转换
    public Date chanangeDate(Date date) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(date);
        return new SimpleDateFormat(pattern).parse(dateString);
    }

    //内容修改 更改文件中批次
    public void fixfile(File file) throws Exception {
        List<String> lines = FileUtil.readLines(file, "GBK");
        String title = lines.get(0);
        lines.remove(0);
        fixLine(file, title, lines);
        //备份
        FileUtil.move(filePath + "\\" + file.getName(), "E:\\fileback_up" + "\\" + file.getName(), false);
    }
    public String findRepeatFile(String lotNo){
        //反查是否有该批次文件已被创建
        List<File> fileList = (List<File>) FileUtil.listFiles(new File(filePath), new String[]{"csv"}, false);
        for(File readFile:fileList){
            String filename=readFile.getName();
            if(filename.contains("-R") && filename.contains(lotNo)){
                return filename;
            }
        }
        return null;
    }
    public String changename(String fileName,String lotNo){
        String name[]= fileName.split("_");
        String newfilename = fileName;
        if(!name[2].equals(lotNo)){
            newfilename=null;
            Arrays.fill(name, 2, 3, lotNo);
            for (int i = 0; i <name.length ; i++) {
                if(i==0){
                    newfilename=name[i];
                }else{
                    newfilename=newfilename+"_"+name[i];
                }
            }
        }
        return newfilename.replace(".csv", "-R.csv");
    }

    public void fancha(String filename,String lotno,List<String> newlines) throws Exception{
        //反查已读文件
        String repeatFileName=findRepeatFile(lotno);
        if(repeatFileName==null){
            log.info("正常创建");
            String filename1=changename(filename,lotno);
            File newFile = new File(filePath + "\\" + filename1);
            FileUtil.writeLines(newFile, "UTF-8", newlines);
        }else{
            log.info("续写");
            //该批次文件已存在，续写至该文件
            newlines.remove(0);
            FileUtil.writeLines(new File(filePath + "\\" +repeatFileName),"UTF-8",newlines,true);
        }
    }

    public String fixone(String str,int size){
        String str1[]=str.split(",");
        log.info(str);
        log.info(str1[7]);
        Arrays.fill(str1, 7, 8, String.valueOf(size));
        log.info(str1[7]);
        String str2 = "";
        for (int l = 0; l < str1.length; l++) {
            if (l == 0) {
                str2 = str1[l];
            } else {
                str2 = str2 + "," + str1[l];
            }
        }
        log.info(str2);
        return str2;
    }

    public void fixLine(File file, String title, List<String> lines) throws Exception {
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
    }
}
