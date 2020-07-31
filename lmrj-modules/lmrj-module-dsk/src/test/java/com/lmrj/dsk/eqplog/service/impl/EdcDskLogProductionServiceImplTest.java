package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.dsk.DskBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@SpringBootTest(classes = DskBootApplication.class)
@RunWith(SpringRunner.class)
public class EdcDskLogProductionServiceImplTest {

    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;

    @Test
    public void exportProductionCsv() throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        Date startTime = simpleDateFormat.parse("2020-07-31 13:55:36");
        Date endTime = simpleDateFormat.parse("2020-07-31 14:04:33");
        edcDskLogProductionService.updateProductionData(startTime,endTime);
        edcDskLogProductionService.exportProductionCsv(startTime,endTime);
    }
}
