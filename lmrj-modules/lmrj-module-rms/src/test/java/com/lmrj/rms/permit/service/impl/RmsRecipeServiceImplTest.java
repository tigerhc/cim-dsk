package com.lmrj.rms.permit.service.impl;

import com.lmrj.rms.RmsBootApplication;
import com.lmrj.rms.permit.service.IRmsRecipePermitService;
import com.lmrj.util.file.FtpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.AccessType;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RmsBootApplication.class)
@RunWith(SpringRunner.class)
public class RmsRecipeServiceImplTest {

    @Autowired
    private IRmsRecipePermitService rmsRecipePermitService;

    public static String[] FTP94 = new String[]{"127.0.0.1", "21", "cim", "Pp123!@#"};

    @Test
    public void permit() throws Exception {
        rmsRecipePermitService.permit("QA","4f601a61259b4128a2cf2c35e6b6bef6","2","XX参数不符合要求");
    }

    @Test
    public void ftpUtil() throws Exception {
//        FtpUtil.copyFile(FTP94,"/recipe","1_V1.txt","/recipe","1.txt");
        String filePath = "/recipe/shanghai/mold/" + "TOWA-Y1E" + "/EQP/" + "SIM-DM1" + "/" + "test";
        //复制recipe到his文件夹
        boolean flag = FtpUtil.copyFile(FTP94, "/recipe/shanghai/mold/TOWA-Y1E/DRAFT/SIM-DM1/test", "1" + "_V" + "1" + ".txt", filePath + "/HIS", "1" + "_V" + "1" + ".txt");
        //删除原来不带版本号的recipe
        FtpUtil.deleteFile(FTP94,filePath + "/" + "1" + ".txt");
        //复制为最新版本
        FtpUtil.copyFile(FTP94,"/recipe/shanghai/mold/TOWA-Y1E/DRAFT/SIM-DM1/test","1" + "_V" + "1" + ".txt",filePath,"1" + ".txt");
        //删除草稿版
        FtpUtil.deleteFile(FTP94,"/recipe/shanghai/mold/" + "TOWA-Y1E" + "/DRAFT/" + "SIM-DM1" + "/" + "test" + "/" + "1" + "_V" + "1" + ".txt");
    }
}
