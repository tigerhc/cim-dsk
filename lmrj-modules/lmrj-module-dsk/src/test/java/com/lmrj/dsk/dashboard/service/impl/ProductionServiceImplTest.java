package com.lmrj.dsk.dashboard.service.impl;

import com.lmrj.dsk.DskBootApplication;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.impl.EdcDskLogProductionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@SpringBootTest(classes = DskBootApplication.class)
@RunWith(SpringRunner.class)
public class ProductionServiceImplTest {
    public String filePath = "E:\\ProTest";
    @Autowired
    EdcDskLogProductionServiceImpl edcDskLogProductionService;
    @Test
    public void test() throws Exception{
        String startTime = "2020-03-02 11:43:50.000000";
        String endTime = "2020-03-03 16:41:13.000000";
        List<EdcDskLogProduction> prolist = edcDskLogProductionService.findProductionlog(startTime,endTime);
        edcDskLogProductionService.printProductionlog(prolist,filePath);
    }
}
