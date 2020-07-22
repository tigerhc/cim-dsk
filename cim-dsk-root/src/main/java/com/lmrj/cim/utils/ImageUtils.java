package com.lmrj.cim.utils;

import com.lmrj.util.file.FileUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.scheduling.annotation.Scheduled;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ImageUtils {
    @Scheduled(cron="0 0 4 * * ?")
    public void ReadImage() throws IOException {

        String path = "E:\\111\\456";
        Calendar now = Calendar.getInstance();
        List<File> fileList =
                (List<File>) FileUtil.listFiles(new File(path), new String[]{"bmp"}, false);
        for (File file : fileList) {
            if(new Date().getTime() - file.lastModified() > 1000*60*60*24*7){
                FileUtil.mkDir(path + "\\" + now.get(Calendar.YEAR) + "\\" + (now.get(Calendar.MONTH) + 1));
                Image img = ImageIO.read(file);
                BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
                FileOutputStream out =
                        new FileOutputStream(path + "\\" + now.get(Calendar.YEAR) + "\\" + (now.get(Calendar.MONTH) + 1) + "\\" + file.getName().replace("bmp", "jpg"));
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                file.delete();

            }
        }
    }

    public static void main(String[] args) throws Exception {
        String path = "E:\\111\\456";
        Calendar now = Calendar.getInstance();
        List<File> fileList =
                (List<File>) FileUtil.listFiles(new File(path), new String[]{"bmp"}, false);
        for (File file : fileList) {
            if(new Date().getTime() - file.lastModified() > 10){
                FileUtil.mkDir(path + "\\" + now.get(Calendar.YEAR) + "\\" + (now.get(Calendar.MONTH) + 1));
                Image img = ImageIO.read(file);
                BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
                FileOutputStream out =
                        new FileOutputStream(path + "\\" + now.get(Calendar.YEAR) + "\\" + (now.get(Calendar.MONTH) + 1) + "\\" + file.getName().replace("bmp", "jpg"));
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                file.delete();
            }
        }
    }
}
