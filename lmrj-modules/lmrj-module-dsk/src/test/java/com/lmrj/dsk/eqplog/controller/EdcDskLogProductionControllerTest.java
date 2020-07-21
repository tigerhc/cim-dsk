package com.lmrj.dsk.eqplog.controller;

import com.lmrj.dsk.DskBootApplication;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = DskBootApplication.class)
@RunWith(SpringRunner.class)
public class EdcDskLogProductionControllerTest {
    IEdcDskLogProductionService edcDskLogProductionService;
    @Autowired
    EdcDskLogProductionController edcDskLogProductionController;
    @Test
    public void fileAnalysistest() throws Exception{
        edcDskLogProductionController.FileAnalysistest();
    }
}
