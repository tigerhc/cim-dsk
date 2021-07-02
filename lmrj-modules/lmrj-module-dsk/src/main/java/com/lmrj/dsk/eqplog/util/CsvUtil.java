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
//            boo=writeFileContent(tempFileName, fileContent,fileName,companyId);
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

    //将内容写入文件
//    public static boolean writeFileContent(String fileNamePath,String writeContent,String fileName,String companyId) throws IOException{
//        boolean boo=false;
//        IEmsSchemeMgr iesm=new EmsSchemeMgrImpl();
//        //文件内容换行
//        String fileC=writeContent+"\r\n"; //new String(writeContent.getBytes(),"UTF-8") +"\r\n";
//        String temp="";
//
//        FileInputStream fis=null;
//        InputStreamReader isr=null;
//        BufferedReader br=null;
//        FileOutputStream fos=null;
//        PrintWriter pw=null;
//        try{
//            File file=new File(fileNamePath);
//            //将文件读入输入流
//            fis=new FileInputStream(file);
//            isr=new InputStreamReader(fis);
//            br=new BufferedReader(isr);
//
//            StringBuffer sb=new StringBuffer();
//            //文件原有内容
//            for (int i = 0; (temp=br.readLine())!=null; i++) {
//                sb.append(temp);
//                //换行
//                sb=sb.append(System.getProperty("line.separator"));
//            }
//            sb.append(fileC);
//            fos = new FileOutputStream(file);
//            pw = new PrintWriter(fos);
//            pw.write(sb.toString().toCharArray());
//            pw.flush();
//            boo = true;
//            long len=(file.length()/1024)+1; //由于整数运算省略小数部分... 故加1
//            //将数据保存在数据库 3.将写入文件的大小，路径，时间，文件名称 ，推送状态  保存在数据库  --放在 写入文件之后
//            EmsReserveEntity reserve=new EmsReserveEntity();
//            reserve.setFileName(fileName);
//            reserve.setFileSize(String.valueOf(len));
//            reserve.setCompanyId(companyId);
//            iesm.updateFileSize(reserve);
//
//            System.out.println("文件的大小："+len);
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            //关闭流
//            if (pw != null) {
//                pw.close();
//            }
//            if (fos != null) {
//                fos.close();
//            }
//            if (br != null) {
//                br.close();
//            }
//            if (isr != null) {
//                isr.close();
//            }
//            if (fis != null) {
//                fis.close();
//            }
//        }
//        return boo;
//    }


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
