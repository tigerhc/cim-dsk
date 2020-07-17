package com.lmrj.eap.job;

import com.lmrj.util.file.FileUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@EnableScheduling
@Component
public class FileReadtest {
    public void readFile() throws Exception{
        File dataFile = new File("D:\\LOG002_20200702_195711_0034.CSV");
        List<String> datalist= FileUtil.readLines(dataFile,"UTF-8");
        String data[]=datalist.get(4).split(",");
    }
}
