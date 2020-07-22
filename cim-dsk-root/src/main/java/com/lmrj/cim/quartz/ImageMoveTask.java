package com.lmrj.cim.quartz;

import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component

public class ImageMoveTask {

    String path = "D:\\DSK1\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率";
    String path1 = "D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率";
/*  String path = "Z:\\IT化データ（一課）\\X線データ\\日連科技\\ボイド率";
    String path1 = "Z:\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率";*/


    @Scheduled(cron = "0 0 4 * * ?")
    public void compressAndMoveImage() throws IOException {
        log.info("执行图片转化");
        Calendar now = Calendar.getInstance();
        List<File> fileList =
                (List<File>) FileUtil.listFiles(new File(path), new String[]{"bmp"}, true);
        fileList.forEach(System.out::println);
        for (File file : fileList) {
            if (new Date().getTime() - file.lastModified() > 1000 * 60 * 60 * 24 * 1) { //先改为1天
                log.info(file.getName());
                log.info("开始处理图片"+file.getAbsolutePath());
                String line = file.getParentFile().getParentFile().getName();
                String productionName = file.getParentFile().getName();
                String destPath = path1+ "\\"+line + "\\" + now.get(Calendar.YEAR)+ "年" + "\\" + (now.get(Calendar.MONTH) + 1)+ "月"+ "\\"+productionName;
                FileUtil.mkDir(destPath );
                Image img = ImageIO.read(file);
                BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
                //FileOutputStream out =
                //        new FileOutputStream(destPath + file.getName().replace("bmp", "jpg"));
                //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                //encoder.encode(tag);
                ImageIO.write(tag, "jpg",  new File(destPath + "\\" + file.getName().replace("bmp", "jpg")) /* target */ );
                file.delete();
                log.info("完成处理图片"+file.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args)throws  Exception {
        new ImageMoveTask().compressAndMoveImage();
    }


}
