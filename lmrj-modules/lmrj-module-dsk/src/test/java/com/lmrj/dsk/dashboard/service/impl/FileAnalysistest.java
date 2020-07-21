package com.lmrj.dsk.dashboard.service.impl;

import com.lmrj.dsk.DskBootApplication;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

//@EnableScheduling
//@Component
@Slf4j
@SpringBootTest(classes = DskBootApplication.class)
@RunWith(SpringRunner.class)
public class FileAnalysistest {
    public String filePath="E:\\FTP\\EQUIPMENT\\SIM\\2020\\PRINTER\\SIM-PRINTER1\\07";
    //获取文件
    public List<File> getFileList(String strPath) throws Exception {
        List<File> fileList = (List<File>) FileUtil.listFiles(new File(strPath), new String[]{"csv"}, false);
        List<File> filenewList = Lists.newArrayList();
        for (File file : fileList) {
            if (!file.getName().endsWith("gxj.csv") && file.getName().contains("Productionlog")) {//0000000000000
                log.info("文件获取"+file.getName());
                filenewList.add(file);
            }
        }
        //对list中文件按文件名中的时间进行排序
        return filenewList;
    }
    //LotNo查询
    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;
    public EdcDskLogProduction findLotNo(String eqpid,String time){
        EdcDskLogProduction edcDskLogProduction=edcDskLogProductionService.findLotNo(time,eqpid);
        log.info("LotNo:"+edcDskLogProduction.getLotNo());
        log.info("startTime:"+edcDskLogProduction.getStartTime());
        log.info("endTime:"+edcDskLogProduction.getEndTime());
        return edcDskLogProduction;
    }

    //文件排序
    public void orderBytime(List<File> fileList){
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                String name1[]=o1.getName().split("_");
                String name2[]=o2.getName().split("_");
                Double time1=Double.parseDouble(name1[3]);
                Double time2=Double.parseDouble(name2[3]);
                if (time1<time2)
                    return -1;
                if (time1>time2)
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
        log.info("文件排序结束");
    }

    //内容修改 更改文件中批次
    public void fixfile(List<File> fileList) throws Exception {
        FileAnalysistest fileAnalysistest=new FileAnalysistest();
        for (int i=0;i<fileList.size()-1;i++){
            File nowFile = fileList.get(i);
            List<String> lines = FileUtil.readLines(nowFile,"UTF-8");
            String data[]=lines.get(1).split(",");
            String eqpid=data[0];
            String starttime=data[4];
            EdcDskLogProduction edcDskLogProduction=fileAnalysistest.findLotNo(starttime,eqpid);
            //如果数据库中没有满足条件的数据则不执行
            if(!Objects.isNull(edcDskLogProduction)){
                List<String> newlines = new ArrayList<>();
                newlines.add(lines.get(0));
                for (int j = 1; j < lines.size(); j++) {
                    String data1[] = lines.get(j).split(",");
                    String pattern="yyyy-MM-dd HH:mm:ss SSS";
                    //判断当前行工作时间段在不在数据库时间段内
                    if(edcDskLogProduction.getStartTime().before(new SimpleDateFormat(pattern).parse(data1[4])) && edcDskLogProduction.getEndTime().after(new SimpleDateFormat(pattern).parse(data1[5]))){
                        if(!data1[14].equals(edcDskLogProduction.getLotNo())){
                            Arrays.fill(data, 14, 15, edcDskLogProduction.getLotNo());
                        }
                    }
                    String str = "";
                    for (int l = 0; l < data.length; l++) {
                        String str1 = data[l];
                        if (l == 0) {
                            str = str + str1;
                        }
                        str = str + "," + str1;
                    }
                    newlines.add(str);
                }
                //备份
                FileUtil.move(filePath+"\\"+nowFile.getName(),"E:\\fileback_up"+"\\"+nowFile.getName(),false);
                //生成修改后的新文件
                File newFile = new File(filePath +"\\"+ nowFile.getName());
                FileUtil.writeLines(newFile, newlines);
                //新文件名称尾部加"-R"
                fileAnalysistest.changename(newFile);
            }
        }
    }
    //修改文件名称 在名称尾部加"-R"
    public void changename(File newFile) {
        String fileName = newFile.getName();
        fileName = fileName.substring(0, fileName.length() -4);
        if (newFile.renameTo(new File( filePath+"\\"+ fileName + "-R.csv"))) {
            log.info("文件名修改成功");
        }
    }

    @Test
    public  void test()  throws Exception{
        List<File> fileList = this.getFileList(filePath);
        this.orderBytime(fileList);
        this.fixfile(fileList);
    }
}
