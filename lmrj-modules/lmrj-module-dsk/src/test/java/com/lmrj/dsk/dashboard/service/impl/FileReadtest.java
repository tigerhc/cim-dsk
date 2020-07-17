package com.lmrj.dsk.dashboard.service.impl;

import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
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
    public  List<File> getFileList(String strPath) throws Exception{
        List<File> filelist=new ArrayList<>();
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) { // 判断是文件还是文件夹
                    String strFileName = files[i].getAbsolutePath();
                    String name[]=strFileName.split("_");
                    //判断是否备份过
                    if(!name[name.length-1].equals("gxj.csv")){
                        new FileReadtest().fileBackUp(files[i],"D:\\testdata\\back_up");
                        log.info(files[i].getName()+"备份结束");
                        String dest1="";
                        filelist.add(files[i]);
                    }
                }
            }
        }
        return filelist;
    }
    //文件首次获取时进行备份
    public void fileBackUp(File file,String dest )throws IOException {
       /* //创建目的地文件夹
        File destfile = new File(dest);
        if(!destfile.exists()){
            destfile.mkdir();
        }*/
        //用字节输入输出流复制文件
            FileInputStream fis = new FileInputStream(file);
            //创建新的文件，保存复制内容。
            File dfile = new File(dest+"\\"+file.getName());
            if(!dfile.exists()){
                dfile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(dfile);
            // 读写数据
            // 定义数组
            byte[] b = new byte[1024];
            // 定义长度
            int len;
            // 循环读取
            while ((len = fis.read(b))!=-1) {
                // 写出数据
                fos.write(b, 0 , len);
            }
            //关闭资源
            fos.close();
            fis.close();
    }
    //修改文件批次
    public  void fixfile(File testfile) throws Exception{
        File newFile = new File("D:\\testdata\\"+testfile.getName());
        List<String> lines = FileUtil.readLines(testfile, "UTF-8");
        List<String> newlines = new ArrayList<>();
        newlines.add(lines.get(0));
        for (int i=1;i<lines.size();i++){
            String data[] = lines.get(i).split(",");
            if(!data[14].equals("0414A")){
                Arrays.fill(data,14,15,"0414A");
            }
            String str= "";
            for (int j=0;j<data.length;j++){
                String str1=data[j];
                if(j==0){
                    str=str+str1;
                }
                str=str+","+str1;
            }
            newlines.add(str);
        }
        //修改文件内容后，对文件名进行变更标记
        FileUtil.writeLines(newFile,newlines);
        log.info("批次修改成功");
        new FileReadtest().changename(testfile);
    }
    //修改文件名称 在名称尾部加"_gxj"
    public void changename(File textFile){
        String name[]=textFile.getName().split("\\.");
        if(textFile.renameTo(new File("D:\\testdata\\"+name[0]+"_gxj."+name[1]))){
            System.out.println("文件名修改成功");
        }
    }
    @Test
    /*@Scheduled(cron = "0/10 * * * * ?")*/
    public static void main(String[] args) throws Exception{
        FileReadtest fileReadtest= new FileReadtest();
        log.info("开始获取文件");
        List<File> fileList =fileReadtest.getFileList("D:\\testdata");
        if(fileList.size()==0){
            log.info("暂无新文件");
        }
        for (File testFile : fileList) {
            log.info("开始: 解析文件{}", testFile.getName());
            fileReadtest.fixfile(testFile);
        }
    }
}
