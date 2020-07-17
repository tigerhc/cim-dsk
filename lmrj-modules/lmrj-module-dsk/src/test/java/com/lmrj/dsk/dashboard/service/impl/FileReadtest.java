package com.lmrj.dsk.dashboard.service.impl;

import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@EnableScheduling
//@Component
@Slf4j
//获取目录下所有文件
//@SpringBootTest(classes = DskBootApplication.class)
//@RunWith(SpringRunner.class)
public class FileReadtest {

    public List<File> getFileList(String strPath) throws Exception {
        List<File> fileList = (List<File>) FileUtil.listFiles(new File(strPath), new String[]{"csv"}, false);
        List<File> filenewList = Lists.newArrayList();
        for (File file : fileList) {
            if (!file.getName().endsWith("gxj.csv")) {
                new FileReadtest().fileBackUp(file, "D:\\testdata\\back_up");
                log.info(file.getName() + "备份结束");
                String dest1 = "";
                filenewList.add(file);
            }

        }
            //FileUtil.move()
            //FileUtil.copy();
        return filenewList;
    }

    //文件首次获取时进行备份
    public void fileBackUp(File file, String dest) throws IOException {
       /* //创建目的地文件夹
        File destfile = new File(dest);
        if(!destfile.exists()){
            destfile.mkdir();
        }*/
        //用字节输入输出流复制文件
        FileInputStream fis = new FileInputStream(file);
        //创建新的文件，保存复制内容。
        File dfile = new File(dest + "\\" + file.getName());
        if (!dfile.exists()) {
            dfile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(dfile);
        // 读写数据
        // 定义数组
        byte[] b = new byte[1024];
        // 定义长度
        int len;
        // 循环读取
        while ((len = fis.read(b)) != -1) {
            // 写出数据
            fos.write(b, 0, len);
        }
        //关闭资源
        fos.close();
        fis.close();
    }

    //修改文件批次
    public void fixfile(File testfile) throws Exception {
        File newFile = new File("D:\\testdata\\" + testfile.getName());
        List<String> lines = FileUtil.readLines(testfile, "UTF-8");
        List<String> newlines = new ArrayList<>();
        newlines.add(lines.get(0));
        for (int i = 1; i < lines.size(); i++) {
            String data[] = lines.get(i).split(",");
            if (!data[14].equals("0414A")) {
                Arrays.fill(data, 14, 15, "0414A");
            }
            String str = "";
            for (int j = 0; j < data.length; j++) {
                String str1 = data[j];
                if (j == 0) {
                    str = str + str1;
                }
                str = str + "," + str1;
            }
            newlines.add(str);
        }
        //修改文件内容后，对文件名进行变更标记
        FileUtil.writeLines(newFile, newlines);
        log.info("批次修改成功");
        new FileReadtest().changename(testfile);
    }

    //修改文件名称 在名称尾部加"_gxj"
    public void changename(File textFile) {
        String fileName = textFile.getName();
        fileName = fileName.substring(0, fileName.length() -4);
        if (textFile.renameTo(new File("D:\\testdata\\" + fileName + "_gxj.csv"))) {
            log.info("文件名修改成功");
        }
    }

    @Test
    /*@Scheduled(cron = "0/10 * * * * ?")*/
    public  void testParse() throws Exception {
        FileReadtest fileReadtest = new FileReadtest();
        log.info("开始获取文件");
        List<File> fileList = fileReadtest.getFileList("D:\\testdata");
        if (fileList.size() == 0) {
            log.info("暂无新文件");
        }
        for (File testFile : fileList) {
            log.info("开始: 解析文件{}", testFile.getName());
            fileReadtest.fixfile(testFile);
        }
    }
}
