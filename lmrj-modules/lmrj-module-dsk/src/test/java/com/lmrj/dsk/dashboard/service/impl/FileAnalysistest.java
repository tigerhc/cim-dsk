package com.lmrj.dsk.dashboard.service.impl;

import com.lmrj.dsk.DskBootApplication;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//@EnableScheduling
//@Component
@Slf4j
@SpringBootTest(classes = DskBootApplication.class)
@RunWith(SpringRunner.class)
public class FileAnalysistest {
    //按照文件名里
    //获取文件 按照文件名上的时间排序
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
    EdcDskLogProductionServiceImpl iEdcDskLogProductionService;
    public EdcDskLogProduction findLotNo(String eqpid,String time){
        EdcDskLogProduction edcDskLogProduction=iEdcDskLogProductionService.findLotNo(time,eqpid);
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
    }

    public  void test()  throws Exception{
        FileAnalysistest fileAnalysistest = new FileAnalysistest();
        List<File> fileList = fileAnalysistest.getFileList("E:\\FTP\\EQUIPMENT\\SIM\\2020\\PRINTER\\SIM-PRINTER1\\07");
        fileAnalysistest.orderBytime(fileList);
        for (int i=0;i<fileList.size()-1;i++){
            List<String> lines = FileUtil.readLines(fileList.get(i),"UTF-8");
            String data[]=lines.get(1).split(",");
            String eqpid=data[0];
            String time=data[4];
            EdcDskLogProduction edcDskLogProduction=fileAnalysistest.findLotNo(time,eqpid);

        }
    }


}
