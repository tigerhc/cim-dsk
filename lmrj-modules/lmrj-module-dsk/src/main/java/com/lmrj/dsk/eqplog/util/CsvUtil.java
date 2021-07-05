package com.lmrj.dsk.eqplog.util;

import com.lmrj.util.file.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.List;

public class CsvUtil {


    //创建文件
    public static boolean createFile(String path,String fileName,List list/*String fileContent*/,String companyId) throws Exception {
        Boolean boo=false;
        String tempFileName=path+fileName;
        String fileBackUpPath=path+fileName+"/Back";
        File file=new File(tempFileName);
        //判断文件路径是否存在，不存在创建文件夹(多层创建)
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        //文件不存在新建,存在则备份
        if(!file.exists()){
            file.createNewFile();
//            boo=writeFileContent(tempFileName, fileContent,fileName,companyId);
            boo=weiteCsv( path,list,fileName );
        }else{
            //获取目录下所有文件判断是否有同名文件存在，若存在将文件备份
            List<File> fileList = (List<File>) FileUtil.listFiles(new File(path), new String[]{"csv"}, false);
            for (File files : fileList) {
                //判断包含指定元素
                if (files.getName().contains("Recipelog.csv")) {
                    if (files.getName().split("_")[1].equals(fileName.split("_")[1]) &&
                            files.getName().split("_")[2].equals(fileName.split("_")[2]) &&
                            !files.getName().split("_")[3].equals(fileName.split("_")[3])) {
                        boo = FileUtil.move( path + "\\" + files.getName(), fileBackUpPath + "\\" + file.getName(), false );
                    }
                }
            }
        }
        return boo;
    }

    //写入内容
    public static boolean  weiteCsv(String path,List list,String fileName){
        Boolean isF = false;
        try {
            try {
                Long start = System.currentTimeMillis();
                File file=new File(path+fileName+".csv");
                FileOutputStream fos=new FileOutputStream(file,true);
                OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw=new BufferedWriter(osw);
                String linesd= StringUtils.strip(list.toString(),"[]");
                String info =linesd.replace("\"", "");
                String item[]  = info.split(";,");
                for(String arr:item){
                    String arrs=arr.substring(0,arr.length()-1);
                    bw.write(arrs+"\t\n");
                }
                //先打开的后关闭，后打开的先关闭
                bw.close();
                osw.close();
                fos.close();
                long end = System.currentTimeMillis();
                System.out.println("完成"+fileName+"入库，耗时：" + (end - start) +" ms");
                isF = true;
            }catch (Exception e){
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return isF;
    }



    /**
     * Description: 向FTP服务器上传文件
     * @param url FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param path FTP服务器保存目录
     * @param filename 上传到FTP服务器上的文件名
     * @param input 输入流
     */
    public static boolean uploadFile(String url,int port,String username, String password, String path, String filename, InputStream input) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(url, port);//连接FTP服务器
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(username, password);//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(path);
            ftp.storeFile(filename, input);
            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }
}
