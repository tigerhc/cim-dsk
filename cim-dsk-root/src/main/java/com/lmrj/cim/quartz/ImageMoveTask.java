package com.lmrj.cim.quartz;

import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import com.lmrj.mes.kongdong.service.IMsMeasureKongdongService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component

public class ImageMoveTask {
    @Autowired
    IMsMeasureKongdongService iMsMeasureKongdongService;
    String path = "D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率";
    String path1 = "D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率";
    String path2 = "D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率\\SMA";
/*  String path = "Z:\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率";
    String path1 = "Z:\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率";*/

    @Value("${dsk.lineNo}")
    String lineNo;
    @Scheduled(cron = "0 0 4 * * ?")
    public void compressAndMoveImage() throws IOException {
        if("SIM".equals(lineNo)){
            log.info("执行图片转化");
            Calendar now = Calendar.getInstance();
            List<File> fileList =
                    (List<File>) FileUtil.listFiles(new File(path), new String[]{"bmp"}, true);
            fileList.forEach(System.out::println);
            for (File file : fileList) {
                if (new Date().getTime() - file.lastModified() > 1000 * 60 * 60 * 24 * 7) {
                    log.info(file.getName());
                    log.info("开始处理图片"+file.getAbsolutePath());
                    String line = file.getParentFile().getParentFile().getName();
                    String productionName = file.getParentFile().getName();
                    String destPath = path1+ "\\"+line + "\\" + now.get(Calendar.YEAR)+ "年" + "\\" + (now.get(Calendar.MONTH) + 1)+ "月"+ "\\"+productionName;
                    FileUtil.mkDir(destPath );
                    Image img = ImageIO.read(file);
                    if(img!=null){
                        BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                        tag.getGraphics().drawImage(img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
                        //FileOutputStream out =
                        //        new FileOutputStream(destPath + file.getName().replace("bmp", "jpg"));
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(tag);
                        ImageIO.write(tag, "jpg",  new File(destPath + "\\" + file.getName().replace("bmp", "jpg")) /* target */ );
                        file.delete();
                    }
                    log.info("完成处理图片"+file.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args)throws  Exception {
        new ImageMoveTask().compressAndMoveImage();
    }
    @Scheduled(cron = "0 0/20 * * * ?")
    public void smaKongDong(){
        if("SIM".equals(lineNo)){
            List<File> fileList =
                    (List<File>) FileUtil.listFiles(new File(path2), new String[]{"bmp"}, true);
            fileList.forEach(System.out::println);
            List<File> newFileList = new ArrayList<>();
            List<String> newFileNameList = new ArrayList<>();
            for (File eqpFile : fileList) {
                if(new Date().getTime() - eqpFile.lastModified() < 1000 * 60 * 60 * 24 ){
                    newFileNameList.add(eqpFile.getName());
                    newFileList.add(eqpFile);
                }
            }
            List<MsMeasureKongdong> list = new ArrayList<>();
            if(newFileList.size()>0){
                for (File eqpFile : newFileList) {
                    String filePath = eqpFile.getAbsolutePath();
                    String fileName = eqpFile.getName();
                    String productionName = "J."+filePath.substring(filePath.indexOf("SMA")+4,filePath.indexOf(fileName)-1);
                    Date time = new Date(eqpFile.lastModified());
                    String createDate = DateUtil.formatDate(time,"yyyy-MM-dd HH:mm:ss");
                    _getDataByFile(list,fileName,createDate,filePath,productionName);
                }
            }
            List<MsMeasureKongdong> newlist = new ArrayList<>();
            for (MsMeasureKongdong measureKongdong : list) {
                if(iMsMeasureKongdongService.findKongdongExist(measureKongdong.getLineNo(),measureKongdong.getProductionName(),measureKongdong.getLotNo(),measureKongdong.getType())==0){
                    newlist.add(measureKongdong);
                }
            }
            if(newlist.size()>0){
                iMsMeasureKongdongService.insertBatch(newlist, 100);
            }
        }
    }


    private void _getDataByFile(List<MsMeasureKongdong> list, String fileName, String createDate, String filePath,String productionName){
        if(_chkFileNameLine(filePath)){
            //filePath = filePath.substring(0,filePath.lastIndexOf("\\2020年"));
            String lineNo = "SMA";//filePath.substring(filePath.lastIndexOf("\\")+1);0D03A 2.7%-MOS-3-4.bmp
            String lotNo = fileName.substring(0,fileName.indexOf(" "));//0927A2.3%-DI-1.jpg,filePathD:\DSK1\IT化データ（一課）\X線データ\データ処理\ボイド率\SIM\2020年\10月\SIM6812M(E)D-URA F2971
            String voidRatio = fileName.substring(fileName.indexOf(" ")+1,fileName.indexOf("%"));
            String type = fileName.substring(fileName.indexOf("%")+2,fileName.indexOf(".bmp"));
            MsMeasureKongdong data = new MsMeasureKongdong();
            data.setLotNo(lotNo);
            data.setVoidRatio(Double.parseDouble(voidRatio));
            data.setLineNo(lineNo);
            data.setProductionName(productionName);
            data.setType(type);
            data.setCreateDate(DateUtil.parseDate(createDate));
            list.add(data);
        }
    }


    private boolean _chkFileNameLine(String fileName){
        if(!fileName.contains("5GI")&& !fileName.contains("6GI")){
            if(fileName.indexOf(" ")<=0){
                return false;
            }
            if(fileName.indexOf("%")<=0){
                return false;
            }
            if(fileName.indexOf("-")<=0){
                return false;
            }
            return true;
        } else {
            if(fileName.indexOf("%")<=0){
                return false;
            }else{
                return true;
            }
        }
    }
}
