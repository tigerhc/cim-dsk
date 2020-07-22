package com.lmrj.cim.quartz;

import com.lmrj.util.file.FileUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ImageTask {

    String path = "";
    String path1 = "";

    @Scheduled(cron = "0 0 4 * * ?")
    public void ReadImage() throws IOException {
        Calendar now = Calendar.getInstance();
        List<File> fileList =
                (List<File>) FileUtil.listFiles(new File(path), new String[]{"bmp"}, false);
        for (File file : fileList) {
            if (new Date().getTime() - file.lastModified() > 1000 * 60 * 60 * 24 * 7) {
                FileUtil.mkDir(path1 + "\\" + now.get(Calendar.YEAR) + "\\" + (now.get(Calendar.MONTH) + 1));
                Image img = ImageIO.read(file);
                BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
                FileOutputStream out =
                        new FileOutputStream(path1 + "\\" + now.get(Calendar.YEAR) + "年" + "\\" + (now.get(Calendar.MONTH) + 1) + "月" + "\\" + file.getName().replace("bmp", "jpg"));
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                file.delete();

            }
        }
    }
}
