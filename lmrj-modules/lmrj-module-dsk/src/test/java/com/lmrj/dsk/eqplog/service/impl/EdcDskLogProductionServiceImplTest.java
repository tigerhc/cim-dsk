package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.dsk.DskBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@SpringBootTest(classes = DskBootApplication.class)
@RunWith(SpringRunner.class)
public class EdcDskLogProductionServiceImplTest {

    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;

    @Test
    public void exportProductionCsv() {
        Date startTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endTime = calendar.getTime();
        edcDskLogProductionService.exportProductionCsv(startTime,endTime);
    }
}
